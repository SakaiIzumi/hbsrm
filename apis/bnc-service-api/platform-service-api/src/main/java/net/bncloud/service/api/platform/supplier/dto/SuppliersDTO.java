package net.bncloud.service.api.platform.supplier.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 询价使用的供应商信息
 */
@Data
public class SuppliersDTO {
    @ApiModelProperty(value = "供应商Id")
    private Long id;

    @ApiModelProperty(value = "供应商编码")
    private String code;

    @ApiModelProperty(value = "供应商名称")
    private String name;

    @ApiModelProperty(value = "供应商oa分类")
    private String oaType;
    @ApiModelProperty(value = "供应商oa编码")
    private String oaCode;

    @ApiModelProperty(value = "统一社会信用代码")
    private String creditCode;

    @ApiModelProperty(value = "供应商的组织机构代码")
    private String supplierOrgCode;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "来源ID")
    private String sourceId;

    @ApiModelProperty(value = "来源编码")
    private String sourceCode;

    @ApiModelProperty(value = "头像")
    private String avatarUrl;

    @ApiModelProperty(value = "邀请日期")
    private Date inviteDate;

    @ApiModelProperty(value = "合作状态")
    private String relevanceStatus;


    @ApiModelProperty(value = "供应商管理员名称")
    private String managerName;

    @ApiModelProperty(value = "供应商管理员电话")
    private String managerMobile;

}
