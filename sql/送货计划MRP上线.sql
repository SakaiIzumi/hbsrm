-- 采购方工厂假期
CREATE TABLE t_factory_vacation
(
    id                 BIGINT(20)   AUTO_INCREMENT PRIMARY KEY COMMENT '主键id',
    belong_type        VARCHAR(255)  NULL COMMENT '所属类型 供应商/采购方',
    belong_code        VARCHAR(255)  NULL COMMENT '所属方 供应商code/采购方code',
    factory_number     VARCHAR(255)  NULL COMMENT '工厂编码',
    vacation_type      VARCHAR(255)  NULL COMMENT '假期类型  节假日/调休休息',
    source_type        VARCHAR(255)  NULL COMMENT '假期来源 自动订阅的节假日  0-手动新增 1-自动订阅 2-非默认工作日的假期',
    vacation_date      DATETIME      NULL COMMENT '假期日期',
    day_in_week_num    INT(11)       NULL COMMENT '星期几',
    remark             VARCHAR(255)  NULL COMMENT '备注',
    status             INT(11)       NULL COMMENT '启用状态 1-启用 0-禁用',
    created_by         BIGINT(20)    NULL COMMENT '创建人',
    created_date       DATETIME      NULL COMMENT '创建时间',
    last_modified_by   BIGINT(20)    NULL COMMENT '更新人',
    last_modified_date DATETIME      NULL COMMENT '更新时间',
    is_deleted         INT(11)       NULL COMMENT '状态[0:未删除,1:删除]'
);

-- 工厂
CREATE TABLE t_factory_info
(
    id                         BIGINT(20)   AUTO_INCREMENT PRIMARY KEY COMMENT '主键id',
    number                     VARCHAR(255)  NULL COMMENT '编码',
    name                       VARCHAR(255)  NULL COMMENT '工厂名称',
    type                       VARCHAR(255)  NULL COMMENT '工厂类型  收货厂/发货厂',
    belong_type                VARCHAR(255)  NULL COMMENT '所属类型  采购方/供应方',
    belong_code                VARCHAR(255)  NULL COMMENT '所属方 采购方编码/供应商编码',
    area                       VARCHAR(255)  NULL COMMENT '所在区域',
    detailed_address           VARCHAR(255)  NULL COMMENT '详细地址',
    address_longitude_latitude VARCHAR(255)  NULL COMMENT '地址经纬度',
    created_by                 BIGINT(20)    NULL COMMENT '创建人',
    created_date               DATETIME      NULL COMMENT '创建时间',
    last_modified_by           BIGINT(20)    NULL COMMENT '更新人',
    last_modified_date         DATETIME      NULL COMMENT '更新时间',
    is_deleted                 INT(11)       NULL COMMENT '状态[0:未删除,1:删除]',
    province                   VARCHAR(255) NULL COMMENT '省',
    city                       VARCHAR(255) NULL COMMENT '市',
    district                   VARCHAR(255) NULL COMMENT '区',
    street                     VARCHAR(255) NULL COMMENT '街道'
);

-- 工作日
CREATE TABLE t_factory_workday
(
    id                 BIGINT(20)   AUTO_INCREMENT PRIMARY KEY COMMENT '主键id',
    belong_type        VARCHAR(255)  NULL COMMENT '所属类型 供应商/采购方',
    belong_code        VARCHAR(255)  NULL COMMENT '所属方 供应商code/采购方code',
    belong_name        VARCHAR(255)  NULL COMMENT '所属名称 供应商名称',
    factory_number     VARCHAR(255)  NULL COMMENT '工厂Code',
    week_start_num     INT(11)       NULL COMMENT '工作日起始星期  1-7',
    week_end_num       INT(11)       NULL COMMENT '工作日周结束数',
    created_by         BIGINT(20)    NULL COMMENT '创建人',
    created_date       DATETIME      NULL COMMENT '创建时间',
    last_modified_by   BIGINT(20)    NULL COMMENT '更新人',
    last_modified_date DATETIME      NULL COMMENT '更新时间',
    is_deleted         INT(11)       NULL COMMENT '状态[0:未删除,1:删除]'
);

-- 工厂运输时长
CREATE TABLE t_factory_transportation_duration
(
    id                      BIGINT(20)   AUTO_INCREMENT PRIMARY KEY COMMENT '主键id',
    delivery_factory_number VARCHAR(255)  NULL COMMENT '发货厂编码',
    receive_factory_number  VARCHAR(255)  NULL COMMENT '收货厂编码',
    transport_way           VARCHAR(255)  NULL COMMENT '运输方式',
    transport_duration      VARCHAR(255)  NULL COMMENT '运输时长',
    remark                  VARCHAR(255)  NULL COMMENT '备注',
    created_by              BIGINT(20)    NULL COMMENT '创建人',
    created_date            DATETIME      NULL COMMENT '创建时间',
    last_modified_by        BIGINT(20)    NULL COMMENT '更新人',
    last_modified_date      DATETIME      NULL COMMENT '更新时间',
    is_deleted              INT(11)       NULL COMMENT '状态[0:未删除,1:删除]'
);

CREATE TABLE `t_order_delivery_supplier` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `supplier_id` bigint DEFAULT NULL COMMENT '供应商id',
  `supplier_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '供应商名称',
  `status` bit(1) DEFAULT NULL COMMENT '启动状态 1-启用 0-未启用 (弃用)',
  `material_type` bit(1) DEFAULT NULL COMMENT '物料类型：0非成品类，1成品类  (弃用)',
  `created_by` bigint DEFAULT NULL COMMENT '创建人',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` bigint DEFAULT NULL COMMENT '更新人',
  `last_modified_date` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int DEFAULT NULL COMMENT '状态[0:未删除,1:删除]',
  `supplier_code` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '供应商编码',
  `supplier_type` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '供应商关系(有多个,以json字符串存放)',
  `material_id` bigint DEFAULT NULL COMMENT '关联的物料分组的id',
  `material_erp_id` bigint DEFAULT NULL COMMENT '关联的物料分组的erpid',
  `material_erp_parent_id` bigint DEFAULT NULL COMMENT '关联的物料分组的erpparentid',
  `material_erp_number` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '关联的物料分组的编码',
  `material_erp_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分组名字',
  `parent_id` bigint DEFAULT NULL COMMENT '关联的物料组的父组的id',
  `parent_erp_id` bigint DEFAULT NULL COMMENT '关联的物料组的父组的erpid',
  `parent_erp_number` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '关联的物料组的父组的物料分组编码',
  `parent_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '关联的物料组的父组的物料分组名字',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1531832594431524867 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE t_holiday_date
(
    id                  BIGINT(20)   AUTO_INCREMENT PRIMARY KEY COMMENT '主键id',
    year                INT(11)       NULL COMMENT '年 2022',
    month               INT(11)       NULL COMMENT '年月 202206',
    date                INT(11)       NULL COMMENT '年月日 20220630',
    year_week           INT(11)       NULL COMMENT '年周 202226',
    year_day            INT(11)       NULL COMMENT '年日 2022181',
    lunar_year          INT(11)       NULL COMMENT '2022  农历',
    lunar_month         INT(11)       NULL,
    lunar_date          INT(11)       NULL,
    lunar_year_day      INT(11)       NULL,
    week                INT(11)       NULL COMMENT '星期几 1-7',
    weekend             INT(11)       NULL COMMENT '是不是周末  1-周末 2-不是周末',
    workday             INT(11)       NULL COMMENT '是不是工作日 1-工作日  2非工作日',
    holiday             INT(11)       NULL COMMENT '节假日代号，指向名称 see https://api.apihubs.cn/enum/get?type=holiday',
    holiday_or          INT(11)       NULL COMMENT '非节假日 1-节假日 2-非节假日',
    holiday_overtime    INT(11)       NULL COMMENT '非节假日调休  1-节假日调休 2-非节假日调休',
    holiday_today       INT(11)       NULL COMMENT '非节日当天 1-节日当天 2-非节日当天',
    holiday_legal       INT(11)       NULL COMMENT '非法定节假日  1-法定节假日 2-非法定节假日',
    holiday_recess      INT(11)       NULL COMMENT '非假期节假日  1-假期节日 2-非假期节日',
    year_cn             VARCHAR(255)  NULL,
    month_cn            VARCHAR(255)  NULL,
    date_cn             VARCHAR(255)  NULL,
    year_week_cn        VARCHAR(255)  NULL,
    year_day_cn         VARCHAR(255)  NULL,
    lunar_year_cn       VARCHAR(255)  NULL,
    lunar_month_cn      VARCHAR(255)  NULL,
    lunar_date_cn       VARCHAR(255)  NULL,
    lunar_year_day_cn   VARCHAR(255)  NULL,
    week_cn             VARCHAR(255)  NULL,
    weekend_cn          VARCHAR(255)  NULL,
    workday_cn          VARCHAR(255)  NULL,
    holiday_cn          VARCHAR(255)  NULL,
    holiday_or_cn       VARCHAR(255)  NULL,
    holiday_overtime_cn VARCHAR(255)  NULL,
    holiday_today_cn    VARCHAR(255)  NULL,
    holiday_legal_cn    VARCHAR(255)  NULL,
    holiday_recess_cn   VARCHAR(255)  NULL,
    created_by          BIGINT(20)    NULL COMMENT '创建人',
    created_date        DATETIME      NULL COMMENT '创建时间',
    last_modified_by    BIGINT(20)    NULL COMMENT '更新人',
    last_modified_date  DATETIME      NULL COMMENT '更新时间',
    is_deleted          INT(11)       NULL COMMENT '状态[0:未删除,1:删除]'
);

CREATE TABLE `t_pln_plan_order` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `source_id` bigint DEFAULT NULL COMMENT 'erp ID',
    `bill_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '单据编号',
    `document_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '单据状态 Z 暂存 A 创建 B 审核中 C 已审核 D 重新审核',
    `bill_type_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '单据类型 CGSQD01_SYS    标准采购申请 CGSQD02_SYS    直运采购申请 CGSQD03_SYS    资产采购申请单 CGSQD04_SYS    费用采购申请',
    `purchaser_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '采购方编码',
    `purchaser_code_org` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '入库组织',
    `supplier_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '供应商',
    `material_id_child` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料编码',
    `material_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '物料名称',
    `specification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '规格型号',
    `unit_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '单位',
    `planFinish_date` datetime DEFAULT NULL COMMENT '建议到货/完工日期',
    `order_qty` decimal(18,2) DEFAULT NULL COMMENT '计划订单量',
    `computer_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '运算编号',
    `approve_date` datetime DEFAULT NULL COMMENT '审核时间',
    `created_by` bigint DEFAULT NULL COMMENT '创建人',
    `created_date` datetime DEFAULT NULL COMMENT '创建时间',
    `last_modified_by` bigint DEFAULT NULL COMMENT '更新人',
    `last_modified_date` datetime DEFAULT NULL COMMENT '更新时间',
    `is_deleted` int DEFAULT NULL COMMENT '状态[0:未删除,1:删除]',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_source_id` (`source_id`) USING BTREE COMMENT 'erpId唯一索引控制'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 送货计划相关改动
-- 添加送货计划MRP数据类型
alter table `delivery`.`t_delivery_plan` add source_type VARCHAR(20) COMMENT '数据来源类型 mrp/purchaseOrder' DEFAULT 'purchaseOrder' after `source_id`;
alter table `delivery`.`t_delivery_plan_detail` add source_type VARCHAR(20) COMMENT '数据来源类型 mrp/purchaseOrder'  DEFAULT 'purchaseOrder'  after `source_id` ;
alter table `delivery`.`t_delivery_plan_detail_item` add source_type VARCHAR(20) COMMENT '数据来源类型 mrp/purchaseOrder'  DEFAULT 'purchaseOrder'  after `source_id` ;

ALTER TABLE `delivery`.`t_delivery_plan_detail` ADD detail_status varchar(255) comment '计划明细状态：1待发布、2待确认、3差异待确认、4已确认';
ALTER TABLE `delivery`.`t_delivery_plan_detail` ADD previous_edition_plan_quantity bigint(20) comment '上一版的计划数量';
# ALTER TABLE `delivery`.`t_delivery_plan_detail_item` ADD confirm_delivery_date datetime(0) comment '确认送货日期';
# ALTER TABLE `delivery`.`t_delivery_plan_detail_item` ADD receipt_date date comment '交货日期';
# ALTER TABLE `delivery`.`t_delivery_plan_detail_item` ADD confirm_receipt_date date comment '确认交货日期';

ALTER TABLE `delivery`.`t_delivery_plan_detail_item` ADD suggested_delivery_date date comment '建议发货日期';
ALTER TABLE `delivery`.`t_delivery_plan_detail_item` ADD confirm_suggested_delivery_date date comment '确认建议发货日期';
ALTER TABLE `delivery`.`t_delivery_plan_detail_item` ADD difference_reason text(0) comment '差异原因';

-- 草稿 --> 待发布
# UPDATE `bncloud_platform`.`ss_sys_dict_item` SET  `label` = '待发布' WHERE `code`='delivery_plan_status' and order_num =  0 ;

-- 3 差异待确认
INSERT INTO `bncloud_platform`.`ss_sys_dict_item`(`is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `ext_json`, `label`, `order_num`, `value`, `code`)
VALUES (b'0', 1, '2022-05-13 16:51:50.000000', 1, '2022-05-13 16:51:53.000000', NULL, '差异待确认', 3, '3', 'delivery_plan_status');


-- 5 配置
INSERT INTO `bncloud_platform`.`ss_sys_config_param` (`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `category_code`, `category_name`, `code`, `company_id`, `org_id`, `filter_type`, `filter_value`, `remark`, `type`, `value`) VALUES (537, b'0', 1, '2022-01-25 10:18:34.000000', 751, '2022-04-25 01:37:38.763000', 'contract', 'mrp默认工作日', 'mrp:default_workday', NULL, 112, NULL, NULL, 'mrp默认工作日', 'JSON', '{\"start\":\"1\",\"end\":\"7\"}');
INSERT INTO `bncloud_platform`.`ss_sys_config_param` (`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `category_code`, `category_name`, `code`, `company_id`, `org_id`, `filter_type`, `filter_value`, `remark`, `type`, `value`) VALUES (538, b'0', 1, '2022-01-25 10:18:34.000000', 751, '2022-05-25 09:44:26.609000', 'contract', 'mrp是否开启订阅法定节假日', 'mrp:auto_holiday', NULL, 112, NULL, NULL, 'mrp是否开启订阅法定节假日', 'BOOL', '{\"100\":\"true\" ,\"111\":\"false\",\"222\":\"false\"}');

-- 存储区域信息
CREATE TABLE `t_data_config` (
     `id` bigint NOT NULL AUTO_INCREMENT,
     `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL comment 'key',
     `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL comment '值',
     `desc` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL comment '描述',
     `created_by` bigint DEFAULT NULL,
     `created_date` datetime DEFAULT NULL,
     `last_modified_by` bigint DEFAULT NULL,
     `last_modified_date` datetime DEFAULT NULL,
     `is_deleted` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 添加送货单发货类型
alter table `delivery`.`t_delivery_note` ADD delivery_type varchar(50) default 'deliveryPlan' comment '送货单发货类型 deliveryPlan/order';
alter table `delivery`.`t_delivery_detail` ADD order_product_detail_id bigint(20) comment '订单明细ID';

-- order_detail表增加的字段
ALTER TABLE `order`.`t_order_product_details`
ADD COLUMN `plan_unit` varchar(255) NULL COMMENT '计划单位' AFTER `source_id`,
ADD COLUMN `remaining_quantity` varchar(255) NULL COMMENT '剩余数量' AFTER `plan_unit`,
ADD COLUMN `delivery_quantity` varchar(255) NULL COMMENT '送货数量' AFTER `remaining_quantity`;

ALTER TABLE `order`.`t_order_product_details`
ADD COLUMN `mrp_status` varchar(255) NULL COMMENT '是不是mrp同步过来的订单' AFTER `delivery_quantity`;

ALTER TABLE `order`.`t_order_product_details`
ADD COLUMN `plan_no` varchar(255) NULL COMMENT '计划编号(srm生成)' AFTER `mrp_status`;


alter table `delivery`.`t_delivery_plan_detail` ADD mrp_plan_quantity bigint(20) comment 'MRP计划数量';
alter table `delivery`.`t_delivery_plan_detail` ADD transit_quantity bigint(20) comment '在途数量';

ALTER TABLE `delivery`.`t_delivery_note`
ADD COLUMN `mrp_status` varchar(255) NULL COMMENT '是否mrp送货单 0-不是 1-是' AFTER `delivery_type`;

ALTER TABLE `delivery`.`t_delivery_detail`
ADD COLUMN `mrp_status` varchar(255) NULL COMMENT '是否mrp送货单明细' AFTER `order_product_detail_id`;

ALTER TABLE `order`.`t_order_product_details`
ADD COLUMN `delivery_address` varchar(255) NULL COMMENT '送货地址' AFTER `plan_no`;

-- 增加的表
CREATE TABLE `t_factory_subscribe` (
  `id` bigint NOT NULL COMMENT '主键id',
  `factory_id` bigint DEFAULT NULL COMMENT '工厂id',
  `year` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '工厂订阅节假日的年份',
  `created_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `last_modified_date` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` int DEFAULT NULL COMMENT '状态[0:未删除,1:删除]是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

ALTER TABLE `delivery`.`t_factory_subscribe`
ADD COLUMN `belong_code` varchar(255) NULL COMMENT '该数据属于哪一个供应/采购商' AFTER `is_deleted`;



alter table `delivery`.`t_delivery_plan_detail` ADD purchase_remark text comment '采购方备注';
alter table `delivery`.`t_delivery_plan_detail` ADD supplier_remark text comment '供应商备注';
alter table `delivery`.`t_delivery_plan_detail` ADD net_demand bigint(0) comment '净需求数';
alter table `delivery`.`t_delivery_plan_detail` ADD variance_number bigint(0) comment '差异数(项次的差异数的和)';

-- 计划明细状态字典
INSERT INTO `bncloud_platform`.`ss_sys_dict`
(`code`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `description`, `ext_json`)
VALUES ('detail_status_enum', b'0', 1, '2022-05-26 15:20:04.000000', 1, '2022-05-26 15:20:08.000000', '送货计划明细状态', NULL);

-- 计划明细状态字典项
INSERT INTO `bncloud_platform`.`ss_sys_dict_item`
(`is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `ext_json`, `label`, `order_num`, `value`, `code`)
VALUES
(b'0', 1, '2022-05-26 15:20:48.000000', 1, '2022-05-26 15:20:50.000000', NULL, '待发布', 1, '1', 'detail_status_enum'),
(b'0', 1, '2022-05-26 15:21:47.000000', 1, '2022-05-26 15:21:44.000000', NULL, '待确认', 2, '2', 'detail_status_enum'),
(b'0', 1, '2022-05-26 15:22:01.000000', 1, '2022-05-26 15:22:04.000000', NULL, '差异待确认', 3, '3', 'detail_status_enum'),
(b'0', 1, '2022-05-26 15:23:03.000000', 1, '2022-05-26 15:22:57.000000', NULL, '已确认', 4, '4', 'detail_status_enum');

-- 询报价的物料表的物料编码字段排序修改 (不修改在联表查询的时候会出问题)
ALTER TABLE `bncloud_quotation`.`t_material_info`
MODIFY COLUMN `material_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料编码' AFTER `id`;

-- 增加假期表belongName字段
ALTER TABLE `delivery`.`t_factory_vacation`
ADD COLUMN `belong_name` varchar(255) NULL COMMENT '供应商名字/采购方名字' AFTER `factory_id`;

-- 版本运算编号
alter table `delivery`.`t_delivery_plan` ADD mrp_computerNo varchar(255) comment 'mrp运算编号';
alter table `delivery`.`t_delivery_plan` ADD mrp_version bigint comment 'mrp版本号';

-- 增加order表的字段适应收料通知单
ALTER TABLE `order`.`t_order`
ADD COLUMN `erp_signing_time` datetime NULL COMMENT 'erp签收时间' AFTER `oem_supplier_name`,
ADD COLUMN `erp_signing_status` varchar(255) NULL COMMENT 'ERP签收状态' AFTER `erp_signing_time`,
ADD COLUMN `erp_id` bigint NULL COMMENT '收料通知单回传的erpId' AFTER `erp_signing_status`,
ADD COLUMN `f_number` varchar(255) NULL COMMENT '收料通知单回传的fNumber' AFTER `erp_id`;

ALTER TABLE `order`.`t_order_product_details`
ADD COLUMN `erp_id` bigint NULL COMMENT '收料通知单回传的erpId' AFTER `delivery_address`;



alter table `delivery`.`t_delivery_plan_detail_item` ADD variance_number bigint(0) comment '差异数';

# alter table `delivery`.`t_delivery_plan` ADD mrp_computer_no varchar(25) comment 'mrp运算编号';


INSERT INTO `bncloud_platform`.`ss_sys_config_param`(`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `category_code`, `category_name`, `code`, `company_id`, `org_id`, `filter_type`, `filter_value`, `remark`, `type`, `value`) VALUES (633, b'0', 751, '2022-05-25 14:18:07.000000', 751, '2022-06-09 09:18:32.681000', 'delivery', '送货协同', 'delivery:delivery_collaboration_method', NULL, 112, NULL, NULL, '送货协同方式', 'JSON', '{\"deliveryPlan\":\"true\",\"planScheduling\":\"true\"}');
INSERT INTO `bncloud_platform`.`ss_sys_config_param`(`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `category_code`, `category_name`, `code`, `company_id`, `org_id`, `filter_type`, `filter_value`, `remark`, `type`, `value`) VALUES (634, b'0', 751, '2022-05-25 14:19:18.000000', 751, '2022-05-25 14:19:23.000000', 'delivery', '送货协同', 'delivery:is_support_manual_addition', NULL, 112, NULL, NULL, '计划排程是否支持手工新增', 'BOOL', 'false');
INSERT INTO `bncloud_platform`.`ss_sys_config_param`(`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `category_code`, `category_name`, `code`, `company_id`, `org_id`, `filter_type`, `filter_value`, `remark`, `type`, `value`) VALUES (635, b'0', 751, '2022-05-30 10:24:19.000000', 751, '2022-05-30 10:24:25.000000', 'delivery', '送货协同', 'delivery:plan_scheduling_auto_send', NULL, 112, NULL, NULL, '计划排程自动发送', 'BOOL', 'false');
INSERT INTO `bncloud_platform`.`ss_sys_config_param`(`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `category_code`, `category_name`, `code`, `company_id`, `org_id`, `filter_type`, `filter_value`, `remark`, `type`, `value`) VALUES (636, b'0', 751, '2022-05-31 09:22:50.000000', 751, '2022-05-31 09:22:55.000000', 'delivery', '送货协同', 'delivery:supplier_discrepancy_reply', NULL, 112, NULL, NULL, '供应商是否可以对送货计划进行差异答复', 'BOOL', 'true');
INSERT INTO `bncloud_platform`.`ss_sys_config_param`(`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `category_code`, `category_name`, `code`, `company_id`, `org_id`, `filter_type`, `filter_value`, `remark`, `type`, `value`) VALUES (640, b'0', 751, '2022-06-09 10:04:07.000000', 751, '2022-06-09 10:04:13.000000', 'delivery', '送货协同', 'delivery:automatically_synchronize_mrp_demand_plans', NULL, 112, NULL, NULL, '是否自动同步MRP需求计划', 'BOOL', 'true');

--order表添加字段
ALTER TABLE `order`.`t_order`
ADD COLUMN `plan_no` varchar(255) NULL COMMENT '送货计划号' AFTER `f_number`;



INSERT INTO `bncloud_platform`.`ss_sys_dict`(`code`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `description`, `ext_json`) VALUES ('transport_mode', b'0', 1, '2022-06-14 13:43:42.000000', 1, '2022-06-14 13:43:46.000000', '运输方式（送货计划2.0）', NULL);

INSERT INTO `bncloud_platform`.`ss_sys_dict_item`(`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `ext_json`, `label`, `order_num`, `value`, `code`) VALUES (653, b'0', 1, '2022-06-14 13:45:59.000000', 1, '2022-06-14 13:46:02.000000', NULL, '铁路运输', 1, '1', 'transport_mode');
INSERT INTO `bncloud_platform`.`ss_sys_dict_item`(`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `ext_json`, `label`, `order_num`, `value`, `code`) VALUES (654, b'0', 1, '2022-06-14 13:47:06.000000', 1, '2022-06-14 13:47:08.000000', NULL, '公路运输', 2, '2', 'transport_mode');
INSERT INTO `bncloud_platform`.`ss_sys_dict_item`(`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `ext_json`, `label`, `order_num`, `value`, `code`) VALUES (655, b'0', 1, '2022-06-14 13:48:33.000000', 1, '2022-06-14 13:48:36.000000', NULL, '水路运输', 3, '3', 'transport_mode');
INSERT INTO `bncloud_platform`.`ss_sys_dict_item`(`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `ext_json`, `label`, `order_num`, `value`, `code`) VALUES (656, b'0', 1, '2022-06-14 13:49:33.000000', 1, '2022-06-14 13:49:37.000000', NULL, '航空运输', 4, '4', 'transport_mode');
INSERT INTO `bncloud_platform`.`ss_sys_dict_item`(`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `ext_json`, `label`, `order_num`, `value`, `code`) VALUES (657, b'0', 1, '2022-06-14 13:50:49.000000', 1, '2022-06-14 13:50:52.000000', NULL, '管道运输', 5, '5', 'transport_mode');


-- 采购方工厂
INSERT INTO `delivery`.`t_factory_info`(`number`, `name`, `type`, `belong_type`, `belong_code`, `belong_name`, `province`, `city`, `district`, `street`, `detailed_address`, `address_longitude_latitude`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `is_deleted`, `area`) VALUES ( '100', '美尚（广州)化妆品股份有限公司', 'receipt', 'purchase', '100', '美尚（广州)化妆品股份有限公司', '北京市', '北京市', '东城区', '东华门街道', '21', NULL, 751, '2022-06-17 14:52:26', 751, '2022-06-17 14:52:26', 0, NULL);
INSERT INTO `delivery`.`t_factory_info`(`number`, `name`, `type`, `belong_type`, `belong_code`, `belong_name`, `province`, `city`, `district`, `street`, `detailed_address`, `address_longitude_latitude`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `is_deleted`, `area`) VALUES ( '200', '世纪美尚（广州）化妆品有限公司', 'receipt', 'purchase', '200', '世纪美尚（广州）化妆品有限公司', '北京市', '北京市', '东城区', '东华门街道', '21', NULL, 751, '2022-06-17 14:52:26', 751, '2022-06-17 14:52:26', 0, NULL);

-- 修改假期表factoryId字段
ALTER TABLE `delivery`.`t_factory_vacation`
MODIFY COLUMN `factory_id` bigint NULL DEFAULT NULL COMMENT '关联工厂id' AFTER `factory_name`;

-- 供应商送货配置
CREATE TABLE `t_supplier_delivery_config` (
      `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
      `supplier_code` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '供应商编码',
      `code` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '供应商配置key',
      `value` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '供应商配置值',
      `remark` VARCHAR(255)  NULL COMMENT '描述',
      `created_by` bigint DEFAULT NULL COMMENT '创建人',
      `created_date` datetime DEFAULT NULL COMMENT '创建时间',
      `last_modified_by` bigint DEFAULT NULL COMMENT '更新人',
      `last_modified_date` datetime DEFAULT NULL COMMENT '更新时间',
      `is_deleted` int DEFAULT NULL COMMENT '状态[0:未删除,1:删除]',
      PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



INSERT INTO `bncloud_platform`.`ss_sys_dict`(`code`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `description`, `ext_json`) VALUES ('delivery_source_type', b'0', 1, '2022-06-22 10:01:17.000000', 1, '2022-06-22 10:01:21.000000', '计划来源（送货协同）', NULL);

INSERT INTO `bncloud_platform`.`ss_sys_dict_item`(`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `ext_json`, `label`, `order_num`, `value`, `code`) VALUES (676, b'0', 1, '2022-06-22 10:02:13.000000', 1, '2022-06-22 10:02:17.000000', NULL, 'MRP计划方案', 1, 'mrp', 'delivery_source_type');
INSERT INTO `bncloud_platform`.`ss_sys_dict_item`(`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `ext_json`, `label`, `order_num`, `value`, `code`) VALUES (677, b'0', 1, '2022-06-22 10:04:50.000000', 1, '2022-06-22 10:04:53.000000', NULL, '计划订单', 2, 'purchaseOrder', 'delivery_source_type');