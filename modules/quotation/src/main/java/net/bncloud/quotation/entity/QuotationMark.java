package net.bncloud.quotation.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

/**
 * <p>
 * 询价单应标关联表
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_rfq_quotation_mark")

public class QuotationMark extends BaseEntity {


    public enum MARK_STATUS {
        REJECT,
        MARKED;
    }

    private static final long serialVersionUID = 1L;


    /**
     * 关联单据ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "关联单据ID")
    private Long quotationId;

    /**
     * 状态,reject拒绝,marked应标
     */
    @ApiModelProperty(value = "状态,REJECT拒绝,MARKED应标")
    private MARK_STATUS markStatus;

    /**
     * 经销商ID
     */
    @ApiModelProperty(value = "经销商ID")
    private Long supplierId;

    /**
     * 应标轮次
     */
    @ApiModelProperty(value = "应标轮次")
    private Integer roundNum;
}
