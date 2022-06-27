package net.bncloud.quotation.param;


import io.swagger.annotations.ApiModelProperty;
import net.bncloud.quotation.entity.TRfqQuotationRecord;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;


/**
 * <p>
 * 报价记录信息
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-25
 */
@Data
public class TRfqQuotationRecordParam extends TRfqQuotationRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 供应商ID
     */
    @ApiModelProperty(value = "供应商名称或编号")
    private String supplierNameOrCode;



}
