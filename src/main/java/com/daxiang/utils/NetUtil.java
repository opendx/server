package com.daxiang.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.*;
import java.util.Enumeration;

/**
 * Created by jiangyitao.
 */
@Slf4j
public class NetUtil {

    private static String ip = null;

    /**
     * 获取本机ip
     *
     * @return
     */
    public static String getIp() throws SocketException {
        if(ip != null) {
            return ip;
        }
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                if (!inetAddress.isLinkLocalAddress() && !inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                    ip = inetAddress.getHostAddress();
                    return ip;
                }
            }
        }
        return ip;
    }
}
