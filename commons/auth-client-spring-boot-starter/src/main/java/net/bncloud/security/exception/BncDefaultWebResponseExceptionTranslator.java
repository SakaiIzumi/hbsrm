package net.bncloud.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.util.Map;

public class BncDefaultWebResponseExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {
    private final static String BAD_CREDENTIALS = "Bad credentials";
    private final static String USER_IS_DISABLED = "User is disabled";

    private final ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        Throwable[] causeChain = throwableAnalyzer.determineCauseChain(e);
        /*Exception ase = (AuthenticationException) throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class, causeChain);
        if (ase != null) {
            return handleOAuth2Exception(new UnauthorizedException(messages.getMessage(MessageCode.unauthorized, "请登录"), e));
        }

        ase = (AccessDeniedException) throwableAnalyzer
                .getFirstThrowableOfType(AccessDeniedException.class, causeChain);
        if (ase != null) {
            return handleOAuth2Exception(new ForbiddenException(ase.getMessage(), ase));
        }

        ase = (InvalidGrantException) throwableAnalyzer.getFirstThrowableOfType(InvalidGrantException.class, causeChain);
        if (ase != null) {
            if (BAD_CREDENTIALS.equals(ase.getMessage())) {
                return handleOAuth2Exception(new BadCredentialsException(
                        messages.getMessage(MessageCode.AccountOrPasswordError, "账号或密码错误"), ase));
            } else if (USER_IS_DISABLED.equals(ase.getMessage())) {
                return handleOAuth2Exception(new UserDisabledException(
                        messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "用户账号不可用"), ase
                ));
            }
        }

        ase = (HttpRequestMethodNotSupportedException) throwableAnalyzer.getFirstThrowableOfType(
                HttpRequestMethodNotSupportedException.class, causeChain);
        if (ase != null) {
            return handleOAuth2Exception(new MethodNotAllowed(ase.getMessage(), ase));
        }

        ase = (OAuth2Exception) throwableAnalyzer.getFirstThrowableOfType(OAuth2Exception.class, causeChain);

        if (ase != null) {
            return handleOAuth2Exception(new BncOAuth2ExceptionAdapter((OAuth2Exception) ase));
        }

        return handleOAuth2Exception(new ServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e));*/
        Exception ase = (OAuth2Exception) throwableAnalyzer.getFirstThrowableOfType(OAuth2Exception.class, causeChain);

        if (ase != null) {
            return handleOAuth2Exception(new BncOAuth2ExceptionAdapter((OAuth2Exception) ase));
        }
        ase = (InsufficientAuthenticationException) throwableAnalyzer.getFirstThrowableOfType(InsufficientAuthenticationException.class, causeChain);
        if (ase != null) {
            return handleOAuth2Exception(new UnauthorizedException("访问此资源需要身份验证", e));
        }
        ase = (AuthenticationException) throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class,
                causeChain);
        if (ase != null) {
            return handleOAuth2Exception(new UnauthorizedException(e.getMessage(), e));
        }

        ase = (AccessDeniedException) throwableAnalyzer
                .getFirstThrowableOfType(AccessDeniedException.class, causeChain);
        if (ase != null) {
            return handleOAuth2Exception(new ForbiddenException(ase.getMessage(), ase));
        }

        ase = (HttpRequestMethodNotSupportedException) throwableAnalyzer.getFirstThrowableOfType(
                HttpRequestMethodNotSupportedException.class, causeChain);
        if (ase != null) {
            return handleOAuth2Exception(new MethodNotAllowed(ase.getMessage(), ase));
        }

        return handleOAuth2Exception(new ServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e));

    }

    private ResponseEntity<OAuth2Exception> handleOAuth2Exception(OAuth2Exception e) {
        e.printStackTrace();
        int status = e.getHttpErrorCode();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        if (status == HttpStatus.UNAUTHORIZED.value() || (e instanceof InsufficientScopeException)) {
            headers.set("WWW-Authenticate", String.format("%s %s", OAuth2AccessToken.BEARER_TYPE, e.getSummary()));
        }

        return new ResponseEntity<>(e, headers, HttpStatus.valueOf(status));
    }

    @JsonSerialize(using = BncOAuth2ExceptionJackson2Serializer.class)
    private static class BadCredentialsException extends OAuth2Exception {

        private static final long serialVersionUID = -3370373704637883666L;

        public BadCredentialsException(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return BAD_CREDENTIALS;
        }

        @Override
        public int getHttpErrorCode() {
            return 401;
        }

        @Override
        public String getSummary() {
            return "账号或密码错误";
        }
    }
    @JsonSerialize(using = BncOAuth2ExceptionJackson2Serializer.class)
    private static class UserDisabledException extends OAuth2Exception {

        private static final long serialVersionUID = -5305584025832645821L;

        public UserDisabledException(String msg, Throwable t) {
            super(msg, t);
        }
        @Override
        public String getOAuth2ErrorCode() {
            return USER_IS_DISABLED;
        }

        @Override
        public int getHttpErrorCode() {
            return 401;
        }

        @Override
        public String getSummary() {
            return "当前用户不可用";
        }
    }
    @JsonSerialize(using = BncOAuth2ExceptionJackson2Serializer.class)
    private static class ForbiddenException extends OAuth2Exception {

        private static final long serialVersionUID = 7961324962731663864L;

        public ForbiddenException(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "access_denied";
        }

        @Override
        public int getHttpErrorCode() {
            return 403;
        }

    }

    @JsonSerialize(using = BncOAuth2ExceptionJackson2Serializer.class)
    private static class ServerErrorException extends OAuth2Exception {

        private static final long serialVersionUID = 4868278112544337120L;

        public ServerErrorException(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "server_error";
        }

        @Override
        public int getHttpErrorCode() {
            return 500;
        }

    }

    @JsonSerialize(using = BncOAuth2ExceptionJackson2Serializer.class)
    private static class UnauthorizedException extends OAuth2Exception {

        private static final long serialVersionUID = -4090153111676637798L;

        public UnauthorizedException(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "unauthorized";
        }

        @Override
        public int getHttpErrorCode() {
            return 401;
        }

    }

    @JsonSerialize(using = BncOAuth2ExceptionJackson2Serializer.class)
    private static class MethodNotAllowed extends OAuth2Exception {

        private static final long serialVersionUID = -1254212449206925281L;

        public MethodNotAllowed(String msg, Throwable t) {
            super(msg, t);
        }

        @Override
        public String getOAuth2ErrorCode() {
            return "method_not_allowed";
        }

        @Override
        public int getHttpErrorCode() {
            return 405;
        }

    }

    @JsonSerialize(using = BncOAuth2ExceptionJackson2Serializer.class)
    private static class BncOAuth2ExceptionAdapter extends OAuth2Exception {
        private static final long serialVersionUID = -7098302534913333523L;

        private final OAuth2Exception exception;


        public BncOAuth2ExceptionAdapter(OAuth2Exception exception) {
            super(exception.getMessage(), exception.getCause());
            this.exception = exception;
        }

        public BncOAuth2ExceptionAdapter(String msg, OAuth2Exception exception) {
            super(msg, exception.getCause());
            this.exception = exception;
        }

        @Override
        public String getOAuth2ErrorCode() {
            return exception.getOAuth2ErrorCode();
        }

        @Override
        public int getHttpErrorCode() {
            return exception.getHttpErrorCode();
        }

        @Override
        public Map<String, String> getAdditionalInformation() {
            return exception.getAdditionalInformation();
        }

        @Override
        public String getSummary() {
            return exception.getSummary();
        }
    }
}
