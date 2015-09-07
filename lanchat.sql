/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50155
Source Host           : 127.0.0.1:3306
Source Database       : lanchat

Target Server Type    : MYSQL
Target Server Version : 50155
File Encoding         : 65001

Date: 2015-03-23 18:58:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `u_id` int(9) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `u_loginname` varchar(255) COLLATE utf8_bin NOT NULL,
  `u_password` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT 'password',
  `u_sex` varchar(2) COLLATE utf8_bin DEFAULT '保密' COMMENT '性别',
  `u_status` int(1) DEFAULT '0' COMMENT '0 or 1 0代表离线',
  `u_ip` varchar(255) COLLATE utf8_bin DEFAULT '127.0.0.1' COMMENT '用户登录的ip地址',
  `u_session` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `u_nickname` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `u_realname` varchar(255) COLLATE utf8_bin DEFAULT 'RealName',
  `u_phone` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `u_email` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `u_description` varchar(255) COLLATE utf8_bin DEFAULT 'My name is ***.',
  `u_PPQ` varchar(255) COLLATE utf8_bin DEFAULT 'My favour book name?',
  `u_PPA` varchar(255) COLLATE utf8_bin DEFAULT 'Answer is .',
  `u_group` longblob,
  PRIMARY KEY (`u_id`),
  UNIQUE KEY `u_loginname` (`u_loginname`),
  UNIQUE KEY `u_nickname` (`u_nickname`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('000000001', 'Widmore', 'qazwsxedc', '男', '1', '10.97.7.50', '2015-03-23 14:22:46', null, 'RealName', null, null, 'My name is ***.', 'My favour book name?', 'Answer is .', 0x3C3F786D6C2076657273696F6E3D22312E302220656E636F64696E673D225554462D38223F3E0A0A3C67726F7570733E200A20203C67726F75703E557365723C2F67726F75703E20200A20203C67726F75703E546561636865720A202020203C75736572206E69636B3D2220223E5465616368657230313C2F757365723E0A20203C2F67726F75703E200A3C2F67726F7570733E0A);
INSERT INTO `users` VALUES ('000000002', 'Goodwin', 'qazwsxedc', '男', '1', '127.0.0.1', '2015-03-25 17:10:13', null, 'RealName', null, null, 'My name is ***.', 'My favour book name?', 'Answer is .', null);
INSERT INTO `users` VALUES ('000000003', 'Alice', 'qazwsxedc', '女', '1', '127.0.0.1', '2015-03-16 17:10:17', null, 'RealName', null, null, 'My name is ***.', 'My favour book name?', 'Answer is .', null);
INSERT INTO `users` VALUES ('000000004', 'Leethan', 'qazwsxedc', '男', '1', '127.0.0.1', '2015-03-15 17:10:22', null, 'RealName', null, null, 'My name is ***.', 'My favour book name?', 'Answer is .', null);
INSERT INTO `users` VALUES ('000000005', 'Yiyuan', 'qazwsxedc', '男', '1', '192.168.137.1', '2015-03-15 17:11:33', null, 'RealName', null, null, 'My name is ***.', 'My favour book name?', 'Answer is .', null);
INSERT INTO `users` VALUES ('000000006', 'Lias', 'qazwsxedc', '女', '1', '127.0.0.1', '2015-03-16 10:02:36', null, 'RealName', null, null, 'My name is ***.', 'My favour book name?', 'Answer is .', null);
INSERT INTO `users` VALUES ('000000007', 'Avatar', 'qazwsxedc', '保密', '1', '127.0.0.1', '2015-03-17 10:02:38', null, 'RealName', null, null, 'My name is ***.', 'My favour book name?', 'Answer is .', null);
INSERT INTO `users` VALUES ('000000008', 'Teacher01', 'qazwsxedc', '保密', '1', '127.0.0.1', '2015-03-23 12:51:57', null, 'RealName', null, null, 'My name is ***.', 'My favour book name?', 'Answer is .', null);
