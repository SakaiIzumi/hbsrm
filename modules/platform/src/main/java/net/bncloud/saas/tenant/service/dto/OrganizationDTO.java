package net.bncloud.saas.tenant.service.dto;

import lombok.Data;
import net.bncloud.saas.tenant.domain.vo.OrgType;

import javax.persistence.*;
import java.io.Serializable;

@Data
public class OrganizationDTO implements Serializable {
    private static final long serialVersionUID = -6845912595300373897L;

    private Long id;

    private String name;

    private String description;

    /**
     * 组织类型
     */
    @Enumerated(EnumType.STRING)
    private OrgType orgType;

    /**
     * 所属企业
     */
}
