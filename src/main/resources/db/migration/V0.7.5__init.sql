/*
 Navicat MySQL Data Transfer

 Source Server         : 本机
 Source Server Type    : MySQL
 Source Server Version : 50717
 Source Host           : localhost:3306
 Source Schema         : daxiang

 Target Server Type    : MySQL
 Target Server Version : 50717
 File Encoding         : 65001

 Date: 13/06/2019 11:20:04
*/

-- ----------------------------
-- Table structure for action
-- ----------------------------
DROP TABLE IF EXISTS `action`;
CREATE TABLE `action` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT 'action名',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '类型：1.基础action（代码形式的） 2.用户在网页前端封装的action 3.测试用例',
  `invoke` varchar(255) DEFAULT NULL COMMENT '基础action专用：调用',
  `return_value_type` varchar(255) NOT NULL COMMENT '返回值类型: void / 其他',
  `return_value_desc` varchar(255) DEFAULT NULL COMMENT '返回值描述',
  `params` json DEFAULT NULL COMMENT '方法参数',
  `local_vars` json DEFAULT NULL COMMENT '局部变量',
  `steps` json DEFAULT NULL COMMENT '步骤',
  `java_imports` json DEFAULT NULL COMMENT 'java imports',
  `action_imports` json DEFAULT NULL COMMENT 'action imports',
  `creator_uid` int(11) DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updator_uid` int(11) DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `platforms` json DEFAULT NULL COMMENT '1.android 2.ios 3.pc web empty.通用',
  `page_id` int(11) DEFAULT NULL COMMENT '所属的page id',
  `category_id` int(11) DEFAULT NULL COMMENT '所属的分类id',
  `project_id` int(11) DEFAULT NULL COMMENT '所属的项目id',
  `state` tinyint(4) NOT NULL DEFAULT '2' COMMENT '禁用: 0 草稿: 1 发布: 2',
  `depends` json null COMMENT '依赖的测试用例id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_name_projectId_type` (`name`,`project_id`,`type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COMMENT='action表';

-- ----------------------------
-- Table structure for app
-- ----------------------------
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `platform` int(4) NOT NULL COMMENT '平台：1.android 2.iOS',
  `name` varchar(100) NOT NULL COMMENT 'app名',
  `version` varchar(100) DEFAULT NULL COMMENT '版本号',
  `package_name` varchar(100) DEFAULT NULL COMMENT 'android: 包名',
  `launch_activity` varchar(100) DEFAULT NULL COMMENT 'android: 启动activity',
  `file_path` varchar(255) NOT NULL COMMENT '服务端保存的文件路径',
  `upload_time` datetime DEFAULT NULL COMMENT '上传时间',
  `uploador_uid` int(11) DEFAULT NULL COMMENT '上传人',
  `project_id` int(11) NOT NULL COMMENT '所属项目',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='app表';

-- ----------------------------
-- Table structure for browser
-- ----------------------------
CREATE TABLE `browser` (
  `id` varchar(100) NOT NULL COMMENT '浏览器id',
  `type` varchar(50) NOT NULL COMMENT '类型: chrome firefox ...',
  `version` varchar(50) NOT NULL COMMENT '版本号',
  `platform` tinyint(4) NOT NULL COMMENT '平台: 1. windows 2.linux 3.macos',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态：0.离线 1.使用中 2.空闲',
  `agent_ip` varchar(50) DEFAULT NULL COMMENT '浏览器所在的agent的ip',
  `agent_port` int(11) DEFAULT NULL COMMENT '浏览器所在的agent的端口',
  `last_online_time` datetime DEFAULT NULL COMMENT '最近一次在线时间',
  `username` varchar(100) DEFAULT NULL COMMENT '最近一次使用人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='浏览器表';

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NOT NULL DEFAULT 0 COMMENT '父id',
  `name` varchar(100) NOT NULL COMMENT '分类名字',
  `type` tinyint(4) NOT NULL COMMENT '类型：1.page 2.action 3.全局变量 4.testcase(action) 5.基础action',
  `project_id` int(11) DEFAULT NULL COMMENT '所属项目的id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator_uid` int(11) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_name_type_projectId` (`name`,`type`,`project_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COMMENT='分类表';

-- ----------------------------
-- Table structure for mobile
-- ----------------------------
DROP TABLE IF EXISTS `mobile`;
CREATE TABLE `mobile` (
  `id` varchar(100) NOT NULL COMMENT 'mobile id',
  `name` varchar(100) DEFAULT NULL COMMENT 'mobile名',
  `emulator` tinyint(4) DEFAULT NULL COMMENT '0: 真机 1: 模拟器',
  `agent_ip` varchar(50) DEFAULT NULL COMMENT '所在的agent的ip',
  `agent_port` int(11) DEFAULT NULL COMMENT '所在的agent的端口',
  `system_version` varchar(50) DEFAULT NULL COMMENT '系统版本',
  `cpu_info` varchar(50) DEFAULT NULL COMMENT 'cpu信息',
  `mem_size` varchar(50) DEFAULT NULL COMMENT '内存大小：GB',
  `screen_width` int(11) DEFAULT NULL COMMENT '屏幕宽（像素）',
  `screen_height` int(11) DEFAULT NULL COMMENT '屏幕高（像素）',
  `img_path` varchar(255) DEFAULT NULL COMMENT '服务端保存的图片路径',
  `platform` tinyint(4) DEFAULT NULL COMMENT '平台：1.android  2.ios',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态：0.离线 1.使用中 2.空闲',
  `last_online_time` datetime DEFAULT NULL COMMENT '最近一次在线时间',
  `username` varchar(100) DEFAULT NULL COMMENT '最近一次使用人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='mobile表';

-- ----------------------------
-- Table structure for device_test_task
-- ----------------------------
DROP TABLE IF EXISTS `device_test_task`;
CREATE TABLE `device_test_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NOT NULL COMMENT '项目id',
  `platform` tinyint(4) NOT NULL COMMENT '平台',
  `capabilities` text DEFAULT NULL COMMENT 'org.openqa.selenium.Capabilities',
  `test_task_id` int(11) NOT NULL COMMENT '测试任务id',
  `test_plan` json NOT NULL COMMENT '下发任务时的testplan',
  `device_id` varchar(100) NOT NULL COMMENT 'deviceId',
  `device` json DEFAULT NULL COMMENT '下发任务时的device',
  `global_vars` json DEFAULT NULL COMMENT '全局变量',
  `pages` json DEFAULT NULL COMMENT 'pages',
  `before_class` json DEFAULT NULL COMMENT 'BeforeClass',
  `before_method` json DEFAULT NULL COMMENT 'BeforeMethod',
  `after_class` json DEFAULT NULL COMMENT 'AfterClass',
  `after_method` json DEFAULT NULL COMMENT 'AfterMethod',
  `testcases` json NOT NULL COMMENT '执行的测试用例',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态：-1:无法运行 0:未开始 1:运行中 2:已完成',
  `code` mediumtext COMMENT 'agent转换后的代码',
  `err_msg` text COMMENT 'status: -1, 错误信息',
  `start_time` datetime DEFAULT NULL COMMENT '开始测试时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束测试时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_testTaskId_deviceId` (`test_task_id`,`device_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='device测试任务表';

-- ----------------------------
-- Table structure for global_var
-- ----------------------------
DROP TABLE IF EXISTS `global_var`;
CREATE TABLE `global_var` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '变量名',
  `type` varchar(255) NOT NULL COMMENT '变量类型',
  `environment_values` json NOT NULL COMMENT '变量值',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `project_id` int(11) NOT NULL COMMENT '所属的项目id',
  `category_id` int(11) DEFAULT NULL COMMENT '所属分类',
  `creator_uid` int(11) DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_name_projectId` (`name`,`project_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='全局变量表';

-- ----------------------------
-- Table structure for page
-- ----------------------------
DROP TABLE IF EXISTS `page`;
CREATE TABLE `page` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT 'page名',
  `project_id` int(11) NOT NULL COMMENT 'page所属项目',
  `type` tinyint(4) NOT NULL COMMENT '1.android_native 2.ios_native 3.web',
  `category_id` int(11) DEFAULT NULL COMMENT 'page所属分类',
  `description` varchar(255) DEFAULT NULL COMMENT 'page描述',
  `img_path` varchar(255) DEFAULT NULL COMMENT '服务端保存的文件路径',
  `window_height` int(11) DEFAULT NULL COMMENT 'window高度',
  `window_width` int(11) DEFAULT NULL COMMENT 'window宽度',
  `window_orientation` varchar(11) DEFAULT NULL COMMENT '屏幕方向',
  `window_hierarchy` mediumtext COMMENT '页面布局',
  `device_id` varchar(100) DEFAULT NULL COMMENT '图片所属的deviceId',
  `elements` json NULL COMMENT '元素',
  `bys` json NULL COMMENT 'By',
  `creator_uid` int(11) DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_name_projectId` (`name`,`project_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='page表';

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '项目名',
  `description` varchar(255) DEFAULT NULL COMMENT '项目描述',
  `platform` tinyint(4) NOT NULL COMMENT '1.andorid 2.iOS 3.pc web',
  `capabilities` text DEFAULT NULL COMMENT 'org.openqa.selenium.Capabilities',
  `creator_uid` int(11) NOT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_name_platform` (`name`,`platform`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目表';

-- ----------------------------
-- Table structure for test_plan
-- ----------------------------
DROP TABLE IF EXISTS `test_plan`;
CREATE TABLE `test_plan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '测试计划名',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `project_id` int(11) NOT NULL COMMENT '所属项目',
  `environment_id` int(11) NOT NULL DEFAULT '-1' COMMENT '环境，默认-1',
  `before_class` int(11) DEFAULT NULL COMMENT 'BeforeClass',
  `before_method` int(11) DEFAULT NULL COMMENT 'BeforeMehtod',
  `after_class` int(11) DEFAULT NULL COMMENT 'AfterClass',
  `after_method` int(11) DEFAULT NULL COMMENT 'AfterMethod',
  `test_suites` json NOT NULL COMMENT '测试集',
  `device_ids` json NOT NULL COMMENT 'deviceIds',
  `run_mode` tinyint(4) NOT NULL COMMENT '运行模式 1:兼容模式 2:高效模式',
  `cron_expression` varchar(255) DEFAULT NULL COMMENT 'cron表达式',
  `enable_schedule` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否开启定时任务，0: 关闭 1: 开启',
  `enable_record_video` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否录制视频，0: 不录制 1: 录制',
  `fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
  `creator_uid` int(11) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_projectId_name` (`name`,`project_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试计划表';

-- ----------------------------
-- Table structure for test_suite
-- ----------------------------
DROP TABLE IF EXISTS `test_suite`;
CREATE TABLE `test_suite` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '测试集名',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `testcases` json NOT NULL COMMENT '测试用例',
  `project_id` int(11) NOT NULL COMMENT '所属项目',
  `creator_uid` int(11) DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_name_projectId` (`name`,`project_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试集';

-- ----------------------------
-- Table structure for test_task
-- ----------------------------
DROP TABLE IF EXISTS `test_task`;
CREATE TABLE `test_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` int(11) NOT NULL COMMENT '所属项目',
  `test_plan_id` int(11) NOT NULL COMMENT '所属测试计划',
  `test_plan` json NOT NULL COMMENT '下发任务时的testplan',
  `status` tinyint(4) DEFAULT NULL COMMENT '任务状态 0:未完成 1:已完成',
  `creator_uid` int(11) DEFAULT NULL COMMENT '任务创建人',
  `pass_case_count` int(11) DEFAULT '0' COMMENT '测试通过用例数',
  `fail_case_count` int(11) DEFAULT '0' COMMENT '测试失败用例数',
  `skip_case_count` int(11) DEFAULT '0' COMMENT '测试跳过用例数',
  `commit_time` datetime DEFAULT NULL COMMENT '任务提交时间',
  `finish_time` datetime DEFAULT NULL COMMENT '任务完成时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测试任务表';

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '用户密码',
  `nick_name` varchar(255) NOT NULL COMMENT '用户昵称',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态，0:禁用 1:正常',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
-- ----------------------------

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '角色名',
  `alias` varchar(100) NOT NULL COMMENT '别名',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';
-- ----------------------------

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_userId_roleId` (`user_id`, `role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色表';

-- ----------------------------
-- Table structure for user_project
-- ----------------------------
DROP TABLE IF EXISTS `user_project`;
CREATE TABLE `user_project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `project_id` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_userId_projectId` (`user_id`, `project_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户项目表';

-- ----------------------------

-- Table structure for driver
-- ----------------------------
DROP TABLE IF EXISTS `driver`;
CREATE TABLE `driver` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `version` varchar(100) NOT NULL COMMENT '版本号',
  `type` tinyint(4) NOT NULL COMMENT '1. chromedriver',
  `files` json NOT NULL COMMENT '各平台文件，1.windows 2.linux 3.macos',
  `device_ids` json DEFAULT NULL COMMENT 'deviceIds',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator_uid` int(11) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_type_version` (`version`,`type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='driver表';

-- Table structure for environment
-- ----------------------------
DROP TABLE IF EXISTS `environment`;
CREATE TABLE `environment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '环境名',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `project_id` int(11) NOT NULL COMMENT '项目id',
  `creator_uid` int(11) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_name_projectId` (`name`,`project_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='环境表';


-- 账号admin 密码admin
INSERT INTO `user`(`id`,`username`, `password`, `nick_name`, `status`, `create_time`) VALUES ('1', 'admin', '$2a$12$v1ERBqyxhU/ocHRPywOjvOAMkhmZGJB3hRoNjr4bWO3HLWZSIlnne', '超级管理员', 1, '2020-02-24 20:14:33');
INSERT INTO `user_role`(`user_id`, `role_id`) VALUES (1, 1);
INSERT INTO `project`(`id`, `name`, `description`, `platform`, `creator_uid`, `create_time`) VALUES (1, 'AndroidDemo', '', 1, 1, '2020-02-24 20:14:33');
INSERT INTO `user_project`(`user_id`, `project_id`) VALUES (1, 1);

INSERT INTO `role`(`id`, `name`, `alias`) VALUES (1, 'admin', '超级管理员');
INSERT INTO `role`(`id`, `name`, `alias`) VALUES (2, 'browser', '浏览器管理员');
INSERT INTO `role`(`id`, `name`, `alias`) VALUES (3, 'app', 'app管理员');
INSERT INTO `role`(`id`, `name`, `alias`) VALUES (4, 'mobile', 'mobile管理员');
INSERT INTO `role`(`id`, `name`, `alias`) VALUES (5, 'driver', 'driver管理员');
INSERT INTO `role`(`id`, `name`, `alias`) VALUES (6, 'environment', '环境管理员');
INSERT INTO `role`(`id`, `name`, `alias`) VALUES (7, 'globalVar', '全局变量管理员');
INSERT INTO `role`(`id`, `name`, `alias`) VALUES (8, 'page', 'page管理员');
INSERT INTO `role`(`id`, `name`, `alias`) VALUES (9, 'action', 'action管理员');
INSERT INTO `role`(`id`, `name`, `alias`) VALUES (10, 'testcase', '测试用例管理员');
INSERT INTO `role`(`id`, `name`, `alias`) VALUES (11, 'testPlan', '测试计划管理员');
INSERT INTO `role`(`id`, `name`, `alias`) VALUES (12, 'testTask', '测试任务管理员');
INSERT INTO `role`(`id`, `name`, `alias`) VALUES (13, 'testSuite', '测试集管理员');

INSERT INTO `category`(`id`, `name`, `type`, `create_time`, `creator_uid`) VALUES (10000, '废弃', 5, '2020-07-23 18:21:00', 1);
