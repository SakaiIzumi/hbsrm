


--查询

--查询角色组
select * from ss_sys_role_group;
-- 查询角色
select * from ss_sys_role;

--查询菜单
select * from ss_sys_menu;


--删除
--1. 删除角色-权限关联
DELETE FROM ss_sys_role_menu WHERE role_id in (66,69);


--2.删除用户-角色关联
DELETE FROM ss_sys_user_role where role_id in (44);

--3. 删除角色
DELETE FROM ss_sys_role WHERE id in (67);
-- 根据角色组织删除橘色
DELETE FROM ss_sys_role WHERE group_id in (11,12,13);


-- 查询平台用户角色
SELECT r.* FROM ss_sys_role r JOIN ss_sys_user_role ur on r.id = ur.role_id  where r.id = 61;
--查询平台用户菜单
SELECT * FROM ss_sys_menu m JOIN ss_sys_role_menu rm on m.id =rm.menu_id WHERE rm.role_id =61;

