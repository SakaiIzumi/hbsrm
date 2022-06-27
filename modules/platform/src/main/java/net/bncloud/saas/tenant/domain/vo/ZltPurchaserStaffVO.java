package net.bncloud.saas.tenant.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ZltPurchaserStaffVO implements Serializable {

    private String name;

    private String mobile;

    private String purchaserCode;
}
