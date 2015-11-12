package com.baidu.oped.apm.collector.config;


public class SpanConfig implements UdpServerConfig {
    private String listenIp;
    private int listenPort;
    private int workerThread;
    private int workerQueueSize;
    private int bufferSize;
    private boolean collectMetric;

    public String getListenIp() {
        return listenIp;
    }

    public void setListenIp(String listenIp) {
        this.listenIp = listenIp;
    }

    public int getListenPort() {
        return listenPort;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    public int getWorkerThread() {
        return workerThread;
    }

    public void setWorkerThread(int workerThread) {
        this.workerThread = workerThread;
    }

    public int getWorkerQueueSize() {
        return workerQueueSize;
    }

    public void setWorkerQueueSize(int workerQueueSize) {
        this.workerQueueSize = workerQueueSize;
    }

    @Override
    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    @Override
    public boolean isCollectMetric() {
        return collectMetric;
    }

    public void setCollectMetric(boolean collectMetric) {
        this.collectMetric = collectMetric;
    }

    @Override
    public String getReceiverName() {
        return "UDP-SPAN";
    }
}
