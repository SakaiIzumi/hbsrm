package net.bncloud.quotation.event.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.bncloud.quotation.entity.QuotationBase;

import java.io.Serializable;

@Data
public class QuotationSupplierQuotedPriceEventData extends QuotationBase implements Serializable {


    /**
     * 供应商编码
     */
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    /**
     * 供应商编码
     */
    @ApiModelProperty(value = "供应商编码")
    private String supplierName;


    /**
     * 组织id
     */
    @ApiModelProperty(value = "组织id")
    private Long orgId;


    private String smsParams;

    private String smsTempCode;

    private Integer smsMsgType;

    private Long businessId;

}
