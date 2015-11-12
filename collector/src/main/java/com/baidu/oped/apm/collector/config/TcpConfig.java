package com.baidu.oped.apm.collector.config;

import java.util.List;


public class TcpConfig implements ServerConfig {
    private String listenIp;
    private int listenPort;
    private List<String> l4IpList;
    private int workerThread;
    private int workerQueueSize;

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

    public List<String> getL4IpList() {
        return l4IpList;
    }

    public void setL4IpList(List<String> l4IpList) {
        this.l4IpList = l4IpList;
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
    public String getReceiverName() {
        return "TCP-Worker";
    }
}
