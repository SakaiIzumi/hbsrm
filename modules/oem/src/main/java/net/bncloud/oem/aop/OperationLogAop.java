package net.bncloud.oem.aop;

import lombok.extern.slf4j.Slf4j;
import net.bncloud.base.BaseUserEntity;
import net.bncloud.oem.annotation.OperationLog;
import net.bncloud.oem.service.OperationLogService;
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
import java.util.Date;

/**
 * @ClassName OperationLogAOP
 * @Description: 操作日志AOP
 * @Author liyh
 * @Date 2021/4/24
 * @Version V1.0
 **/
@Aspect
@Component
@Slf4j
public class OperationLogAop {

    @Autowired
    private OperationLogService operationLogService;

    @Pointcut(value = "@annotation(net.bncloud.oem.annotation.OperationLog)")
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
            net.bncloud.oem.domain.entity.OperationLog log=net.bncloud.oem.domain.entity.OperationLog.builder().billId(contractId)
//                    .content(annotation.content())
//                    .operationNo(user.getUserId())
                    .operatorName(user.getUserName())
                    .build();
            log.setCreatedDate(new Date());
            operationLogService.save(log);
        } catch (Exception e) {
            log.error("记录oem操作日志异常",e);
        }

    }
}
