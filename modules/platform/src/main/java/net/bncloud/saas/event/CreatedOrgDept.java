package net.bncloud.saas.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatedOrgDept implements Serializable {

    private static final long serialVersionUID = 83712273390021184L;
    private Long orgId;
    private String orgName;
    private Long deptId;
    private String deptName;
}
