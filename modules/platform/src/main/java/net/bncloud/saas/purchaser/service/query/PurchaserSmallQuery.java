package net.bncloud.saas.purchaser.service.query;

import lombok.Data;

import java.io.Serializable;

@Data
public class PurchaserSmallQuery implements Serializable {

    private static final long serialVersionUID = -8086421831716482787L;
    private String qs;
    private String code;
    private String name;
    private String companyCode;
    private String companyName;
    private Long orgId;
    private String scene;

    private Long supId;

}
