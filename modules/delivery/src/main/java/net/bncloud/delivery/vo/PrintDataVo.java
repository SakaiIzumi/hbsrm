package net.bncloud.delivery.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author ddh
 * @version 1.0.0
 * @description 打印
 * @since 2022/1/20
 */
@Data
public class PrintDataVo<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商")
    private String supplier;

    /**
     * 打印人
     */
    @ApiModelProperty(value = "打印人")
    private String printBy;

    /**
     * 打印日期
     */
    @ApiModelProperty(value = "打印日期")
    private Date printDate;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private List<T> data;
}
