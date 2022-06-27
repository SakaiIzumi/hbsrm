package net.bncloud.saas.supplier.service.query;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.saas.supplier.web.vm.SupplierTags;
import net.bncloud.saas.supplier.web.vm.SupplierTypes;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class SupplierQuery {

    private String supplierCode;
    private String supplierName;
    private String qs;
    private List<SupplierTypes> supplierTypesList;
    private List<SupplierTags> supplierTagsList;
    private Date inviteStartDate;
    private Date inviteEndDate;
    private List<Integer> tags;
    private List<Integer> types;
    private String scene; //场景值
    private Long orgId; // 组织Id
    private Long purId; //采购方id

    private List<Long> tagIds;//标签Id
    private List<Long> typeIds;// 分类Id

    //合作关系
    private String relevanceStatus;
}
