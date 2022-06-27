package net.bncloud.delivery.vo.event;


import lombok.Data;
import net.bncloud.common.util.DateUtil;
import net.bncloud.delivery.entity.DeliveryCustomsInformation;
import net.bncloud.delivery.entity.DeliveryDetail;
import net.bncloud.delivery.entity.DeliveryNote;
import net.bncloud.delivery.entity.FileInfo;

import java.io.Serializable;
import java.util.Date;
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
public class DeliveryNoteEvent extends DeliveryNote implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 送货明细
     */
    private List<DeliveryDetail> deliveryDetailList;

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

    private Long deliveryId;

    private Long businessId;

    private String addTime = DateUtil.formatDateTime(new Date());
}
