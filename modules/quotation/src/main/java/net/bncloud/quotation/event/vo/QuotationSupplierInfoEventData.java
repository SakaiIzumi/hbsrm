package net.bncloud.quotation.event.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.bncloud.quotation.entity.QuotationSupplier;

@Data
public class QuotationSupplierInfoEventData extends QuotationSupplier {
    //msm短信的code
    private String smsTempCode;
    //短信的类型
    private Integer smsMsgType;
    //参数
    private String smsParams;
    /**
     * 组织id
     */
    private Long orgId;

    private Long businessId;

    private String quotationNo;
}
