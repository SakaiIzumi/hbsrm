package net.bncloud.saas.purchaser.service.query;

import lombok.Data;

import java.io.Serializable;

@Data
public class PurchaserStaffQuery implements Serializable {

    private static final long serialVersionUID = 5449311463278273707L;
    private String name;
    private String userCode;
    private String mobile;
    private Long orgId;
    private Long purchaserId;
    private Boolean enabled;
    private String code;
}
