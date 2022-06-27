package net.bncloud.service.api.file.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.bncloud.base.BaseEntity;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 费用单据信息表
 * </p>
 */
@Data
public class FinancialCostBillDto extends BaseEntity implements Serializable {

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
     * 费用单号
     */
    @ApiModelProperty(value = "费用单号，自动生成")
    private String costBillNo;

    /**
     * 金蝶ERP单据id
     */
    @ApiModelProperty(value = "金蝶ERP单据id")
    private Long erpBillId;

    /**
     * 金蝶ERP单据编码
     */
    @ApiModelProperty(value = "金蝶ERP单据编码")
    private String erpBillNo;

    /**
     * 单据类型
     */
    @ApiModelProperty(value = "金蝶ERP单据类型")
    private String erpBillType;


    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    @ApiModelProperty(value = "发布时间")
    private LocalDate publishTime;

    /**
     * 发布人名称
     */
    @ApiModelProperty(value = "发布人名称")
    private String publisher;

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
     * 费用单类型
     */
    @ApiModelProperty(value = "费用单类型:0一般费用单（目前只有一种）")
    private String costBillType;

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
     * 确认时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    @ApiModelProperty(value = "确认时间")
    private LocalDate confirmTime;

    /**
     * 价税合计金额
     */
    @ApiModelProperty(value = "价税合计金额")
    private BigDecimal allAmount;

    /**
     * 确认人名称
     */
    @ApiModelProperty(value = "确认人名称")
    private String confirmName;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "费用明细列表")
    private List<FinancialCostBillLineDto> costBillLineList;
}
