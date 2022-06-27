package net.bncloud.logging.extractor;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.alibaba.fastjson.JSON;
import net.bncloud.common.util.ThrowableUtils;
import net.bncloud.logging.annotation.SysLog;
import net.bncloud.logging.context.RequestContext;
import net.bncloud.logging.context.ResponseContext;
import net.bncloud.logging.spi.ClientInfoResolver;
import net.bncloud.logging.spi.Principal;
import net.bncloud.logging.spi.PrincipalResolver;
import net.bncloud.logging.spi.support.DefaultClientInfoResolver;
import net.bncloud.logging.spi.support.SpringSecurityPrincipalResolver;
import net.bncloud.logging.util.AopUtils;
import net.bncloud.logging.web.ClientInfo;
import net.bncloud.logging.web.ClientInfoHolder;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

public abstract class AbstractLogContextExtractor implements LogContextExtractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractLogContextExtractor.class);

    protected ClientInfoResolver clientInfoResolver = new DefaultClientInfoResolver();
    protected PrincipalResolver principalResolver = new SpringSecurityPrincipalResolver();
    @Override
    public RequestContext extractRequestFrom(JoinPoint joinPoint, SysLog sysLog) {
        RequestContext requestContext = new RequestContext();
        ClientInfo clientInfo = clientInfoResolver.resolveFrom(joinPoint, sysLog);
        if (clientInfo != null) {
            requestContext.setClientIp(clientInfo.getClientAddress());
            requestContext.setServerIp(clientInfo.getServerAddress());
        }
        Principal principal = principalResolver.resolveFrom(joinPoint, sysLog);
        LOGGER.info("获取用户信息: {}", principal);
        requestContext.setPrincipal(principal);

        extractRequestInfo(requestContext, joinPoint, sysLog);
        return requestContext;
    }

    protected void extractRequestInfo(RequestContext requestContext, JoinPoint joinPoint, SysLog sysLog) {
        String className = joinPoint.getTarget().getClass().getName();
        requestContext.setClassName(className);
        requestContext.setMethod(AopUtils.extractMethodToString(joinPoint));
        requestContext.setRequestParam(AopUtils.extractMethodWithParamToString(joinPoint));
        requestContext.setModule(sysLog.module());
        requestContext.setAction(resolveAction(joinPoint, sysLog));
        requestContext.setResource(resolveResource(joinPoint, sysLog));
        HttpServletRequest httpServletRequest = getHttpServletRequest();
        if (httpServletRequest != null) {
            requestContext.setUri(httpServletRequest.getRequestURI());
            requestContext.setHttpMethod(httpServletRequest.getMethod());
            final UserAgent userAgent = UserAgentUtil.parse(httpServletRequest.getHeader("User-Agent"));
            requestContext.setUserAgent(userAgent);
        }
    }

    @Override
    public ResponseContext extractResponseFrom(JoinPoint joinPoint, Object retVal, SysLog sysLog) {
        boolean ignoreResponse = false;
        if (sysLog != null) {
            ignoreResponse = sysLog.ignoreResponse();
        }
        String response;
        if (ignoreResponse) {
            response = "此次操作日志忽略返回值记录";
        } else {
            response = JSON.toJSONString(retVal);
        }
        ResponseContext responseContext = new ResponseContext();
        responseContext.setResponse(response);
        responseContext.setResponseAt(Instant.now());
        responseContext.setSuccess(true);
        return responseContext;
    }

    @Override
    public ResponseContext extractResponseFrom(JoinPoint joinPoint, Exception exception) {
        ResponseContext responseContext = new ResponseContext();
        responseContext.setResponse(ThrowableUtils.toString(exception));
        responseContext.setResponseAt(Instant.now());
        responseContext.setSuccess(false);
        return responseContext;
    }


    protected ClientInfo resolveClientInfo(JoinPoint joinPoint) {
        return ClientInfoHolder.getClientInfo();
    }

    protected String resolveAction(JoinPoint joinPoint, SysLog sysLog) {
        if (sysLog != null) {
            if (StringUtils.isNotBlank(sysLog.action())) {
                return sysLog.action();
            }
        }
        return AopUtils.extractAction(joinPoint);
    }

    protected String resolveResource(JoinPoint joinPoint, SysLog sysLog) {
        if (sysLog != null && StringUtils.isNotBlank(sysLog.resource())) {
            return sysLog.resource();
        }

        return AopUtils.extractMethodToString(joinPoint);
    }

    protected HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        return (requestAttributes).getRequest();
    }

}
