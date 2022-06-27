package net.bncloud.delivery.param;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bncloud.delivery.entity.DeliveryDetail;
import net.bncloud.delivery.entity.DeliveryNote;
import net.bncloud.delivery.entity.FileInfo;
import net.bncloud.delivery.vo.DeliveryDetailVo;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 送货单信息保存请求参数
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeliveryNoteSaveParam extends DeliveryNote implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 送货明细
     */
    @ApiModelProperty(value = "送货明细")
    private List<DeliveryDetailVo> deliveryDetailList;

    /**
     *附件列表
     */
    @ApiModelProperty(value = "附件列表")
    private List<FileInfo> attachmentList;

}
