package net.bncloud.delivery.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
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
 * 国家信息表
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_delivery_country")
@ApiModel(value = "国家信息实体类", description = "国家信息描述类")
public class DeliveryCountry extends BaseEntity {

	private static final long serialVersionUID = 1L;


    /**
     * 国家编码
     */
    @ApiModelProperty(value = "国家编码")
    private String countryCode;

    /**
     * 国家名称
     */
    @ApiModelProperty(value = "国家名称")
    private String countryName;

}
