package net.bncloud.delivery.aop;

import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.security.BncUserDetails;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.delivery.annotation.OperationLog;
import net.bncloud.delivery.entity.DeliveryOperationLog;
import net.bncloud.delivery.service.DeliveryOperationLogService;
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
    private DeliveryOperationLogService deliveryOperationLogService;

    @Pointcut(value = "@annotation(net.bncloud.delivery.annotation.OperationLog)")
    public void operationLog() {
    }

    @After("operationLog()")
    public void recordLog(JoinPoint joinPoint) {
        try {
            Long id = (Long)joinPoint.getArgs()[0];
            //获取当前登录信息
            BncUserDetails currentUser = SecurityUtils.getCurrentUser().get();
            Signature signature = joinPoint.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Object target = joinPoint.getTarget();
            Method method = target.getClass().getMethod(methodSignature.getName(),
                    methodSignature.getParameterTypes());
            OperationLog annotation = method.getAnnotation(OperationLog.class);
            DeliveryOperationLog operationLog = DeliveryOperationLog.builder().billId(id)
                    .operatorContent(annotation.content())
                    .operatorNo(currentUser.getId().toString())
                    .operatorName(currentUser.getName()).build();
            deliveryOperationLogService.save(operationLog);
        } catch (Exception e) {
            log.error("记录送货通知操作日志异常",e);
        }

    }
}
