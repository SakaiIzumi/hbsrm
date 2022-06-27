package net.bncloud.quotation.event.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.bncloud.quotation.entity.QuotationBase;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class QuotationInfoEventData extends QuotationBase implements Serializable {


    /**
     * 供应商编码
     */
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;


    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     * 组织id
     */
    @ApiModelProperty(value = "组织id")
    private Long orgId;

    /**
     * 业务ID,各单据的主键ID，发送消息和代办使用
     */
    private Long businessId;

    private String smsParams;

    private String smsTempCode;

    private Integer smsMsgType;


}
