

select * from ss_tenant_organization;

delete from ss_tenant_organization where id in (113,114,115,116);

select * from ss_tenant_org_manager;

delete from ss_tenant_org_manager where org_id in (113,114,115,116);

delete from ss_tenant_org_employee where org_id in (113,114,115,116);

select * from ss_tenant_org_employee_role;

-- 删除指定组织的组织成员的角色
delete from ss_tenant_org_employee_role er where er.employee_id in (
    select id from  ss_tenant_org_employee where  org_id in (113,114,115,116);
);


-- 为组织成员添加指定角色
insert into ss_tenant_org_employee_role (employee_id, role_id) values (295, 48);