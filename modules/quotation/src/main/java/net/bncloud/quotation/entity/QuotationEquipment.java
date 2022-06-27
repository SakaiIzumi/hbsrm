package net.bncloud.quotation.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

/**
 * <p>
 * 设备能力要求信息
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_rfq_quotation_equipment")

public class QuotationEquipment extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 询价基础信息主键ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "询价基础信息主键ID")
    private Long quotationBaseId;

    /**
     * 设备种类编码
     */
    @ApiModelProperty(value = "设备种类编码")
    private String equipmentCode;

    /**
     * 设备种类名称
     */
    @ApiModelProperty(value = "设备种类名称")
    private String equipmentName;

    /**
     * 关键参数
     */
    @ApiModelProperty(value = "关键参数")
    private String param;

    /**
     * 生产精度
     */
    @ApiModelProperty(value = "生产精度")
    private String productionPrecision;

    /**
     * 设备说明
     */
    @ApiModelProperty(value = "设备说明")
    private String description;

}
