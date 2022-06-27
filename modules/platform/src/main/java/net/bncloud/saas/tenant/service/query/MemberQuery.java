package net.bncloud.saas.tenant.service.query;

import lombok.Data;
import net.bncloud.api.feign.saas.user.SubjectType;

import java.io.Serializable;

@Data
public class MemberQuery implements Serializable {

    private String name;
    private String userCode;
    private String mobile;
    private Long orgId;
    private Long subjectId;
    private SubjectType subjectType;
    private String code;
    private Boolean enabled;


}
