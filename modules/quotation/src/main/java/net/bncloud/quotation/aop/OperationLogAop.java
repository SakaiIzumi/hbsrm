package net.bncloud.quotation.aop;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.base.BaseUserEntity;
import net.bncloud.common.security.BncUserDetails;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.quotation.annotation.OperationLog;
import net.bncloud.quotation.entity.QuotationOperationLog;
import net.bncloud.quotation.service.IQuotationOperationLogService;
import net.bncloud.utils.AuthUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.OffsetDateTime;
import java.util.Date;

/**
 * @ClassName OperationLogAOP
 * @Description: 操作日志AOP
 * @Author Administrator
 * @Date 2021/3/16
 * @Version V1.0
 **/
@Aspect
@Component
@Slf4j
public class OperationLogAop {

    @Autowired
    private IQuotationOperationLogService operationLogService;

    @Pointcut(value = "@annotation(net.bncloud.quotation.annotation.OperationLog)")
    public void operationLog() {
    }

    @After("operationLog()")
    public void recordLog(JoinPoint joinPoint) {
        try {
            //默认第一个参数为BaseId
            Long contractId = (Long)joinPoint.getArgs()[0];
            //获取当前登录信息
            BaseUserEntity user = AuthUtil.getUser();
            Signature signature = joinPoint.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Object target = joinPoint.getTarget();
            Method method = target.getClass().getMethod(methodSignature.getName(),
                    methodSignature.getParameterTypes());
            OperationLog annotation = method.getAnnotation(OperationLog.class);
            QuotationOperationLog operationLog = QuotationOperationLog.builder().billId(contractId)
                    .content(annotation.content())
                    .operationNo(user.getUserId())
                    .operatorName(user.getUserName())
                    .build();
            operationLog.setCreatedDate(new Date());
            operationLogService.save(operationLog);
        } catch (Exception e) {
            log.error("记录询价操作日志异常",e);
        }

    }
}
