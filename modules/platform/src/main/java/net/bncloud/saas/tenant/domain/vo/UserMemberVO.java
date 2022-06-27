package net.bncloud.saas.tenant.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserMemberVO implements Serializable {

    private static final long serialVersionUID = 4211844635919753284L;

    private Long id;

    private Long subjectId;

    private String subjectType;

    private String position;

    private String jobNo;

    private List<Long> roleIds;

    private Boolean enabled;
}
