package net.bncloud.saas.tenant.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrgEmployeeMangeScopeDTO implements Serializable {
    private Long id;

    /**
     * 员工的id
     * */
    private Long employeeId;

    /**
     * 权限范围
     * */
    private String scope;

    /**
     * 同scope，回传给前端使用这个字段
     * */
    private String boundary;
}
