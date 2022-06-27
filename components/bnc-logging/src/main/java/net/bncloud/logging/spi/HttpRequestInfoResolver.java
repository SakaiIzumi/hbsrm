package net.bncloud.logging.spi;

import org.aspectj.lang.JoinPoint;

public interface HttpRequestInfoResolver {

    HttpRequestInfo resolveFrom(JoinPoint target);
}
