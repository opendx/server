ALTER TABLE `action`
ADD COLUMN `set_up` json NULL COMMENT '前置步骤' AFTER `local_vars`,
ADD COLUMN `tear_down` json NULL COMMENT '后置步骤' AFTER `steps`;