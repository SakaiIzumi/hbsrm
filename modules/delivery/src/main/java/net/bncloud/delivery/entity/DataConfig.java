package net.bncloud.delivery.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

 
/**
 * 数据配置
 * @author ddh
 * @since 2022/5/19
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_data_config")
public class DataConfig extends BaseEntity {

    private static final long serialVersionUID = -2954051684249728756L;
    /**
     * 键
     */
    @ApiModelProperty(value="键")
    private String code;

    /**
     * 值
     */
    @ApiModelProperty(value="值")
    private String value;

    /**
     * 描述
     */
    private String description;
}