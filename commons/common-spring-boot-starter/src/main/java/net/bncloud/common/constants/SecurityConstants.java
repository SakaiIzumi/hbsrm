package net.bncloud.common.constants;

public interface SecurityConstants {
    /**
     * 传递的attribute前缀
     */
    String ATTRIBUTE_PREFFIX = "security_";
    /** 加密request */
    String SECURITY_REQUEST = "req_security";

    String REDIS_TOKEN_STORE_PREFIX = "BNC:";
}
