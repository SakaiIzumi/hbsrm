package net.bncloud.saas.supplier.service.dto;

/**
 * 供应商档案
 */

import lombok.Getter;
import lombok.Setter;
import net.bncloud.convert.base.BaseDTO;
import net.bncloud.saas.supplier.domain.SupplierExt;
import net.bncloud.saas.supplier.domain.SupplierSourceType;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class SupplierArchiveDTO extends BaseDTO {
    private Long id;
    private Long orgId;
    private String code;
    private String name;
    /**
     * 统一社会信用代码
     */
    private String creditCode;
    /**
     * 供应商的组织机构代码
     */
    private String supplierOrgCode;
    private String remark;
    private SupplierSourceType sourceType;
    private String sourceId;
    private String sourceCode;

    private Date inviteDate;
    private String relevanceStatus;
    private SupplierExt supplierExt;
    private String managerName;
    private String managerMobile;
    private List<TagConfigItemDTO> tags;
    private List<TypeConfigItemDTO> types;

    private Map<String, Boolean> permissionButton;


}
