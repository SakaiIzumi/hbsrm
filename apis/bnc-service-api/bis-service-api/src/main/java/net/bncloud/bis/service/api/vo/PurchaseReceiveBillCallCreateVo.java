package net.bncloud.bis.service.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * desc: 收料通知单 创建调用信息
 *
 * @author Rao
 * @Date 2022/01/22
 **/
@Data
public class PurchaseReceiveBillCallCreateVo implements Serializable {

    private static final long serialVersionUID = 1494699671588773415L;

    /**
     * ERP 主键ID
     */
    private Long fId;

    /**
     * 单据编码
     */
    private String fNumber;

    /**
     * 创建
     */
    private List<PurchaseReceiveBillEntryCallCreateVo> purchaseReceiveBillEntryCallCreateVoList;

}
