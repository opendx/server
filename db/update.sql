-- 0.2.2 -> 0.2.3
ALTER TABLE test_plan ADD `cron_expression` varchar(255) DEFAULT NULL COMMENT 'cron表达式';
ALTER TABLE test_plan ADD `enable_schedule` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否开启定时任务，0: 关闭 1: 开启';
-- 0.2.3 -> 0.2.4
ALTER TABLE device_test_task ADD `code` mediumtext COMMENT 'agent转换后的代码';
ALTER TABLE device_test_task ADD `err_msg` text COMMENT 'status: -1, 错误信息';