package net.bncloud.logging.spi.support;

import net.bncloud.logging.spi.ClientInfoResolver;
import net.bncloud.logging.web.ClientInfo;
import net.bncloud.logging.web.ClientInfoHolder;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultClientInfoResolver implements ClientInfoResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultClientInfoResolver.class);

    @Override
    public ClientInfo resolveFrom(JoinPoint joinPoint, Object retVal) {
        final ClientInfo clientInfo = ClientInfoHolder.getClientInfo();
        if (clientInfo != null) {
            return clientInfo;
        }

        LOGGER.warn("No ClientInfo could be found. Returning empty ClientInfo object.");

        return ClientInfo.EMPTY_CLIENT_INFO;
    }
}
