-- 根据组织成员id查询角色信息
select r.* from ss_sys_role r join ss_tenant_org_employee_role er on r.id =er.role_id where  er.employee_id = 295;

-- 组织id查询组织成员
select * from `ss_tenant_org_employee` where org_id = 112;

-- 手机号码查询组织成员
select * from `ss_tenant_org_employee` where mobile = '';

select * from ss_tenant_org_employee_role where employee_id = 295;

--查询组织成员所有角色
select r.* from ss_sys_role r join ss_tenant_org_employee_role er on r.id =er.role_id where  er.employee_id = 332;

-- 查询角色所有菜单
select m.* from ss_sys_menu m join ss_sys_role_menu rm on m.id = rm.menu_id where rm.role_id = 104;

--
select * from ss_tenant_org_employee_role where  employee_id = 295;

--赋予组织成员角色
insert into ss_tenant_org_employee_role(employee_id,role_id) values (295,48);

delete from ss_tenant_org_employee_role where role_id =48 and employee_id =295;