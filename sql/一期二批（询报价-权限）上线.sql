----------------------------------
-- 一 、询报价权限表创建
----------------------------------

CREATE TABLE `ss_tenant_org_employee_manage_scope` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表主键id',
  `employee_id` bigint DEFAULT NULL COMMENT '采购方的员工id',
  `scope` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'self 本人, org_all 全组织',
  `created_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '最后修改人',
  `last_modified_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  `is_deleted` bit(1) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `UK_4v48b9421sg2k6pvv7adpa5lb` (`employee_id`)
) ENGINE=InnoDB AUTO_INCREMENT=359 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
