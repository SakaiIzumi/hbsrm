/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.2.136-美尚
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : 192.168.2.136:3306
 Source Schema         : bncloud_file

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 10/02/2022 17:53:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for f_file_download
-- ----------------------------
DROP TABLE IF EXISTS `f_file_download`;
CREATE TABLE `f_file_download`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `download_by_user_id` bigint NULL DEFAULT NULL,
  `download_by_user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `file_info_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKrmfmdracn9bu1akwd2s4cxve9`(`file_info_id`) USING BTREE,
  CONSTRAINT `FKrmfmdracn9bu1akwd2s4cxve9` FOREIGN KEY (`file_info_id`) REFERENCES `f_file_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 621 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for f_file_info
-- ----------------------------
DROP TABLE IF EXISTS `f_file_info`;
CREATE TABLE `f_file_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `content_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `extension` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `filename` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_img` bit(1) NULL DEFAULT NULL,
  `original_filename` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `size` bigint NOT NULL,
  `storage_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 188 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for f_storage_config_local
-- ----------------------------
DROP TABLE IF EXISTS `f_storage_config_local`;
CREATE TABLE `f_storage_config_local`  (
  `id` bigint NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `created_by` bigint NOT NULL,
  `created_date` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint NULL DEFAULT NULL,
  `last_modified_date` datetime(6) NULL DEFAULT NULL,
  `root_dir` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for hibernate_sequence
-- ----------------------------
DROP TABLE IF EXISTS `hibernate_sequence`;
CREATE TABLE `hibernate_sequence`  (
  `next_val` bigint NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
