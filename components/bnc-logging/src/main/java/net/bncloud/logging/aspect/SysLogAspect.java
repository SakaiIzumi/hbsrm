package net.bncloud.logging.aspect;

import net.bncloud.logging.LogManager;
import net.bncloud.logging.annotation.SysLog;
import net.bncloud.logging.context.LogContext;
import net.bncloud.logging.context.RequestContext;
import net.bncloud.logging.context.ResponseContext;
import net.bncloud.logging.extractor.LogContextExtractor;
import net.bncloud.logging.extractor.LogContextExtractorManager;
import net.bncloud.logging.extractor.DefaultLogContextExtractor;
import net.bncloud.logging.spi.Principal;
import net.bncloud.logging.spi.PrincipalResolver;
import net.bncloud.logging.spi.support.SpringSecurityPrincipalResolver;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.List;

@Aspect
public class SysLogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysLogAspect.class);

    private final LogContextExtractorManager logContextExtractorManager;

    private final LogContextExtractor defaultLogContextExtractor = new DefaultLogContextExtractor();

    private final List<LogManager> logManagers;

    private PrincipalResolver principalResolver = new SpringSecurityPrincipalResolver();

    @Value("${spring.application.name}")
    private String application;

    private boolean failOnLogFailures = false;

    public SysLogAspect(LogContextExtractorManager logContextExtractorManager, List<LogManager> logManagers) {
        this.logContextExtractorManager = logContextExtractorManager;
        this.logManagers = logManagers;
    }

    public void setFailOnLogFailures(boolean failOnLogFailures) {
        this.failOnLogFailures = failOnLogFailures;
    }

    /**
     * Pointcut that matches all repositories, services and Web REST endpoints.
     */
    /*@Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)")
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    @Pointcut("within(@org.springframework.stereotype.Controller *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }*/

    @Pointcut("@annotation(net.bncloud.logging.annotation.SysLog)")
    public void sysLogAnnotationPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

//    @Around("controllerPointcut() || (springBeanPointcut() && sysLogAnnotationPointcut())")
    @Around("sysLogAnnotationPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        SysLog sysLog = getSysLogAnnotation(joinPoint);
        if (sysLog == null || !sysLog.enable()) {
            return joinPoint.proceed();
        }
        LogContextExtractor logContextExtractor = null;
        for (LogContextExtractor extractor : logContextExtractorManager.getExtractors()) {
            if (extractor.supports(joinPoint)) {
                logContextExtractor = extractor;
            }
        }
        if (logContextExtractor == null) {
            logContextExtractor = defaultLogContextExtractor;
        }
        RequestContext requestContext = logContextExtractor.extractRequestFrom(joinPoint, sysLog);

        final Principal principal = principalResolver.resolve();
        requestContext.setPrincipal(principal);

        ResponseContext responseContext = null;
        Instant start = Instant.now();
        try {

            Object retObj = joinPoint.proceed();

            responseContext = logContextExtractor.extractResponseFrom(joinPoint, retObj, sysLog);
            return retObj;
        } catch (Throwable t) {
            final Exception e = wrapIfNecessary(t);
            responseContext = logContextExtractor.extractResponseFrom(joinPoint, e);
            throw t;
        } finally {
            executeLog(requestContext, responseContext, start, Instant.now());
        }
    }

    private void executeLog(RequestContext requestContext, ResponseContext responseContext, Instant requestAt, Instant responseAt) {

        requestContext.setApplication(this.application);

        LogContext actionContext = new LogContext(requestContext, responseContext, requestAt, responseAt);

        for (LogManager logManager : this.logManagers) {
            try {
                logManager.record(actionContext);
            } catch (final Exception e) {
                if (this.failOnLogFailures) {
                    throw e;
                }
                LOGGER.error("Failed to record trail context for " + actionContext.getAction()
                        + " and principal " + actionContext.getPrincipal(), e);
            }
        }
    }


    /**
     * 尝试获取切面的SysLog注解
     */
    private SysLog getSysLogAnnotation(JoinPoint point) {
        try {
            SysLog annotation = null;
            if (point.getSignature() instanceof MethodSignature) {
                Method method = ((MethodSignature)point.getSignature()).getMethod();
                if (method != null) {
                    annotation = method.getAnnotation(SysLog.class);
                }
            }
            return annotation;
        } catch (Exception e) {
            LOGGER.warn("获取 {}.{} 的 @Log 注解发生异常", point.getSignature().getDeclaringTypeName(), point.getSignature().getName(), e);
            return null;
        }
    }

    private Exception wrapIfNecessary(final Throwable t) {
        return t instanceof Exception ? (Exception) t : new Exception(t);
    }
}
