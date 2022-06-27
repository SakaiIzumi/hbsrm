package net.bncloud.saas.tenant.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bncloud.saas.authorize.service.dto.RoleDTO;

import java.util.List;


/**
 * 产品说的物料
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    private Long id;
    private String code;
    private String name;
    private String mobile;
    private List<RoleDTO> roles;



}
