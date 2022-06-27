package net.bncloud.saas.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 武书静 wusj4 shujing.wu@meicloud.com
 */
@ConfigurationProperties(prefix = "bncloud.security.data")
public class DataSecurityProperties {

    private String appCode;

    private static final String DEFAULT_CACHE_KEY_PREFIX = "BNCLOUD:SECURITY:DATA";

    private String cacheKeyPrefix = DEFAULT_CACHE_KEY_PREFIX;

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getCacheKeyPrefix() {
        return cacheKeyPrefix;
    }

    public void setCacheKeyPrefix(String cacheKeyPrefix) {
        this.cacheKeyPrefix = cacheKeyPrefix;
    }
}
