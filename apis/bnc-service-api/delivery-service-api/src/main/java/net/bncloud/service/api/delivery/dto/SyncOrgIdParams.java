package net.bncloud.service.api.delivery.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * desc: 同步orgId 参数
 *
 * @author Rao
 * @Date 2022/02/12
 **/
@Data
public class SyncOrgIdParams implements Serializable {

    private static final long serialVersionUID = 1243390608442843422L;

    /**
     * 采购方Code
     */
    private String purchaseCode;
    /**
     * 查询供应商名称
     */
    private String purchaseName;

    /**
     * 组织ID
     */
    private Long orgId;
    /**
     * 供应商code集合
     */
    private List<String > supplierCodeList;
    /**
     * ....
     */
    private Map<String,String> supplierCodeNameMap;


    public SyncOrgIdParams() {

    }

    public SyncOrgIdParams(String purchaseCode, Long orgId, List<String> supplierCodeList) {
        this.purchaseCode = purchaseCode;
        this.orgId = orgId;
        this.supplierCodeList = supplierCodeList;
    }

    public static SyncOrgIdParams of(String purchaseCode, Long orgId, List<String> supplierCodeList) {
        return new SyncOrgIdParams(purchaseCode, orgId, supplierCodeList);
    }

}
