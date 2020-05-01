-- 0.5.5
ALTER TABLE `page`
MODIFY COLUMN `window_orientation` varchar(11) NULL COMMENT '屏幕方向' AFTER `window_width`;

ALTER TABLE `page`
ADD COLUMN `type` tinyint(4) NOT NULL COMMENT '1.android_native 2.ios_native 3.web' AFTER `project_id`;

UPDATE page pg JOIN project pj ON pg.project_id = pj.id SET type = IF(pj.platform = 1, 1, 2)

-- 0.5.8
ALTER TABLE `project`
MODIFY COLUMN `platform` tinyint(4) NOT NULL COMMENT '1.andorid 2.iOS 3.pc web' AFTER `description`,
ADD COLUMN `capabilities` text NULL COMMENT 'org.openqa.selenium.Capabilities' AFTER `platform`;

ALTER TABLE `device_test_task`
ADD COLUMN `capabilities` text NULL COMMENT 'org.openqa.selenium.Capabilities' AFTER `platform`;
