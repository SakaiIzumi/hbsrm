package net.bncloud.logging.service;

import com.alibaba.fastjson.JSON;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.logging.context.LoginLogContext;
import net.bncloud.logging.domain.Browser;
import net.bncloud.logging.domain.LoginLog;
import net.bncloud.logging.repository.LoginLogRepository;
import net.bncloud.logging.service.query.LoginLogQuery;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.Optional;

@Service
public class LoginLogService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginLogService.class);

    private final LoginLogRepository loginLogRepository;

    public LoginLogService(LoginLogRepository loginLogRepository) {
        this.loginLogRepository = loginLogRepository;
    }

    public void receiveLoginLog(String json) {
        LOGGER.info("接收到登录日志: {}", json);
        final LoginLogContext logContext = JSON.parseObject(json, LoginLogContext.class);
        LoginLog log = new LoginLog();
        log.setLoginType(logContext.getLoginType());
        log.setLogin(logContext.getLogin());
        log.setName(logContext.getName());
        log.setMobile(logContext.getMobile());
        log.setRequestIp(logContext.getRequestIp());
        log.setLoginAt(logContext.getLoginAt());

        Optional.ofNullable(logContext.getUserAgent()).ifPresent(userAgent -> {
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
            log.setMobileClient(userAgent.isMobile());
        });
        log.setSuccess(logContext.getSuccess());
        log.setToken(logContext.getToken());
        log.setFailReason(logContext.getFailReason());
        save(log);
    }

    @Transactional
    public void save(LoginLog log) {
        LOGGER.info("保存登录日志，接收参数:{}",JSON.toJSONString(log));
        loginLogRepository.save(log);
    }

    public Page<LoginLog> pageQuery(QueryParam<LoginLogQuery> queryParam, Pageable pageable) {
        final LoginLogQuery param = queryParam.getParam();
        return loginLogRepository.findAll((Specification<LoginLog>) (root, query, criteriaBuilder) -> {
            final Predicate predicate = criteriaBuilder.conjunction();
            if (param == null) {
                return predicate;
            }
            if (StringUtils.isNotBlank(param.getName())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("name"), "%" + param.getName() + "%"));
            }
            if (StringUtils.isNotBlank(param.getMobile())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("mobile"), "%" + param.getMobile() + "%"));
            }
            if (param.getMobileClient() != null) {
                predicate.getExpressions().add(param.getMobileClient()?criteriaBuilder.isTrue(root.get("mobileClient")):criteriaBuilder.isFalse(root.get("mobile")));
            }
            if (param.getSuccess() != null) {
                predicate.getExpressions().add(param.getMobileClient()?criteriaBuilder.isTrue(root.get("success")):criteriaBuilder.isFalse(root.get("mobile")));
            }
            if (param.getStart() != null) {
                predicate.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.get("loginAt"), param.getStart()));
            }
            if (param.getEnd() != null) {
                predicate.getExpressions().add(criteriaBuilder.lessThanOrEqualTo(root.get("loginAt"), param.getEnd()));
            }
            return predicate;
        }, pageable);
    }
}
