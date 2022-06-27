/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.2.136-美尚
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : 192.168.2.136:3306
 Source Schema         : ecology

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 10/02/2022 18:03:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for uf_hzhbxxb
-- ----------------------------
DROP TABLE IF EXISTS `uf_hzhbxxb`;
CREATE TABLE `uf_hzhbxxb`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `requestId` int NULL DEFAULT NULL,
  `tjrq` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `hzfbm` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `hzxz` int NULL DEFAULT NULL,
  `qyqc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `qyjc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `zrrxm` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `zrrjc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `xb` int NULL DEFAULT NULL,
  `mslxrdh` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `clrq` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `tyshxydm` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `qyyyzz` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `scjyxkz` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `gszcdz` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `qyjj` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `sfzhz` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `sjdz` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `hzywms` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `formmodeid` int NULL DEFAULT NULL,
  `modedatacreater` int NULL DEFAULT NULL,
  `modedatacreatertype` int NULL DEFAULT NULL,
  `modedatacreatedate` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `modedatacreatetime` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `modedatamodifier` int NULL DEFAULT NULL,
  `modedatamodifydatetime` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MODEUUID` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `qy` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `zrr` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mslxrdh1` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `clrq1` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `hzywms1` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `hzhblx` int NULL DEFAULT NULL,
  `sjdz1` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `zylxr` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sfhz` int NULL DEFAULT NULL,
  `qyyc2` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `qyyc3` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `qyyc4` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `qyyc5` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `qyyc6` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `qyyc7` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `qyyc8` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `zrryc2` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `zrryc3` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `zrryc4` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `zrryc5` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `zrryc7` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `zrryc8` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `zrryc6` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `gyslb` int NULL DEFAULT NULL,
  `sfhzdgys` int NULL DEFAULT NULL,
  `jdnumber` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `jdid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `qyzczj` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `qd` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `khbm` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ck` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `gh` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mslxr` int NULL DEFAULT NULL,
  `mslxr1` int NULL DEFAULT NULL,
  `gh1` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `khfzbm` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `khfz` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `xsy` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `xsy1` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `jdcg` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `lssjfjdz` varchar(999) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `hzhbxxjdczzy` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `wfgsmc` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `jdNewestNumber` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uf_ind_1626921260258`(`MODEUUID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 735 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for uf_hzhbxxb_dt1
-- ----------------------------
DROP TABLE IF EXISTS `uf_hzhbxxb_dt1`;
CREATE TABLE `uf_hzhbxxb_dt1`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `mainid` int NULL DEFAULT NULL,
  `fddbrxm` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sjh` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `yxdz` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `frsfz` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `hzxz` int NULL DEFAULT NULL,
  `bz` varchar(999) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uf_hzhbxxb_dt1_mId`(`mainid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 688 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for uf_hzhbxxb_dt2
-- ----------------------------
DROP TABLE IF EXISTS `uf_hzhbxxb_dt2`;
CREATE TABLE `uf_hzhbxxb_dt2`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `mainid` int NULL DEFAULT NULL,
  `xm` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `zw` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sfzhzh` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `yxdz` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sjhm` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `wxhm` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `hzxz` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uf_hzhbxxb_dt2_mId`(`mainid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 690 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for uf_hzhbxxb_dt3
-- ----------------------------
DROP TABLE IF EXISTS `uf_hzhbxxb_dt3`;
CREATE TABLE `uf_hzhbxxb_dt3`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `mainid` int NULL DEFAULT NULL,
  `yhzhmc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `yhzh` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `khhqc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `hzxz` int NULL DEFAULT NULL,
  `bz` varchar(999) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `khxkz` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uf_hzhbxxb_dt3_mId`(`mainid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 693 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for uf_hzhbxxb_dt4
-- ----------------------------
DROP TABLE IF EXISTS `uf_hzhbxxb_dt4`;
CREATE TABLE `uf_hzhbxxb_dt4`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `mainid` int NULL DEFAULT NULL,
  `qxx` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `hzxz` int NULL DEFAULT NULL,
  `fptt` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `nsdjh` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `khyh` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `yhzh` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `kplxdh` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `kptxdz` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `fplx` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `sysl` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `nsrlx` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uf_hzhbxxb_dt4_mId`(`mainid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 689 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for uf_hzhbxxb_dt5
-- ----------------------------
DROP TABLE IF EXISTS `uf_hzhbxxb_dt5`;
CREATE TABLE `uf_hzhbxxb_dt5`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `mainid` int NULL DEFAULT NULL,
  `xm` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `zw` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `yxdz` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sfzhz` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `hzxz` int NULL DEFAULT NULL,
  `sjhm` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bz` varchar(999) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `wxhm` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uf_hzhbxxb_dt5_mId`(`mainid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 47 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for uf_hzhbxxb_dt6
-- ----------------------------
DROP TABLE IF EXISTS `uf_hzhbxxb_dt6`;
CREATE TABLE `uf_hzhbxxb_dt6`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `mainid` int NULL DEFAULT NULL,
  `xm` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `zw` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `wxh` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `yxdz` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sfzhz` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `hzxz` int NULL DEFAULT NULL,
  `sjhm` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uf_hzhbxxb_dt6_mId`(`mainid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for uf_hzhbxxb_dt7
-- ----------------------------
DROP TABLE IF EXISTS `uf_hzhbxxb_dt7`;
CREATE TABLE `uf_hzhbxxb_dt7`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `mainid` int NULL DEFAULT NULL,
  `yhzhmc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `yhzh` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `khhqc` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `hzxz` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uf_hzhbxxb_dt7_mId`(`mainid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for uf_hzhbxxb_dt8
-- ----------------------------
DROP TABLE IF EXISTS `uf_hzhbxxb_dt8`;
CREATE TABLE `uf_hzhbxxb_dt8`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `mainid` int NULL DEFAULT NULL,
  `sfnkp` int NULL DEFAULT NULL,
  `qxx` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `hzxz` int NULL DEFAULT NULL,
  `fptt` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `nsdjh` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `khyh` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `yhzh` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `kplxdh` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `kptxdz` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `fplx` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `sysl` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `MODEUUID` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `nsrlx` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uf_hzhbxxb_dt8_mId`(`mainid`) USING BTREE,
  INDEX `uf_ind_1631185387718`(`MODEUUID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for uf_srm_contract
-- ----------------------------
DROP TABLE IF EXISTS `uf_srm_contract`;
CREATE TABLE `uf_srm_contract`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `requestId` int NULL DEFAULT NULL,
  `sqr` int NULL DEFAULT NULL,
  `szbm` int NULL DEFAULT NULL,
  `sqrq` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sfxq` int NULL DEFAULT NULL,
  `hzflx` int NULL DEFAULT NULL,
  `wffzr` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `dffzr` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `qdlx` int NULL DEFAULT NULL,
  `sfsw` int NULL DEFAULT NULL,
  `bz` int NULL DEFAULT NULL,
  `htje` decimal(38, 2) NULL DEFAULT NULL,
  `sfhs` int NULL DEFAULT NULL,
  `sm` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `xgfj` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `htfxjb` int NULL DEFAULT NULL,
  `beizhu` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `lcbh` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `wfdwmc` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `szpp` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `manager` int NULL DEFAULT NULL,
  `bmc` int NULL DEFAULT NULL,
  `dfdwmc` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `hth` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `zxcsr` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `nfkp` int NULL DEFAULT NULL,
  `nsrzz` int NULL DEFAULT NULL,
  `fplx` int NULL DEFAULT NULL,
  `kpqk` int NULL DEFAULT NULL,
  `zq` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `dhzq` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `fktj` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `htlb` int NULL DEFAULT NULL,
  `sfndkjxy` int NULL DEFAULT NULL,
  `fklx` int NULL DEFAULT NULL,
  `cxsm` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `htzje` decimal(38, 2) NULL DEFAULT NULL,
  `yjsfkykp` int NULL DEFAULT NULL,
  `htlx2` int NULL DEFAULT NULL,
  `fjsc` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `xglc` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `sysl` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `sfyddhzhbzrsqlc` int NULL DEFAULT NULL,
  `ddhzhbzrsqlch` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `hzflxht` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `hzhb` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `dfdwmc1` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `htdyfs` int NULL DEFAULT NULL,
  `erp_sysl` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `fplx1` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `sfdfxgz` int NULL DEFAULT NULL,
  `fqrsj` int NULL DEFAULT NULL,
  `fplxoa` int NULL DEFAULT NULL,
  `sfzdgys` int NULL DEFAULT NULL,
  `a` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `glcgysqlc` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `formmodeid` int NULL DEFAULT NULL,
  `modedatacreater` int NULL DEFAULT NULL,
  `modedatacreatertype` int NULL DEFAULT NULL,
  `modedatacreatedate` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `modedatacreatetime` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `modedatamodifier` int NULL DEFAULT NULL,
  `modedatamodifydatetime` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MODEUUID` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bt` varchar(99) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `szbm_txt` varchar(99) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sqr_txt` varchar(99) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `szpp_txt` varchar(99) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sfxq_txt` varchar(99) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `hzflx_txt` varchar(99) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sfzdgys_txt` varchar(99) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `glcgysqlc_txt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `wfdwmc_txt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `hzhb_txt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `htlx2_txt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `htlb_txt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sfndkjxy_txt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bz_txt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `yjsfkykp_txt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `nfkp_txt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `nsrzz_txt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `fplx1_txt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `erp_sysl_txt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `kpqk_txt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sfdfxgz_txt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `htfxjb_txt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `srmhqhtsfcg` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `srmlczt` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uf_ind_1642484880298`(`MODEUUID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
