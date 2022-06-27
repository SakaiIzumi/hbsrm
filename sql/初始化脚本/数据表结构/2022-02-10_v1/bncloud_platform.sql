/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.2.136-美尚
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : 192.168.2.136:3306
 Source Schema         : bncloud_platform

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 10/02/2022 17:49:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for logging_sys_log
-- ----------------------------
DROP TABLE IF EXISTS `logging_sys_log`;
CREATE TABLE `logging_sys_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `action` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `application` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `browser_engine` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `browser_engine_version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `browser_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `browser_version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `class_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `client_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `duration` bigint NULL DEFAULT NULL,
  `exception` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `http_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `login` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `is_mobile` bit(1) NULL DEFAULT NULL,
  `module` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `os_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `platform_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `request` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `request_at` datetime(6) NULL DEFAULT NULL,
  `request_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `resource` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `response` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `response_at` datetime(6) NULL DEFAULT NULL,
  `server_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `success` bit(1) NULL DEFAULT NULL,
  `uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for login_log
-- ----------------------------
DROP TABLE IF EXISTS `login_log`;
CREATE TABLE `login_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `browser_engine` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `browser_engine_version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `browser_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `browser_version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `fail_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `login` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `login_at` datetime(6) NULL DEFAULT NULL,
  `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `is_mobile` bit(1) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `os_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `platform_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `request_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `success` bit(1) NULL DEFAULT NULL,
  `target_system` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `token` varchar(10240) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`  (
  `client_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `access_token_validity` int NULL DEFAULT NULL,
  `additional_information` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `authorities` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `autoapprove` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `client_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `disabled` bit(1) NOT NULL,
  `authorized_grant_types` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `origin_secret` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `refresh_token_validity` int NULL DEFAULT NULL,
  `resource_ids` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `scope` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_ding_app
-- ----------------------------
DROP TABLE IF EXISTS `ss_ding_app`;
CREATE TABLE `ss_ding_app`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `admin_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `agent_id` bigint NULL DEFAULT NULL,
  `app_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `app_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `app_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `h5url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `pc_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `corp_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `access_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_token_at` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKbj6qsjxcnm3vgmyd2wnkhxnvo`(`corp_id`) USING BTREE,
  CONSTRAINT `FKbj6qsjxcnm3vgmyd2wnkhxnvo` FOREIGN KEY (`corp_id`) REFERENCES `ss_ding_corp` (`corp_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_ding_app_access_token
-- ----------------------------
DROP TABLE IF EXISTS `ss_ding_app_access_token`;
CREATE TABLE `ss_ding_app_access_token`  (
  `app_id` bigint NOT NULL,
  `access_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_at` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`app_id`) USING BTREE,
  CONSTRAINT `FKj5j60gcdbgxd3jrkxn2qisfiv` FOREIGN KEY (`app_id`) REFERENCES `ss_ding_app` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_ding_corp
-- ----------------------------
DROP TABLE IF EXISTS `ss_ding_corp`;
CREATE TABLE `ss_ding_corp`  (
  `corp_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `org_id` bigint NULL DEFAULT NULL,
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`corp_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_ding_corp_manager
-- ----------------------------
DROP TABLE IF EXISTS `ss_ding_corp_manager`;
CREATE TABLE `ss_ding_corp_manager`  (
  `corp_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `manager_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`corp_id`, `user_id`) USING BTREE,
  CONSTRAINT `FKlbpi6fqsxj4hurimvi8tx840e` FOREIGN KEY (`corp_id`) REFERENCES `ss_ding_corp` (`corp_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_ding_corp_token
-- ----------------------------
DROP TABLE IF EXISTS `ss_ding_corp_token`;
CREATE TABLE `ss_ding_corp_token`  (
  `agent_id` bigint NOT NULL,
  `corp_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `access_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_at` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`agent_id`, `corp_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_ding_department
-- ----------------------------
DROP TABLE IF EXISTS `ss_ding_department`;
CREATE TABLE `ss_ding_department`  (
  `corp_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `dept_id` bigint NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `auto_add_user` bit(1) NULL DEFAULT NULL,
  `create_dept_group` bit(1) NULL DEFAULT NULL,
  `dept_group_chat_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dept_manager_userid_list` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dept_permits` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `from_union_org` bit(1) NULL DEFAULT NULL,
  `hide_dept` bit(1) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `order_num` bigint NULL DEFAULT NULL,
  `org_dept_owner` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `outer_dept` bit(1) NULL DEFAULT NULL,
  `outer_dept_only_self` bit(1) NULL DEFAULT NULL,
  `outer_permit_depts` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `outer_permit_users` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `parent_id` bigint NULL DEFAULT NULL,
  `source_identifier` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_permits` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`corp_id`, `dept_id`) USING BTREE,
  CONSTRAINT `FK4189yrrnfqxtnc7g1x21kso78` FOREIGN KEY (`corp_id`) REFERENCES `ss_ding_corp` (`corp_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_ding_integration_config
-- ----------------------------
DROP TABLE IF EXISTS `ss_ding_integration_config`;
CREATE TABLE `ss_ding_integration_config`  (
  `corp_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `agent_id` bigint NULL DEFAULT NULL,
  `app_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `app_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `is_org` int NULL DEFAULT NULL,
  `org_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`corp_id`) USING BTREE,
  CONSTRAINT `FKsapuq3v74iu7u852ii70vn5ft` FOREIGN KEY (`corp_id`) REFERENCES `ss_ding_corp` (`corp_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_ding_internal_app
-- ----------------------------
DROP TABLE IF EXISTS `ss_ding_internal_app`;
CREATE TABLE `ss_ding_internal_app`  (
  `agent_id` bigint NOT NULL,
  `corp_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `admin_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `app_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `app_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `app_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `h5url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `pc_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`agent_id`, `corp_id`) USING BTREE,
  INDEX `FKf07vtfi8hkn5pidute21utnw1`(`corp_id`) USING BTREE,
  CONSTRAINT `FKf07vtfi8hkn5pidute21utnw1` FOREIGN KEY (`corp_id`) REFERENCES `ss_ding_corp` (`corp_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_ding_new_depart
-- ----------------------------
DROP TABLE IF EXISTS `ss_ding_new_depart`;
CREATE TABLE `ss_ding_new_depart`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `dd_id` bigint NULL DEFAULT NULL,
  `dd_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `depart_id` bigint NULL DEFAULT NULL,
  `level` int NULL DEFAULT NULL,
  `org_id` bigint NULL DEFAULT NULL,
  `parent_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_ding_new_staff
-- ----------------------------
DROP TABLE IF EXISTS `ss_ding_new_staff`;
CREATE TABLE `ss_ding_new_staff`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `dd_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dd_mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dd_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ddept_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `employee_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `org_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_ding_role
-- ----------------------------
DROP TABLE IF EXISTS `ss_ding_role`;
CREATE TABLE `ss_ding_role`  (
  `id` bigint NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `corp_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `group_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKe2jhva2e1kwysjtvgt4jnf2od`(`corp_id`) USING BTREE,
  INDEX `FKeen1rcvw1dsuthkj3w8cesddp`(`group_id`) USING BTREE,
  CONSTRAINT `FKe2jhva2e1kwysjtvgt4jnf2od` FOREIGN KEY (`corp_id`) REFERENCES `ss_ding_corp` (`corp_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKeen1rcvw1dsuthkj3w8cesddp` FOREIGN KEY (`group_id`) REFERENCES `ss_ding_role_group` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_ding_role_group
-- ----------------------------
DROP TABLE IF EXISTS `ss_ding_role_group`;
CREATE TABLE `ss_ding_role_group`  (
  `id` bigint NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `corp_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKerourfk82gky3b2omvxcy1gev`(`corp_id`) USING BTREE,
  CONSTRAINT `FKerourfk82gky3b2omvxcy1gev` FOREIGN KEY (`corp_id`) REFERENCES `ss_ding_corp` (`corp_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_ding_user
-- ----------------------------
DROP TABLE IF EXISTS `ss_ding_user`;
CREATE TABLE `ss_ding_user`  (
  `corp_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `is_active` bit(1) NULL DEFAULT NULL,
  `is_admin` bit(1) NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `is_boss` bit(1) NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `hide_mobile` bit(1) NULL DEFAULT NULL,
  `job_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `real_authed` bit(1) NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `is_senior` bit(1) NULL DEFAULT NULL,
  `state_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `union_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `work_place` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dept_id_list` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`corp_id`, `user_id`) USING BTREE,
  CONSTRAINT `FKacd7r2reb9w3hjnphdul1vbii` FOREIGN KEY (`corp_id`) REFERENCES `ss_ding_corp` (`corp_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_order_config
-- ----------------------------
DROP TABLE IF EXISTS `ss_order_config`;
CREATE TABLE `ss_order_config`  (
  `id` bigint NOT NULL,
  `online_sign` bit(1) NULL DEFAULT NULL COMMENT '网签开关0开启1关闭',
  `send_out` int NULL DEFAULT NULL COMMENT '订单自动发出关联订单类型',
  `unit_price` bit(1) NULL DEFAULT NULL COMMENT '单价开关0开启1关闭',
  `list_delivery_date` bit(1) NULL DEFAULT NULL COMMENT '批量修改交期开关0开启1关闭',
  `before_delivery_date` bit(1) NULL DEFAULT NULL COMMENT '供应商答复交期是否允许早于订单交期\n\n\n0开启1关闭',
  `is_deleted` bit(1) NULL DEFAULT NULL,
  `created_by` bigint NULL DEFAULT NULL,
  `created_date` datetime NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单协调配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_supplier_electric_signature_config
-- ----------------------------
DROP TABLE IF EXISTS `ss_supplier_electric_signature_config`;
CREATE TABLE `ss_supplier_electric_signature_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `company_id` bigint NULL DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `supplier_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `supplier_id` bigint NULL DEFAULT NULL,
  `supplier_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ons` bit(1) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_supplier_tag_item_ref
-- ----------------------------
DROP TABLE IF EXISTS `ss_supplier_tag_item_ref`;
CREATE TABLE `ss_supplier_tag_item_ref`  (
  `supplier_id` bigint NOT NULL,
  `tag_item_id` bigint NOT NULL,
  INDEX `FKiki60xxj5nj7fx7tg8f9ewk1h`(`tag_item_id`) USING BTREE,
  INDEX `FKhleajwjgmuiaehw26t850b18a`(`supplier_id`) USING BTREE,
  CONSTRAINT `FKhleajwjgmuiaehw26t850b18a` FOREIGN KEY (`supplier_id`) REFERENCES `t_supplier_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKiki60xxj5nj7fx7tg8f9ewk1h` FOREIGN KEY (`tag_item_id`) REFERENCES `t_tag_config_item` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_supplier_type_item_ref
-- ----------------------------
DROP TABLE IF EXISTS `ss_supplier_type_item_ref`;
CREATE TABLE `ss_supplier_type_item_ref`  (
  `supplier_id` bigint NOT NULL,
  `type_item_id` bigint NOT NULL,
  INDEX `FK5rkfetmbysv4j0khjufbouy6x`(`type_item_id`) USING BTREE,
  INDEX `FKkyv08qrjwaeedptb0e2vsvux2`(`supplier_id`) USING BTREE,
  CONSTRAINT `FK5rkfetmbysv4j0khjufbouy6x` FOREIGN KEY (`type_item_id`) REFERENCES `t_type_config_item` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKkyv08qrjwaeedptb0e2vsvux2` FOREIGN KEY (`supplier_id`) REFERENCES `t_supplier_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_api
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_api`;
CREATE TABLE `ss_sys_api`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `module_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `order_num` int NULL DEFAULT NULL,
  `perms` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_config_param
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_config_param`;
CREATE TABLE `ss_sys_config_param`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '参数ID',
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `category_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'tab编码',
  `category_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'tab名称',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数编码',
  `company_id` bigint NULL DEFAULT NULL COMMENT '公司ID',
  `org_id` bigint NULL DEFAULT NULL COMMENT '协作组织id',
  `filter_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `filter_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数类型：INPUT 文本框,BOOL 开关,SINGLE 单选 ,MULTIPLE 复选,JSON json数据',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数值，多个逗号分割',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 608 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_config_param_mould
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_config_param_mould`;
CREATE TABLE `ss_sys_config_param_mould`  (
  `id` bigint NOT NULL COMMENT '参数ID',
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `category_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'tab编码',
  `category_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'tab名称',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数编码',
  `filter_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `filter_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数类型：INPUT 文本框,BOOL 开关,SINGLE 单选 ,MULTIPLE 复选,JSON json数据',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数值，多个逗号分割',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_currency
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_currency`;
CREATE TABLE `ss_sys_currency`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `currency_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '币种编码',
  `currency_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '币种名称',
  `english_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币种英文名',
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '币种' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_data_app_config
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_data_app_config`;
CREATE TABLE `ss_sys_data_app_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `app_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用编号',
  `app_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_data_dimension
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_data_dimension`;
CREATE TABLE `ss_sys_data_dimension`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `dimension_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维度编号',
  `dimension_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维度名称',
  `key_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据类型',
  `dimension_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维度分类 用于角色',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_data_grant
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_data_grant`;
CREATE TABLE `ss_sys_data_grant`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `dimension_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据维度编码',
  `dimension_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据维度值',
  `is_special` bit(1) NULL DEFAULT NULL COMMENT '是否是特权',
  `subject_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据主题ID',
  `dimension_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维度分类 权限属性统称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3465 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_data_subject
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_data_subject`;
CREATE TABLE `ss_sys_data_subject`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `app_id` bigint NULL DEFAULT NULL,
  `key_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `key_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主题key:mapperId或请求Url',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKodf3md3329ck3dehqr8dg6dun`(`app_id`) USING BTREE,
  CONSTRAINT `FKodf3md3329ck3dehqr8dg6dun` FOREIGN KEY (`app_id`) REFERENCES `ss_sys_data_app_config` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_data_subject_dimension_rel
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_data_subject_dimension_rel`;
CREATE TABLE `ss_sys_data_subject_dimension_rel`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `alias` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '别名',
  `dimension_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维度code',
  `subject_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK5xo3da8soedofy6fkd8omr553`(`subject_id`) USING BTREE,
  CONSTRAINT `FK5xo3da8soedofy6fkd8omr553` FOREIGN KEY (`subject_id`) REFERENCES `ss_sys_data_subject` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 86 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_desensitize_field_mapping
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_desensitize_field_mapping`;
CREATE TABLE `ss_sys_desensitize_field_mapping`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '脱敏字段名',
  `rule_id` bigint NULL DEFAULT NULL COMMENT '规则id',
  `dimension_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '脱敏字段维度',
  `subject_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKnl5as0munnl45hengi7t5d8e1`(`rule_id`) USING BTREE,
  INDEX `FKt1r733kop840f2yibm2vjmgcs`(`subject_id`) USING BTREE,
  CONSTRAINT `FKnl5as0munnl45hengi7t5d8e1` FOREIGN KEY (`rule_id`) REFERENCES `ss_sys_desensitize_rule` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKt1r733kop840f2yibm2vjmgcs` FOREIGN KEY (`subject_id`) REFERENCES `ss_sys_data_subject` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_desensitize_rule
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_desensitize_rule`;
CREATE TABLE `ss_sys_desensitize_rule`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `rule_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '脱敏规则名',
  `rule_pattern` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '脱敏规则',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_desensitize_type
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_desensitize_type`;
CREATE TABLE `ss_sys_desensitize_type`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `dimension_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维度分类 权限属性统称',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '脱敏名',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '脱敏值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_dict`;
CREATE TABLE `ss_sys_dict`  (
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ext_json` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_dict_bk
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_dict_bk`;
CREATE TABLE `ss_sys_dict_bk`  (
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ext_json` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_dict_item`;
CREATE TABLE `ss_sys_dict_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `ext_json` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `order_num` int NOT NULL,
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKlpimb64t2fid7a0u9u69vs61e`(`code`) USING BTREE,
  CONSTRAINT `FKlpimb64t2fid7a0u9u69vs61e` FOREIGN KEY (`code`) REFERENCES `ss_sys_dict` (`code`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 378 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_dict_item_bk
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_dict_item_bk`;
CREATE TABLE `ss_sys_dict_item_bk`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `ext_json` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `order_num` int NOT NULL,
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKlpimb64t2fid7a0u9u69vs61e`(`code`) USING BTREE,
  CONSTRAINT `ss_sys_dict_item_bk_ibfk_1` FOREIGN KEY (`code`) REFERENCES `ss_sys_dict` (`code`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 306 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_dimension_perm
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_dimension_perm`;
CREATE TABLE `ss_sys_dimension_perm`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dimension_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维度分类 权限属性统称',
  `perm_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据权限名',
  `perm_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据权限值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 221 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_menu`;
CREATE TABLE `ss_sys_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `order_num` int NULL DEFAULT NULL,
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `hidden` bit(1) NOT NULL,
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `menu_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `need_auth` bit(1) NOT NULL,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `redirect` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `route` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `parent_id` bigint NULL DEFAULT NULL,
  `perm` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sys_type` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `enabled` bit(1) NULL DEFAULT NULL,
  `menu_nav_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `subject_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `route`(`route`) USING BTREE,
  INDEX `FKg8egf679ys5w6taqjvh628oep`(`parent_id`) USING BTREE,
  CONSTRAINT `FKg8egf679ys5w6taqjvh628oep` FOREIGN KEY (`parent_id`) REFERENCES `ss_sys_menu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 877 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_menu_nav
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_menu_nav`;
CREATE TABLE `ss_sys_menu_nav`  (
  `id` bigint NOT NULL,
  `menu_nav_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `need_auth` bit(1) NULL DEFAULT NULL,
  `subject_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `order_num` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_privilege
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_privilege`;
CREATE TABLE `ss_sys_privilege`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `order_num` int NULL DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `parent_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK3i0m2seh8togv82fi7958uq2m`(`parent_id`) USING BTREE,
  CONSTRAINT `FK3i0m2seh8togv82fi7958uq2m` FOREIGN KEY (`parent_id`) REFERENCES `ss_sys_privilege` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 421 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_privilege_menu
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_privilege_menu`;
CREATE TABLE `ss_sys_privilege_menu`  (
  `menu_id` bigint NOT NULL,
  `privilege_id` bigint NOT NULL,
  INDEX `FKrmgdnoltmuemfrvfsvoss53hr`(`privilege_id`) USING BTREE,
  INDEX `FKjfywpits4nklijf7ebmfhm0je`(`menu_id`) USING BTREE,
  CONSTRAINT `FKjfywpits4nklijf7ebmfhm0je` FOREIGN KEY (`menu_id`) REFERENCES `ss_sys_menu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKrmgdnoltmuemfrvfsvoss53hr` FOREIGN KEY (`privilege_id`) REFERENCES `ss_sys_privilege` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_process_node_config
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_process_node_config`;
CREATE TABLE `ss_sys_process_node_config`  (
  `id` bigint NOT NULL,
  `config_param_id` bigint NULL DEFAULT NULL COMMENT '参数id',
  `business_node_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务节点名称',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开关：true开，false关',
  `process_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程编码',
  `created_by` bigint NULL DEFAULT NULL COMMENT '创建人',
  `created_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` bigint NULL DEFAULT NULL COMMENT '修改人',
  `last_modified_date` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `is_deleted` bit(1) NULL DEFAULT NULL COMMENT '逻辑删除：0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程节点配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_role
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_role`;
CREATE TABLE `ss_sys_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `group_id` bigint NULL DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `coped` bit(1) NULL DEFAULT NULL,
  `org_id` bigint NULL DEFAULT NULL,
  `subject_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sup_id` bigint NULL DEFAULT NULL,
  `sys_default` bit(1) NOT NULL,
  `source_role_id` bigint NULL DEFAULT NULL COMMENT '复制原角色Id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_idx_role_name_group`(`name`, `group_id`) USING BTREE,
  INDEX `FKq4a511abvahdbw859drt8mrul`(`group_id`) USING BTREE,
  CONSTRAINT `FKq4a511abvahdbw859drt8mrul` FOREIGN KEY (`group_id`) REFERENCES `ss_sys_role_group` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 106 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_role_data_grant_rel
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_role_data_grant_rel`;
CREATE TABLE `ss_sys_role_data_grant_rel`  (
  `role_id` bigint NOT NULL,
  `grant_id` bigint NOT NULL,
  INDEX `FK23xcc0nhq9084lu0nosj4auqc`(`role_id`) USING BTREE,
  INDEX `FK9c5cle6vm1nf0l3bvkkrkfkmt`(`grant_id`) USING BTREE,
  CONSTRAINT `FK23xcc0nhq9084lu0nosj4auqc` FOREIGN KEY (`role_id`) REFERENCES `ss_sys_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK9c5cle6vm1nf0l3bvkkrkfkmt` FOREIGN KEY (`grant_id`) REFERENCES `ss_sys_data_grant` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_role_dimension_perm_rel
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_role_dimension_perm_rel`;
CREATE TABLE `ss_sys_role_dimension_perm_rel`  (
  `perm_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  INDEX `FKbpg2vutkrcpshp177muoe2wgn`(`role_id`) USING BTREE,
  INDEX `FKr2rlv350hftrjlx9f23jgqae6`(`perm_id`) USING BTREE,
  CONSTRAINT `FKbpg2vutkrcpshp177muoe2wgn` FOREIGN KEY (`role_id`) REFERENCES `ss_sys_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKr2rlv350hftrjlx9f23jgqae6` FOREIGN KEY (`perm_id`) REFERENCES `ss_sys_dimension_perm` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_role_group
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_role_group`;
CREATE TABLE `ss_sys_role_group`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sys_default` bit(1) NOT NULL,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `subject_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `visible` bit(1) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_role_menu`;
CREATE TABLE `ss_sys_role_menu`  (
  `role_id` bigint NOT NULL,
  `menu_id` bigint NOT NULL,
  INDEX `FKb2vdc93rp3w8f6tvrm2vek4ac`(`menu_id`) USING BTREE,
  INDEX `FKce1ccvs3hwlnyqyhc8u7goxtw`(`role_id`) USING BTREE,
  CONSTRAINT `FKb2vdc93rp3w8f6tvrm2vek4ac` FOREIGN KEY (`menu_id`) REFERENCES `ss_sys_menu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKce1ccvs3hwlnyqyhc8u7goxtw` FOREIGN KEY (`role_id`) REFERENCES `ss_sys_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_role_privilege
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_role_privilege`;
CREATE TABLE `ss_sys_role_privilege`  (
  `role_id` bigint NOT NULL,
  `privilege_id` bigint NOT NULL,
  INDEX `FKl6pw2xpb38kf1f5c29ujv0l9n`(`privilege_id`) USING BTREE,
  INDEX `FKlk9l4wg03pe5ovn86oxlpptl3`(`role_id`) USING BTREE,
  CONSTRAINT `FKl6pw2xpb38kf1f5c29ujv0l9n` FOREIGN KEY (`privilege_id`) REFERENCES `ss_sys_privilege` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKlk9l4wg03pe5ovn86oxlpptl3` FOREIGN KEY (`role_id`) REFERENCES `ss_sys_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_search_history
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_search_history`;
CREATE TABLE `ss_sys_search_history`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `company_id` bigint NULL DEFAULT NULL,
  `module` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `platform` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_id` bigint NULL DEFAULT NULL,
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 120 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_settlement_pool_rule
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_settlement_pool_rule`;
CREATE TABLE `ss_sys_settlement_pool_rule`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `rule_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则编码',
  `rule_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则名称',
  `bill_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据类型（有字典）',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开关：true开，false关',
  `created_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `created_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `last_modified_date` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `is_deleted` bit(1) NULL DEFAULT NULL COMMENT '删除标志：0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '结算池规制设置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_settlement_pool_rule_rel
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_settlement_pool_rule_rel`;
CREATE TABLE `ss_sys_settlement_pool_rule_rel`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `settlement_pool_rule_id` bigint NULL DEFAULT NULL COMMENT '结算池规则id',
  `bill_status` int NULL DEFAULT NULL COMMENT '单据状态',
  `satisfy_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据状态满足：数值',
  `satisfy_unit` int NULL DEFAULT NULL COMMENT '单据状态满足：单位（有字典financial_bill_status_satisfy_unit）',
  `bill_prefix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单据编码的前缀，多个前缀以逗号隔开',
  `created_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `created_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `last_modified_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `last_modified_date` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `is_deleted` bit(1) NULL DEFAULT NULL COMMENT '删除标志：0未删除，1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '结算池规则设置关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_user`;
CREATE TABLE `ss_sys_user`  (
  `user_id` bigint NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `ss_sys_user_role`;
CREATE TABLE `ss_sys_user_role`  (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  INDEX `FKmeldii3qkpygjqtsmxxpk8uo0`(`role_id`) USING BTREE,
  INDEX `FKa8rr6pm8ngs087j8e14u6wp10`(`user_id`) USING BTREE,
  CONSTRAINT `FKa8rr6pm8ngs087j8e14u6wp10` FOREIGN KEY (`user_id`) REFERENCES `ss_sys_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKmeldii3qkpygjqtsmxxpk8uo0` FOREIGN KEY (`role_id`) REFERENCES `ss_sys_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_tenant_company
-- ----------------------------
DROP TABLE IF EXISTS `ss_tenant_company`;
CREATE TABLE `ss_tenant_company`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `credit_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `register_by_id` bigint NULL DEFAULT NULL,
  `register_by_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tax_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_tenant_company_manager
-- ----------------------------
DROP TABLE IF EXISTS `ss_tenant_company_manager`;
CREATE TABLE `ss_tenant_company_manager`  (
  `company_id` bigint NOT NULL,
  `manager_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `manager_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `manager_user_id` bigint NULL DEFAULT NULL,
  INDEX `FKp2fnyalm5wt0n3au02jeewyxj`(`company_id`) USING BTREE,
  CONSTRAINT `FKp2fnyalm5wt0n3au02jeewyxj` FOREIGN KEY (`company_id`) REFERENCES `ss_tenant_company` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_tenant_org_department
-- ----------------------------
DROP TABLE IF EXISTS `ss_tenant_org_department`;
CREATE TABLE `ss_tenant_org_department`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `order_num` int NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `org_id` bigint NULL DEFAULT NULL,
  `source_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `parent_id` bigint NULL DEFAULT NULL,
  `company_id` bigint NULL DEFAULT NULL,
  `supplier_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `supplier_id` bigint NULL DEFAULT NULL,
  `supplier_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK1jsqadtfta9x1rn92xarqqvrg`(`parent_id`) USING BTREE,
  CONSTRAINT `FK1jsqadtfta9x1rn92xarqqvrg` FOREIGN KEY (`parent_id`) REFERENCES `ss_tenant_org_department` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 126 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_tenant_org_department_manager
-- ----------------------------
DROP TABLE IF EXISTS `ss_tenant_org_department_manager`;
CREATE TABLE `ss_tenant_org_department_manager`  (
  `dept_id` bigint NOT NULL,
  `manager_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `manager_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `manager_user_id` bigint NULL DEFAULT NULL,
  INDEX `FKe2kqw6oj39onumwch45l7c6it`(`dept_id`) USING BTREE,
  CONSTRAINT `FKe2kqw6oj39onumwch45l7c6it` FOREIGN KEY (`dept_id`) REFERENCES `ss_tenant_org_department` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_tenant_org_department_supplier
-- ----------------------------
DROP TABLE IF EXISTS `ss_tenant_org_department_supplier`;
CREATE TABLE `ss_tenant_org_department_supplier`  (
  `dept_id` bigint NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `manager_id` bigint NULL DEFAULT NULL,
  `manager_mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `manager_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `supplier_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `supplier_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`dept_id`) USING BTREE,
  CONSTRAINT `FKsg02qtob2wkgfvtukqloodpfb` FOREIGN KEY (`dept_id`) REFERENCES `ss_tenant_org_department` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_tenant_org_employee
-- ----------------------------
DROP TABLE IF EXISTS `ss_tenant_org_employee`;
CREATE TABLE `ss_tenant_org_employee`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_id` bigint NULL DEFAULT NULL,
  `dept_id` bigint NULL DEFAULT NULL,
  `job_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `position` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_by_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `org_id` bigint NULL DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_mobile_org_id`(`mobile`, `org_id`) USING BTREE,
  INDEX `FK2k6qjqrbdfqdqoc242uoruuaf`(`dept_id`) USING BTREE,
  INDEX `FKl466b4advjusx9qrk4mbii36t`(`org_id`) USING BTREE,
  CONSTRAINT `FK2k6qjqrbdfqdqoc242uoruuaf` FOREIGN KEY (`dept_id`) REFERENCES `ss_tenant_org_department` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKl466b4advjusx9qrk4mbii36t` FOREIGN KEY (`org_id`) REFERENCES `ss_tenant_organization` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 331 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_tenant_org_employee_role
-- ----------------------------
DROP TABLE IF EXISTS `ss_tenant_org_employee_role`;
CREATE TABLE `ss_tenant_org_employee_role`  (
  `employee_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  INDEX `FK5c23e74bdg96rpyhwxjl43818`(`role_id`) USING BTREE,
  INDEX `FKkvw511l49hqebf176t3htb8xs`(`employee_id`) USING BTREE,
  CONSTRAINT `FK5c23e74bdg96rpyhwxjl43818` FOREIGN KEY (`role_id`) REFERENCES `ss_sys_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKkvw511l49hqebf176t3htb8xs` FOREIGN KEY (`employee_id`) REFERENCES `ss_tenant_org_employee` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_tenant_org_manager
-- ----------------------------
DROP TABLE IF EXISTS `ss_tenant_org_manager`;
CREATE TABLE `ss_tenant_org_manager`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `dept_id` bigint NULL DEFAULT NULL,
  `dept_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `manager_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_id` bigint NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `org_id` bigint NULL DEFAULT NULL,
  `manager_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `manager_user_id` bigint NULL DEFAULT NULL,
  `employee_id` bigint NULL DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK4tjs5kqqmtpgvo6t5x87xijp7`(`org_id`) USING BTREE,
  INDEX `FKb23r8p5ik1911iioqu0cob01w`(`employee_id`) USING BTREE,
  CONSTRAINT `FK4tjs5kqqmtpgvo6t5x87xijp7` FOREIGN KEY (`org_id`) REFERENCES `ss_tenant_organization` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKb23r8p5ik1911iioqu0cob01w` FOREIGN KEY (`employee_id`) REFERENCES `ss_tenant_org_employee` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 118 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_tenant_org_manager_record
-- ----------------------------
DROP TABLE IF EXISTS `ss_tenant_org_manager_record`;
CREATE TABLE `ss_tenant_org_manager_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `enabled` bit(1) NULL DEFAULT NULL,
  `manager_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_id` bigint NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_tenant_org_record
-- ----------------------------
DROP TABLE IF EXISTS `ss_tenant_org_record`;
CREATE TABLE `ss_tenant_org_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `org_id` bigint NULL DEFAULT NULL,
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `org_manager_record_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKjkrnfmhftesdj91d77640k3g5`(`org_manager_record_id`) USING BTREE,
  CONSTRAINT `FKjkrnfmhftesdj91d77640k3g5` FOREIGN KEY (`org_manager_record_id`) REFERENCES `ss_tenant_org_manager_record` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 75 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_tenant_org_supplier_ref
-- ----------------------------
DROP TABLE IF EXISTS `ss_tenant_org_supplier_ref`;
CREATE TABLE `ss_tenant_org_supplier_ref`  (
  `org_id` bigint NOT NULL,
  `supplier_id` bigint NOT NULL,
  INDEX `FKsv6mfpoamtgkrv3hyu3g4at5m`(`supplier_id`) USING BTREE,
  INDEX `FKjv95y7yvlu4jtb7tcad18fsh2`(`org_id`) USING BTREE,
  CONSTRAINT `FKjv95y7yvlu4jtb7tcad18fsh2` FOREIGN KEY (`org_id`) REFERENCES `ss_tenant_organization` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKsv6mfpoamtgkrv3hyu3g4at5m` FOREIGN KEY (`supplier_id`) REFERENCES `t_supplier_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_tenant_organization
-- ----------------------------
DROP TABLE IF EXISTS `ss_tenant_organization`;
CREATE TABLE `ss_tenant_organization`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `org_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `company_id` bigint NOT NULL,
  `root_dept_id` bigint NULL DEFAULT NULL,
  `zlt_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKhnnuy7esgp0gstcs647yjtfnh`(`company_id`) USING BTREE,
  INDEX `FKjc2pkskvtudg81jr98qbwy399`(`root_dept_id`) USING BTREE,
  CONSTRAINT `FKhnnuy7esgp0gstcs647yjtfnh` FOREIGN KEY (`company_id`) REFERENCES `ss_tenant_company` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKjc2pkskvtudg81jr98qbwy399` FOREIGN KEY (`root_dept_id`) REFERENCES `ss_tenant_org_department` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 116 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_tenant_organization_managers
-- ----------------------------
DROP TABLE IF EXISTS `ss_tenant_organization_managers`;
CREATE TABLE `ss_tenant_organization_managers`  (
  `organization_id` bigint NOT NULL,
  `managers_id` bigint NOT NULL,
  UNIQUE INDEX `UK_2q8v1lp3bc9k4a9yf32ebgeqb`(`managers_id`) USING BTREE,
  INDEX `FKt7fqafe4jfuq3y9ebsx268s2m`(`organization_id`) USING BTREE,
  CONSTRAINT `FKg114sc3ao9r49clg0uqhvn3f4` FOREIGN KEY (`managers_id`) REFERENCES `ss_tenant_org_manager` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKt7fqafe4jfuq3y9ebsx268s2m` FOREIGN KEY (`organization_id`) REFERENCES `ss_tenant_organization` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_tenant_position
-- ----------------------------
DROP TABLE IF EXISTS `ss_tenant_position`;
CREATE TABLE `ss_tenant_position`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `disabled` bit(1) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `order_num` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_tenant_purchaser
-- ----------------------------
DROP TABLE IF EXISTS `ss_tenant_purchaser`;
CREATE TABLE `ss_tenant_purchaser`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `code` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购方编码',
  `company_code` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司编码',
  `company_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司id',
  `company_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公司名字',
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购方名',
  `source_code` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购方编码',
  `source_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '采购方id',
  `artificial_person` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属法人',
  `description` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `enabled` bit(1) NULL DEFAULT NULL COMMENT '状态',
  `org_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK1q9u9bb45cq3qxrm4gudidklc`(`org_id`) USING BTREE,
  CONSTRAINT `FK1q9u9bb45cq3qxrm4gudidklc` FOREIGN KEY (`org_id`) REFERENCES `ss_tenant_organization` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 59 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_tenant_purchaser_staff
-- ----------------------------
DROP TABLE IF EXISTS `ss_tenant_purchaser_staff`;
CREATE TABLE `ss_tenant_purchaser_staff`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `department` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门',
  `job_no` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工号',
  `mobile` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话',
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `position` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '岗位',
  `purchaser_id` bigint NULL DEFAULT NULL COMMENT '采购方关联id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKdwbdkgf4ls4j6agpleod7o08s`(`purchaser_id`) USING BTREE,
  CONSTRAINT `FKdwbdkgf4ls4j6agpleod7o08s` FOREIGN KEY (`purchaser_id`) REFERENCES `ss_tenant_purchaser` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_tenant_purchaser_supplier_ref
-- ----------------------------
DROP TABLE IF EXISTS `ss_tenant_purchaser_supplier_ref`;
CREATE TABLE `ss_tenant_purchaser_supplier_ref`  (
  `pur_id` bigint NOT NULL,
  `sup_id` bigint NOT NULL,
  INDEX `FKa287bqwp4p2ochoebmn8tu61g`(`sup_id`) USING BTREE,
  INDEX `FK3u4ft669vm4k1uffx937lh2wg`(`pur_id`) USING BTREE,
  CONSTRAINT `FK3u4ft669vm4k1uffx937lh2wg` FOREIGN KEY (`pur_id`) REFERENCES `ss_tenant_purchaser` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKa287bqwp4p2ochoebmn8tu61g` FOREIGN KEY (`sup_id`) REFERENCES `t_supplier_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_user_bind_openid
-- ----------------------------
DROP TABLE IF EXISTS `ss_user_bind_openid`;
CREATE TABLE `ss_user_bind_openid`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `access_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `access_token_expire_at` datetime(6) NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ext_attrs` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `open_id_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `refresh_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `refresh_token_expire_at` datetime(6) NULL DEFAULT NULL,
  `union_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_user_info
-- ----------------------------
DROP TABLE IF EXISTS `ss_user_info`;
CREATE TABLE `ss_user_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `birthday` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `first_login_error_time` datetime(6) NULL DEFAULT NULL,
  `last_login_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `last_login_time` datetime(6) NULL DEFAULT NULL,
  `login_count` int NULL DEFAULT NULL,
  `login_error_count` int NULL DEFAULT NULL,
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `state_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `last_password_change_time` datetime(6) NULL DEFAULT NULL,
  `next_reset_password_time` datetime(6) NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `reset_password` bit(1) NULL DEFAULT NULL,
  `source_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `account_expired` bit(1) NOT NULL,
  `account_locked` bit(1) NOT NULL,
  `disable_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `disabled` bit(1) NOT NULL,
  `expire_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `lock_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ding_talk_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `we_chat_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `qq_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mobile_area` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1234 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_user_info_current
-- ----------------------------
DROP TABLE IF EXISTS `ss_user_info_current`;
CREATE TABLE `ss_user_info_current`  (
  `user_id` bigint NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `current_company_id` bigint NULL DEFAULT NULL,
  `current_company_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `current_org_id` bigint NULL DEFAULT NULL,
  `current_org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `current_supplier_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `current_supplier_id` bigint NULL DEFAULT NULL,
  `current_supplier_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `current_menu_nav_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `current_subject_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  CONSTRAINT `FK14mnhvemqmoxksl57x5im6xvh` FOREIGN KEY (`user_id`) REFERENCES `ss_user_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_user_joined_org
-- ----------------------------
DROP TABLE IF EXISTS `ss_user_joined_org`;
CREATE TABLE `ss_user_joined_org`  (
  `user_id` bigint NOT NULL,
  `company_id` bigint NULL DEFAULT NULL,
  `company_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `org_id` bigint NULL DEFAULT NULL,
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  INDEX `FK6mt4916v20kufrfmb0aurxde9`(`user_id`) USING BTREE,
  CONSTRAINT `FK6mt4916v20kufrfmb0aurxde9` FOREIGN KEY (`user_id`) REFERENCES `ss_user_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ss_user_joined_supplier
-- ----------------------------
DROP TABLE IF EXISTS `ss_user_joined_supplier`;
CREATE TABLE `ss_user_joined_supplier`  (
  `user_id` bigint NOT NULL,
  `supplier_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `supplier_id` bigint NULL DEFAULT NULL,
  `supplier_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  INDEX `FKakncb0ek0asnq4aqhqdl8u8as`(`user_id`) USING BTREE,
  CONSTRAINT `FKakncb0ek0asnq4aqhqdl8u8as` FOREIGN KEY (`user_id`) REFERENCES `ss_user_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_supplier_account
-- ----------------------------
DROP TABLE IF EXISTS `t_supplier_account`;
CREATE TABLE `t_supplier_account`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bank_account` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '银行账号',
  `bank_account_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账户名称',
  `bank_deposit` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开户银行网点',
  `bank_outlet` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开户行',
  `supplier_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKi8d384ed1cthmg25qddd91msm`(`supplier_id`) USING BTREE,
  CONSTRAINT `FKi8d384ed1cthmg25qddd91msm` FOREIGN KEY (`supplier_id`) REFERENCES `t_supplier_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2331 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_supplier_info
-- ----------------------------
DROP TABLE IF EXISTS `t_supplier_info`;
CREATE TABLE `t_supplier_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `is_deleted` bit(1) NOT NULL COMMENT '记录删除状态',
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL COMMENT '供应商记录创建时间',
  `last_modified_by` bigint NULL DEFAULT NULL COMMENT '供应商记录最后修改人',
  `last_modified_date` datetime(6) NULL DEFAULT NULL COMMENT '供应商记录最后修改时间',
  `admin_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `admin_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商编码',
  `credit_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '统一社会信用代码',
  `invite_date` datetime(6) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `relevance_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '合作状态',
  `remark` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `source_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源编码',
  `source_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源ID',
  `source_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商来源分类',
  `supplier_org_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `manager_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理员名称',
  `manager_mobile` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理员联系电话',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `oa_type` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '供应商来源分类',
  `oa_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'OA供应商编码',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK9bjdv2k7cvtkrgnkgmf3j8una`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3472 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_supplier_info_ext
-- ----------------------------
DROP TABLE IF EXISTS `t_supplier_info_ext`;
CREATE TABLE `t_supplier_info_ext`  (
  `supplier_id` bigint NOT NULL COMMENT '主键id',
  `is_deleted` bit(1) NOT NULL COMMENT '表记录删除状态',
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL COMMENT '记录创建时间',
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL COMMENT '记录最后修改时间',
  `bank_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '银行账号',
  `bank_deposit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开户银行网点',
  `bourse_currency` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交易币别',
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `classification_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `company_id` bigint NULL DEFAULT NULL,
  `company_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `company_nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `country` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dept_id` bigint NULL DEFAULT NULL,
  `dept_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `management_forms` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `org_id` bigint NULL DEFAULT NULL,
  `org_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `payment_clause` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `purchase_person_id` bigint NULL DEFAULT NULL,
  `supplier_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `supplier_nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `taxpayer_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `terms_exchange` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `bank_account_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '银行账户名称',
  `bank_outlet` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '开户银行网点',
  PRIMARY KEY (`supplier_id`) USING BTREE,
  CONSTRAINT `FKcel5nb5tao1mcpk62v6uewh32` FOREIGN KEY (`supplier_id`) REFERENCES `t_supplier_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_supplier_link_man
-- ----------------------------
DROP TABLE IF EXISTS `t_supplier_link_man`;
CREATE TABLE `t_supplier_link_man`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `is_deleted` bit(1) NOT NULL COMMENT '记录删除状态',
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL COMMENT '创建日期',
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL COMMENT '记录最后修改时间',
  `department` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `fax` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `link_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `position` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '职位',
  `qq` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `wechat_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信号',
  `wisdom_easily_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `supplier_id` bigint NULL DEFAULT NULL,
  `allow_ops` bit(1) NULL DEFAULT NULL COMMENT '允许操作',
  `source_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '来源类型',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKtqe4fa8cf5cds30p0uexr1jax`(`supplier_id`) USING BTREE,
  CONSTRAINT `FKtqe4fa8cf5cds30p0uexr1jax` FOREIGN KEY (`supplier_id`) REFERENCES `t_supplier_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 479 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_supplier_manager
-- ----------------------------
DROP TABLE IF EXISTS `t_supplier_manager`;
CREATE TABLE `t_supplier_manager`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `manager_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `supplier_id` bigint NOT NULL,
  `staff_id` bigint NULL DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK1e9dfucssgi6mtfy80ee5bcuf`(`supplier_id`) USING BTREE,
  INDEX `FK4n3bx4or31dun6qdwi2rn6jfe`(`staff_id`) USING BTREE,
  CONSTRAINT `FK1e9dfucssgi6mtfy80ee5bcuf` FOREIGN KEY (`supplier_id`) REFERENCES `t_supplier_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK4n3bx4or31dun6qdwi2rn6jfe` FOREIGN KEY (`staff_id`) REFERENCES `t_supplier_staff` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 464 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_supplier_ops_log
-- ----------------------------
DROP TABLE IF EXISTS `t_supplier_ops_log`;
CREATE TABLE `t_supplier_ops_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作内容',
  `remark` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '说明',
  `supplier_id` bigint NULL DEFAULT NULL COMMENT '供应商Id',
  `ops_user_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 121 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_supplier_staff
-- ----------------------------
DROP TABLE IF EXISTS `t_supplier_staff`;
CREATE TABLE `t_supplier_staff`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `position` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `user_id` bigint NULL DEFAULT NULL,
  `supplier_id` bigint NULL DEFAULT NULL,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `qq` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `job_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK2hnglwmmwpsx0juh1f9ds6qhb`(`supplier_id`) USING BTREE,
  CONSTRAINT `FK2hnglwmmwpsx0juh1f9ds6qhb` FOREIGN KEY (`supplier_id`) REFERENCES `t_supplier_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2185 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_supplier_staff_role
-- ----------------------------
DROP TABLE IF EXISTS `t_supplier_staff_role`;
CREATE TABLE `t_supplier_staff_role`  (
  `supplier_staff_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  INDEX `FKf29tfwrhgdk6d3h771n7h2inb`(`role_id`) USING BTREE,
  INDEX `FKlnbkaarrewhgcnmbsqesfv65f`(`supplier_staff_id`) USING BTREE,
  CONSTRAINT `FKf29tfwrhgdk6d3h771n7h2inb` FOREIGN KEY (`role_id`) REFERENCES `ss_sys_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKlnbkaarrewhgcnmbsqesfv65f` FOREIGN KEY (`supplier_staff_id`) REFERENCES `t_supplier_staff` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_supplier_tag
-- ----------------------------
DROP TABLE IF EXISTS `t_supplier_tag`;
CREATE TABLE `t_supplier_tag`  (
  `supplier_id` bigint NOT NULL,
  `tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tag_id` bigint NULL DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK7c381t2579tum7opa3d2gmtoi`(`supplier_id`) USING BTREE,
  CONSTRAINT `FK7c381t2579tum7opa3d2gmtoi` FOREIGN KEY (`supplier_id`) REFERENCES `t_supplier_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 202 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_supplier_type
-- ----------------------------
DROP TABLE IF EXISTS `t_supplier_type`;
CREATE TABLE `t_supplier_type`  (
  `supplier_id` bigint NOT NULL,
  `types` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type_id` bigint NULL DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKcxeggnmmyoi7atwx2bd9yw6e8`(`supplier_id`) USING BTREE,
  CONSTRAINT `FKcxeggnmmyoi7atwx2bd9yw6e8` FOREIGN KEY (`supplier_id`) REFERENCES `t_supplier_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 200 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_tag_config
-- ----------------------------
DROP TABLE IF EXISTS `t_tag_config`;
CREATE TABLE `t_tag_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `org_id` bigint NULL DEFAULT NULL,
  `company_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_idx_org_tag_config`(`org_id`, `group_name`) USING BTREE,
  UNIQUE INDEX `unique_idx_company_tag_config`(`company_id`, `group_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 146 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_tag_config_item
-- ----------------------------
DROP TABLE IF EXISTS `t_tag_config_item`;
CREATE TABLE `t_tag_config_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `item` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tag_config_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK25ehg9ejgopmop6cu3p9fmxus`(`tag_config_id`) USING BTREE,
  CONSTRAINT `FK25ehg9ejgopmop6cu3p9fmxus` FOREIGN KEY (`tag_config_id`) REFERENCES `t_tag_config` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 200 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_type_config
-- ----------------------------
DROP TABLE IF EXISTS `t_type_config`;
CREATE TABLE `t_type_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `company_id` bigint NULL DEFAULT NULL,
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `org_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_idx_org_type_config`(`org_id`, `group_name`) USING BTREE,
  UNIQUE INDEX `unique_idx_company_type_config`(`company_id`, `group_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_type_config_item
-- ----------------------------
DROP TABLE IF EXISTS `t_type_config_item`;
CREATE TABLE `t_type_config_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `item` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type_config_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKk5qynhvqb9g5d0rrhj3eavc5q`(`type_config_id`) USING BTREE,
  CONSTRAINT `FKk5qynhvqb9g5d0rrhj3eavc5q` FOREIGN KEY (`type_config_id`) REFERENCES `t_type_config` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
