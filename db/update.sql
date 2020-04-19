-- 0.5.3
ALTER TABLE `page`
MODIFY COLUMN `window_orientation` varchar(11) NULL COMMENT '屏幕方向' AFTER `window_width`;

ALTER TABLE `page`
ADD COLUMN `type` tinyint(4) NOT NULL COMMENT '1.android_native 2.ios_native 3.web' AFTER `project_id`;