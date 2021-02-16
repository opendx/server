DROP TABLE IF EXISTS `agent_ext_jar`;
CREATE TABLE `agent_ext_jar` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT 'jar name',
  `version` varchar(20) NOT NULL COMMENT '版本',
  `md5` varchar(255) NOT NULL COMMENT 'jar文件 md5',
  `file_path` varchar(255) NOT NULL COMMENT '服务端保存的文件路径',
  `file_size` bigint(20) NOT NULL COMMENT '文件大小',
  `upload_time` datetime NOT NULL COMMENT '上传时间',
  `uploador_uid` int(11) NOT NULL COMMENT '上传人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_name_version` (`name`, `version`),
  UNIQUE KEY `uniq_md5` (`md5`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='agent ext jar表';