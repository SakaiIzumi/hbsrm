package net.bncloud.logging.spi.support;

import net.bncloud.logging.spi.HttpRequestInfo;
import net.bncloud.logging.spi.HttpRequestInfoResolver;
import org.aspectj.lang.JoinPoint;

public class DefaultHttpRequestInfoResolver implements HttpRequestInfoResolver {
    @Override
    public HttpRequestInfo resolveFrom(JoinPoint target) {
        Class declaringType = target.getSignature().getDeclaringType();
        System.out.println(declaringType);
        return new HttpRequestInfo();
    }
}
