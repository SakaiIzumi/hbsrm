package net.bncloud.common.exception;

/**
 * 登录异常
 */
public class LoginException extends RuntimeException{

    private static final long serialVersionUID = -2895372858955701831L;
    private final int resultCode;
    private final String resultMsg;

    public LoginException(int resultCode, String resultMsg) {
        super(resultMsg);
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }


    public LoginException(int resultCode, String resultMsg, Throwable cause) {
        super(resultMsg, cause);
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    public int getResultCode() {
        return this.resultCode;
    }
    
    public String getResultMsg() {
        return this.resultMsg;
    }
}
