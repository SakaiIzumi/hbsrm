package net.bncloud.delivery.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * @author ddh
 * @version 1.0.0
 * @description 供需平衡明细报表请求参数
 * @since 2022/3/4
 */
@Data
public class  SupplyDemandDetailViewParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日期
     */
    private String date;

    /**
     * 产品编码
     */
    @Length(min = 0, max = 255)
    private String productCode;

    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商")
    @Length(min = 0, max = 255)
    private String supplier;

    /**
     * 日期-开始区间
     */
    @ApiModelProperty(value = "日期-开始区间")
    private String startDate;

    /**
     * 日期-结束区间
     */
    @ApiModelProperty(value = "日期-结束区间")
    private String endDate;

    /**
     * 单据类型
     */
    @ApiModelProperty(value = "单据类型")
    @Length(min = 0, max = 255)
    private String billType;

}
