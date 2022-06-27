package net.bncloud.service.api.delivery.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 送货物料通知单 DTO
 * @author Toby
 */
@Data
public class DeliveryMaterialNoticeDTO implements Serializable {
    private static final long serialVersionUID = -1;

    /**
     * 送货单基础信息
     */
    @ApiModelProperty(value = "送货单基础信息")
    private DeliveryNoteDTO deliveryNote;


    /**
     * 送货单明细列表
     */
    @ApiModelProperty(value = "送货单明细列表")
    private List<DeliveryDetailDTO> deliveryDetailList;

}
