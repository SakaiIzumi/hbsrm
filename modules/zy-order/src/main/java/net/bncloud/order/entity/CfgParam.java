package net.bncloud.order.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CfgParam  implements Serializable {

    private Long id;
    private Long orgId;
    private Long companyId;
    private String code;
    private String value;
    private String remark;

}
