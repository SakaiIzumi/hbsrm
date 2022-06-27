package net.bncloud.quotation.vo;


import io.swagger.annotations.ApiModelProperty;
import net.bncloud.quotation.entity.QuotationMark;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * <p>
 * 询价单应标关联表
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-01
 */
@Data
public class QuotationMarkVo extends QuotationMark implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 供应商账号
     */
    @ApiModelProperty(value = "供应商账号")
    private String supplierAccount;

    /**
     * 供应商编码
     */
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;


}
