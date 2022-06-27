package net.bncloud.logging.extractor;

import net.bncloud.logging.annotation.SysLog;
import net.bncloud.logging.context.RequestContext;
import net.bncloud.logging.context.ResponseContext;
import org.aspectj.lang.JoinPoint;

public interface LogContextExtractor {

    RequestContext extractRequestFrom(JoinPoint joinPoint, SysLog sysLog);

    ResponseContext extractResponseFrom(JoinPoint joinPoint, Object retVal, SysLog sysLog);

    ResponseContext extractResponseFrom(JoinPoint joinPoint, Exception exception);

    boolean supports(JoinPoint joinPoint);

}
