package net.bncloud.saas.tenant.web.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrgEmployeeMangeScopeParam implements Serializable {

    private Long employeeId;

    private String scope;

    /**
     * 同scope，前端使用这个字段
     * */
    private String boundary;
}
