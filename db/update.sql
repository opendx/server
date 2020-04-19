-- 0.5.3
ALTER TABLE `page`
MODIFY COLUMN `window_orientation` varchar(11) NULL COMMENT '屏幕方向' AFTER `window_width`;