package net.bncloud.logging.service;

import cn.hutool.http.useragent.UserAgent;
import com.alibaba.fastjson.JSON;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.logging.context.LogContext;
import net.bncloud.logging.domain.Browser;
import net.bncloud.logging.domain.SysLog;
import net.bncloud.logging.repository.SysLogRepository;
import net.bncloud.logging.service.query.SysLogQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class SysLogService implements DisposableBean {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private boolean defaultExecutorService = true;
    private boolean asynchronous = true;

    private final SysLogRepository sysLogRepository;

    public SysLogService(SysLogRepository sysLogRepository) {
        this.sysLogRepository = sysLogRepository;
    }

    public void receiveSysLog(String json) {
        final LogContext logContext = JSON.parseObject(json, LogContext.class);

        final LoggingTask task = new LoggingTask(logContext, this.sysLogRepository);
        if (this.asynchronous) {
            this.executorService.execute(task);
        } else {
            task.run();
        }
    }

    protected static class LoggingTask implements Runnable {
        private final LogContext logContext;
        private final SysLogRepository sysLogRepository;

        public LoggingTask(LogContext logContext, SysLogRepository sysLogRepository) {
            this.logContext = logContext;
            this.sysLogRepository = sysLogRepository;
        }

        @Override
        public void run() {
            SysLog log = new SysLog();
            log.setRequestId(logContext.getRequestId());
            log.setUserId(logContext.getPrincipal().getUserId());
            log.setLogin(logContext.getPrincipal().getLogin());
            log.setName(logContext.getPrincipal().getName());
            log.setApplication(logContext.getApplication());
            log.setModule(logContext.getModule());
            log.setAction(logContext.getAction());
            log.setResource(logContext.getResource());
            log.setClientIp(logContext.getClientIp());
            log.setServerIp(logContext.getServerIp());
            log.setUri(logContext.getUri());
            log.setHttpMethod(logContext.getHttpMethod());
            log.setClassName(logContext.getClassName());
            log.setClassMethod(logContext.getMethod());
            log.setRequestAt(logContext.getRequestAt());
            log.setResponseAt(logContext.getResponseAt());
            log.setSuccess(logContext.getSuccess());
            log.setDuration(logContext.getDuration().toMillis());
            final UserAgent userAgent = logContext.getUserAgent();
            if (userAgent != null) {
                Browser browser = new Browser();
                Optional.ofNullable(userAgent.getBrowser()).ifPresent(b -> {
                    browser.setBrowserName(b.getName());
                });
                Optional.ofNullable(userAgent.getEngine()).ifPresent(e -> {
                    browser.setBrowserEngine(e.getName());
                });
                Optional.ofNullable(userAgent.getEngineVersion()).ifPresent(browser::setBrowserEngineVersion);
                log.setBrowser(browser);

                Optional.of(userAgent.getOs()).ifPresent(os -> {
                    log.setOsName(os.getName());
                });
                log.setMobile(userAgent.isMobile());
            }

            log.setRequest(logContext.getRequest());
            log.setResponse(logContext.getResponse());
            log.setException(logContext.getException());
            sysLogRepository.save(log);
        }
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
        this.defaultExecutorService = false;
    }

    public void setAsynchronous(boolean asynchronous) {
        this.asynchronous = asynchronous;
    }

    @Override
    public void destroy() throws Exception {
        if (this.defaultExecutorService) {
            this.executorService.shutdown();
        }
    }

    public Page<SysLog> pageQuery(QueryParam<SysLogQuery> queryParam, Pageable pageable) {
        final SysLogQuery param = queryParam.getParam();
        return sysLogRepository.findAll((Specification<SysLog>) (root, query, criteriaBuilder) -> {
            final Predicate predicate = criteriaBuilder.conjunction();
            if (param == null) {
                return predicate;
            }
            if (StringUtils.isNotBlank(param.getApplication())) {
                predicate.getExpressions().add(criteriaBuilder.equal(root.get("application"), param.getApplication()));
            }
            if (StringUtils.isNotBlank(param.getAction())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("action"), "%" + param.getAction() + "%"));
            }
            if (StringUtils.isNotBlank(param.getName())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("name"), "%" + param.getName() + "%"));
            }
            if (StringUtils.isNotBlank(param.getLogin())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("login"), "%" + param.getLogin() + "%"));
            }
            if (StringUtils.isNotBlank(param.getUri())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("uri"), "%" + param.getUri() + "%"));
            }
            if (StringUtils.isNotBlank(param.getHttpMethod())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("httpMethod"), "%" + param.getHttpMethod() + "%"));
            }
            if (StringUtils.isNotBlank(param.getClassName())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("className"), "%" + param.getClassName() + "%"));
            }
            if (StringUtils.isNotBlank(param.getClassMethod())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("classMethod"), "%" + param.getClassMethod() + "%"));
            }
            if (StringUtils.isNotBlank(param.getBrowserName())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("browser.name"), "%" + param.getBrowserName() + "%"));
            }
            if (StringUtils.isNotBlank(param.getOsName())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("osName"), "%" + param.getOsName() + "%"));
            }
            if (param.getMobile() != null) {
                predicate.getExpressions().add(param.getMobile() ? criteriaBuilder.isTrue(root.get("mobile")) : criteriaBuilder.isFalse(root.get("mobile")));
            }
            if (param.getStart() != null) {
                predicate.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.get("requestAt"), param.getStart()));
            }
            if (param.getEnd() != null) {
                predicate.getExpressions().add(criteriaBuilder.lessThanOrEqualTo(root.get("requestAt"), param.getEnd()));
            }

            return predicate;
        }, pageable);
    }

    public SysLog findById(Long id) {
        return sysLogRepository.findById(id).orElse(null);
    }
}
