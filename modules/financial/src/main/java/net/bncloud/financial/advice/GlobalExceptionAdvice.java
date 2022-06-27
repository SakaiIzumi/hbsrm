package net.bncloud.financial.advice;

import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @ClassName GlobalExceptionAdvice
 * @Description: 异常通知
 * @Author Administrator
 * @Date 2021/3/15
 * @Version V1.0
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {

    @Value("${application.validation.field-show:false}")
    private boolean showField;

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R validException(MethodArgumentNotValidException e) {
        //验证请求的参数合法性
        FieldError fieldError = e.getBindingResult().getFieldError();
        String field = fieldError.getField();
        String defaultMessage = fieldError.getDefaultMessage();
        if (showField) {
            defaultMessage = "[" + field + "]" + defaultMessage;
            log.info(defaultMessage);
        }
        return R.fail(defaultMessage);
    }


}
