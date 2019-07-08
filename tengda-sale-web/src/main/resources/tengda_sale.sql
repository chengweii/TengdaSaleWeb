/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50506
Source Host           : localhost:3306
Source Database       : tengda_sale

Target Server Type    : MYSQL
Target Server Version : 50506
File Encoding         : 65001

Date: 2019-07-02 12:06:00
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='销售配件表';

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售记录表';

-- ----------------------------
-- Table structure for sale_report
-- ----------------------------
DROP TABLE IF EXISTS `sale_report`;
CREATE TABLE `sale_report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `query_sql` varchar(1000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
