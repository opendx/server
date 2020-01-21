-- 0.2.2 -> 0.2.3
ALTER TABLE test_plan ADD `cron_expression` varchar(255) DEFAULT NULL COMMENT 'cron表达式';
ALTER TABLE test_plan ADD `enable_schedule` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否开启定时任务，0: 关闭 1: 开启';
-- 0.2.3 -> 0.2.4
ALTER TABLE device_test_task ADD `code` mediumtext COMMENT 'agent转换后的代码';
ALTER TABLE device_test_task ADD `err_msg` text COMMENT 'status: -1, 错误信息';
-- 0.2.7
ALTER TABLE action ADD COLUMN `state` tinyint(4) NOT NULL DEFAULT 2 COMMENT '禁用: 0  草稿: 1  发布: 2' AFTER `test_suite_id`;

DELIMITER $$
CREATE PROCEDURE handle_action_steps_status()
  BEGIN
    DECLARE done int default FALSE;
    DECLARE action_id int;
    DECLARE steps_length int;

    DECLARE cur CURSOR FOR SELECT id, JSON_LENGTH(steps) from action where steps is not null;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cur;
    repeat
      FETCH cur into action_id,steps_length;
      while steps_length > 0 do
        set steps_length = steps_length - 1;
        update action set steps = JSON_INSERT(steps,REPLACE('$[i].status','i',steps_length), 1) where id = action_id;
      end while;
    until done
    end repeat;
    CLOSE cur;
  END;
$$
DELIMITER ;
CALL handle_action_steps_status(); -- 每个action step加上status: 1
DROP PROCEDURE handle_action_steps_status;
-- 0.2.8
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

ALTER TABLE global_var ADD COLUMN `environment_values` json NOT NULL COMMENT '变量值' AFTER `type`;

DELIMITER $$
CREATE PROCEDURE handle_global_var_value()
  BEGIN
    DECLARE done int default FALSE;
    DECLARE global_var_id int;
    DECLARE global_var_value varchar(255);

    DECLARE cur CURSOR FOR SELECT id, value from global_var;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cur;
    repeat
      FETCH cur into global_var_id, global_var_value;
      update global_var set environment_values = JSON_ARRAY(JSON_OBJECT("environmentId", -1, "value", global_var_value)) where id = global_var_id;
    until done
    end repeat;
    CLOSE cur;
  END;
$$
DELIMITER ;
CALL handle_global_var_value(); -- global_var.value -> environment_values
DROP PROCEDURE handle_global_var_value;

ALTER TABLE global_var DROP COLUMN `value`;

DELIMITER $$
CREATE PROCEDURE handle_action_local_vars_value()
  BEGIN
    DECLARE done int default FALSE;
    DECLARE action_id int;
    DECLARE local_vars_length int;

    DECLARE cur CURSOR FOR select id, JSON_LENGTH(local_vars) from action where JSON_LENGTH(local_vars) > 0;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN cur;
    repeat
      FETCH cur into action_id, local_vars_length;
      while local_vars_length > 0 do
        set local_vars_length = local_vars_length - 1;
        update action set local_vars = JSON_INSERT(local_vars, REPLACE('$[!].environmentValues','!',local_vars_length),
                                                   JSON_ARRAY(JSON_OBJECT("environmentId", -1, "value", JSON_EXTRACT(local_vars, REPLACE('$[!].value','!',local_vars_length))))) where id = action_id;
        update action set local_vars = JSON_REMOVE(local_vars, REPLACE('$[!].value','!',local_vars_length)) where id = action_id;
      end while;
    until done
    end repeat;
    CLOSE cur;
  END;
$$
DELIMITER ;
CALL handle_action_local_vars_value(); -- action.local_vars.value -> environment_values
DROP PROCEDURE handle_action_local_vars_value;

ALTER TABLE test_plan
ADD COLUMN `environment_id` int(11) NOT NULL DEFAULT -1 COMMENT '环境，默认-1' AFTER `project_id`,
ADD COLUMN `enable_record_video` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否录制视频，0: 不录制 1: 录制' AFTER `enable_schedule`,
ADD COLUMN `fail_retry_count` int(11) NOT NULL DEFAULT 0 COMMENT '失败重试次数' AFTER `enable_record_video`;

ALTER TABLE test_task
DROP COLUMN `test_plan_name`,
ADD COLUMN `test_plan` json NOT NULL COMMENT '下发任务时的testplan' AFTER `test_plan_id`;

ALTER TABLE device_test_task
ADD COLUMN `test_plan` json NOT NULL COMMENT '下发任务时的testplan' AFTER `test_task_id`;

-- 0.2.9
ALTER TABLE page ADD COLUMN `elements` json NULL COMMENT '元素' AFTER `device_id`;
UPDATE page SET elements = '[]';
ALTER TABLE device_test_task
ADD COLUMN `pages` json NULL COMMENT 'pages' AFTER `global_vars`;

-- 0.3.0
ALTER TABLE device_test_task ADD COLUMN `platform` tinyint(4) NOT NULL COMMENT '平台' AFTER `project_id`;

DELETE FROM action WHERE id <= 20; -- 注意！！！ 重新导入基础action https://github.com/opendx/agent/blob/master/src/main/java/com/daxiang/action/sql/basic_action.sql

ALTER TABLE action ADD COLUMN `platforms` json null COMMENT '1.android 2.ios 3.android微信web 4.android微信小程序 null.通用' AFTER `update_time`;

UPDATE action SET platforms = REPLACE('[p]','p',platform) where type in (2,3);

ALTER TABLE action DROP COLUMN `platform`;

-- 0.3.1
ALTER TABLE action ADD COLUMN `depends` json null COMMENT '依赖的测试用例id' AFTER `state`;
DELETE FROM action WHERE id <= 23; -- 注意！！！ 重新导入基础action https://github.com/opendx/agent/blob/master/src/main/java/com/daxiang/action/sql/basic_action.sql

-- 0.3.2
ALTER TABLE page
CHANGE COLUMN `img_height` `window_height` int(11) NULL DEFAULT NULL COMMENT 'window高度' AFTER `img_url`,
CHANGE COLUMN `img_width` `window_width` int(11) NULL DEFAULT NULL COMMENT 'window宽度' AFTER `window_height`,
ADD COLUMN `window_orientation` varchar(11) DEFAULT 'portrait' COMMENT '屏幕方向' AFTER `window_width`;

DELETE FROM action WHERE id <= 23; -- 注意！！！ 重新导入基础action https://github.com/opendx/agent/blob/master/src/main/java/com/daxiang/action/sql/basic_action.sql

-- 0.3.3
ALTER TABLE action
ADD COLUMN `action_imports` json NULL COMMENT 'action imports' AFTER `java_imports`;
