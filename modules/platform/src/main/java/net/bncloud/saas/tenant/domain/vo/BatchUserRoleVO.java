package net.bncloud.saas.tenant.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BatchUserRoleVO implements Serializable {


    private List<Long> userIds;

    private List<Long> roleIds;

    /**
     * 询价单权限范围：本人：self  全组织：org_all
     * */
    private String boundary;
}
