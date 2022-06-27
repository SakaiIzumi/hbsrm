package net.bncloud.oem.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

 
/**
 * @author ddh
 * @since 2022/4/24
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "t_oem_receiving_address")
public class ReceivingAddress extends BaseEntity {


    private static final long serialVersionUID = -3678170151562783597L;
    /**
    * 送货地址编码
    */
    @ApiModelProperty(value="送货地址编码")
    private String code;

    /**
    * 送货地址
    */
    @ApiModelProperty(value="送货地址")
    private String address;

    /**
    * OEM供应商编码
    */
    @ApiModelProperty(value="OEM供应商编码")
    private String supplierCode;

    /**
    * OEM供应商名称
    */
    @ApiModelProperty(value="OEM供应商名称")
    private String supplierName;

    /**
    * 数据来源：1用户维护，2用户导入，3ERP同步
    */
    @ApiModelProperty(value="数据来源：1用户维护，2用户导入，3ERP同步")
    private String sourceType;

    /**
    * 状态：0待维护，1已维护
    */
    @ApiModelProperty(value="状态：0待维护，1已维护")
    private String status;

}