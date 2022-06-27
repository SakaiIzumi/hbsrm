package net.bncloud.saas.supplier.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import net.bncloud.convert.base.BaseDTO;
import net.bncloud.saas.supplier.domain.SupplierSourceType;


@Getter
@Setter
public class SupplierLinkManDTO extends BaseDTO {
    private static final long serialVersionUID = 3917437660881823975L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "职位")
    private String position;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "QQ")
    private String qq;

    @ApiModelProperty(value = "部门")
    private String department;

    @ApiModelProperty(value = "联系电话")
    private String linkPhone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "微信号")
    private String wechatId;

    @ApiModelProperty(value = "智易号")
    private String wisdomEasilyId;

    @ApiModelProperty(value = "传真")
    private String fax;

    @ApiModelProperty(value = "供应商id")
    private Long supplierId;

    @ApiModelProperty(value = "是否可操作")
    private Boolean allowOps;

    @ApiModelProperty(value = "来源")
    private SupplierSourceType sourceType;
}
