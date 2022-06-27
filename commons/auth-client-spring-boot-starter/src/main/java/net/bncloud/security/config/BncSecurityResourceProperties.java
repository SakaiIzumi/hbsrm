package net.bncloud.security.config;

import java.util.ArrayList;
import java.util.List;

public class BncSecurityResourceProperties {
    private List<String> authorizeRequests =new ArrayList<>();
    private List<String> addFilterBeforeUsernamePasswordAuthenticationFilter;
    private List<String> ignoreUrls = new ArrayList<>();
    private Boolean inner = false;

    public List<String> getAuthorizeRequests() {
        return authorizeRequests;
    }

    public void setAuthorizeRequests(List<String> authorizeRequests) {
        this.authorizeRequests = authorizeRequests;
    }

    public List<String> getAddFilterBeforeUsernamePasswordAuthenticationFilter() {
        return addFilterBeforeUsernamePasswordAuthenticationFilter;
    }

    public void setAddFilterBeforeUsernamePasswordAuthenticationFilter(List<String> addFilterBeforeUsernamePasswordAuthenticationFilter) {
        this.addFilterBeforeUsernamePasswordAuthenticationFilter = addFilterBeforeUsernamePasswordAuthenticationFilter;
    }

    public List<String> getIgnoreUrls() {
        return ignoreUrls;
    }

    public void setIgnoreUrls(List<String> ignoreUrls) {
        this.ignoreUrls = ignoreUrls;
    }

    public Boolean getInner() {
        return inner;
    }

    public void setInner(Boolean inner) {
        this.inner = inner;
    }
}
