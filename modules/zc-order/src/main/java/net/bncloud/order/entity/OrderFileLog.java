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
 * 订单文件下载日志表
 * </p>
 *
 * @author Auto-generator
 * @since 2021-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_order_file_log")

public class OrderFileLog extends BaseEntity {

	private static final long serialVersionUID = 1L;


    /**
     * 关联采购单号
     */
    @ApiModelProperty(value = "关联采购单号")
    private String purchaseOrderCode;

    /**
     * 文件大小
     */
    @ApiModelProperty(value = "文件大小")
    private String fileSize;

    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名称")
    private String fileName;
    
    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String createdName;

}
