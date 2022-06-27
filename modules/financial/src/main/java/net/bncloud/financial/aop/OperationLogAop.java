package net.bncloud.financial.aop;

import lombok.extern.slf4j.Slf4j;
import net.bncloud.base.BaseUserEntity;
import net.bncloud.financial.annotation.OperationLog;
import net.bncloud.financial.entity.FinancialOperationLog;
import net.bncloud.financial.service.FinancialOperationLogService;
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

/**
 * 操作日志AOP
 *
 * @author ddh
 * @version 1.0.0
 * @date 2021/12/20
 */
@Aspect
@Component
@Slf4j
public class OperationLogAop {

    @Autowired
    private FinancialOperationLogService operationLogService;

    @Pointcut(value = "@annotation(net.bncloud.financial.annotation.OperationLog)")
    public void operationLog() {
    }

    @After("operationLog()")
    public void recordLog(JoinPoint joinPoint) {
        try {
            Long billId = (Long) joinPoint.getArgs()[0];
            //获取当前登录信息
            BaseUserEntity user = AuthUtil.getUser();
            Signature signature = joinPoint.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Object target = joinPoint.getTarget();
            Method method = target.getClass().getMethod(methodSignature.getName(),
                    methodSignature.getParameterTypes());
            OperationLog annotation = method.getAnnotation(OperationLog.class);
            FinancialOperationLog financialOperationLog = new FinancialOperationLog()
                    .setBillId(billId)
                    .setBillType(annotation.billType())
                    .setContent(annotation.content())
                    .setOperationNo(user.getUserId())
                    .setOperatorName(user.getUserName());

            operationLogService.save(financialOperationLog);
        } catch (Exception e) {
            log.error("记录操作日志异常", e);
        }

    }
}
