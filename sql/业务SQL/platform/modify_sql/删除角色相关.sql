
--delete from ss_sys_role where id in (-1) ;

--删除角色时的前置删除sql条件
delete from ss_sys_user_role where role_id in (-1);

delete from t_supplier_staff_role where role_id in (-1);

delete from ss_tenant_org_employee_role where role_id in (-1);

delete from ss_sys_role_data_grant_rel where role_id in (-1);

delete from ss_sys_role_dimension_perm_rel where  role_id in (-1);

delete from ss_sys_role_menu where role_id in (-1);

-- 删除组织成员角色
delete  from ss_tenant_org_employee_role where employee_id in (select id from ss_tenant_org_employee where id in (333));
-- 删除供应商成员角色
delete from t_supplier_staff_role where supplier_staff_id in (select id from `t_supplier_staff` where id in (2183,2184));

--2.删除用户-角色关联
delete from t_supplier_staff_role where role_id in (44);
