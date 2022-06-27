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
 * 订单操作记录表
 * </p>
 *
 * @author lv
 * @since 2021-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_order_operation_log")

public class OrderOperationLog extends BaseEntity {

	private static final long serialVersionUID = 1L;


    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;

    /**
     * 采购单号
     */
    @ApiModelProperty(value = "采购单号")
    private String purchaseOrderCode;
    
    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String createdName;

}
