package net.bncloud.saas.user.web.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryByMobilePayload {
    private String mobile;
    private String stateCode;
}
