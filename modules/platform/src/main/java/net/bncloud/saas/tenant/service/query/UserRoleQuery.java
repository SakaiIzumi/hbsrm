package net.bncloud.saas.tenant.service.query;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRoleQuery implements Serializable {

    private String searchValue;
    private Long roleId;
}
