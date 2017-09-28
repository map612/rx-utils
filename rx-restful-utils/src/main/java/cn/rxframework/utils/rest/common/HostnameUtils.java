package cn.rxframework.utils.rest.common;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HostnameUtils {

	private static Logger log = LoggerFactory.getLogger(HostnameUtils.class);
	
    /**
     * Return hostname of the system with DNS suffix
     * @return
     */
    public static String getHostname() {
        String hostName;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
            return hostName;
        } catch (UnknownHostException e) {
        	log.error("", e);
        }
        return "";
    }

    /**
     * Return hostname of the system without DNS suffix
     * @return
     */
    public static String getShortHostname() {
        String hostname = getHostname();
        if (hostname.contains("."))
            hostname = hostname.substring(0, hostname.indexOf("."));

        return hostname;
    }

    /**
     * Returns the client's (user) IP
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * Returns true if client (user) is within VMware's network
     * @param request
     * @return
     */
    public static boolean isIntranet(HttpServletRequest request) {
        String ip = getIp(request).split(",")[0].trim();
        String serverName = request.getServerName().trim();
        return ip.startsWith("192.168.") || ip.startsWith("172.16.") || ip.startsWith("10.") || serverName.equalsIgnoreCase("localhost") || serverName.equalsIgnoreCase("127.0.0.1");
    }
}
