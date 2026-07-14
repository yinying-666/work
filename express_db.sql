/*
 Navicat Premium Dump SQL

 Source Server         : 111
 Source Server Type    : MySQL
 Source Server Version : 80046 (8.0.46)
 Source Host           : localhost:3306
 Source Schema         : express_db

 Target Server Type    : MySQL
 Target Server Version : 80046 (8.0.46)
 File Encoding         : 65001

 Date: 14/07/2026 15:23:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_express
-- ----------------------------
DROP TABLE IF EXISTS `tb_express`;
CREATE TABLE `tb_express`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收件人手机号',
  `take_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '唯一8位取件码',
  `express_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '快递单号',
  `status` tinyint NULL DEFAULT 1 COMMENT '1待取件 2已出库 3异常滞留',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '快递入库时间',
  `out_time` datetime NULL DEFAULT NULL COMMENT '快递出库核销时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '快递信息主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_express
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
