package net.bncloud.bis.service.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * desc: 收料通知单 明细调用创建对象
 *
 * @author Rao
 * @Date 2022/01/22
 **/
@Data
public class PurchaseReceiveBillEntryCallCreateVo implements Serializable {
    private static final long serialVersionUID = 3344989342712646747L;

    /**
     * erp 的 id
     */
    private Long erpId;

    /**
     * Srm 的id
     */
    private Long srmId;

}
