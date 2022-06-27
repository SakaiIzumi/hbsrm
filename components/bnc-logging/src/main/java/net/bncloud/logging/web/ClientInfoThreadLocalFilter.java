package net.bncloud.logging.web;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Creates a ClientInfo object and passes it to the {@link ClientInfoHolder}
 */
public class ClientInfoThreadLocalFilter implements Filter {

    public static final String CONST_IP_ADDRESS_HEADER = "alternativeIpAddressHeader";
    public static final String CONST_SERVER_IP_ADDRESS_HEADER = "alternateServerAddrHeaderName";
    public static final String CONST_USE_SERVER_HOST_ADDRESS = "useServerHostAddress";

    private String alternateLocalAddrHeaderName;
    private boolean useServerHostAddress;
    private String alternateServerAddrHeaderName;

    @Override
    public void destroy() {
        // nothing to do here
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain) throws IOException, ServletException {
        try {
            final ClientInfo clientInfo =
                    new ClientInfo((HttpServletRequest) request,
                            this.alternateServerAddrHeaderName,
                            this.alternateLocalAddrHeaderName,
                            this.useServerHostAddress);
            ClientInfoHolder.setClientInfo(clientInfo);
            filterChain.doFilter(request, response);
        } finally {
            ClientInfoHolder.clear();
        }
    }

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        this.alternateLocalAddrHeaderName = filterConfig.getInitParameter(CONST_IP_ADDRESS_HEADER);
        this.alternateServerAddrHeaderName = filterConfig.getInitParameter(CONST_SERVER_IP_ADDRESS_HEADER);
        String useServerHostAddr = filterConfig.getInitParameter(CONST_USE_SERVER_HOST_ADDRESS);
        if (useServerHostAddr != null && !useServerHostAddr.isEmpty()) {
            this.useServerHostAddress = Boolean.valueOf(useServerHostAddr);
        }
    }
}
