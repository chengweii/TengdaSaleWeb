/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50506
Source Host           : localhost:3306
Source Database       : tengda_sale

Target Server Type    : MYSQL
Target Server Version : 50506
File Encoding         : 65001

Date: 2019-07-09 19:33:01
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
INSERT INTO `sale_parts` VALUES ('18', '20190705162416', '测试配件', '详见纸质二维码', '3', '1.00', '1.00', '1.00', '2019-07-09 19:29:58', '2019-07-08 15:50:27');

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
INSERT INTO `sale_record` VALUES ('1', '1', '20190705162416', '测试配件', '3', '1.00', '3.00', '', '2019-07-09 19:30:19', '2019-06-05 16:50:11');
INSERT INTO `sale_record` VALUES ('2', '2', '20190705162416', '测试配件', '2', '2.00', '4.00', '', '2019-07-09 19:29:28', '2019-07-08 21:20:35');

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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sale_report
-- ----------------------------
INSERT INTO `sale_report` VALUES ('2', '当月总账报表', 'SELECT sum(case type when \'1\' then order_amount end) as \'[1]当月支出\',sum(case type when \'2\' then order_amount end) as \'[2]当月收入\',sum(case type when \'2\' then order_amount end)-sum(case type when \'1\' then order_amount end) as \'[3]当月盈利\' from sale_record where create_time >= @@MONTH_CURRENT_BEGIN and create_time <= @@MONTH_CURRENT_END', '2019-07-08 20:20:51');
INSERT INTO `sale_report` VALUES ('3', '当年月度报表', 'SELECT DATE_FORMAT(create_time,\'%Y-%m\') AS \'[1]月份\',sum(case type when \'1\' then order_amount end) as \'[2]支出\',sum(case type when \'2\' then order_amount end) as \'[3]收入\',sum(case type when \'2\' then order_amount end)-sum(case type when \'1\' then order_amount end) as \'[4]盈利\' from sale_record where create_time >= @@YEAR_BEGIN and create_time <= @@YEAR_END group by DATE_FORMAT(create_time,\'%Y-%m\') order by DATE_FORMAT(create_time,\'%Y-%m\') desc', '2019-07-09 18:46:09');
INSERT INTO `sale_report` VALUES ('4', '去年月度报表', 'SELECT DATE_FORMAT(create_time,\'%Y-%m\') AS \'[1]月份\',sum(case type when \'1\' then order_amount end) as \'[2]支出\',sum(case type when \'2\' then order_amount end) as \'[3]收入\',sum(case type when \'2\' then order_amount end)-sum(case type when \'1\' then order_amount end) as \'[4]盈利\' from sale_record where create_time >= @@LAST_YEAR_BEGIN and create_time <= @@LAST_YEAR_END group by DATE_FORMAT(create_time,\'%Y-%m\') order by DATE_FORMAT(create_time,\'%Y-%m\') desc', '2019-07-09 18:54:39');
INSERT INTO `sale_report` VALUES ('5', '当年配件销售报表', 'SELECT a.parts_code as \'[1]配件编号\',a.parts_name as \'[2]配件名称\',sum(a.total_num) as \'[3]库存数量\',sum(b.parts_num) as \'[4]出货数量\' from sale_parts a LEFT JOIN (SELECT * from sale_record c where c.type = \'2\' and c.create_time >= @@YEAR_BEGIN and c.create_time <= @@YEAR_END) b on a.parts_code = b.parts_code GROUP BY a.parts_code,a.parts_name  order by sum(b.parts_num)  asc', '2019-07-09 19:17:26');
