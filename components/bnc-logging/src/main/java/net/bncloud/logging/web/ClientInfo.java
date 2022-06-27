package net.bncloud.logging.web;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;

public class ClientInfo {

    public static ClientInfo EMPTY_CLIENT_INFO = new ClientInfo();

    /**
     * IP Address of the server (local).
     */
    private final String serverAddress;

    /**
     * IP Address of the client (Remote).
     */
    private final String clientAddress;

    private ClientInfo() {
        this(null);
    }

    public ClientInfo(final HttpServletRequest request) {
        this(request, null, null, false);
    }

    public ClientInfo(String serverAddress, String clientAddress) {
        this.serverAddress = serverAddress;
        this.clientAddress = clientAddress;
    }

    public ClientInfo(final HttpServletRequest request,
                      final String alternateServerAddrHeaderName,
                      final String alternateLocalAddrHeaderName,
                      final boolean useServerHostAddress) {

        try {
            String serverIpAddress = request != null ? request.getLocalAddr() : null;
            String clientIpAddress = request != null ? request.getRemoteAddr() : null;

            if (request != null) {
                if (useServerHostAddress) {
                    serverIpAddress = Inet4Address.getLocalHost().getHostAddress();
                } else if (alternateServerAddrHeaderName != null && !alternateServerAddrHeaderName.isEmpty()) {
                    serverIpAddress = request.getHeader(alternateServerAddrHeaderName) != null
                            ? request.getHeader(alternateServerAddrHeaderName) : request.getLocalAddr();
                }

                if (alternateLocalAddrHeaderName != null && !alternateLocalAddrHeaderName.isEmpty()) {
                    clientIpAddress = request.getHeader(alternateLocalAddrHeaderName) != null ? request.getHeader
                            (alternateLocalAddrHeaderName) : request.getRemoteAddr();
                }
            }

            this.serverAddress = serverIpAddress == null ? "unknown" : serverIpAddress;
            this.clientAddress = clientIpAddress == null ? "unknown" : clientIpAddress;

        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public String getClientAddress() {
        return clientAddress;
    }
}
