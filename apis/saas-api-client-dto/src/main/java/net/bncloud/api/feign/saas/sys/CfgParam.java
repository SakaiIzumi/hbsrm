package net.bncloud.api.feign.saas.sys;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Getter
@Setter
public class CfgParam implements Serializable {

    private static final long serialVersionUID = 310445762684701523L;
    private Long id;
    private Long companyId;
    private String code;
    @Enumerated(EnumType.STRING)
    private ParamType type;
    private String value;
    private String remark;
    @Embedded
    private ParamFilter paramFilter;
    @Embedded
    private ParamCategory category;
}
