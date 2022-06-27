package net.bncloud.quotation.vo;


import io.swagger.annotations.ApiModelProperty;
import net.bncloud.quotation.entity.BiddingLineExt;
import net.bncloud.quotation.entity.PricingRecord;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 定价请示记录信息
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Data
public class PricingRecordVo extends PricingRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 供应商账号
     */
    @ApiModelProperty(value = "供应商账号")
    private String supplierAccount;

    /**
     * 供应商编码
     */
    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    /**
     * 供应商类型，formal正式，potential潜在
     */
    @ApiModelProperty(value = "供应商类型，formal正式，potential潜在")
    private String supplierType;

    /**
     * 平台类型
     */
    @ApiModelProperty(value = "平台类型")
    private String platformType;

    /**
     * 响应状态
     */
    @ApiModelProperty(value = "响应状态")
    private String responseStatus;

    /**
     * 响应时间
     */
    @ApiModelProperty(value = "响应时间")
    private Date responseTime;

    /**
     * 接收人姓名
     */
    @ApiModelProperty(value = "接收人姓名")
    private String receiver;

    /**
     * 联系人手机号
     */
    @ApiModelProperty(value = "联系人手机号")
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;

    private List<BiddingLineExt> allBiddingLineExt;


    /**
     * 供应商企业名字
     */
    @ApiModelProperty(value = "供应商企业名字")
    private String companyName;


}
