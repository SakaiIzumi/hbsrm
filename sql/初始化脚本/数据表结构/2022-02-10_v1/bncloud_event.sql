/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.2.136-美尚
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : 192.168.2.136:3306
 Source Schema         : bncloud_event

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 10/02/2022 17:54:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for evt_event_detail
-- ----------------------------
DROP TABLE IF EXISTS `evt_event_detail`;
CREATE TABLE `evt_event_detail`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `from_user_id` bigint NULL DEFAULT NULL,
  `from_user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type_id` bigint NOT NULL,
  `org_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKa1i0qf4s6rf0snii3m20j4eox`(`type_id`) USING BTREE,
  CONSTRAINT `FKa1i0qf4s6rf0snii3m20j4eox` FOREIGN KEY (`type_id`) REFERENCES `evt_event_type` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3161 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for evt_event_detail_data
-- ----------------------------
DROP TABLE IF EXISTS `evt_event_detail_data`;
CREATE TABLE `evt_event_detail_data`  (
  `event_id` bigint NOT NULL,
  `data` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`event_id`) USING BTREE,
  CONSTRAINT `FK5fpu0lf5ow6fit5amj6stqrh2` FOREIGN KEY (`event_id`) REFERENCES `evt_event_detail` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for evt_event_role
-- ----------------------------
DROP TABLE IF EXISTS `evt_event_role`;
CREATE TABLE `evt_event_role`  (
  `evt_type_id` bigint NOT NULL,
  `relate_at` datetime(6) NULL DEFAULT NULL,
  `role_id` bigint NULL DEFAULT NULL,
  `role_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `id` bigint NOT NULL DEFAULT 0,
  `is_deleted` bit(1) NULL DEFAULT NULL,
  `created_by` bigint NULL DEFAULT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `roles_evt_type_id` bigint NULL DEFAULT 0,
  `disabled` bit(1) NOT NULL,
  INDEX `FKqn62y1tw9a03fi5dmyodboo76`(`evt_type_id`) USING BTREE,
  CONSTRAINT `FKqn62y1tw9a03fi5dmyodboo76` FOREIGN KEY (`evt_type_id`) REFERENCES `evt_event_type` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for evt_event_type
-- ----------------------------
DROP TABLE IF EXISTS `evt_event_type`;
CREATE TABLE `evt_event_type`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `module` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `notify` bit(1) NOT NULL,
  `scene` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `msg_tpl_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `msg_tpl_tag` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `disabled` bit(1) NOT NULL,
  `modular_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板类型 字典modularType',
  `bis_type` int NULL DEFAULT NULL,
  `event_group_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `event_type` int NULL DEFAULT NULL,
  `is_default` bit(1) NOT NULL,
  `msg_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `org_id` int NULL DEFAULT NULL,
  `receiver_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发送方',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 99 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
