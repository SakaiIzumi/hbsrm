package net.bncloud.saas.tenant.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleCopyVO implements Serializable {

    private Long roleId;

    private Long subjectId;

    private String name;

    private String subjectCode; //采购方编码或供应商编码

}
