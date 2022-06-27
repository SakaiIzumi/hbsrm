package net.bncloud.common.exception;

import net.bncloud.common.api.IResultCode;

public class BizException extends RuntimeException{

    private static final long serialVersionUID = -2895372858955701831L;
    private final IResultCode resultCode;

    public BizException(IResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }
    

    public BizException(IResultCode resultCode, Throwable cause) {
        super(resultCode.getMessage(), cause);
        this.resultCode = resultCode;
    }

    public IResultCode getResultCode() {
        return this.resultCode;
    }
}
