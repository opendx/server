-- 0.5.5
ALTER TABLE `page`
MODIFY COLUMN `window_orientation` varchar(11) NULL COMMENT '屏幕方向' AFTER `window_width`;

ALTER TABLE `page`
ADD COLUMN `type` tinyint(4) NOT NULL COMMENT '1.android_native 2.ios_native 3.web' AFTER `project_id`;

UPDATE page pg JOIN project pj ON pg.project_id = pj.id SET type = IF(pj.platform = 1, 1, 2)

-- 0.5.8
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

ALTER TABLE `project`
MODIFY COLUMN `platform` tinyint(4) NOT NULL COMMENT '1.andorid 2.iOS 3.pc web' AFTER `description`,
ADD COLUMN `capabilities` text NULL COMMENT 'org.openqa.selenium.Capabilities' AFTER `platform`;

ALTER TABLE `device_test_task`
ADD COLUMN `capabilities` text NULL COMMENT 'org.openqa.selenium.Capabilities' AFTER `platform`;

ALTER TABLE `device` DROP COLUMN `last_offline_time`;

delete from `action` where id < 10000 -- 重新导入基础action https://github.com/opendx/agent/tree/master/src/main/java/com/daxiang/action/action.sql

-- 0.6.5
RENAME TABLE `device` TO `mobile`;
ALTER TABLE `mobile` ADD COLUMN `emulator` tinyint(4) NULL COMMENT '0: 真机 1: 模拟器' AFTER `name`;

update role set name='browser',alias='浏览器管理员' where id = 2;
update role set name='mobile',alias='mobile管理员' where id = 4;

ALTER TABLE `device_test_task`
ADD COLUMN `device` json NULL COMMENT '下发任务时的device' AFTER `device_id`;

-- 0.7.0
ALTER TABLE `category`
ADD COLUMN `parent_id` int(11) NOT NULL DEFAULT 0 COMMENT '父id' AFTER `id`;
