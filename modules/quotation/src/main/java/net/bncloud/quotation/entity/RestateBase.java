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
 * 询价重报基础信息
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_rfq_restate_base")

public class RestateBase extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 询价单主键ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "询价单主键ID")
    private Long quotationBaseId;

    /**
     * 是否推送每行最低报价行，1是，0否
     */
    @ApiModelProperty(value = "是否推送每行最低报价行，1是，0否")
    private String pushCheapest;

    /**
     * 重新报价次数
     */
    @ApiModelProperty(value = "重新报价次数")
    private Integer roundNumber;

}
