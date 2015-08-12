package com.baidu.oped.apm.bootstrap.util;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.logging.Logger;

/**
 * class NetworkUtils
 *
 * @author meidongxu@baidu.com
 */
public final class NetworkUtils {

    public static final String ERROR_HOST_NAME = "UNKNOWN-HOST";

    private NetworkUtils() {
    }

    public static String getHostName() {
        try {
            final InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostName();
        } catch (UnknownHostException e) {
            // Try to get machine name from network interface.
            return getMachineName();
        }
    }

    public static String getHostIp() {
        String hostIp;
        try {
            final InetAddress thisIp = InetAddress.getLocalHost();
            hostIp = thisIp.getHostAddress();
        } catch (UnknownHostException e) {
            Logger.getLogger(NetworkUtils.class.getClass().getName()).warning(e.getMessage());
            hostIp = "127.0.0.1";
        }
        return hostIp;
    }

    @Deprecated
    public static String getMachineName() {
        try {
            Enumeration<NetworkInterface> enet = NetworkInterface.getNetworkInterfaces();
            while (enet.hasMoreElements()) {

                NetworkInterface net = enet.nextElement();
                if (net.isLoopback()) {
                    continue;
                }

                Enumeration<InetAddress> eaddr = net.getInetAddresses();

                while (eaddr.hasMoreElements()) {
                    InetAddress inet = eaddr.nextElement();

                    final String canonicalHostName = inet.getCanonicalHostName();
                    if (!canonicalHostName.equalsIgnoreCase(inet.getHostAddress())) {
                        return canonicalHostName;
                    }
                }
            }
            return ERROR_HOST_NAME;
        } catch (SocketException e) {
            Logger.getLogger(NetworkUtils.class.getClass().getName()).warning(e.getMessage());
            return ERROR_HOST_NAME;
        }
    }

    public static String getHostFromURL(final String urlSpec) {
        if (urlSpec == null) {
            return null;
        }
        try {
            final URL url = new URL(urlSpec);

            final String host = url.getHost();
            final int port = url.getPort();

            if (port == -1) {
                return host;
            } else {
                // TODO should we still specify the port number if default port is used?
                return host + ":" + port;
            }
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
