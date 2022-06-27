package net.bncloud.service.api.delivery.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Toby
 */
@Data
public class DeliveryNoteUpdateDTO implements Serializable {

    /* 接口SRM009：每半小时同步ERP入库单数据，更新智采送货单的收货数量。
    * 送货状态-已完成
    * ERP签收状态-已签收
    * 签收时间
    * 送货明细-收货数量
    * */

    private static final long serialVersionUID = -1;

    /**
     * 物料通知单ID
     */
    private String materialNoticeId;

    /**
     * 送货单ID
     */
    private String deliveryId;

    /**
     * 签收时间
     */
    private Date signingTime;

    /**
     * 送货明细列表
     */
    private List<DeliveryDetailUpdateDTO> deliveryDetailList;
}
