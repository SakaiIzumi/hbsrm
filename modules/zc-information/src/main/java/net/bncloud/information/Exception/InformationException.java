package net.bncloud.information.Exception;

import net.bncloud.common.exception.BizException;
import net.bncloud.information.Enum.InformationResultCode;

public class InformationException extends BizException {
    private static final long serialVersionUID = -5055956980216281491L;

    public InformationException() {
        super(InformationResultCode.VALIDATION_MISMATCH);
    }

    public InformationException(Throwable cause) {
        super(InformationResultCode.VALIDATION_MISMATCH, cause);
    }
}
