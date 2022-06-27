package net.bncloud.saas.authorize.service.dto;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import net.bncloud.api.feign.saas.user.SubjectType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * role
 */
@Setter
@Getter
public class RoleDetailDTO implements Serializable {

    private static final long serialVersionUID = 2922613942592195354L;

    private Long id;

    private String name;

    private String description;

    private String supplierName;

    private String orgName;

    @Enumerated(EnumType.STRING)
    private SubjectType subjectType;

    private Long subjectId;

    private Boolean enabled;

    private String roleGroupName;

    private List<Long> menuIds = Lists.newArrayList();

    private List<DimensionPermDTO> purGrants;

    private List<DimensionPermDTO> supGrants;

    private List<DimensionPermDTO> amountGrants;
}
