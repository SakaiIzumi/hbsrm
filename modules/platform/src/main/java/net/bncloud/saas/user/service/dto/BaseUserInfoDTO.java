package net.bncloud.saas.user.service.dto;

import lombok.*;
import net.bncloud.common.security.Org;
import net.bncloud.common.security.Supplier;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseUserInfoDTO implements Serializable {

    private Long id;

    private String name;

    private String mobile;

    private String avatar;

    private String email;

    private String subjectType;

    private Long currentSupId;

    private Long currentOrgId;

    private String currentMenuNav;

    private Org currentOrg;

    private Supplier currentSupplier;
}
