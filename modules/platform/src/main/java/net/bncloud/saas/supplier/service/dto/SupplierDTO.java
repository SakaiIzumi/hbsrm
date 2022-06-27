package net.bncloud.saas.supplier.service.dto;


import io.swagger.annotations.ApiModelProperty;
import net.bncloud.convert.base.BaseDTO;
import net.bncloud.saas.supplier.domain.*;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class SupplierDTO extends BaseDTO {

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


    @ApiModelProperty(value = "供应商来源分类")
    private SupplierSourceType sourceType;

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

    private SupplierExtDTO supplierExt;

    private List<SupplierLinkManDTO> linkMans;

    private List<TagConfigItemDTO> tags;

    private List<TypeConfigItemDTO> types;

    private Map<String, Boolean> permissionButton;

    private List<SupplierAccount> accounts;


}
