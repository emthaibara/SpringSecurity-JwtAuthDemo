/*
 Navicat Premium Data Transfer

 Source Server         : LiYongJie
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : localhost:3306
 Source Schema         : LYJ

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 13/11/2021 00:25:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for User
-- ----------------------------
DROP TABLE IF EXISTS `User`;
CREATE TABLE `User` (
  `phoneNumber` varchar(255) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `introduction` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`phoneNumber`),
  KEY `id` (`phoneNumber`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of User
-- ----------------------------
BEGIN;
INSERT INTO `User` VALUES ('13266746454', 'user01', 'root', 'NONMEMBER', 'HelloWorld');
INSERT INTO `User` VALUES ('13266746455', 'merchant-user', 'root', 'MERCHANT', 'HelloWorld');
INSERT INTO `User` VALUES ('13266746456', 'member-user', 'root', 'MEMBER', 'HelloWorld');
INSERT INTO `User` VALUES ('13266746457', 'nonmember-user', 'root', 'NONMEMBER', 'HelloWorld');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
