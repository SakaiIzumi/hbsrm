package net.bncloud.quotation.param;


import io.swagger.annotations.ApiModelProperty;
import net.bncloud.quotation.entity.QuotationBase;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;


/**
 * <p>
 * 询价基础信息
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Data
public class QuotationBaseParam extends QuotationBase implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long supplierId;

    /**
     * 截止查询开始日期
     */
    @ApiModelProperty("截止查询开始日期")
    private String custOffStartDate;

    /**
     * 截止查询结束日期
     */
    @ApiModelProperty("截止查询结束日期")
    private String custOffEndDate;

    /**
     * 发布查询开始日期
     */
    @ApiModelProperty("发布查询开始日期")
    private String publishStartDate;

    /**
     * 发布查询结束日期
     */
    @ApiModelProperty("发布查询结束日期")
    private String publishEndDate;

}
