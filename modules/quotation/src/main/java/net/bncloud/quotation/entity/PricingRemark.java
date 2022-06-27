package net.bncloud.quotation.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

/**
 * <p>
 * 定价说明信息
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_rfq_pricing_remark")

public class PricingRemark extends BaseEntity {

	private static final long serialVersionUID = 1L;


    /**
     * 询价单主键ID
     */
    @ApiModelProperty(value = "询价单主键ID")
    private Long quotationBaseId;

    /**
     * 说明信息
     */
    @ApiModelProperty(value = "说明信息")
    private String pricingRemark;

}
