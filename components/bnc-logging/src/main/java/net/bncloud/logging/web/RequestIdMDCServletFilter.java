package net.bncloud.logging.web;

import net.bncloud.logging.LogConstants;
import org.slf4j.MDC;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class RequestIdMDCServletFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            String requestId = UUID.randomUUID().toString().replaceAll("-", "");
            MDC.put(LogConstants.REQUEST_ID_KEY, requestId);
            ((HttpServletResponse) servletResponse).addHeader(LogConstants.REQUEST_ID_KEY, requestId);

            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.clear();
        }
    }
}
