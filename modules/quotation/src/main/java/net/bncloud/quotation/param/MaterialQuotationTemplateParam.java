package net.bncloud.quotation.param;


import io.swagger.annotations.ApiModelProperty;
import net.bncloud.quotation.entity.MaterialQuotationTemplate;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;


/**
 * <p>
 * 物料报价模板
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Data
public class MaterialQuotationTemplateParam extends MaterialQuotationTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 开始日期
     */
    @ApiModelProperty("开始日期")
    private String startDate;

    /**
     * 结束日期
     */
    @ApiModelProperty("结束日期")
    private String endDate;



}
