
package net.bncloud.saas.tenant.service.query;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrgMemberQuery implements Serializable {

    private String code;

    private String name;

    private String mobile;

    private Long orgId;

    private Boolean enabled;



}
