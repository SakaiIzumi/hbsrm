package net.bncloud.common.exception;

import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.common.api.ResultCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@ControllerAdvice
public class ExceptionTranslator {

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<R<Void>> processRequestParameterException(HttpServletRequest request,
                                                                    HttpServletResponse response,
                                                                    MissingServletRequestParameterException e) {
        log.error("参数缺失：{}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(R.fail(400, "参数缺失"));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<R<Void>> httpMessageNotReadableException(HttpServletRequest request,
                                                                   HttpMessageNotReadableException e) {
        log.error("参数格式错误：{}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(R.fail(400, "参数格式错误"));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<R<Void>> HttpRequestMethodNotSupportedException(HttpServletRequest request,
                                                                          HttpRequestMethodNotSupportedException e) {
        log.error("请求方法不支持：{}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(R.fail(405, "请求方法不支持"));
    }

    @ExceptionHandler(BizException.class)
    public ResponseEntity<R<Void>> handleBizException(BizException e) {
        log.error("业务异常：{}", e.getMessage(), e);
        return ResponseEntity.ok(R.fail(e.getResultCode()));
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<R<Void>> handleApiException(ApiException e) {
        log.error("API调用异常：{}", e.getMessage(), e);
        return ResponseEntity.ok(R.fail(e.getResultCode(),e.getResultMsg()));
    }

    @ExceptionHandler(CheckException.class)
    public ResponseEntity<R<Void>> handleCheckException(CheckException e) {
        log.error("参数异常：{}", e.getMessage(), e);
        return ResponseEntity.ok(R.fail(e.getResultCode(),e.isSuccess()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<R<Void>> handleAuthenticationException(AuthenticationException e) {
        log.error("未认证：{}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(R.fail(ResultCode.UN_AUTHORIZED));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<R<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("系统异常：{}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(R.fail(ResultCode.FAILURE, e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<R<Void>> handleException(Exception e) {
        log.error("系统异常：{}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(R.fail(ResultCode.INTERNAL_SERVER_ERROR));
    }
}
