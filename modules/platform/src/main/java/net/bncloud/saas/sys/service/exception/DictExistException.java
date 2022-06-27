package net.bncloud.saas.sys.service.exception;

import net.bncloud.common.exception.BizException;
import net.bncloud.saas.result.SaaSResultCode;

public class DictExistException extends BizException {
    private static final long serialVersionUID = -5055956980216281491L;

    public DictExistException() {
        super(SaaSResultCode.DICT_EXIST_ERROR);
    }

    public DictExistException(Throwable cause) {
        super(SaaSResultCode.DICT_EXIST_ERROR, cause);
    }
}
