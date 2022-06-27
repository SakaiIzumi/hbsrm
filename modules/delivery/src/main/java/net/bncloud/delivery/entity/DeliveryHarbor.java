package net.bncloud.delivery.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

/**
 * <p>
 * 港口信息表
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_delivery_harbor")
@ApiModel(value = "港口信息实体类", description = "港口信息描述类")
public class DeliveryHarbor extends BaseEntity {

	private static final long serialVersionUID = 1L;


    /**
     * 港口编码
     */
    @ApiModelProperty(value = "港口编码")
    private String harborCode;

    /**
     * 港口名称
     */
    @ApiModelProperty(value = "港口名称")
    private String harborName;

}
