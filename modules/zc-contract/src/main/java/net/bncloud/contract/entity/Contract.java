package net.bncloud.contract.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import net.bncloud.base.BaseEntity;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import net.bncloud.common.util.DateUtil;
import net.bncloud.common.web.jackson.AmountSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 合同信息表
 * </p>
 *
 * @author huangtao
 * @since 2021-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_purchase_contract")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contract extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 供应商编码
     */
    @ApiModelProperty(value = "供应商编码")
    @NotBlank(message = "供应商编码不能为空")
    private String supplierCode;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    @NotBlank(message = "供应商名称不能为空")
    private String supplierName;

    /**
     * 采购方编码
     */
    @ApiModelProperty(value = "采购方编码")
    @NotBlank(message = "采购方编码不能为空")
    private String customerCode;

    /**
     * 采购方名称
     */
    @ApiModelProperty(value = "采购方名称")
    @NotBlank(message = "采购方名称不能为空")
    private String customerName;

    /**
     * 合同类型序号 t_purchase_contract_type.id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "合同类型序号 t_purchase_contract_type.id")
    @NotNull(message = "合同类型不能为空")
    private Long contractTypeId;

    /**
     * 合同类型名称
     */
    @ApiModelProperty(value = "合同类型名称")
    private String contractTypeName;

    /**
     * 合同编号
     */
    @ApiModelProperty(value = "合同编号")
    @NotBlank(message = "合同编号不能为空")
    private String contractCode;

    /**
     * 合同标题
     */
    @ApiModelProperty(value = "合同标题")
    @NotBlank(message = "合同标题不能为空")
    private String contractTitle;

    /**
     * 确认时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING ,pattern = DateUtil.PATTERN_DATETIME)
    @ApiModelProperty(value = "签订时间")
    private Date signedTime;

    /**
     * 合同含税金额
     */
    @ApiModelProperty(value = "合同含税金额")
//    @JsonSerialize(using = AmountSerializer.class)
    private BigDecimal taxIncludedAmount;

    /**
     * 合同不含税金额
     */
    @ApiModelProperty(value = "合同不含税金额")
//    @JsonSerialize(using = AmountSerializer.class)
    private BigDecimal excludingTaxAmount;

    /**
     * 是否必要 t_contract_type.is_necessary
     */
    @ApiModelProperty(value = "是否必要 t_contract_type.is_necessary")
    private String necessary;

    /**
     * 有效期类型 由字典contract_valid_period_type定义
     */
    @ApiModelProperty(value = "有效期类型 由字典contract_valid_period_type定义")
    @NotBlank(message = "有效期不能为空")
    private String validPeriodType;

    /**
     * 到期日
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING ,pattern = DateUtil.PATTERN_DATE)
    @ApiModelProperty(value = "到期日")
    private Date expiryDate;

    /**
     * 合同状态编码 由字典contract_status_code定义
     */
    @ApiModelProperty(value = "合同状态编码 由字典contract_status_code定义")
    private String contractStatusCode;

    /**
     * 合同状态名称
     */
    @ApiModelProperty(value = "合同状态名称")
    private String contractStatusName;

    /**
     * 创建人名称
     */
    @ApiModelProperty(value = "创建人名称")
    private String createdByName;

    /**
     * 操作人名称
     */
    @ApiModelProperty(value = "操作人名字")
    private String operatorByName;
    /**
     * 操作人id
     */
    @ApiModelProperty(value = "操作人id")
    private Long operatorById;

    /**
     * 组织id
     */
    @ApiModelProperty(value = "组织id")
    private Long orgId;

    /**
     * 事项类型
     */
    @ApiModelProperty(value = "事项类型")
    private Integer eventType;

    /**
     * 流程编号
     */
    @ApiModelProperty(value = "流程编号")
    private String processNumber;

    /**
     * 请求id
     */
    @ApiModelProperty(value = "请求id")
    private Integer requestId;

    /**
     * 合作方类型
     */
    @ApiModelProperty(value = "合作方类型")
    private Integer partnerType;

    /**
     * 流程状态
     */
    @ApiModelProperty(value = "流程状态")
    private String processStatus;

    /**
     * 同步的oa合同创建时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING ,pattern = DateUtil.PATTERN_DATE)
    @ApiModelProperty(value = "同步的oa合同创建时间")
    private Date contractCreateTime;


}
