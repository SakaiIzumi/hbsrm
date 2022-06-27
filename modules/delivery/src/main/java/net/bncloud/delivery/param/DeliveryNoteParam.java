package net.bncloud.delivery.param;


import io.swagger.annotations.ApiModelProperty;
import net.bncloud.delivery.entity.Attachment;
import net.bncloud.delivery.entity.DeliveryDetail;
import net.bncloud.delivery.entity.DeliveryNote;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 送货单信息表
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
@Data
public class DeliveryNoteParam extends DeliveryNote implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 附件列表
     */
    @Valid
    private List<Attachment> attachmentList;
    /**
     * 附件列表
     */
    @Valid
    private String attachment;

    @Valid
    private List<DeliveryDetail> deliveryDetailList;

    /**
     * 采购单号
     */
    @ApiModelProperty(value = "采购单号")
    private String purchaseOrderCode;

    /**
     * 送货日期最小日期
     */
    @ApiModelProperty(value = "送货日期最小日期")
    private String deliveryDateMin;

    /**
     * 送货日期最大日期
     */
    @ApiModelProperty(value = "送货日期最大日期")
    private String deliveryDateMax;
    /**
     * 采购方过滤
     */
    @ApiModelProperty(value = "orgTrue")
    private String orgTrue;


    /**
     * 产品条件：产品名称或产品编码
     */
    private String productCondition;


}
