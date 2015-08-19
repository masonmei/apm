//package com.baidu.oped.apm.config.hbase;
//
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Configuration;
//
///**
// * Created by mason on 8/5/15.
// */
//@Configuration
//@ConfigurationProperties(prefix = "hbase")
//public class HBaseProperties {
//
//    private String clientHost;
//    private String clientPort;
//    private boolean clientTcpNodelay;
//    private int rpcTimeout;
//    private int operationTimeout;
//    private int socketReadTimeout;
//    private int socketWriteTimeout;
//    private int maxThreadSize;
//    private int poolQueueSize;
//    private boolean prestartPool;
//
//    public String getClientHost() {
//        return clientHost;
//    }
//
//    public void setClientHost(String clientHost) {
//        this.clientHost = clientHost;
//    }
//
//    public String getClientPort() {
//        return clientPort;
//    }
//
//    public void setClientPort(String clientPort) {
//        this.clientPort = clientPort;
//    }
//
//    public boolean isClientTcpNodelay() {
//        return clientTcpNodelay;
//    }
//
//    public void setClientTcpNodelay(boolean clientTcpNodelay) {
//        this.clientTcpNodelay = clientTcpNodelay;
//    }
//
//    public int getRpcTimeout() {
//        return rpcTimeout;
//    }
//
//    public void setRpcTimeout(int rpcTimeout) {
//        this.rpcTimeout = rpcTimeout;
//    }
//
//    public int getOperationTimeout() {
//        return operationTimeout;
//    }
//
//    public void setOperationTimeout(int operationTimeout) {
//        this.operationTimeout = operationTimeout;
//    }
//
//    public int getSocketReadTimeout() {
//        return socketReadTimeout;
//    }
//
//    public void setSocketReadTimeout(int socketReadTimeout) {
//        this.socketReadTimeout = socketReadTimeout;
//    }
//
//    public int getSocketWriteTimeout() {
//        return socketWriteTimeout;
//    }
//
//    public void setSocketWriteTimeout(int socketWriteTimeout) {
//        this.socketWriteTimeout = socketWriteTimeout;
//    }
//
//    public int getMaxThreadSize() {
//        return maxThreadSize;
//    }
//
//    public void setMaxThreadSize(int maxThreadSize) {
//        this.maxThreadSize = maxThreadSize;
//    }
//
//    public int getPoolQueueSize() {
//        return poolQueueSize;
//    }
//
//    public void setPoolQueueSize(int poolQueueSize) {
//        this.poolQueueSize = poolQueueSize;
//    }
//
//    public boolean isPrestartPool() {
//        return prestartPool;
//    }
//
//    public void setPrestartPool(boolean prestartPool) {
//        this.prestartPool = prestartPool;
//    }
//}
