package net.bncloud.common.exception;

import net.bncloud.common.api.IResultCode;

/**
 * 自定义API业务异常
 */
public class CheckException extends RuntimeException{

    private static final long serialVersionUID = -2895372858955701831L;

    private final IResultCode resultCode;
    private boolean success;



    public CheckException(IResultCode resultCode,boolean success) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
        this.success = success;
    }


    public CheckException(IResultCode resultCode, Throwable cause,boolean success) {
        super(resultCode.getMessage(), cause);
        this.resultCode = resultCode;
        this.success = success;
    }
    public IResultCode getResultCode() {
        return this.resultCode;
    }


    public boolean isSuccess() {
        return success;
    }


}
