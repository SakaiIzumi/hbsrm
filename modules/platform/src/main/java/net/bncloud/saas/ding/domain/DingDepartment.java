package net.bncloud.saas.ding.domain;

import com.dingtalk.api.request.OapiV2DepartmentCreateRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;
import net.bncloud.saas.ding.domain.pk.DingDeptPK;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name = "ss_ding_department")
@Getter
@Setter
public class DingDepartment extends AbstractAuditingEntity {
    private static final long serialVersionUID = 2621974797165770969L;

    @EmbeddedId
    private DingDeptPK id;
    private Long parentId;

    private String name;
    private String sourceIdentifier;

    /**
     * 当部门群已经创建后，是否有新人加入部门会自动加入该群：
     *
     * true：自动加入群
     * false：不会自动加入群
     */
    private Boolean autoAddUser;

    /**
     * 部门是否来自关联组织：
     *
     * true：是
     * false：不是
     */
    private Boolean fromUnionOrg;

    /**
     * 在父部门中的次序值
     */
    @Column(name = "order_num")
    private long order;

    @Column(name = "create_dept_group")
    private Boolean createDeptGroup;

    /**
     * 部门群ID
     */
    @Column(name = "dept_group_chat_id")
    private String deptGroupChatId;

    /**
     * 企业群群主ID
     */
    @Column(name = "org_dept_owner")
    private String orgDeptOwner;

    /**
     * 部门的主管列表
     */
    @Column(name = "dept_manager_userid_list", length = 1024)
    private String deptManagerUseridList;

    /**
     * 是否限制本部门成员查看通讯录：
     *
     * true：开启限制。开启后本部门成员只能看到限定范围内的通讯录
     * false：不限制
     */
    @Column(name = "outer_dept")
    private Boolean outerDept;

    /**
     * 当限制部门成员的通讯录查看范围时（即outer_dept为true时），配置的部门员工可见部门列表。
     */
    @Column(name = "outer_permit_depts")
    private String outerPermitDepts;

    /**
     * 当限制部门成员的通讯录查看范围时（即outer_dept为true时），配置的部门员工可见员工列表。
     */
    @Column(name = "outer_permit_users")
    private String outerPermitUsers;

    /**
     * 本部门成员是否只能看到所在部门及下级部门通讯录：
     *
     * true：只能看到所在部门及下级部门通讯录
     * false：不能查看所有通讯录，在通讯录中仅能看到自己
     * 当outer_dept为true时，此参数生效。
     */
    @Column(name = "outer_dept_only_self")
    private Boolean outerDeptOnlySelf;

    /**
     * 是否隐藏本部门：
     *
     * true：隐藏部门，隐藏后本部门将不会显示在公司通讯录中
     * false：显示部门
     */
    @Column(name = "hide_dept")
    private Boolean hideDept;

    /**
     * 当隐藏本部门时（即hide_dept为true时），配置的允许在通讯录中查看本部门的员工列表。
     */
    @Column(name = "user_permits")
    private String userPermits;

    /**
     * 当隐藏本部门时（即hide_dept为true时），配置的允许在通讯录中查看本部门的部门列表
     */
    @Column(name = "dept_permits")
    private String deptPermits;

    @JsonIgnoreProperties("users")
    @MapsId("corpId")
    @ManyToOne
    @JoinColumn(name = "corp_id", referencedColumnName = "corp_id", nullable = false, updatable = false)
    private DingTalkCorp corp;

    public OapiV2DepartmentCreateRequest toCreateRequest() {
        OapiV2DepartmentCreateRequest req = new OapiV2DepartmentCreateRequest();
        req.setName(name);
        if (parentId != null) {
            req.setParentId(parentId);
        }
        req.setOrder(order);

        req.setOuterDept(outerDept);
        req.setHideDept(hideDept);
        req.setCreateDeptGroup(createDeptGroup);
        req.setOrder(order);
        req.setSourceIdentifier(sourceIdentifier);
        req.setDeptPermits(deptPermits);
        req.setUserPermits(userPermits);
        req.setOuterPermitUsers(outerPermitUsers);
        req.setOuterPermitDepts(outerPermitDepts);
        req.setOuterDeptOnlySelf(outerDeptOnlySelf);
        return req;
    }
}