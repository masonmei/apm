
package com.baidu.oped.apm.collector.receiver.udp;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.baidu.oped.apm.collector.receiver.DataReceiver;
import com.baidu.oped.apm.collector.receiver.DispatchHandler;
import com.baidu.oped.apm.collector.util.DatagramPacketFactory;
import com.baidu.oped.apm.collector.util.ObjectPool;
import com.baidu.oped.apm.collector.util.PacketUtils;
import com.baidu.oped.apm.common.util.ExecutorFactory;
import com.baidu.oped.apm.common.util.PinpointThreadFactory;
import com.baidu.oped.apm.rpc.util.CpuUtils;

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
 *   change to abstract class
 */
public abstract class AbstractUDPReceiver implements DataReceiver {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private String bindAddress;
    private int port;
    private int receiverBufferSize;

    private String receiverName = this.getClass().getSimpleName();

    @Autowired
    private MetricRegistry metricRegistry;

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

    private DispatchHandler dispatchHandler;


    private AtomicInteger rejectedExecutionCount = new AtomicInteger(0);

    private AtomicBoolean state = new AtomicBoolean(true);


    public AbstractUDPReceiver() {
    }

    public AbstractUDPReceiver(String receiverName, DispatchHandler dispatchHandler, String bindAddress, int port, int receiverBufferSize, int workerThreadSize, int workerThreadQueueSize) {
        if (dispatchHandler == null) {
            throw new NullPointerException("dispatchHandler must not be null");
        }
        if (bindAddress == null) {
            throw new NullPointerException("bindAddress must not be null");
        }
        this.receiverName = receiverName;
        this.dispatchHandler = dispatchHandler;
        this.bindAddress = bindAddress;
        this.port = port;
        this.receiverBufferSize = receiverBufferSize;

        this.workerThreadSize = workerThreadSize;
        this.workerThreadQueueSize = workerThreadQueueSize;
    }

    abstract Runnable getPacketDispatcher(AbstractUDPReceiver receiver, DatagramPacket packet);
    
    public void afterPropertiesSet() {
        Assert.notNull(dispatchHandler, "dispatchHandler must not be null");
        Assert.notNull(metricRegistry, "metricRegistry must not be null");

        this.socket = createSocket(bindAddress, port, receiverBufferSize);

        final int packetPoolSize = getPacketPoolSize(workerThreadSize, workerThreadQueueSize);
        this.datagramPacketPool = new ObjectPool<DatagramPacket>(new DatagramPacketFactory(), packetPoolSize);
        this.worker = ExecutorFactory.newFixedThreadPool(workerThreadSize, workerThreadQueueSize, receiverName + "-Worker", true);

        this.timer = metricRegistry.timer(receiverName + "-timer");
        this.rejectedCounter = metricRegistry.counter(receiverName + "-rejected");
        this.io = (ThreadPoolExecutor) Executors.newCachedThreadPool(new PinpointThreadFactory(receiverName + "-Io", true));
    }


    private void receive() {
        if (logger.isInfoEnabled()) {
            logger.info("start ioThread localAddress:{}, IoThread:{}", socket.getLocalAddress(), Thread.currentThread().getName());
        }
        final SocketAddress localSocketAddress = socket.getLocalSocketAddress();
        final boolean debugEnabled = logger.isDebugEnabled();

        // need shutdown logic
        while (state.get()) {
            DatagramPacket packet = read0();
            if (packet == null) {
                continue;
            }
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
                worker.execute(getPacketDispatcher(this, packet));
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

    private DatagramPacket read0() {
        boolean success = false;
        DatagramPacket packet = datagramPacketPool.getObject();
        if (packet == null) {
            logger.error("datagramPacketPool is empty");
            return null;
        }
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
                datagramPacketPool.returnObject(packet);
            }
        }
        return packet;
    }

    private DatagramSocket createSocket(String bindAddress, int port, int receiveBufferSize) {
        try {
            DatagramSocket so = new DatagramSocket(null);
            so.setReceiveBufferSize(receiveBufferSize);
            if (logger.isWarnEnabled()) {
                final int checkReceiveBufferSize = so.getReceiveBufferSize();
                if (receiveBufferSize != checkReceiveBufferSize) {
                    logger.warn("DatagramSocket.setReceiveBufferSize() error. {}!={}", receiveBufferSize, checkReceiveBufferSize);
                }
            }
            so.setSoTimeout(1000 * 5);
            // bind timing feels a bit early
            so.bind(new InetSocketAddress(bindAddress, port));
            return so;
        } catch (SocketException ex) {
            throw new RuntimeException("Socket create Fail. port:" + port + " Caused:" + ex.getMessage(), ex);
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
        if (socket == null) {
            throw new RuntimeException("socket create fail");
        }

        logger.info("UDP Packet reader:{} started.", ioThreadSize);
        for (int i = 0; i < ioThreadSize; i++) {
            io.execute(new Runnable() {
                @Override
                public void run() {
                    receive();
                }
            });
        }

    }

    @PreDestroy
    @Override
    public void shutdown() {
        logger.info("{} shutdown.", this.receiverName);
        state.set(false);
        // is it okay to just close here?
        socket.close();
        shutdownExecutor(io, "IoExecutor");
        shutdownExecutor(worker, "WorkerExecutor");
    }

    private void shutdownExecutor(ExecutorService executor, String executorName) {
        executor.shutdown();
        try {
            executor.awaitTermination(1000*10, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.info("{}.shutdown() Interrupted", executorName, e);
            Thread.currentThread().interrupt();
        }
    }

    public Timer getTimer() {
        return timer;
    }

    public DispatchHandler getDispatchHandler() {
        return dispatchHandler;
    }

    public ObjectPool<DatagramPacket> getDatagramPacketPool() {
        return datagramPacketPool;
    }

    public DatagramSocket getSocket() {
        return socket;
    }
}
