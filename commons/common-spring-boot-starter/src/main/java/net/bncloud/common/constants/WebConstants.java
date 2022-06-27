package net.bncloud.common.constants;

public interface WebConstants {

    /**
     * false字符串
     */
    String FALSE = "false";
    /**
     * true字符串
     */
    String TRUE = "true";

    String PROTOCOL_HTTP = "http://";

    String PROTOCOL_HTTPS = "https://";

    /**
     * 路径分隔符
     */
    String SPT = "/";
    /**
     * 数组分隔符
     */
    String ARRAY_SPT = ",";

    /**
     * UTF-8编码
     */
    String UTF8 = "UTF-8";
    /**
     * cookie中的JSESSIONID名称
     */
    String JSESSION_COOKIE = "JSESSIONID";
    /**
     * 微信openid
     */
    String WX_OPENID = "wxopenId";
    /**
     * HTTP POST请求
     */
    String POST = "POST";
    /**
     * HTTP GET请求
     */
    String GET = "GET";

    /**
     * HTTP OPTIONS请求
     */
    String OPTIONS = "OPTIONS";
    /**
     * 国际化 英文
     */
    String LAN_EN_US = "en_US";
    /**
     * 国际化 中文简体
     */
    String LAN_ZH_CN = "zh_CN";

    /**
     * 验证码key前缀
     */
    String KCAPTCHA_PREFIX = "kcaptcha_";

    String ADMIN_PREFIX = "/manager";
    String USER_PREFIX = "/member";
    String COMMON_PREFIX = "/common";
    String LOGIN_URL = "/login";

}
