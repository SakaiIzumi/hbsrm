package net.bncloud.logging;

public interface LogConstants {

    String ACTION_POSTFIX_SUCCESS = "_SUCCESS";

    String ACTION_POSTFIX_CREATED = "_CREATED";

    String ACTION_POSTFIX_FAILED = "_FAILED";

    String REQUEST_ID_KEY = "RequestId";

    String REDIS_CHANNEL_SYS_LOG = "bncloud-sys-log";

    String REDIS_CHANNEL_LOGIN_LOG = "bncloud-login-log";
}
