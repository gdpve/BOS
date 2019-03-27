/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50538
Source Host           : localhost:3306
Source Database       : crm_heima32

Target Server Type    : MYSQL
Target Server Version : 50538
File Encoding         : 65001

Date: 2019-03-27 16:13:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_customer
-- ----------------------------
DROP TABLE IF EXISTS `t_customer`;
CREATE TABLE `t_customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '客户id',
  `name` varchar(32) DEFAULT NULL,
  `station` varchar(32) DEFAULT NULL,
  `telephone` varchar(32) DEFAULT NULL,
  `address` varchar(32) DEFAULT NULL,
  `decidedzone_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_customer
-- ----------------------------
INSERT INTO `t_customer` VALUES ('1', 'gdp', null, '15170420298', '南昌青山湖', 'k002');
INSERT INTO `t_customer` VALUES ('2', 'kinggrid', null, '131451423421', '南昌高新', 'k003');
