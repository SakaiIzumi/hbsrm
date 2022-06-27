package net.bncloud.logging.spi;

import net.bncloud.logging.web.ClientInfo;
import org.aspectj.lang.JoinPoint;

public interface ClientInfoResolver {

    ClientInfo resolveFrom(JoinPoint joinPoint, Object retVal);
}
