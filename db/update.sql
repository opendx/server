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