/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50506
Source Host           : localhost:3306
Source Database       : tengda_sale

Target Server Type    : MYSQL
Target Server Version : 50506
File Encoding         : 65001

Date: 2019-07-08 22:27:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sale_parts
-- ----------------------------
DROP TABLE IF EXISTS `sale_parts`;
CREATE TABLE `sale_parts` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parts_code` varchar(100) NOT NULL COMMENT '配件编号',
  `parts_name` varchar(100) NOT NULL COMMENT '配件名称',
  `parts_image` varchar(255) NOT NULL COMMENT '配件图片',
  `total_num` int(11) NOT NULL COMMENT '配件总数量',
  `current_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '最新成本价',
  `max_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '历史最高成本价',
  `min_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '历史最低成本价',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_parts_code` (`parts_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COMMENT='销售配件表';

-- ----------------------------
-- Records of sale_parts
-- ----------------------------
INSERT INTO `sale_parts` VALUES ('18', '20190705162416', '轴承阻力钢圈52分', '详见纸质二维码', '5', '378.50', '378.50', '358.50', '2019-07-05 16:24:22', '2019-07-08 15:50:27');
INSERT INTO `sale_parts` VALUES ('22', '20190705162416', '轴承阻力钢圈42分', '详见纸质二维码', '0', '358.50', '358.50', '358.50', '2019-07-05 16:24:22', '2019-07-05 16:24:22');
INSERT INTO `sale_parts` VALUES ('23', '20190705162416', '轴承阻力钢圈42分', '详见纸质二维码', '0', '358.50', '358.50', '358.50', '2019-07-05 16:24:22', '2019-07-05 16:24:22');
INSERT INTO `sale_parts` VALUES ('24', '20190705162416', '轴承阻力钢圈88分', '详见纸质二维码', '88', '778.50', '778.50', '358.50', '2019-07-05 16:24:22', '2019-07-08 16:02:07');
INSERT INTO `sale_parts` VALUES ('25', '20190705162416', '轴承阻力钢圈42分', '详见纸质二维码', '0', '358.50', '358.50', '358.50', '2019-07-05 16:24:22', '2019-07-05 16:24:22');
INSERT INTO `sale_parts` VALUES ('26', '20190705162416', '轴承阻力钢圈42分', '详见纸质二维码', '0', '358.50', '358.50', '358.50', '2019-07-05 16:24:22', '2019-07-05 16:24:22');
INSERT INTO `sale_parts` VALUES ('27', '20190705162416', '轴承阻力钢圈488分', '详见纸质二维码', '0', '358.50', '358.50', '358.50', '2019-07-05 16:24:22', '2019-07-08 16:02:44');
INSERT INTO `sale_parts` VALUES ('28', '20190705162416', '轴承阻力钢圈42分', '详见纸质二维码', '0', '358.50', '358.50', '358.50', '2019-07-05 16:24:22', '2019-07-05 16:24:22');
INSERT INTO `sale_parts` VALUES ('29', '20190705162416', '轴承阻力钢圈42分', '详见纸质二维码', '0', '358.50', '358.50', '358.50', '2019-07-05 16:24:22', '2019-07-05 16:24:22');
INSERT INTO `sale_parts` VALUES ('39', '345', '轴承阻力钢圈88分', '详见纸质二维码', '8', '359.50', '359.50', '359.50', '2019-07-08 17:50:25', '2019-07-08 17:50:25');
INSERT INTO `sale_parts` VALUES ('40', '201934343333', '原发发梦777', '详见纸质二维码', '88', '778.50', '778.50', '778.50', '2019-07-08 17:51:11', '2019-07-08 17:51:11');
INSERT INTO `sale_parts` VALUES ('41', '2019343433666', '轴承6力钢圈46分', '详见纸质二维码', '8', '359.50', '359.50', '359.50', '2019-07-08 18:00:18', '2019-07-08 18:00:18');

-- ----------------------------
-- Table structure for sale_record
-- ----------------------------
DROP TABLE IF EXISTS `sale_record`;
CREATE TABLE `sale_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` tinyint(4) NOT NULL DEFAULT '2' COMMENT '记录类型：1-进货，2-出货',
  `parts_code` varchar(100) NOT NULL COMMENT '配件编号',
  `parts_name` varchar(100) NOT NULL COMMENT '配件名称',
  `parts_num` int(11) NOT NULL DEFAULT '1' COMMENT '进货/出货数量',
  `parts_price` decimal(10,2) NOT NULL COMMENT '进货/出货价格',
  `order_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `sale_object` varchar(100) DEFAULT NULL COMMENT '销售对象',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='销售记录表';

-- ----------------------------
-- Records of sale_record
-- ----------------------------
INSERT INTO `sale_record` VALUES ('1', '1', '20190705162416', '轴承阻力钢圈42分', '99', '358.50', '6658.50', '18991166408', '2019-06-08 12:24:40', '2019-06-05 16:50:11');
INSERT INTO `sale_record` VALUES ('2', '2', '20190705162416', '轴承阻力钢圈42分', '88', '358.50', '1792.50', '', '2019-07-08 12:24:40', '2019-07-08 21:20:35');
INSERT INTO `sale_record` VALUES ('4', '0', '201934343333', '轴承阻力钢圈46分', '5', '555.00', '115.00', '18301166408', '2019-06-08 18:02:01', '2019-07-08 18:03:25');
INSERT INTO `sale_record` VALUES ('5', '0', '201934343777', '轴承阻力钢圈46分', '5', '555.00', '2775.00', '18355566408', '2019-07-08 18:03:51', '2019-07-08 18:03:51');

-- ----------------------------
-- Table structure for sale_report
-- ----------------------------
DROP TABLE IF EXISTS `sale_report`;
CREATE TABLE `sale_report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `query_sql` varchar(1000) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sale_report
-- ----------------------------
INSERT INTO `sale_report` VALUES ('2', '月报表', 'SELECT a.parts_code as \'[1]编码\',a.parts_name as \'[2]名称\',(CASE b.type WHEN \'1\' THEN \'进货\' ELSE \'出货\' END) as \'[3]类型\',DATE_FORMAT(b.create_time,\'%Y-%m-%d %H:%i:%s\') as \'[4]时间\' from sale_parts a inner join sale_record b on a.parts_code = b.parts_code where b.create_time > @@MONTH_CURRENT', '2019-07-08 20:20:51');
