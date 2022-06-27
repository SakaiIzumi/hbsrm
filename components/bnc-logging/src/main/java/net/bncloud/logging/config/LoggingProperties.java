package net.bncloud.logging.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.logging")
public class LoggingProperties {

    private boolean enabled;

    /**
     * 获取server address的Http Request Header Name
     */
    private String alternateServerAddrHeaderName;

    /**
     * 获取client address的Http Request Header Name
     * <p>
     *  如果配置了反向代理，比如Nginx，这里的值通常应为X-Forwarded-For
     * </p>
     */
    private String alternateClientAddrHeaderName;

    /**
     * 是否通过DNS查找服务器地址
     */
    private boolean useServerHostAddress;

    /**
     * 当记录log出错时是否忽略错误，true时忽略
     */
    private boolean ignoreLogFailures = true;

    private boolean controller = true;
    private boolean service = true;
    private boolean repository = false;

    private Slf4jLogProperties slf4j = new Slf4jLogProperties();


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getAlternateServerAddrHeaderName() {
        return alternateServerAddrHeaderName;
    }

    public void setAlternateServerAddrHeaderName(String alternateServerAddrHeaderName) {
        this.alternateServerAddrHeaderName = alternateServerAddrHeaderName;
    }

    public String getAlternateClientAddrHeaderName() {
        return alternateClientAddrHeaderName;
    }

    public void setAlternateClientAddrHeaderName(String alternateClientAddrHeaderName) {
        this.alternateClientAddrHeaderName = alternateClientAddrHeaderName;
    }

    public boolean isUseServerHostAddress() {
        return useServerHostAddress;
    }

    public void setUseServerHostAddress(boolean useServerHostAddress) {
        this.useServerHostAddress = useServerHostAddress;
    }

    public boolean isIgnoreLogFailures() {
        return ignoreLogFailures;
    }

    public void setIgnoreLogFailures(boolean ignoreLogFailures) {
        this.ignoreLogFailures = ignoreLogFailures;
    }

    public boolean isController() {
        return controller;
    }

    public void setController(boolean controller) {
        this.controller = controller;
    }

    public boolean isService() {
        return service;
    }

    public void setService(boolean service) {
        this.service = service;
    }

    public boolean isRepository() {
        return repository;
    }

    public void setRepository(boolean repository) {
        this.repository = repository;
    }

    public Slf4jLogProperties getSlf4j() {
        return slf4j;
    }

    public static class Slf4jLogProperties {
        private boolean useSingleLine;

        private String singleLineSeparator = "|";

        /**
         * The trail format to use in the logs.
         * Accepted value are: DEFAULT, JSON.
         */
        private String trailFormat = "DEFAULT";

        public boolean isUseSingleLine() {
            return useSingleLine;
        }

        public void setUseSingleLine(boolean useSingleLine) {
            this.useSingleLine = useSingleLine;
        }

        public String getSingleLineSeparator() {
            return singleLineSeparator;
        }

        public void setSingleLineSeparator(String singleLineSeparator) {
            this.singleLineSeparator = singleLineSeparator;
        }

        public String getTrailFormat() {
            return trailFormat;
        }

        public void setTrailFormat(String trailFormat) {
            this.trailFormat = trailFormat;
        }
    }
}
