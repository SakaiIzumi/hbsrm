/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.2.136-美尚
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : 192.168.2.136:3306
 Source Schema         : order

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 10/02/2022 17:57:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order`  (
  `id` bigint NOT NULL COMMENT '主键',
  `order_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '订单金额',
  `order_confirm_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '订单确认金额',
  `confirm_user_id` bigint NULL DEFAULT NULL COMMENT '订单确认人ID',
  `confirm_user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单确认人名称',
  `trade_term` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交易条件(美尚隐藏)',
  `currency` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '币别',
  `tax_category` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '税别(美尚隐藏)',
  `payment_terms` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '付款条件',
  `confirm_source` int NULL DEFAULT NULL COMMENT '确认方 1智采 2 智易',
  `confirm_time` datetime NULL DEFAULT NULL COMMENT '确认时间',
  `purchase_time` datetime NULL DEFAULT NULL COMMENT '采购日期',
  `purchase_user_id` bigint NULL DEFAULT NULL COMMENT '采购员ID',
  `purchase_user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购员名称',
  `purchase_department` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购部门',
  `purchase_order_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购单号',
  `purchase_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购方编码',
  `purchase_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购方名称',
  `supplier_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `supplier_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商编码',
  `supplier_files` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商附件',
  `side_letter` varchar(225) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '补充条款（美尚隐藏）',
  `logistics_mode` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流方式（美尚隐藏）',
  `receiving_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货地址',
  `consignee` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货人（美尚隐藏）',
  `order_remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单备注',
  `msg_type` int NULL DEFAULT NULL COMMENT '消息状态0未读1已读',
  `change_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '变更状态 1）无变更\n2）已确认并更\n3）未确认变更\n4）待确认变更',
  `before_order_status` int NULL DEFAULT NULL COMMENT '上一个订单状态',
  `sum_status` int NULL DEFAULT NULL COMMENT '计价状态 1未计算 2已计算',
  `order_status` int NULL DEFAULT NULL COMMENT '订单状态 1）草稿\n2）待答交\n3）挂起\n4）答交差异\n5）退回\n6）变更中\n7）已确认\n8）已完成',
  `order_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单类型 ERP获取类型',
  `take_over_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货状态 1）待收货\n2）部分收货\n3）收货完成\n4）已结案',
  `difference_details` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '差异详情 1）单价差异\n2）数量差异\n3）交期差异\n4）无差异',
  `sign_contract_status` int NULL DEFAULT NULL COMMENT '签约状态\n1）待签约\n2）异常\n3）已签约\n',
  `is_deleted` int NULL DEFAULT NULL COMMENT '乐观锁0有效1无效',
  `created_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `created_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `org_id` int NULL DEFAULT NULL,
  `order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单编号',
  `acceptance_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '验收方式',
  `payment_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '付款方式',
  `accounting_period` bigint NULL DEFAULT NULL COMMENT '账期（天）',
  `item_class` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料分类',
  `shipping_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '运输方式',
  `change_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '变更原因',
  `change_order_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '变更状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单表 订单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_order_change_log
-- ----------------------------
DROP TABLE IF EXISTS `t_order_change_log`;
CREATE TABLE `t_order_change_log`  (
  `id` bigint NOT NULL COMMENT '主键',
  `update_types` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改类型集合',
  `purchase_order_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购单号',
  `order_communicate_log_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联答交记录表',
  `source` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源',
  `is_deleted` int NULL DEFAULT NULL COMMENT '乐观锁0有效1无效',
  `created_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `created_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `change_reason` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改原因',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '修改日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_order_communicate_log
-- ----------------------------
DROP TABLE IF EXISTS `t_order_communicate_log`;
CREATE TABLE `t_order_communicate_log`  (
  `id` bigint NOT NULL COMMENT '主键',
  `order_product_details_id` bigint NULL DEFAULT NULL COMMENT '关联order_product_details表',
  `purchase_order_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购单号',
  `item_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联项次',
  `item` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项次',
  `delivery_time` datetime NULL DEFAULT NULL COMMENT '交货日期',
  `delivery_time_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交货日期修改类型0未修改1修改',
  `purchase_num` decimal(32, 2) NULL DEFAULT NULL COMMENT '采购数量',
  `send_num` decimal(10, 0) NULL DEFAULT NULL COMMENT '已发数量',
  `purchase_num_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购数量修改类型0未修改1修改',
  `unit_price` decimal(32, 2) NULL DEFAULT NULL COMMENT '单价',
  `unit_price_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单价修改类型0未修改1修改',
  `brand_remarks` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `files` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件',
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型1变更信息2答交信息3答交差异',
  `status` int NULL DEFAULT 0 COMMENT '0未生效1已处理2作废',
  `take_over_status` int NULL DEFAULT NULL COMMENT '收货状态 1）待收货\n2）部分收货\n3）收货完成\n4）已结案',
  `batch` int NULL DEFAULT NULL COMMENT '批次,辅助字段',
  `is_deleted` int NULL DEFAULT NULL COMMENT '乐观锁0有效1无效',
  `created_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `created_date` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_date` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单答交日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_order_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `t_order_operation_log`;
CREATE TABLE `t_order_operation_log`  (
  `id` bigint NOT NULL COMMENT '主键',
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容',
  `purchase_order_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购单号',
  `is_deleted` int NULL DEFAULT NULL COMMENT '乐观锁0有效1无效',
  `created_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `created_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `created_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_date` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单操作记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_order_product_details
-- ----------------------------
DROP TABLE IF EXISTS `t_order_product_details`;
CREATE TABLE `t_order_product_details`  (
  `id` bigint NOT NULL COMMENT '主键',
  `purchase_order_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购单号',
  `item_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项次',
  `product_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产品编码',
  `product_name` varchar(2555) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产品名称',
  `product_specs` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产品规格',
  `take_over_department_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货部门名称',
  `take_over_department_id` bigint NULL DEFAULT NULL COMMENT '收货部门ID',
  `take_over_warehouse` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货仓库',
  `purchase_remarks` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购说明',
  `delivery_time` datetime NULL DEFAULT NULL COMMENT '交货日期',
  `delivery_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交货方式',
  `status` int NULL DEFAULT NULL COMMENT '答交状态\n1、订单状态：\n1）草稿\n2）待答交\n3）已留置\n4）答交差异\n5）退回\n6）变更中\n7）已确认\n8）已完成\n',
  `take_over_status` int NULL DEFAULT NULL COMMENT '收货状态 1）待收货\n2）部分收货\n3）收货完成\n4）已结案',
  `sum_status` int NULL DEFAULT NULL COMMENT '计算状态1 未计算 2已计算用于计算订单金额',
  `purchase_num` decimal(10, 2) NULL DEFAULT NULL COMMENT '采购数量',
  `valuation_unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计价数量',
  `purchase_unit` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购单位',
  `purchase_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购编码',
  `send_num` decimal(20, 2) NULL DEFAULT NULL COMMENT '已发数量（美尚隐藏）',
  `mark_down_num` decimal(10, 2) NULL DEFAULT NULL COMMENT '计价数量',
  `unit_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '单价',
  `product_total_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '产品总价',
  `supplier_remarks` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商备注',
  `brand_remarks` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌方备注',
  `files` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件',
  `is_deleted` int NULL DEFAULT NULL COMMENT '乐观锁0有效1无效',
  `created_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `created_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tax_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '含税单价',
  `tax_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '税率',
  `tax_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '税额',
  `all_amount` decimal(10, 0) NULL DEFAULT NULL COMMENT '税价合计',
  `change_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '变更标志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '产品明细表 产品明细表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_order_product_details_change_info
-- ----------------------------
DROP TABLE IF EXISTS `t_order_product_details_change_info`;
CREATE TABLE `t_order_product_details_change_info`  (
  `id` bigint NOT NULL COMMENT '主键',
  `product_history_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '历史明细id',
  `item_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项次',
  `created_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `created_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tax_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '含税单价',
  `tax_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '税率',
  `tax_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '税额',
  `all_amount` decimal(10, 0) NULL DEFAULT NULL COMMENT '税价合计',
  `change_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '变更标志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '产品明细表 产品明细表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_order_product_details_history
-- ----------------------------
DROP TABLE IF EXISTS `t_order_product_details_history`;
CREATE TABLE `t_order_product_details_history`  (
  `id` bigint NOT NULL COMMENT '主键',
  `purchase_order_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购单号',
  `item_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项次',
  `product_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产品编码',
  `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产品名称',
  `product_specs` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产品规格',
  `take_over_department_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货部门名称',
  `take_over_department_id` bigint NULL DEFAULT NULL COMMENT '收货部门ID',
  `take_over_warehouse` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货仓库',
  `purchase_remarks` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购说明',
  `delivery_time` datetime NULL DEFAULT NULL COMMENT '交货日期',
  `delivery_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交货方式',
  `status` int NULL DEFAULT NULL COMMENT '答交状态\n1、订单状态：\n1）草稿\n2）待答交\n3）已留置\n4）答交差异\n5）退回\n6）变更中\n7）已确认\n8）已完成\n',
  `take_over_status` int NULL DEFAULT NULL COMMENT '收货状态 1）待收货\n2）部分收货\n3）收货完成\n4）已结案',
  `sum_status` int NULL DEFAULT NULL COMMENT '计算状态1 未计算 2已计算用于计算订单金额',
  `purchase_num` decimal(20, 2) NULL DEFAULT NULL COMMENT '采购数量',
  `valuation_unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计价数量',
  `purchase_unit` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购单位',
  `purchase_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购编码',
  `send_num` decimal(20, 2) NULL DEFAULT NULL COMMENT '已发数量（美尚隐藏）',
  `mark_down_num` decimal(10, 2) NULL DEFAULT NULL COMMENT '计价数量',
  `unit_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '单价',
  `product_total_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '产品总价',
  `supplier_remarks` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商备注',
  `brand_remarks` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌方备注',
  `files` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件',
  `is_deleted` int NULL DEFAULT NULL COMMENT '乐观锁0有效1无效',
  `created_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `created_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tax_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '含税单价',
  `tax_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '税率',
  `tax_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '税额',
  `all_amount` decimal(10, 0) NULL DEFAULT NULL COMMENT '税价合计',
  `change_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '变更标志a)含税单价变更 b)数量变更 c) 交期变更 d) 税率变更',
  `before_change` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '与上一条明细字段变更json',
  `pd_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '产品明细表 产品明细表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
