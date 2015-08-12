package com.baidu.oped.apm.collector.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by mason on 8/5/15.
 */
@Configuration
@ConfigurationProperties(prefix = "collector")
public class MxCollectorProperties {
    private long flushPeriod;
    private String tcpListenIp;
    private int tcpListenPort;
    private String udpSpanListenIp;
    private int udpSpanListenPort;
    private int udpSpanSocketReceiveBufferSize;
    private int udpSpanWorkerThread;
    private int udpSpanWorkerQueueSize;
    private String udpStatListenIp;
    private int udpStatListenPort;
    private int udpStatSocketReceiveBufferSize;
    private int udpStatWorkerThread;
    private int udpStatWorkerQueueSize;
    private List<String> l4ip;

    public long getFlushPeriod() {
        return flushPeriod;
    }

    public void setFlushPeriod(long flushPeriod) {
        this.flushPeriod = flushPeriod;
    }

    public String getTcpListenIp() {
        return tcpListenIp;
    }

    public void setTcpListenIp(String tcpListenIp) {
        this.tcpListenIp = tcpListenIp;
    }

    public int getTcpListenPort() {
        return tcpListenPort;
    }

    public void setTcpListenPort(int tcpListenPort) {
        this.tcpListenPort = tcpListenPort;
    }

    public String getUdpSpanListenIp() {
        return udpSpanListenIp;
    }

    public void setUdpSpanListenIp(String udpSpanListenIp) {
        this.udpSpanListenIp = udpSpanListenIp;
    }

    public int getUdpSpanListenPort() {
        return udpSpanListenPort;
    }

    public void setUdpSpanListenPort(int udpSpanListenPort) {
        this.udpSpanListenPort = udpSpanListenPort;
    }

    public int getUdpSpanSocketReceiveBufferSize() {
        return udpSpanSocketReceiveBufferSize;
    }

    public void setUdpSpanSocketReceiveBufferSize(int udpSpanSocketReceiveBufferSize) {
        this.udpSpanSocketReceiveBufferSize = udpSpanSocketReceiveBufferSize;
    }

    public int getUdpSpanWorkerThread() {
        return udpSpanWorkerThread;
    }

    public void setUdpSpanWorkerThread(int udpSpanWorkerThread) {
        this.udpSpanWorkerThread = udpSpanWorkerThread;
    }

    public int getUdpSpanWorkerQueueSize() {
        return udpSpanWorkerQueueSize;
    }

    public void setUdpSpanWorkerQueueSize(int udpSpanWorkerQueueSize) {
        this.udpSpanWorkerQueueSize = udpSpanWorkerQueueSize;
    }

    public String getUdpStatListenIp() {
        return udpStatListenIp;
    }

    public void setUdpStatListenIp(String udpStatListenIp) {
        this.udpStatListenIp = udpStatListenIp;
    }

    public int getUdpStatListenPort() {
        return udpStatListenPort;
    }

    public void setUdpStatListenPort(int udpStatListenPort) {
        this.udpStatListenPort = udpStatListenPort;
    }

    public int getUdpStatSocketReceiveBufferSize() {
        return udpStatSocketReceiveBufferSize;
    }

    public void setUdpStatSocketReceiveBufferSize(int udpStatSocketReceiveBufferSize) {
        this.udpStatSocketReceiveBufferSize = udpStatSocketReceiveBufferSize;
    }

    public int getUdpStatWorkerThread() {
        return udpStatWorkerThread;
    }

    public void setUdpStatWorkerThread(int udpStatWorkerThread) {
        this.udpStatWorkerThread = udpStatWorkerThread;
    }

    public int getUdpStatWorkerQueueSize() {
        return udpStatWorkerQueueSize;
    }

    public void setUdpStatWorkerQueueSize(int udpStatWorkerQueueSize) {
        this.udpStatWorkerQueueSize = udpStatWorkerQueueSize;
    }

    public List<String> getL4ip() {
        return l4ip;
    }

    public void setL4ip(List<String> l4ip) {
        this.l4ip = l4ip;
    }
}
