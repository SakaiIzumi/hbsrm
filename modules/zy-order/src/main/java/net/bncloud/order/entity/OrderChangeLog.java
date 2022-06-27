package net.bncloud.order.entity;


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
 * 修改日志表
 * </p>
 *
 * @author 吕享1义
 * @since 2021-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_order_change_log")

public class OrderChangeLog extends BaseEntity {

	private static final long serialVersionUID = 1L;


    /**
     * 修改类型集合
     */
    @ApiModelProperty(value = "修改类型集合")
    private String updateTypes;

    /**
     * 采购单号
     */
    @ApiModelProperty(value = "采购单号")
    private String purchaseOrderCode;

    /**
     * 关联答交记录表
     */
    @ApiModelProperty(value = "关联答交记录表")
    private String orderCommunicateLogId;

    /**
     * 来源
     */
    @ApiModelProperty(value = "来源")
    private String source;
    @ApiModelProperty(value = "修改原因")
    private String changeReason;


}
