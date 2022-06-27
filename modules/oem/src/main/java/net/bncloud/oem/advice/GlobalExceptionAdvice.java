package net.bncloud.oem.advice;

import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.common.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @ClassName GlobalExceptionAdvice
 * @Description: 异常通知
 * @Author liyh
 * @Date 2021/4/26
 * @Version V1.0
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = Exception.class)
    public R exceptionThrowHandler(Exception e) {
       if(e instanceof ApiException){
           log.info("接口调用异常-->{}",e.getMessage());
           return R.fail(e.getMessage());
       }
       if(e instanceof RuntimeException){
           log.info("运行时异常-->{}",e.getMessage());
           return R.fail(e.getMessage());
       }
       log.error("服务器异常-->",e);
       return R.fail("服务器异常，请稍后重试");
    }

}
