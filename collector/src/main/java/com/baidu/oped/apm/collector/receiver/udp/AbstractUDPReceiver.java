
package com.baidu.oped.apm.collector.receiver.udp;

import com.baidu.oped.apm.collector.config.UdpServerConfig;
import com.baidu.oped.apm.collector.receiver.DataReceiver;
import com.baidu.oped.apm.collector.util.*;
import com.baidu.oped.apm.common.util.ExecutorFactory;
import com.baidu.oped.apm.common.util.ApmThreadFactory;
import com.baidu.oped.apm.rpc.util.CpuUtils;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author emeroad
 * @author netspider
 * @author jaehong.kim
 *         change to abstract class
 */
public abstract class AbstractUDPReceiver implements DataReceiver {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private final String bindAddress;
    private final int port;
    private final String receiverName;

    @Autowired
    private MetricRegistry metricRegistry;

    private final boolean enableCollectorMetric;

    private Timer timer;
    private Counter rejectedCounter;

    // increasing ioThread size wasn't very effective
    private int ioThreadSize = CpuUtils.cpuCount();
    private ThreadPoolExecutor io;

    // modify thread pool size appropriately when modifying queue capacity 
    private ThreadPoolExecutor worker;
    private int workerThreadSize = 128;
    private int workerThreadQueueSize = 1024;

    // can't really allocate memory as max udp packet sizes are unknown.
    // not allocating memory in advance as I am unsure of the max udp packet size.
    // packet cache is necessary as the JVM does not last long if they are dynamically created with the maximum size.
    private ObjectPool<DatagramPacket> datagramPacketPool;


    private volatile DatagramSocket socket = null;

    private final PacketHandlerFactory<DatagramPacket> packetHandlerFactory;


    private AtomicInteger rejectedExecutionCount = new AtomicInteger(0);

    private AtomicBoolean state = new AtomicBoolean(true);


    public AbstractUDPReceiver(UdpServerConfig serverConfig, PacketHandlerFactory<DatagramPacket> packetHandlerFactory) {

        if (packetHandlerFactory == null) {
            throw new NullPointerException("packetHandlerFactory must not be null");
        }

        if (serverConfig == null) {
            throw new NullPointerException("serverConfig must not be null");
        }

        this.receiverName = serverConfig.getReceiverName();
        this.bindAddress = serverConfig.getListenIp();
        this.port = serverConfig.getListenPort();
        this.socket = createSocket(serverConfig.getBufferSize());

        this.workerThreadSize = serverConfig.getWorkerThread();
        this.workerThreadQueueSize = serverConfig.getWorkerQueueSize();
        this.packetHandlerFactory = packetHandlerFactory;
        this.enableCollectorMetric = serverConfig.isCollectMetric();
    }

    public void afterPropertiesSet() {
        Assert.notNull(packetHandlerFactory, "packetHandlerFactory must not be null");
        Assert.notNull(metricRegistry, "metricRegistry must not be null");

        final int packetPoolSize = getPacketPoolSize(workerThreadSize, workerThreadQueueSize);
        this.datagramPacketPool = new DefaultObjectPool<>(new DatagramPacketFactory(), packetPoolSize);
        this.worker = ExecutorFactory.newFixedThreadPool(workerThreadSize, workerThreadQueueSize, receiverName + "-Worker", true);

        this.timer = metricRegistry.timer(receiverName + "-timer");
        this.rejectedCounter = metricRegistry.counter(receiverName + "-rejected");
        this.io = (ThreadPoolExecutor) Executors.newCachedThreadPool(new ApmThreadFactory(receiverName + "-Io", true));
    }


    private void receive(final DatagramSocket socket) {
        if (logger.isInfoEnabled()) {
            logger.info("start ioThread localAddress:{}, IoThread:{}", socket.getLocalAddress(), Thread.currentThread().getName());
        }
        final SocketAddress localSocketAddress = socket.getLocalSocketAddress();
        final boolean debugEnabled = logger.isDebugEnabled();

        // need shutdown logic
        while (state.get()) {
            PooledObject<DatagramPacket> pooledObject = read0(socket);
            if (pooledObject == null) {
                continue;
            }

            final DatagramPacket packet = pooledObject.getObject();
            if (packet.getLength() == 0) {
                if (debugEnabled) {
                    logger.debug("length is 0 ip:{}, port:{}", packet.getAddress(), packet.getPort());
                }
                return;
            }
            if (debugEnabled) {
                logger.debug("pool getActiveCount:{}", worker.getActiveCount());
            }
            try {
                worker.execute(wrapDispatchTask(pooledObject));
            } catch (RejectedExecutionException ree) {
                rejectedCounter.inc();
                final int error = rejectedExecutionCount.incrementAndGet();
                final int mod = 100;
                if ((error % mod) == 0) {
                    logger.warn("RejectedExecutionCount={}", error);
                }
            }
        }
        if (logger.isInfoEnabled()) {
            logger.info("stop ioThread localAddress:{}, IoThread:{}", localSocketAddress, Thread.currentThread().getName());
        }
    }

    private Runnable wrapDispatchTask(PooledObject<DatagramPacket> pooledPacket) {
        return () -> {
            PacketHandler<DatagramPacket> dispatchPacket = packetHandlerFactory.createPacketHandler();
            PooledPacketWrap pooledPacketWrap = new PooledPacketWrap(dispatchPacket, pooledPacket);
            Runnable execution = pooledPacketWrap;
            if (enableCollectorMetric) {
                execution = new TimingWrap(timer, execution);
            }
            execution.run();
        };
    }


    private PooledObject<DatagramPacket> read0(final DatagramSocket socket) {
        boolean success = false;
        PooledObject<DatagramPacket> pooledObject = datagramPacketPool.getObject();
        if (pooledObject == null) {
            logger.error("datagramPacketPool is empty");
            return null;
        }

        DatagramPacket packet = pooledObject.getObject();
        try {
            try {
                socket.receive(packet);
                success = true;
            } catch (SocketTimeoutException e) {
                return null;
            }
            if (logger.isDebugEnabled()) {
                logger.debug("DatagramPacket SocketAddress:{} read size:{}", packet.getSocketAddress(), packet.getLength());
                if (logger.isTraceEnabled()) {
                    // use trace as packet dump may be large
                    logger.trace("dump packet:{}", PacketUtils.dumpDatagramPacket(packet));
                }
            }
        } catch (IOException e) {
            if (!state.get()) {
                // shutdown
            } else {
                logger.error("IoError, Caused:", e.getMessage(), e);
            }
            return null;
        } finally {
            if (!success) {
                pooledObject.returnObject();
            }
        }
        return pooledObject;
    }

    private DatagramSocket createSocket(int receiveBufferSize) {
        try {
            DatagramSocket socket = new DatagramSocket(null);
            socket.setReceiveBufferSize(receiveBufferSize);
            if (logger.isWarnEnabled()) {
                final int checkReceiveBufferSize = socket.getReceiveBufferSize();
                if (receiveBufferSize != checkReceiveBufferSize) {
                    logger.warn("DatagramSocket.setReceiveBufferSize() error. {}!={}", receiveBufferSize, checkReceiveBufferSize);
                }
            }
            socket.setSoTimeout(1000 * 5);
            return socket;
        } catch (SocketException ex) {
            throw new RuntimeException("Socket create Fail. port:" + port + " Caused:" + ex.getMessage(), ex);
        }
    }

    private void bindSocket(DatagramSocket socket, String bindAddress, int port) {
        if (socket == null) {
            throw new NullPointerException("socket must not be null");
        }
        try {
            logger.info("DatagramSocket.bind() {}/{}", bindAddress, port);
            socket.bind(new InetSocketAddress(bindAddress, port));
        } catch (SocketException ex) {
            throw new IllegalStateException("Socket bind Fail. port:" + port + " Caused:" + ex.getMessage(), ex);
        }
    }

    private int getPacketPoolSize(int workerThreadSize, int workerThreadQueueSize) {
        return workerThreadSize + workerThreadQueueSize + ioThreadSize;
    }

    @PostConstruct
    @Override
    public void start() {
        logger.info("{} start.", receiverName);
        afterPropertiesSet();
        final DatagramSocket socket = this.socket;
        if (socket == null) {
            throw new RuntimeException("socket create fail");
        }
        bindSocket(socket, bindAddress, port);
        logger.info("UDP Packet reader:{} started.", ioThreadSize);
        for (int i = 0; i < ioThreadSize; i++) {
            io.execute(() -> receive(socket));
        }
    }

    @PreDestroy
    @Override
    public void shutdown() {
        logger.info("{} shutdown.", this.receiverName);
        state.set(false);
        if (socket != null) {
            socket.close();
        }
        shutdownExecutor(io, "IoExecutor");
        shutdownExecutor(worker, "WorkerExecutor");
    }

    private void shutdownExecutor(ExecutorService executor, String executorName) {
        executor.shutdown();
        try {
            executor.awaitTermination(1000 * 10, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.info("{}.shutdown() Interrupted", executorName, e);
            Thread.currentThread().interrupt();
        }
    }

    public Timer getTimer() {
        return timer;
    }

    public ObjectPool<DatagramPacket> getDatagramPacketPool() {
        return datagramPacketPool;
    }

    public DatagramSocket getSocket() {
        return socket;
    }
}
