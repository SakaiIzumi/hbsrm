package net.bncloud.delivery.vo;


import net.bncloud.delivery.entity.*;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 送货单信息表
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
@Data
public class DeliveryNoteVo extends DeliveryNote implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 送货明细
     */
    private List<DeliveryDetailVo> deliveryDetailList;

    /**
     * 附件列表
     */
    private List<FileInfo> attachmentList;

    /**
     * 报关资料
     */
    private DeliveryCustomsInformation deliveryCustomsInformation;

    /**
     * 可操作按钮
     */
    private Map<String,Boolean> permissionButton;

    private List<DeliveryPlanDetailItemVo> deliveryAsPlan;




}
