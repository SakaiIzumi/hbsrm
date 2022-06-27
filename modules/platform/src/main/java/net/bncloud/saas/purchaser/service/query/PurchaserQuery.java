package net.bncloud.saas.purchaser.service.query;

import lombok.Data;

import java.io.Serializable;

@Data
public class PurchaserQuery implements Serializable {

    private static final long serialVersionUID = -8086421831716482787L;
    private String code;
    private String name;
    private Long orgId;
}
