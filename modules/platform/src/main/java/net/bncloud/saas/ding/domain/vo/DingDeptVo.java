package net.bncloud.saas.ding.domain.vo;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.saas.ding.domain.DingNewStaff;

import java.util.List;

//此表为ss_tenant_org_department表的媒介表，不是ss_ding_new_depart表的媒介表
@Getter
@Setter
public class DingDeptVo {
    private Long id;//部门ID
    private Long orgId;//所在组织ID
    private Long parentId;//父ID
    private String name;//部门名称
    private Integer level;//层级
    private Boolean del;//删除状态-对应部门业务表
    private List<DingDeptVo> children;//子级
    private List<DingNewStaff> staffs;//员工集合
}