package net.bncloud.service.api.file.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * @author: liulu
 * @Date: 2022-02-27 20:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_financial_delivery_bill")

public class FinancialDeliveryBillDto extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 协助组织
     */
    @ApiModelProperty(value = "协作组织id")
    private Long orgId;
    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    @NotBlank(message = "客户名称不能为空")
    private String customerName;

    /**
     * 客户编码
     */
    @ApiModelProperty(value = "客户编码")
    @NotBlank(message = "客户编码不能为空")
    private String customerCode;


    /**
     * 供应商编码
     * */
    @ApiModelProperty(value = "供应商编码")
    @NotBlank(message = "供应商编码不能为空")
    private String supplierCode ;

    /**
     * 供应商名称
     * */
    @ApiModelProperty(value = "供应商名称")
    @NotBlank(message = "供应商名称不能为空")
    private String supplierName ;

    /**
     * 送货单号
     */
    @ApiModelProperty(value = "送货单号，自动生成")
    private String deliveryBillNo;

    /**
     * 金蝶ERP单据id
     */
    @ApiModelProperty(value = "金蝶ERP单据id")
    private Long erpBillId;

    /**
     * 单据编码
     */
    @ApiModelProperty(value = "金蝶ERP单据编码")
    private String erpBillNo;

    /**
     * 单据类型
     */
    @ApiModelProperty(value = "金蝶ERP单据类型")
    private String erpBillType;

    /**
     * 币种编码
     */
    @ApiModelProperty(value = "币种编码")
    @NotBlank(message = "币种编码不能为空")
    private String currencyCode;

    /**
     * 币种名称
     */
    @ApiModelProperty(value = "币种名称")
    private String currencyName;


    /**
     * 来源
     */
    @ApiModelProperty(value = "来源")
    private String sourceType;

    /**
     *结算池，入池状态，N未入池，Y已入池
     */
    @ApiModelProperty(value = "结算池，入池状态，N未入池，Y已入池")
    private String settlementPoolSyncStatus;

    /**
     * 送货通知签收时间
     */
    @ApiModelProperty(value = "送货通知签收时间")
    private LocalDate signingTime;

    /**
     * 送货数量
     */
    @ApiModelProperty(value = "送货数量")
    private Integer deliveryNum;

    /**
     * 送货金额
     */
    @ApiModelProperty(value = "送货金额")
    private BigDecimal deliveryAmount;

    /**
     * 送货日期
     */
    @ApiModelProperty(value = "送货日期")
    private Date deliveryDate;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "送货明细列表")
    private List<FinancialDeliveryBillLineDto> deliveryBillLineList;
}
