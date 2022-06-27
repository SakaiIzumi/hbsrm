package net.bncloud.common.exception;

import net.bncloud.common.api.ResultCode;

/**
 * 自定义API业务异常
 */
public class ApiException extends RuntimeException{

    private static final long serialVersionUID = -2895372858955701831L;
    private final int resultCode;
    private final String resultMsg;

    public ApiException(int resultCode,String resultMsg) {
        super(resultMsg);
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
    

    public ApiException(int resultCode,String resultMsg, Throwable cause) {
        super(resultMsg, cause);
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    public ApiException(String message){

        super( message);
        this.resultCode  = ResultCode.FAILURE.getCode();
        this.resultMsg = message;

    }

    public int getResultCode() {
        return this.resultCode;
    }
    
    public String getResultMsg() {
        return this.resultMsg;
    }
}
