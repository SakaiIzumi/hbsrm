package net.bncloud.saas.user.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrganizationManagerVO implements Serializable {
    private static final long serialVersionUID = 8478468779328021783L;

    private String mobile;

    private String name;

    private Boolean enabled;
}
