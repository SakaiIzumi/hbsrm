package net.bncloud.common.base.repeatrequest;

import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.security.BncUserDetails;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author ddh
 * @version 1.0.0
 * @description 处理重复请求操作Aop
 * @since 2022/2/28
 */
@Aspect
@Component
@Slf4j
public class HandleRepeatRequestAop {


    private final StringRedisTemplate stringRedisTemplate;
    public HandleRepeatRequestAop(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Around(value = "@annotation(net.bncloud.common.base.repeatrequest.RepeatRequestOperation)")
    public Object joinPointProcess(ProceedingJoinPoint pjp) throws Throwable {

        Optional<BncUserDetails> currentUserOpt = SecurityUtils.getCurrentUser();
        if (! currentUserOpt.isPresent()) {
            return pjp.proceed();
        }

        BncUserDetails bncUserDetails = currentUserOpt.get();
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        String simpleName = methodSignature.getClass().getSimpleName();
        String methodName = simpleName + "_"+ methodSignature.getMethod().getName();
        // 去重key
        String key = bncUserDetails.getId() + "_" + methodName;

        Boolean lockSuccess = Optional.ofNullable(stringRedisTemplate.opsForValue().setIfAbsent(key, Thread.currentThread().getId() + "", 30, TimeUnit.SECONDS)).orElse(false);
        if( lockSuccess){
            try {
                return pjp.proceed();
            }finally {
                stringRedisTemplate.delete( key );
            }
        }
        throw new ApiException(500, "重复请求，请稍后再试！");
    }
}
