-- 询报价


-- 合同

-- 配置表 ss_sys_config_param
-- 插入配置值
INSERT INTO `bncloud_platform`.`ss_sys_config_param` (`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `category_code`, `category_name`, `code`, `company_id`, `org_id`, `filter_type`, `filter_value`, `remark`, `type`, `value`) VALUES (535, b'0', 1, '2022-01-25 10:18:34.000000', 751, '2022-03-17 02:24:58.667000', 'contract', '合同自动发送', 'contract:contract_auto_send', NULL, 112, NULL, NULL, '合同同步自动发送', 'BOOL', 'false');

-- 合同表文件表
-- 请求id
ALTER TABLE contract.t_purchase_attachment_rel ADD  request_id int NULL DEFAULT NULL COMMENT '请求id' ;

-- 合同表
-- 事项类型字段
ALTER TABLE contract.t_purchase_contract ADD  event_type int NULL DEFAULT NULL COMMENT '事项类型' ;

-- 流程编号
ALTER TABLE contract.t_purchase_contract ADD  process_number VARCHAR(255) COMMENT '流程编号' ;

-- 请求id
ALTER TABLE contract.t_purchase_contract ADD  request_id int NULL DEFAULT NULL COMMENT '请求id' ;

-- 流程状态
ALTER TABLE contract.t_purchase_contract ADD  process_status VARCHAR(255) COMMENT '流程状态' ;

-- 合作方类型
ALTER TABLE contract.t_purchase_contract ADD  partner_type int NULL DEFAULT NULL COMMENT '合作方类型' ;

-- 同步的oa合同创建时间
ALTER TABLE contract.t_purchase_contract ADD  contract_create_time datetime NULL DEFAULT NULL COMMENT '同步的oa合同创建时间' ;

-- 对账

-- 配置 已上uat
INSERT INTO `bncloud_platform`.`ss_sys_dict`(`code`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `description`, `ext_json`) VALUES ('financial_status', b'0', 751, '2022-02-28 01:29:37.905000', 751, '2022-02-28 01:29:37.905000', '对账状态', NULL);

INSERT INTO `bncloud_platform`.`ss_sys_dict_item`( `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `ext_json`, `label`, `order_num`, `value`, `code`) VALUES ( b'0', 751, '2022-02-28 01:29:48.871000', 1, '2022-03-10 03:36:12.496000', NULL, '已对账', 0, 'Y', 'financial_status');
INSERT INTO `bncloud_platform`.`ss_sys_dict_item`( `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `ext_json`, `label`, `order_num`, `value`, `code`) VALUES ( b'0', 751, '2022-02-28 01:30:15.813000', 1, '2022-03-10 03:36:03.199000', NULL, '未对账', 0, 'N', 'financial_status');

-- 对账模块表结构 已上uat

-- ----------------------------
-- Table structure for t_financial_attachment
-- ----------------------------
use bncloud_platform
DROP TABLE IF EXISTS `t_financial_attachment`;
CREATE TABLE `t_financial_attachment`  (
  `id` bigint(0) NOT NULL COMMENT '主键ID',
  `business_form_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务表单ID',
  `attachment_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件ID',
  `attachment_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件名称',
  `attachment_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件地址',
  `attachment_biz_type` int(0) NULL DEFAULT 0 COMMENT '附件业务类型 0-对账单  1-发票通知单',
  `business_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务编码',
  `business_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务名称',
  `created_by` bigint(0) NULL DEFAULT NULL COMMENT '创建人',
  `created_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` bigint(0) NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int(0) NULL DEFAULT NULL COMMENT '删除标志;0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '对账附件信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_financial_back_reason
-- ----------------------------
DROP TABLE IF EXISTS `t_financial_back_reason`;
CREATE TABLE `t_financial_back_reason`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `bill_id` bigint(0) NULL DEFAULT NULL COMMENT '单据ID',
  `bill_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据类型，0对账单，1费用单',
  `reason` varchar(900) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退回原因',
  `created_by` bigint(0) NULL DEFAULT NULL COMMENT '创建人',
  `created_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` bigint(0) NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int(0) NULL DEFAULT NULL COMMENT '删除标志;0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '对账单退回原因信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_financial_cost_bill
-- ----------------------------
DROP TABLE IF EXISTS `t_financial_cost_bill`;
CREATE TABLE `t_financial_cost_bill`  (
  `id` bigint(0) NOT NULL COMMENT '主键ID',
  `org_id` bigint(0) NULL DEFAULT NULL COMMENT '协作组织id',
  `customer_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户名称',
  `customer_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户编码',
  `supplier_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商编码',
  `supplier_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `publish_time` date NULL DEFAULT NULL COMMENT '发布时间',
  `publisher` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发布人名称',
  `cost_bill_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用单号',
  `currency_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '币种编码',
  `currency_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '币种名称',
  `cost_bill_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用单类型：一般费用单（目前只有一种）',
  `source_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源：supplier 表示供应方，purchase 表示采购方',
  `settlement_pool_sync_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '结算池，入池状态，N未入池，Y已入池',
  `created_by` bigint(0) NULL DEFAULT NULL COMMENT '创建人',
  `created_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` bigint(0) NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int(0) NULL DEFAULT NULL COMMENT '删除标志;0未删除，1已删除',
  `confirm_time` date NULL DEFAULT NULL COMMENT '确认时间',
  `confirm_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '确认人',
  `erp_bill_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'erp金蝶单据编码',
  `erp_bill_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'erp金蝶单据类型',
  `erp_bill_id` bigint(0) NULL DEFAULT NULL COMMENT 'erp金蝶单据id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `all_amount` decimal(24, 4) NULL DEFAULT NULL COMMENT '价税合计金额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '费用单据信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_financial_cost_bill_line
-- ----------------------------
DROP TABLE IF EXISTS `t_financial_cost_bill_line`;
CREATE TABLE `t_financial_cost_bill_line`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `cost_bill_id` bigint(0) NULL DEFAULT NULL COMMENT '费用单据id',
  `cost_summary` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用摘要',
  `cost_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用类型;字典account_cost_type',
  `cost_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用原因',
  `cost_amount` decimal(24, 2) NULL DEFAULT NULL COMMENT '费用金额',
  `created_by` bigint(0) NULL DEFAULT NULL COMMENT '创建人',
  `created_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` bigint(0) NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int(0) NULL DEFAULT NULL COMMENT '删除标志;0未删除，1已删除',
  `cost_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用名称',
  `erp_line_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'erp明细id',
  `tax_rate` decimal(24, 4) NULL DEFAULT NULL COMMENT '税率',
  `tax_included_amount` decimal(24, 4) NULL DEFAULT NULL COMMENT '含税金额',
  `not_tax_amount` decimal(24, 4) NULL DEFAULT NULL COMMENT '不含税金额',
  `tax_amount` decimal(24, 4) NULL DEFAULT NULL COMMENT '税额',
  `erp_cost_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'erp费用编码',
  `have_tax` bit(1) NULL DEFAULT NULL COMMENT '是否含税',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '费用明细信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_financial_cost_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_financial_cost_detail`;
CREATE TABLE `t_financial_cost_detail`  (
  `id` bigint(0) NOT NULL COMMENT '主键ID',
  `statement_id` bigint(0) NULL DEFAULT NULL COMMENT '对账单ID，t_account_statement.id',
  `cost_bill_id` bigint(0) NULL DEFAULT NULL COMMENT '费用单ID，t_financial_cost_bill.id',
  `document_type_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据类型编码',
  `document_type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据类型名称',
  `cost_amount` decimal(24, 2) NULL DEFAULT NULL COMMENT '费用金额',
  `check_include_tax` decimal(24, 2) NULL DEFAULT NULL COMMENT '对账含税金额',
  `check_not_tax_amount` decimal(24, 2) NULL DEFAULT NULL COMMENT '对账未税金额',
  `customer_confirm` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户确认',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `created_by` bigint(0) NULL DEFAULT NULL COMMENT '创建人',
  `created_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` bigint(0) NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int(0) NULL DEFAULT NULL COMMENT '删除标志;0未删除，1已删除',
  `cost_bill_line_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用明细id',
  `bill_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用单号',
  `erp_bill_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ERP费用单号',
  `item_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项次',
  `check_tax_amount` decimal(24, 4) NULL DEFAULT NULL COMMENT '对账税额',
  `cost_remark` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用说明',
  `erp_bill_id` bigint(0) NULL DEFAULT NULL COMMENT 'erp金蝶单据id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '对账费用信息明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_financial_delivery_bill
-- ----------------------------
DROP TABLE IF EXISTS `t_financial_delivery_bill`;
CREATE TABLE `t_financial_delivery_bill`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `created_by` bigint(0) NULL DEFAULT NULL COMMENT '创建人',
  `created_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` bigint(0) NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int(0) NULL DEFAULT NULL COMMENT '删除标志;0未删除，1已删除',
  `customer_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户名称',
  `customer_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户编码',
  `supplier_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商编码',
  `supplier_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `org_id` bigint(0) NULL DEFAULT NULL COMMENT '协作组织id',
  `delivery_bill_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '送货单号',
  `erp_bill_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'erp金蝶单据编码',
  `erp_bill_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'erp金蝶单据类型',
  `currency_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '币种编码',
  `currency_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '币种名称',
  `source_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源：supplier 表示供应方，purchase 表示采购方',
  `signing_time` date NULL DEFAULT NULL COMMENT '送货通知签收时间',
  `remark` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `delivery_date` datetime(0) NULL DEFAULT NULL COMMENT '送货日期',
  `delivery_num` int(0) NULL DEFAULT NULL COMMENT '送货数量',
  `erp_bill_id` bigint(0) NULL DEFAULT NULL COMMENT 'erp金蝶单据id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '送货信息抬头表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_financial_delivery_bill_line
-- ----------------------------
DROP TABLE IF EXISTS `t_financial_delivery_bill_line`;
CREATE TABLE `t_financial_delivery_bill_line`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `created_by` bigint(0) NULL DEFAULT NULL COMMENT '创建人',
  `created_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` bigint(0) NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int(0) NULL DEFAULT NULL COMMENT '删除标志;0未删除，1已删除',
  `delivery_bill_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '送货单据id',
  `erp_line_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'erp明细id',
  `instock_no` varchar(0) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '入库单号',
  `delivery_amount` decimal(24, 4) NULL DEFAULT NULL COMMENT '送货金额',
  `tax_rate` decimal(24, 4) NULL DEFAULT NULL COMMENT '税率',
  `tax_included_amount` decimal(24, 4) NULL DEFAULT NULL COMMENT '含税金额',
  `not_tax_amount` decimal(24, 4) NULL DEFAULT NULL COMMENT '不含税金额',
  `tax_amount` decimal(24, 4) NULL DEFAULT NULL COMMENT '税额',
  `have_tax` bit(1) NULL DEFAULT NULL COMMENT '是否含税',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '送货信息明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_financial_delivery_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_financial_delivery_detail`;
CREATE TABLE `t_financial_delivery_detail`  (
  `id` bigint(0) NOT NULL COMMENT '主键ID',
  `created_by` bigint(0) NULL DEFAULT NULL COMMENT '创建人',
  `created_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` bigint(0) NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int(0) NULL DEFAULT NULL COMMENT '删除标志;0 未删除，1已删除',
  `statement_id` bigint(0) NULL DEFAULT NULL COMMENT '对账单ID;t_account_statement.id',
  `item_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项次',
  `delivery_bill_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '送货id',
  `bill_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '送货单号',
  `erp_bill_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ERP标准应付单号',
  `delivery_num` int(0) NULL DEFAULT NULL COMMENT '送货数量',
  `delivery_amount` decimal(24, 4) NULL DEFAULT NULL COMMENT '送货金额',
  `delivery_date` datetime(0) NULL DEFAULT NULL COMMENT '送货日期',
  `check_amount` decimal(24, 4) NULL DEFAULT NULL COMMENT '对账金额',
  `tax_rate` decimal(24, 4) NULL DEFAULT NULL COMMENT '税率',
  `have_tax` bit(1) NULL DEFAULT NULL COMMENT '是否含税',
  `confirmed_amount` decimal(24, 4) NULL DEFAULT NULL COMMENT '确认金额',
  `check_quantity` int(0) NULL DEFAULT NULL COMMENT '对账数量',
  `check_include_tax` decimal(24, 4) NULL DEFAULT NULL COMMENT '对账含税金额',
  `check_not_tax_amount` decimal(24, 4) NULL DEFAULT NULL COMMENT '对账未税金额',
  `check_tax_amount` decimal(24, 4) NULL DEFAULT NULL COMMENT '对账税额',
  `customer_confirm` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户确认',
  `remark` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `delivery_remark` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '送货备注',
  `erp_bill_id` bigint(0) NULL DEFAULT NULL COMMENT 'erp金蝶单据id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '对账送货信息明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_financial_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `t_financial_operation_log`;
CREATE TABLE `t_financial_operation_log`  (
  `id` bigint(0) NOT NULL COMMENT '主键ID',
  `bill_id` bigint(0) NULL DEFAULT NULL COMMENT '单据id',
  `bill_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据类型',
  `operation_no` bigint(0) NULL DEFAULT NULL COMMENT '操作人工号',
  `operator_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作人姓名',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作内容',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '说明',
  `created_by` bigint(0) NULL DEFAULT NULL COMMENT '创建人',
  `created_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` bigint(0) NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int(0) NULL DEFAULT NULL COMMENT '删除标志;0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '对账单操作记录日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_financial_pay_method
-- ----------------------------
DROP TABLE IF EXISTS `t_financial_pay_method`;
CREATE TABLE `t_financial_pay_method`  (
  `id` bigint(0) NOT NULL COMMENT '主键ID',
  `method_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付方式名称',
  `org_id` bigint(0) NULL DEFAULT NULL COMMENT '协作组织ID',
  `created_by` bigint(0) NULL DEFAULT NULL COMMENT '创建人',
  `created_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` bigint(0) NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int(0) NULL DEFAULT NULL COMMENT '删除标志;0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '支付方式信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_financial_settlement_pool
-- ----------------------------
DROP TABLE IF EXISTS `t_financial_settlement_pool`;
CREATE TABLE `t_financial_settlement_pool`  (
  `id` bigint(0) NOT NULL COMMENT '主键ID',
  `bill_id` bigint(0) NULL DEFAULT NULL COMMENT '单据ID',
  `bill_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据编码',
  `bill_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据类型，字典financial_bill_type',
  `customer_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购方编码',
  `customer_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购方名称',
  `supplier_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商编码',
  `supplier_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `amount` decimal(24, 4) NULL DEFAULT NULL COMMENT '金额',
  `have_tax` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否含税',
  `tax_rate` decimal(24, 4) NULL DEFAULT NULL COMMENT '税率',
  `confirm_time` date NULL DEFAULT NULL COMMENT '确认时间',
  `collaboration_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '协作组织编码',
  `statement_created` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '已生成对账单明细标志，N未生成，Y已生成',
  `created_by` bigint(0) NULL DEFAULT NULL COMMENT '创建人',
  `created_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` bigint(0) NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int(0) NULL DEFAULT NULL COMMENT '删除标志;0未删除，1已删除',
  `erp_bill_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'erp金蝶单据编码',
  `erp_bill_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'erp金蝶单据类型',
  `currency_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '币种编码',
  `currency_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '币种名称',
  `delivery_num` int(0) NULL DEFAULT NULL COMMENT '送货数量',
  `delivery_date` datetime(0) NULL DEFAULT NULL COMMENT '送货日期',
  `erp_bill_id` bigint(0) NULL DEFAULT NULL COMMENT 'erp金蝶单据id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_bill_no`(`erp_bill_no`) USING BTREE,
  INDEX `idx_customer_code`(`customer_code`) USING BTREE,
  INDEX `idx_customer_name`(`customer_name`) USING BTREE,
  INDEX `idx_supplier_code`(`supplier_code`) USING BTREE,
  INDEX `idx_supplier_name`(`supplier_name`) USING BTREE,
  INDEX `idx_confirm_time`(`confirm_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '结算池单据信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_financial_statement
-- ----------------------------
DROP TABLE IF EXISTS `t_financial_statement`;
CREATE TABLE `t_financial_statement`  (
  `id` bigint(0) NOT NULL COMMENT '主键ID',
  `org_id` bigint(0) NULL DEFAULT NULL COMMENT '协作组织ID',
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '协作组织名称',
  `customer_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购方编码',
  `customer_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购方名称',
  `audit_time` datetime(0) NULL DEFAULT NULL COMMENT '审核时间',
  `statement_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '对账单号',
  `audit_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核人',
  `supplier_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商编号',
  `supplier_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `taxpayer_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '纳税人识别号',
  `bank_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '银行账号',
  `publish_time` datetime(0) NULL DEFAULT NULL COMMENT '发布时间',
  `created_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `bank_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开户行名称',
  `period_start` datetime(0) NULL DEFAULT NULL COMMENT '对账周期开始时间',
  `period_end` datetime(0) NULL DEFAULT NULL COMMENT '对账周期截止时间',
  `currency_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '币种编码',
  `currency_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '币种名称',
  `payment_terms` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '付款条件',
  `account_due_date` datetime(0) NULL DEFAULT NULL COMMENT '账款到期日',
  `pay_method_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付方式编码',
  `pay_method_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付方式名称',
  `statement_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
  `customer_marks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购方备注',
  `supplier_marks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商备注',
  `source_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源，purchase 采购方，supplier 供应商',
  `shipment_including_tax` decimal(24, 2) NULL DEFAULT NULL COMMENT '送货汇总含税金额',
  `shipment_not_tax` decimal(24, 2) NULL DEFAULT NULL COMMENT '送货汇总未含税金额',
  `shipment_tax_amount` decimal(24, 2) NULL DEFAULT NULL COMMENT '送货汇总税额',
  `cost_including_tax` decimal(24, 2) NULL DEFAULT NULL COMMENT '费用汇总含税金额',
  `cost_not_including_tax` decimal(24, 2) NULL DEFAULT NULL COMMENT '费用汇总未含税金额',
  `cost_tax_amount` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '费用汇总税额',
  `statement_including_tax` decimal(24, 2) NULL DEFAULT NULL COMMENT '对账含税金额',
  `statement_not_tax` decimal(24, 2) NULL DEFAULT NULL COMMENT '对账未税金额',
  `statement_tax_amount` decimal(24, 2) NULL DEFAULT NULL COMMENT '对账税额',
  `created_by` bigint(0) NULL DEFAULT NULL COMMENT '创建人',
  `created_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` bigint(0) NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int(0) NULL DEFAULT NULL COMMENT '删除标志',
  `check_shipment_amount` decimal(24, 4) NULL DEFAULT NULL COMMENT '确认送货金额',
  `check_cost_amount` decimal(24, 4) NULL DEFAULT NULL COMMENT '确认费用金额',
  `check_statement_including_tax` decimal(24, 4) NULL DEFAULT NULL COMMENT '确认对账含税金额',
  `check_statement_not_tax` decimal(24, 4) NULL DEFAULT NULL COMMENT '确认对账不含税金额',
  `check_statement_tax_amount` decimal(24, 4) NULL DEFAULT NULL COMMENT '确认对账税额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '对账单信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_financial_statement_pool_rel
-- ----------------------------
DROP TABLE IF EXISTS `t_financial_statement_pool_rel`;
CREATE TABLE `t_financial_statement_pool_rel`  (
  `id` bigint(0) NOT NULL COMMENT '主键ID',
  `settlement_pool_id` bigint(0) NULL DEFAULT NULL COMMENT '结算池单据ID;t_account_settlement_pool.id',
  `statement_id` bigint(0) NULL DEFAULT NULL COMMENT '对账单ID;t_account_statement.id',
  `created_by` bigint(0) NULL DEFAULT NULL COMMENT '创建人',
  `created_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` bigint(0) NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int(0) NULL DEFAULT NULL COMMENT '删除标志;0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '对账单与结算单池单据关联关系表' ROW_FORMAT = Dynamic;


