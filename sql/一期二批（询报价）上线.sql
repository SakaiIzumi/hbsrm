----------------------------------
-- 一 、业务库初始化，同步表结构即可
----------------------------------
bncloud_quotation

----------------------------------
-- 二、同步询报价菜单数据，涉及表
----------------------------------
bncloud_platform.ss_sys_menu

----------------------------------
-- 三、同步询报价字典数据，涉及表
----------------------------------
bncloud_platform.ss_sys_dict,
bncloud_platform.ss_sys_dict_item

----------------------------------
-- 四、同步询报价协同配置，涉及表
----------------------------------
bncloud_platform.ss_sys_config_param

INSERT INTO `bncloud_platform`.`ss_sys_config_param` ( `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `category_code`, `category_name`, `code`, `company_id`, `org_id`, `filter_type`, `filter_value`, `remark`, `type`, `value`)
VALUES ( b'0', 1, '2022-01-25 10:18:34.000000', 751, '2022-04-06 08:02:49.661000', 'contract', '供应商预警', 'quotation_supplier:early_warning', NULL, 112, NULL, NULL, '报价预警设置', 'JSON', '{\"type\":[\"1\",\"2\"],\"hour\":\"2\",\"quotationSwitch\":true}');

-- bis 服务需要一个 枚举表
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for k3_erp_enum
-- ----------------------------
DROP TABLE IF EXISTS `k3_erp_enum`;
CREATE TABLE `k3_erp_enum`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `field_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段名称',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '枚举值',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '枚举名称',
  `status` tinyint(0) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `k3_erp_enum_id_uindex`(`id`) USING BTREE,
  UNIQUE INDEX `k3_erp_enum_field_key_code_uindex`(`field_key`, `code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '同步金蝶需要枚举字段保存表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of k3_erp_enum
-- ----------------------------
INSERT INTO `k3_erp_enum` VALUES (1, 'MATERIAL_PROPERTY_ENUM_KEY', '1', '外购', 0);
INSERT INTO `k3_erp_enum` VALUES (2, 'MATERIAL_PROPERTY_ENUM_KEY', '2', '自制', 0);
INSERT INTO `k3_erp_enum` VALUES (3, 'MATERIAL_PROPERTY_ENUM_KEY', '3', '委外', 0);
INSERT INTO `k3_erp_enum` VALUES (4, 'MATERIAL_PROPERTY_ENUM_KEY', '9', '配置', 0);
INSERT INTO `k3_erp_enum` VALUES (5, 'MATERIAL_PROPERTY_ENUM_KEY', '10', '资产', 0);
INSERT INTO `k3_erp_enum` VALUES (6, 'MATERIAL_PROPERTY_ENUM_KEY', '4', '特征', 0);
INSERT INTO `k3_erp_enum` VALUES (7, 'MATERIAL_PROPERTY_ENUM_KEY', '11', '费用', 0);
INSERT INTO `k3_erp_enum` VALUES (8, 'MATERIAL_PROPERTY_ENUM_KEY', '5', '虚拟', 0);
INSERT INTO `k3_erp_enum` VALUES (9, 'MATERIAL_PROPERTY_ENUM_KEY', '6', '服务', 0);
INSERT INTO `k3_erp_enum` VALUES (10, 'MATERIAL_PROPERTY_ENUM_KEY', '7', '一次性', 0);
INSERT INTO `k3_erp_enum` VALUES (11, 'MATERIAL_PROPERTY_ENUM_KEY', '12', '模型', 0);
INSERT INTO `k3_erp_enum` VALUES (12, 'MATERIAL_PROPERTY_ENUM_KEY', '13', '产品系列', 0);
INSERT INTO `k3_erp_enum` VALUES (13, 'MATERIAL_SUITE_ENUM_KEY', '0', '否', 0);
INSERT INTO `k3_erp_enum` VALUES (14, 'MATERIAL_SUITE_ENUM_KEY', '1', '是', 0);

SET FOREIGN_KEY_CHECKS = 1;




----------------------------------
-- 五、消息中心，涉及表和sql
----------------------------------

CREATE TABLE `zc_information_sms` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tag` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '对应zc_information_tag的tag',
  `add_time` timestamp NULL DEFAULT NULL COMMENT '入库时间',
  `get_uid` bigint DEFAULT NULL COMMENT '接收者uid',
  `get_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '接收人名称',
  `get_mobile` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '接收者手机号',
  `get_supplier_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '接收者公司编码',
  `get_supplier_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '接收者公司编码',
  `send_uid` bigint DEFAULT NULL COMMENT '发送者或对应联系人uid',
  `send_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发送人名字',
  `send_mobile` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发送者或对应联系手机号',
  `send_supplier_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发送者或对应联系公司编码',
  `send_supplier_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发送者或对应联系公司名称',
  `created_by` bigint DEFAULT NULL COMMENT '创建人',
  `created_date` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` bigint DEFAULT NULL COMMENT '更新人',
  `last_modified_date` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `msg_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '消息名称',
  `module_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '模块类型',
  `system_type` int DEFAULT '0' COMMENT '系统类型',
  `business_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '业务id',
  `send_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'org' COMMENT '发送类型 org 组织 sup 供应商',
  `receiver_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 'supplier' COMMENT '接收类型 org 组织 sup 供应商',
  `send_subject_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发送主体(组织或供应商)',
  `send_subject_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发送主体编号(组织或供应商)',
  `receiver_subject_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '接收主体(组织或供应商)',
  `receiver_subject_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '接收主体编码(组织或供应商)',
  `send_temp` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发送模板',
  `send_data` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发送数据',
  `send_status` int DEFAULT NULL COMMENT '发送状态1.成功2.失败',
  `send_msg_type` int DEFAULT NULL COMMENT '发送消息类型1.普通短信消息2.短信验证码',
  `respons_msg` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发送返回消息',
  `is_deleted` int DEFAULT NULL COMMENT '状态[0:未删除,1:删除]',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1514426006380818434 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='智采发送短信消息表';

-- -------------evt_event_type---------------

INSERT INTO `bncloud_event`.`evt_event_type` (`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `code`, `description`, `module`, `name`, `notify`, `scene`, `msg_tpl_name`, `msg_tpl_tag`, `disabled`, `modular_type`, `bis_type`, `event_group_code`, `event_type`, `is_default`, `msg_name`, `org_id`, `receiver_type`) VALUES (102, b'0', 1, '2022-02-09 16:12:22.000000', 1, '2022-03-08 10:36:41.494000', 'quotation_winner:pricing', '询价单定价成功提醒供应商', 'quotation', '询价单定价成功提醒供应商模板', b'1', NULL, '询价单定价成功提醒供应商', 'quotation_winner:pricing', b'0', '8', 1, NULL, NULL, b'0', NULL, NULL, 'supplier');

INSERT INTO `bncloud_event`.`evt_event_type` (`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `code`, `description`, `module`, `name`, `notify`, `scene`, `msg_tpl_name`, `msg_tpl_tag`, `disabled`, `modular_type`, `bis_type`, `event_group_code`, `event_type`, `is_default`, `msg_name`, `org_id`, `receiver_type`) VALUES (103, b'0', 1, '2022-02-09 16:12:22.000000', 1, '2022-03-08 10:36:37.597000', 'quotation_loser:pricing', '询价单定价失败提醒供应商', 'quotation', '询价单定价失败提醒供应商模板', b'1', NULL, '询价单定价成功提醒供应商', 'quotation_loser:pricing', b'0', '8', 1, NULL, NULL, b'0', NULL, NULL, 'supplier');

INSERT INTO `bncloud_event`.`evt_event_type` (`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `code`, `description`, `module`, `name`, `notify`, `scene`, `msg_tpl_name`, `msg_tpl_tag`, `disabled`, `modular_type`, `bis_type`, `event_group_code`, `event_type`, `is_default`, `msg_name`, `org_id`, `receiver_type`) VALUES (104, b'0', 1, '2022-02-09 16:12:22.000000', 1, '2022-03-08 10:36:32.885000', 'quotation_winner_sms:pricing', '询价单定价成功短信提醒供应商', 'quotation', '询价单定价成功短信提醒供应商', b'1', NULL, '询价单定价成功短信提醒供应商', 'quotation_winner_sms:pricing', b'0', '8', 2, NULL, NULL, b'0', NULL, NULL, 'supplier');

INSERT INTO `bncloud_event`.`evt_event_type` (`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `code`, `description`, `module`, `name`, `notify`, `scene`, `msg_tpl_name`, `msg_tpl_tag`, `disabled`, `modular_type`, `bis_type`, `event_group_code`, `event_type`, `is_default`, `msg_name`, `org_id`, `receiver_type`) VALUES (105, b'0', 1, '2022-02-09 16:12:22.000000', 1, '2022-03-08 10:36:29.152000', 'quotation_loser_sms:pricing', '询价单定价失败短信提醒供应商', 'quotation', '询价单定价失败短信提醒供应商', b'1', NULL, '询价单定价失败短信提醒供应商', 'quotation_loser_sms:pricing', b'0', '8', 2, NULL, NULL, b'0', NULL, NULL, 'supplier');

INSERT INTO `bncloud_event`.`evt_event_type` (`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `code`, `description`, `module`, `name`, `notify`, `scene`, `msg_tpl_name`, `msg_tpl_tag`, `disabled`, `modular_type`, `bis_type`, `event_group_code`, `event_type`, `is_default`, `msg_name`, `org_id`, `receiver_type`) VALUES (106, b'0', 1, '2022-03-04 17:02:55.000000', 1, '2022-03-08 10:36:21.521000', 'quotation_supplier:early_warning', '询价单预警事件', 'quotation', '询价单预警消息提醒', b'1', NULL, '询价单预警消息提醒', 'quotation_supplier:early_warning', b'0', '8', 1, NULL, NULL, b'0', NULL, NULL, 'supplier');

INSERT INTO `bncloud_event`.`evt_event_type` (`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `code`, `description`, `module`, `name`, `notify`, `scene`, `msg_tpl_name`, `msg_tpl_tag`, `disabled`, `modular_type`, `bis_type`, `event_group_code`, `event_type`, `is_default`, `msg_name`, `org_id`, `receiver_type`) VALUES (107, b'0', 1, '2022-03-04 17:02:55.000000', 1, '2022-03-08 10:36:18.079000', 'quotation_restate:pricing', '询价单重新报价通知供应商', 'quotation', '询价单重新报价通知供应商', b'1', NULL, '询价单重新报价短信提醒供应商', 'quotation_restate:pricing', b'0', '8', 1, NULL, NULL, b'0', NULL, NULL, 'supplier');

INSERT INTO `bncloud_event`.`evt_event_type` (`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `code`, `description`, `module`, `name`, `notify`, `scene`, `msg_tpl_name`, `msg_tpl_tag`, `disabled`, `modular_type`, `bis_type`, `event_group_code`, `event_type`, `is_default`, `msg_name`, `org_id`, `receiver_type`) VALUES (108, b'0', 1, '2022-03-04 17:02:55.000000', 1, '2022-03-08 10:36:25.565000', 'quotation_supplier_sms:early_warning', '询价单供应商短信预警事件', 'quotation', '询价单供应商短信预警事件', b'1', NULL, '询价单供应商短信预警事件', 'quotation_supplier_sms:early_warning', b'0', '8', 2, NULL, NULL, b'0', NULL, NULL, 'supplier');

INSERT INTO `bncloud_event`.`evt_event_type` (`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `code`, `description`, `module`, `name`, `notify`, `scene`, `msg_tpl_name`, `msg_tpl_tag`, `disabled`, `modular_type`, `bis_type`, `event_group_code`, `event_type`, `is_default`, `msg_name`, `org_id`, `receiver_type`) VALUES (109, b'0', 1, '2022-03-04 17:02:55.000000', 1, '2022-03-08 10:36:18.079000', 'quotation_restate_sms:pricing', '定价单重新报价通知供应商', 'quotation', '询价单重新报价短信提醒供应商', b'1', NULL, '询价单重新报价短信提醒供应商', 'quotation_restate_sms:pricing', b'0', '8', 2, NULL, NULL, b'0', NULL, NULL, 'supplier');

INSERT INTO `bncloud_event`.`evt_event_type` (`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `code`, `description`, `module`, `name`, `notify`, `scene`, `msg_tpl_name`, `msg_tpl_tag`, `disabled`, `modular_type`, `bis_type`, `event_group_code`, `event_type`, `is_default`, `msg_name`, `org_id`, `receiver_type`) VALUES (110, b'0', 1, '2022-03-04 17:02:55.000000', 1, '2022-03-08 10:36:25.565000', 'quotation_supplier_early_warning', '询价单供应商预警事件', 'quotation', '询价单供应商预警事件', b'1', NULL, '询价单供应商预警事件', 'quotation_supplier_early_warning', b'0', '8', 1, NULL, NULL, b'0', NULL, NULL, 'supplier');

INSERT INTO `bncloud_event`.`evt_event_type` (`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `code`, `description`, `module`, `name`, `notify`, `scene`, `msg_tpl_name`, `msg_tpl_tag`, `disabled`, `modular_type`, `bis_type`, `event_group_code`, `event_type`, `is_default`, `msg_name`, `org_id`, `receiver_type`) VALUES (111, b'0', 1, '2022-03-04 17:02:55.000000', 1, '2022-03-10 10:20:35.458000', 'quotation_supplier:notice_bid', '通知供应商应标事件', 'quotation', '通知供应商应标事件', b'1', NULL, '通知供应商应标事件', 'quotation_supplier:notice_bid', b'0', '8', 1, NULL, NULL, b'0', NULL, NULL, 'supplier');

INSERT INTO `bncloud_event`.`evt_event_type` (`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `code`, `description`, `module`, `name`, `notify`, `scene`, `msg_tpl_name`, `msg_tpl_tag`, `disabled`, `modular_type`, `bis_type`, `event_group_code`, `event_type`, `is_default`, `msg_name`, `org_id`, `receiver_type`) VALUES (112, b'0', 1, '2022-03-04 17:02:55.000000', 1, '2022-03-08 10:36:25.565000', 'quotation_supplier_sms:notice_bid', '通知供应商应标短信事件', 'quotation', '通知供应商应标短信事件', b'1', NULL, '通知供应商应标短信事件', 'quotation_supplier_sms:notice_bid', b'0', '8', 2, NULL, NULL, b'0', NULL, NULL, 'supplier');

INSERT INTO `bncloud_event`.`evt_event_type` (`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `code`, `description`, `module`, `name`, `notify`, `scene`, `msg_tpl_name`, `msg_tpl_tag`, `disabled`, `modular_type`, `bis_type`, `event_group_code`, `event_type`, `is_default`, `msg_name`, `org_id`, `receiver_type`) VALUES (113, b'0', 1, '2022-03-04 17:02:55.000000', 1, '2022-03-21 06:17:16.011000', 'quotation_supplier:quoted_price', '供应商报价事件', 'quotation', '供应商报价事件', b'1', NULL, '供应商报价事件', 'quotation_supplier:quoted_price', b'0', '8', 1, NULL, NULL, b'0', NULL, NULL, 'org');

INSERT INTO `bncloud_event`.`evt_event_type` (`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `code`, `description`, `module`, `name`, `notify`, `scene`, `msg_tpl_name`, `msg_tpl_tag`, `disabled`, `modular_type`, `bis_type`, `event_group_code`, `event_type`, `is_default`, `msg_name`, `org_id`, `receiver_type`) VALUES (114, b'0', 1, '2022-03-04 17:02:55.000000', 1, '2022-03-21 06:18:00.725000', 'quotation_supplier_sms:quoted_price', '供应商报价短信事件', 'quotation', '供应商报价短信事件', b'1', NULL, '供应商报价事件', 'quotation_supplier_sms:quoted_price', b'0', '8', 2, NULL, NULL, b'0', NULL, NULL, 'org');

INSERT INTO `bncloud_event`.`evt_event_type` (`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `code`, `description`, `module`, `name`, `notify`, `scene`, `msg_tpl_name`, `msg_tpl_tag`, `disabled`, `modular_type`, `bis_type`, `event_group_code`, `event_type`, `is_default`, `msg_name`, `org_id`, `receiver_type`) VALUES (115, b'0', 1, '2022-03-04 17:02:55.000000', 1, '2022-03-22 07:49:53.136000', 'quotation_info:publish', '询价单发布通知', 'quotation', '询价单发布通知', b'1', NULL, '询价单发布通知', 'quotation_info:publish', b'0', '8', 1, NULL, NULL, b'0', NULL, NULL, 'supplier');

INSERT INTO `bncloud_event`.`evt_event_type` (`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `code`, `description`, `module`, `name`, `notify`, `scene`, `msg_tpl_name`, `msg_tpl_tag`, `disabled`, `modular_type`, `bis_type`, `event_group_code`, `event_type`, `is_default`, `msg_name`, `org_id`, `receiver_type`) VALUES (117, b'0', 1, '2022-03-26 11:33:39.000000', 1, '2022-03-26 06:04:58.265000', 'quotation_supplier:response_notice', '供应商应标通知', 'quotation', '供应商应标事件', b'1', NULL, '供应商应标通知', 'quotation_supplier:response_notice', b'0', '8', 1, NULL, NULL, b'0', NULL, NULL, 'org');

INSERT INTO `bncloud_event`.`evt_event_type` (`id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `code`, `description`, `module`, `name`, `notify`, `scene`, `msg_tpl_name`, `msg_tpl_tag`, `disabled`, `modular_type`, `bis_type`, `event_group_code`, `event_type`, `is_default`, `msg_name`, `org_id`, `receiver_type`) VALUES (118, b'0', 1, '2022-03-31 16:36:57.000000', 1, '2022-03-31 08:38:52.052000', 'quotation_supplier:reject', '供应商应标拒绝', 'quotation', '供应商应标拒绝', b'1', NULL, '供应商应标拒绝', 'quotation_supplier:reject', b'0', '8', 1, NULL, NULL, b'0', NULL, NULL, 'org');



-- -------------zc-information-tag---------------

INSERT INTO `information`.`zc_information_tag` (`id`, `tag`, `name`, `status`, `roles`, `msg_id_prefix`, `source`, `terminal_type`, `pr_route`, `dd_route`, `wx_template`, `msg_template`, `msg_type`, `system_type`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `module_type`, `message_template`, `send_type`, `receiver_type`) VALUES (01376445766801813614, 'quotation_supplier:quoted_price', '供应商报价事件', '0', NULL, NULL, NULL, '1,2,3', NULL, NULL, NULL, '{\"消息标题\":\"@msgTitle@\",\"收件人ID\":\"@getUid@\",\"收件人名称\":\"@getName@\",\"收件人手机\":\"@getMobile@\",\"收件企业编号\":\"@getSupplierNo@\",\"收件企业名称\":\"@getSupplierName@\",\"发件人ID\":\"@sendUid@\",\"发件人名称\":\"@sendName@\",\"发件人手机\":\"@sendMobile@\",\"发件企业编号\":\"@sendSupplierNo@\",\"发件企业名称\":\"@sendSupplierName@\",\"钉钉工作台路由\":\"@ddRoute@\",\"站内跳转路由\":\"@prRoute@\",\"接收时间\":\"@addTime@\",\"消息内容\":\"供应商：@supplierName@已经对物料询价进行报价，询价单号：@quotationNo@，查看详情！\"}', '0', '1', '0', 1, '2022-04-02 08:45:12', 751, '2022-04-02 16:45:12', '8', '供应商：@supplierName@已经对物料询价进行报价，询价单号：@quotationNo@，查看详情！', 'supplier', 'org');

INSERT INTO `information`.`zc_information_tag` (`id`, `tag`, `name`, `status`, `roles`, `msg_id_prefix`, `source`, `terminal_type`, `pr_route`, `dd_route`, `wx_template`, `msg_template`, `msg_type`, `system_type`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `module_type`, `message_template`, `send_type`, `receiver_type`) VALUES (01376445766801813616, 'quotation_supplier:response_notice', '供应商应标通知', '0', NULL, NULL, NULL, '1,2,3', NULL, NULL, NULL, '{\"消息标题\":\"@msgTitle@\",\"收件人ID\":\"@getUid@\",\"收件人名称\":\"@getName@\",\"收件人手机\":\"@getMobile@\",\"收件企业编号\":\"@getSupplierNo@\",\"收件企业名称\":\"@getSupplierName@\",\"发件人ID\":\"@sendUid@\",\"发件人名称\":\"@sendName@\",\"发件人手机\":\"@sendMobile@\",\"发件企业编号\":\"@sendSupplierNo@\",\"发件企业名称\":\"@sendSupplierName@\",\"钉钉工作台路由\":\"@ddRoute@\",\"站内跳转路由\":\"@prRoute@\",\"接收时间\":\"@addTime@\",\"消息内容\":\"供应商：@supplierName@已响应物料询价，询价单号：@quotationNo@，查看详情！\"}', '0', '1', '0', 1, '2022-04-02 08:43:13', 751, '2022-04-02 16:43:14', '8', '供应商：@supplierName@已响应物料询价，询价单号：@quotationNo@，查看详情！', 'supplier', 'org');

INSERT INTO `information`.`zc_information_tag` (`id`, `tag`, `name`, `status`, `roles`, `msg_id_prefix`, `source`, `terminal_type`, `pr_route`, `dd_route`, `wx_template`, `msg_template`, `msg_type`, `system_type`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `module_type`, `message_template`, `send_type`, `receiver_type`) VALUES (01376445766801813617, 'quotation_winner:pricing', '中标结果通知', '0', NULL, NULL, NULL, '1,2,3', NULL, NULL, NULL, '{\"消息标题\":\"@msgTitle@\",\"收件人ID\":\"@getUid@\",\"收件人名称\":\"@getName@\",\"收件人手机\":\"@getMobile@\",\"收件企业编号\":\"@getSupplierNo@\",\"收件企业名称\":\"@getSupplierName@\",\"发件人ID\":\"@sendUid@\",\"发件人名称\":\"@sendName@\",\"发件人手机\":\"@sendMobile@\",\"发件企业编号\":\"@sendSupplierNo@\",\"发件企业名称\":\"@sendSupplierName@\",\"钉钉工作台路由\":\"@ddRoute@\",\"站内跳转路由\":\"@prRoute@\",\"接收时间\":\"@addTime@\",\"消息内容\":\"您本次参与的物料询价已成功中标，询价单号：@quotationNo@，请及时和客户：@supplierName@取得联系！\"}', '0', '0', '0', 1, '2022-04-02 08:39:22', 751, '2022-04-02 16:39:23', '8', '您本次参与的物料询价已成功中标，询价单号：@quotationNo@，请及时和客户：@supplierName@取得联系！', 'org', 'supplier');

INSERT INTO `information`.`zc_information_tag` (`id`, `tag`, `name`, `status`, `roles`, `msg_id_prefix`, `source`, `terminal_type`, `pr_route`, `dd_route`, `wx_template`, `msg_template`, `msg_type`, `system_type`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `module_type`, `message_template`, `send_type`, `receiver_type`) VALUES (01376445766801813618, 'quotation_loser:pricing', '中标结果通知', '0', NULL, NULL, NULL, '1,2,3', NULL, NULL, NULL, '{\"消息标题\":\"@msgTitle@\",\"收件人ID\":\"@getUid@\",\"收件人名称\":\"@getName@\",\"收件人手机\":\"@getMobile@\",\"收件企业编号\":\"@getSupplierNo@\",\"收件企业名称\":\"@getSupplierName@\",\"发件人ID\":\"@sendUid@\",\"发件人名称\":\"@sendName@\",\"发件人手机\":\"@sendMobile@\",\"发件企业编号\":\"@sendSupplierNo@\",\"发件企业名称\":\"@sendSupplierName@\",\"钉钉工作台路由\":\"@ddRoute@\",\"站内跳转路由\":\"@prRoute@\",\"接收时间\":\"@addTime@\",\"消息内容\":\"您本次参与的物料询价未能成功中标，询价单号：@quotationNo@，感谢您的参与！\"}', '0', '0', '0', 1, '2022-04-02 08:41:51', 751, '2022-04-02 16:41:51', '8', '您本次参与的物料询价未能成功中标，询价单号：@quotationNo@，感谢您的参与！', 'org', 'supplier');

INSERT INTO `information`.`zc_information_tag` (`id`, `tag`, `name`, `status`, `roles`, `msg_id_prefix`, `source`, `terminal_type`, `pr_route`, `dd_route`, `wx_template`, `msg_template`, `msg_type`, `system_type`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `module_type`, `message_template`, `send_type`, `receiver_type`) VALUES (01376445766801813621, 'quotation_restate:pricing', '重新报价通知', '0', NULL, NULL, NULL, '1,2,3', NULL, NULL, NULL, '{\"消息标题\":\"@msgTitle@\",\"收件人ID\":\"@getUid@\",\"收件人名称\":\"@getName@\",\"收件人手机\":\"@getMobile@\",\"收件企业编号\":\"@getSupplierNo@\",\"收件企业名称\":\"@getSupplierName@\",\"发件人ID\":\"@sendUid@\",\"发件人名称\":\"@sendName@\",\"发件人手机\":\"@sendMobile@\",\"发件企业编号\":\"@sendSupplierNo@\",\"发件企业名称\":\"@sendSupplierName@\",\"钉钉工作台路由\":\"@ddRoute@\",\"站内跳转路由\":\"@prRoute@\",\"接收时间\":\"@addTime@\",\"消息内容\":\"客户：@customerName@邀请您参与重新报价，询价单号：@quotationNo@，请及时处理！\"}', '1', '0', '0', 1, '2022-04-02 08:41:13', 751, '2022-04-02 16:41:13', '8', '客户：@customerName@邀请您参与重新报价，询价单号：@quotationNo@，请及时处理！', 'org', 'supplier');

INSERT INTO `information`.`zc_information_tag` (`id`, `tag`, `name`, `status`, `roles`, `msg_id_prefix`, `source`, `terminal_type`, `pr_route`, `dd_route`, `wx_template`, `msg_template`, `msg_type`, `system_type`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `module_type`, `message_template`, `send_type`, `receiver_type`) VALUES (01376445766801813623, 'quotation_supplier:early_warning', '报价截止时间预警通知', '0', NULL, NULL, NULL, '1,2,3', NULL, NULL, NULL, '{\"消息标题\":\"@msgTitle@\",\"收件人ID\":\"@getUid@\",\"收件人名称\":\"@getName@\",\"收件人手机\":\"@getMobile@\",\"收件企业编号\":\"@getSupplierNo@\",\"收件企业名称\":\"@getSupplierName@\",\"发件人ID\":\"@sendUid@\",\"发件人名称\":\"@sendName@\",\"发件人手机\":\"@sendMobile@\",\"发件企业编号\":\"@sendSupplierNo@\",\"发件企业名称\":\"@sendSupplierName@\",\"钉钉工作台路由\":\"@ddRoute@\",\"站内跳转路由\":\"@prRoute@\",\"接收时间\":\"@addTime@\",\"消息内容\":\"您参与的物料询价，询价单号：@quotationNo@，即将截止报价，请及时处理！\"}', '0', '0', '0', 1, '2022-04-02 08:35:33', 751, '2022-04-02 16:35:33', '8', '您参与的物料询价，询价单号：@quotationNo@，即将截止报价，请及时处理！', 'org', 'supplier');

INSERT INTO `information`.`zc_information_tag` (`id`, `tag`, `name`, `status`, `roles`, `msg_id_prefix`, `source`, `terminal_type`, `pr_route`, `dd_route`, `wx_template`, `msg_template`, `msg_type`, `system_type`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `module_type`, `message_template`, `send_type`, `receiver_type`) VALUES (01376445766801813624, 'quotation_supplier:notice_bid', '邀请询价通知', '0', NULL, NULL, NULL, '1,2,3', NULL, NULL, NULL, '{\"消息标题\":\"@msgTitle@\",\"收件人ID\":\"@getUid@\",\"收件人名称\":\"@getName@\",\"收件人手机\":\"@getMobile@\",\"收件企业编号\":\"@getSupplierNo@\",\"收件企业名称\":\"@getSupplierName@\",\"发件人ID\":\"@sendUid@\",\"发件人名称\":\"@sendName@\",\"发件人手机\":\"@sendMobile@\",\"发件企业编号\":\"@sendSupplierNo@\",\"发件企业名称\":\"@sendSupplierName@\",\"钉钉工作台路由\":\"@ddRoute@\",\"站内跳转路由\":\"@prRoute@\",\"接收时间\":\"@addTime@\",\"消息内容\":\"客户：@customerName@邀请您参与物料询价，询价单号：@quotationNo@，查看详情！\"}', '0', '0', '0', 1, '2022-04-02 08:40:48', 751, '2022-04-02 16:40:48', '8', '客户：@customerName@邀请您参与物料询价，询价单号：@quotationNo@，查看详情！', 'org', 'supplier');

INSERT INTO `information`.`zc_information_tag` (`id`, `tag`, `name`, `status`, `roles`, `msg_id_prefix`, `source`, `terminal_type`, `pr_route`, `dd_route`, `wx_template`, `msg_template`, `msg_type`, `system_type`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `module_type`, `message_template`, `send_type`, `receiver_type`) VALUES (01376445766801813625, 'quotation_info:publish', '询价单发布通知', '0', NULL, NULL, NULL, '1,2,3', NULL, NULL, NULL, '{\"消息标题\":\"@msgTitle@\",\"收件人ID\":\"@getUid@\",\"收件人名称\":\"@getName@\",\"收件人手机\":\"@getMobile@\",\"收件企业编号\":\"@getSupplierNo@\",\"收件企业名称\":\"@getSupplierName@\",\"发件人ID\":\"@sendUid@\",\"发件人名称\":\"@sendName@\",\"发件人手机\":\"@sendMobile@\",\"发件企业编号\":\"@sendSupplierNo@\",\"发件企业名称\":\"@sendSupplierName@\",\"钉钉工作台路由\":\"@ddRoute@\",\"站内跳转路由\":\"@prRoute@\",\"接收时间\":\"@addTime@\",\"消息内容\":\"客户：@customerName@，发布物料询价，询价单号：@quotationNo@，请及时查看！\"}', '1', '0', '0', 1, '2022-04-02 08:40:33', 751, '2022-04-02 16:40:34', '8', '客户：@customerName@，发布物料询价，询价单号：@quotationNo@，请及时查看！', 'org', 'supplier');

INSERT INTO `information`.`zc_information_tag` (`id`, `tag`, `name`, `status`, `roles`, `msg_id_prefix`, `source`, `terminal_type`, `pr_route`, `dd_route`, `wx_template`, `msg_template`, `msg_type`, `system_type`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `module_type`, `message_template`, `send_type`, `receiver_type`) VALUES (01376445766801813626, 'quotation_supplier:reject', '供应商应标拒绝', '0', NULL, NULL, NULL, '1,2,3', NULL, NULL, NULL, '{\"消息标题\":\"@msgTitle@\",\"收件人ID\":\"@getUid@\",\"收件人名称\":\"@getName@\",\"收件人手机\":\"@getMobile@\",\"收件企业编号\":\"@getSupplierNo@\",\"收件企业名称\":\"@getSupplierName@\",\"发件人ID\":\"@sendUid@\",\"发件人名称\":\"@sendName@\",\"发件人手机\":\"@sendMobile@\",\"发件企业编号\":\"@sendSupplierNo@\",\"发件企业名称\":\"@sendSupplierName@\",\"钉钉工作台路由\":\"@ddRoute@\",\"站内跳转路由\":\"@prRoute@\",\"接收时间\":\"@addTime@\",\"消息内容\":\"供应商：@supplierName@已拒绝物料询价，询价单号：@quotationNo@，查看详情！！\"}', '0', '1', '0', 1, '2022-04-02 08:34:40', 751, '2022-04-02 16:34:41', '8', '供应商：@supplierName@已拒绝物料询价，询价单号：@quotationNo@，查看详情！！', 'supplier', 'org');


-- -------------zc-information-route---------------

INSERT INTO `information`.`zc_information_route` (`id`, `route_type`, `route_url`, `template_id`, `disabled`, `tag_id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `parameter_template`) VALUES (1394173976809226246, '1', NULL, NULL, 0, 1376445766801813601, 0, 1, '2022-03-04 17:35:32', 1, '2022-03-04 17:35:35', NULL);

INSERT INTO `information`.`zc_information_route` (`id`, `route_type`, `route_url`, `template_id`, `disabled`, `tag_id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `parameter_template`) VALUES (1394173976809226247, '1', NULL, NULL, 0, 1376445766801813614, 0, 1, '2022-03-04 17:35:32', 1, '2022-03-04 17:35:35', NULL);

INSERT INTO `information`.`zc_information_route` (`id`, `route_type`, `route_url`, `template_id`, `disabled`, `tag_id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `parameter_template`) VALUES (1394173976809226248, '1', NULL, NULL, 0, 1376445766801813601, 0, 1, '2022-03-04 17:35:32', 1, '2022-03-04 17:35:35', NULL);

INSERT INTO `information`.`zc_information_route` (`id`, `route_type`, `route_url`, `template_id`, `disabled`, `tag_id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `parameter_template`) VALUES (1394173976809226249, '1', 'tradeTendersEnquiryDetail', NULL, 0, 1376445766801813615, 0, 1, '2022-03-04 17:35:32', 1, '2022-03-04 17:35:35', 'id');

INSERT INTO `information`.`zc_information_route` (`id`, `route_type`, `route_url`, `template_id`, `disabled`, `tag_id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `parameter_template`) VALUES (1394173976809226251, '1', 'tradeTendersEnquiryDetail', NULL, 0, 1376445766801813602, 0, 1, '2022-03-25 11:40:52', 1, '2022-03-25 11:40:59', 'id');

INSERT INTO `information`.`zc_information_route` (`id`, `route_type`, `route_url`, `template_id`, `disabled`, `tag_id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `parameter_template`) VALUES (1394173976809226252, '1', 'tradeTendersEnquiryDetail', NULL, 0, 1376445766801813613, 0, 1, '2022-03-25 11:40:52', 1, '2022-03-25 11:40:59', 'id');

INSERT INTO `information`.`zc_information_route` (`id`, `route_type`, `route_url`, `template_id`, `disabled`, `tag_id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `parameter_template`) VALUES (1394173976809226253, '1', NULL, NULL, 0, 1376445766801813616, 0, 1, '2022-03-26 14:13:04', 1, '2022-03-26 14:13:07', NULL);

INSERT INTO `information`.`zc_information_route` (`id`, `route_type`, `route_url`, `template_id`, `disabled`, `tag_id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `parameter_template`) VALUES (1394173976809226254, '1', NULL, NULL, 0, 1376445766801813617, 0, 1, '2022-03-30 10:45:37', 1, '2022-03-30 10:45:44', NULL);

INSERT INTO `information`.`zc_information_route` (`id`, `route_type`, `route_url`, `template_id`, `disabled`, `tag_id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `parameter_template`) VALUES (1394173976809226255, '1', NULL, NULL, 0, 1376445766801813618, 0, 1, '2022-03-30 10:49:26', 1, '2022-03-30 10:49:29', NULL);

INSERT INTO `information`.`zc_information_route` (`id`, `route_type`, `route_url`, `template_id`, `disabled`, `tag_id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `parameter_template`) VALUES (1394173976809226256, '1', 'tradeTendersEnquiryDetail', NULL, 0, 1376445766801813621, 0, 1, '2022-03-30 10:50:06', 1, '2022-03-30 10:50:09', 'id');

INSERT INTO `information`.`zc_information_route` (`id`, `route_type`, `route_url`, `template_id`, `disabled`, `tag_id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `parameter_template`) VALUES (1394173976809226257, '1', NULL, NULL, 0, 1376445766801813623, 0, 1, '2022-03-30 10:50:36', 1, '2022-03-30 10:50:39', NULL);

INSERT INTO `information`.`zc_information_route` (`id`, `route_type`, `route_url`, `template_id`, `disabled`, `tag_id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `parameter_template`) VALUES (1394173976809226258, '1', NULL, NULL, 0, 1376445766801813624, 0, 1, '2022-03-30 10:51:09', 1, '2022-03-30 10:51:12', NULL);

INSERT INTO `information`.`zc_information_route` (`id`, `route_type`, `route_url`, `template_id`, `disabled`, `tag_id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `parameter_template`) VALUES (1394173976809226259, '1', 'tradeTendersEnquiryDetail', NULL, 0, 1376445766801813625, 0, 1, '2022-03-30 10:51:30', 1, '2022-03-30 10:51:32', 'id');

INSERT INTO `information`.`zc_information_route` (`id`, `route_type`, `route_url`, `template_id`, `disabled`, `tag_id`, `is_deleted`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `parameter_template`) VALUES (1394173976809226260, '1', NULL, NULL, 0, 1376445766801813626, 0, 1, '2022-03-31 16:35:25', 1, '2022-03-31 16:35:29', NULL);



----------------------------------
-- 六、询报价表字段修改
----------------------------------

ALTER TABLE `bncloud_quotation`.`t_material_info`
MODIFY COLUMN `forbidden_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '\A' COMMENT '禁用状态：A有效，B无效' AFTER `suite`;

ALTER TABLE `bncloud_quotation`.`t_rfq_material_form_ext`
ADD COLUMN `validate` varchar(255) NULL COMMENT '字段校验规则，根据props字段属性校验' AFTER `is_amount`;

ALTER TABLE `bncloud_quotation`.`t_rfq_material_template_ext`
ADD COLUMN `validate` varchar(255) NULL COMMENT '字段校验规则，根据props字段属性校验' AFTER `is_amount`;

ALTER TABLE `bncloud_quotation`.`t_rfq_quotation_line_ext`
ADD COLUMN `validate` varchar(255) NULL COMMENT '字段校验规则，根据props字段属性校验' AFTER `is_amount`;

ALTER TABLE `bncloud_quotation`.`t_rfq_bidding_line_ext`
ADD COLUMN `validate` varchar(255) NULL COMMENT '字段校验规则，根据props字段属性校验' AFTER `is_amount`;

ALTER TABLE `bncloud_quotation`.`t_rfq_bidding_line_ext`
ADD COLUMN `pricing_show` varchar(255) NULL COMMENT '定价显示，true 是，false 否' AFTER `is_amount`;


ALTER TABLE `bncloud_quotation`.`t_rfq_material_form_ext`
ADD COLUMN `options` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '保存表单设计器选择器组件字段' AFTER `validate`;


ALTER TABLE `bncloud_quotation`.`t_rfq_material_template_ext`
ADD COLUMN `options` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '保存表单设计器选择器组件字段' AFTER `validate`;


ALTER TABLE `bncloud_quotation`.`t_rfq_quotation_line_ext`
ADD COLUMN `options` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '保存表单设计器选择器组件字段' AFTER `validate`;



ALTER TABLE `bncloud_quotation`.`t_rfq_bidding_line_ext`
ADD COLUMN `options` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '保存表单设计器选择器组件字段' AFTER `validate`;


ALTER TABLE `bncloud_quotation`.`t_rfq_quotation_record`
MODIFY COLUMN `ext_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '本轮报价，招标行信息' AFTER `round_number`;


ALTER TABLE `bncloud_quotation`.`t_rfq_material_template_ext`
MODIFY COLUMN `ext_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT 'json扩展内容' AFTER `data_type`;
