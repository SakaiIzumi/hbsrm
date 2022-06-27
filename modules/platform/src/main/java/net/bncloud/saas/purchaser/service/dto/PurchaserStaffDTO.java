package net.bncloud.saas.purchaser.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PurchaserStaffDTO implements Serializable {
    private static final long serialVersionUID = 5728532753274167995L;
    private Long id;
    private String name;
    private String mobile;
    private Long purchaserId;
    private String purchaserName;

}
