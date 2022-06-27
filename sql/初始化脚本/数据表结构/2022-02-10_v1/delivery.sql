/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.2.136-美尚
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : 192.168.2.136:3306
 Source Schema         : delivery

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 10/02/2022 17:54:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_delivery_attachment_rel
-- ----------------------------
DROP TABLE IF EXISTS `t_delivery_attachment_rel`;
CREATE TABLE `t_delivery_attachment_rel`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `business_form_id` bigint NULL DEFAULT NULL COMMENT '业务表单ID 送货表的主键ID',
  `attachment_id` bigint NULL DEFAULT NULL COMMENT '附件ID t_sys_attachment.id',
  `attachment_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件名称',
  `attachment_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件URL',
  `attachment_size` bigint NULL DEFAULT NULL COMMENT '附件大小',
  `business_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务编码',
  `business_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务名称',
  `created_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `created_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_date` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int NULL DEFAULT 0 COMMENT '删除标志 0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '发货与附件关联关系表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_delivery_country
-- ----------------------------
DROP TABLE IF EXISTS `t_delivery_country`;
CREATE TABLE `t_delivery_country`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `country_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '国家编码',
  `country_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '国家名称',
  `created_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `created_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_date` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int NULL DEFAULT 0 COMMENT '删除标志 0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '国家信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_delivery_customs_information
-- ----------------------------
DROP TABLE IF EXISTS `t_delivery_customs_information`;
CREATE TABLE `t_delivery_customs_information`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `delivery_id` bigint NULL DEFAULT NULL COMMENT '送货单ID t_delivery_note.delivery_id',
  `delivery_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '送货单号',
  `inner_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内部单号',
  `tracking_number` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提单号码',
  `customs_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报关类型',
  `destination_harbor` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目的港',
  `shipment_harbor` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '装运港',
  `origin_country` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原产国',
  `invoice_number` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发票号码',
  `invoice_date` datetime NULL DEFAULT NULL COMMENT '发票日期',
  `created_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `created_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_date` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int NULL DEFAULT NULL COMMENT '删除标志 0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '报关资料信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_delivery_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_delivery_detail`;
CREATE TABLE `t_delivery_detail`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `delivery_id` bigint NULL DEFAULT NULL COMMENT '送货单ID t_delivery_note.delivery_id',
  `item_no` int NULL DEFAULT NULL COMMENT '项次',
  `plan_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '送货计划编号',
  `plan_detail_item_source_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '送货计划明细项次来源系统ID，t_delivery_plan_detail_item.source_id',
  `purchase_order_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '采购单号',
  `product_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品编码',
  `product_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品名称',
  `product_specs` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品规格',
  `plan_quantity` decimal(20, 2) NULL DEFAULT NULL COMMENT '计划数量',
  `real_delivery_quantity` decimal(20, 2) NULL DEFAULT NULL COMMENT '实际送货数量',
  `bar_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '条码',
  `batch_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次',
  `delivery_unit_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计划单位编码',
  `delivery_unit_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计划单位名称',
  `receipt_quantity` decimal(20, 2) NULL DEFAULT NULL COMMENT '收货数量',
  `warehouse` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入货仓',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `created_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `created_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_date` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int NULL DEFAULT NULL COMMENT '删除标志 0未删除，1已删除',
  `currency` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结算币种',
  `material_classification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料分类',
  `plan_unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计划单位',
  `erp_id` bigint NULL DEFAULT NULL COMMENT '收料通知单回传的erpId',
  `delivery_plan_detail_item_id` bigint NULL DEFAULT NULL COMMENT '项次id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_delivery_id`(`delivery_id`) USING BTREE,
  INDEX `uidx_plan_detail_item_source_id`(`plan_detail_item_source_id`, `is_deleted`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '送货明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_delivery_harbor
-- ----------------------------
DROP TABLE IF EXISTS `t_delivery_harbor`;
CREATE TABLE `t_delivery_harbor`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `harbor_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '港口编码',
  `harbor_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '港口名称',
  `created_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `created_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_date` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int NULL DEFAULT NULL COMMENT '删除标志 0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '港口信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_delivery_note
-- ----------------------------
DROP TABLE IF EXISTS `t_delivery_note`;
CREATE TABLE `t_delivery_note`  (
  `id` bigint NOT NULL COMMENT '送货单ID',
  `supplier_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商编码',
  `supplier_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `customer_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户编码/采购方编码',
  `customer_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户名称',
  `receipt` datetime NULL DEFAULT NULL COMMENT '收货时间（废弃）',
  `delivery_date` date NULL DEFAULT NULL COMMENT '送货日期',
  `estimated_time` datetime NULL DEFAULT NULL COMMENT '预计到厂',
  `barcode_version` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '条形码版本',
  `delivery_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '送货单号',
  `receipt_dept_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货部门编码',
  `receipt_dept_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货部门名称',
  `purchase_remark` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购说明',
  `inner_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内部单号',
  `delivery_method` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '送货方式 由字典编码delivery_method定义',
  `delivery_car_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '送货车牌',
  `self_mention_address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自提地址',
  `delivery_address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '送货地址',
  `shipment_number` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流单号',
  `delivery_remark` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '送货备注',
  `transport_method` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '运输方式 由字典编码delivery_transport_method定义',
  `driver_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '司机姓名',
  `driver_telephone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '司机电话',
  `receiving_contact` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货联系人',
  `gross_weight` decimal(32, 2) NULL DEFAULT NULL COMMENT '整单毛重',
  `package_method` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '包装方式 由字典编码delivery_package_method定义',
  `package_num` decimal(32, 4) NULL DEFAULT NULL COMMENT '包装总数',
  `net_weight` decimal(32, 4) NULL DEFAULT NULL COMMENT '整单净重',
  `weight_unit_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '重量单位 由字典编码common_weight_unit定义',
  `package_total` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '明细包装汇总',
  `real_delivery_num` decimal(32, 2) NULL DEFAULT NULL COMMENT '实际送货总数量',
  `delivery_amount` decimal(32, 2) NULL DEFAULT NULL COMMENT '送货单金额',
  `delivery_status_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '送货状态 由字典delivery_status_code定义',
  `delivery_check_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '校验状态 由字典delivery_check_status定义',
  `delivery_purchase_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购类型 由字典delivery_purchase_type定义',
  `delivery_type_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '送货类型 由字典delivery_type_code定义',
  `delivery_ticket` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '随货送票 1是，2否',
  `delivery_application` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '送货申请开启状态 N 关闭，Y 开启',
  `priority_check` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优先校验 Y是，N否',
  `logistics_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流状态 由字典logistics_status 定义',
  `arrival_time` datetime NULL DEFAULT NULL COMMENT '到货时间',
  `check_results` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '检验结果',
  `sign_in_discrepancy` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '签收差异，由字典delivery_difference_state配置',
  `signing_time` datetime NULL DEFAULT NULL COMMENT '签收时间',
  `erp_signing_time` datetime NULL DEFAULT NULL COMMENT 'ERP签收时间',
  `erp_signing_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ERP签收状态',
  `settlement_pool_sync_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '结算池，入池状态，N未入池，Y已入池',
  `org_id` bigint NULL DEFAULT NULL COMMENT '组织ID',
  `created_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `created_by_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `created_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_date` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int NULL DEFAULT NULL COMMENT '删除标志 0未删除，1已删除',
  `currency` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结算币种',
  `material_classification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料分类',
  `plan_unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计划单位',
  `erp_id` bigint NULL DEFAULT NULL COMMENT '收料通知单回传的erpId',
  `f_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收料通知单回传的fNumber',
  `bill_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据编号(对应计划表的bill_no)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '送货单信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_delivery_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `t_delivery_operation_log`;
CREATE TABLE `t_delivery_operation_log`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `bill_id` bigint NULL DEFAULT NULL COMMENT '单据id：送货计划id、送货单id',
  `operator_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作人工号',
  `operator_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作人姓名',
  `operator_content` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作内容',
  `remark` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '说明',
  `created_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `created_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` bigint NULL DEFAULT NULL COMMENT '更新人',
  `last_modified_date` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int NULL DEFAULT NULL COMMENT '删除标志 0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '送货通知操作记录信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_delivery_plan
-- ----------------------------
DROP TABLE IF EXISTS `t_delivery_plan`;
CREATE TABLE `t_delivery_plan`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `org_id` bigint NULL DEFAULT NULL COMMENT '协作组织id',
  `plan_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计划编号',
  `purchase_order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购订单号',
  `bill_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据编号',
  `supplier_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商编码',
  `purchase_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购方编码',
  `purchase_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购方名称',
  `publisher` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发布人',
  `publish_date` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `plan_start_date` datetime NULL DEFAULT NULL COMMENT '计划区间-开始时间',
  `plan_end_date` datetime NULL DEFAULT NULL COMMENT '计划区间-结束时间',
  `plan_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计划状态：0草稿、1待确认、2已确认',
  `plan_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计划说明',
  `currency` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结算币种',
  `warehousing` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '入货仓',
  `material_classification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料分类',
  `source_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源系统主键ID',
  `created_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `created_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `last_modified_date` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `is_deleted` bit(1) NULL DEFAULT NULL COMMENT '逻辑删除：0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_source_id`(`source_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '送货计划基础信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_delivery_plan_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_delivery_plan_detail`;
CREATE TABLE `t_delivery_plan_detail`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `delivery_plan_id` bigint NULL DEFAULT NULL COMMENT '送货计划id',
  `product_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产品编码',
  `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产品名称',
  `product_specification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产品规格',
  `supplier_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商编码',
  `supplier_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `purchase_order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购单号',
  `delivery_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '送货地址',
  `plan_unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计划单位',
  `plan_quantity` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计划数量',
  `confirm_quantity` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '确认数量',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态（目前等价于计划状态）',
  `latest_delivery_date` datetime NULL DEFAULT NULL COMMENT '最近交货日期',
  `latest_plan_quantity` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最近计划数量',
  `latest_confirm_quantity` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最近确认数量',
  `source_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源系统ID',
  `created_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `created_date` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `last_modified_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `last_modified_date` datetime NULL DEFAULT NULL COMMENT '修改日期',
  `is_deleted` bit(1) NULL DEFAULT NULL COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_delivery_plan_detail_item
-- ----------------------------
DROP TABLE IF EXISTS `t_delivery_plan_detail_item`;
CREATE TABLE `t_delivery_plan_detail_item`  (
  `id` bigint NOT NULL COMMENT '主键ID',
  `delivery_plan_detail_id` bigint NULL DEFAULT NULL COMMENT '送货计划明细id',
  `delivery_note_id` bigint NULL DEFAULT NULL COMMENT '送货单id',
  `delivery_note_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '送货单号',
  `delivery_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '送货状态：0草稿，1待签收，2已签收',
  `delivery_date` datetime NULL DEFAULT NULL COMMENT '送货日期',
  `delivery_quantity` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '送货数量',
  `confirm_date` datetime NULL DEFAULT NULL COMMENT '确认时间',
  `confirm_quantity` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '确认数量',
  `source_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源系统ID',
  `created_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `created_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `last_modified_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `is_deleted` bit(1) NULL DEFAULT NULL COMMENT '逻辑删除：0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '送货计划明细批次表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
