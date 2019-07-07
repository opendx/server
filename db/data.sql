-- actions.android------------------------start(1-100)------------------------
-- 1.ClearApkData
INSERT INTO `action` (
	`id`,
	`name`,
	`description`,
	`type`,
	`class_name`,
	`need_driver`,
	`has_return_value`,
	`return_value_desc`,
	`platform`,
	`params`
)
VALUES
(
	1,
	'清除APK数据',
	'pm clear {packageName}',
	1,
	'com.daxiang.actions.android.ClearApkData',
	1,
	0,
	null,
	1,
	'[{"name": "packageName", "description": "包名"}]'
);
-- 2.ExcuteAdbShellCmd

INSERT INTO `action` (
	`id`,
	`name`,
	`description`,
	`type`,
	`class_name`,
	`need_driver`,
	`has_return_value`,
	`return_value_desc`,
	`platform`,
	`params`
)
VALUES
(
	2,
	'adb shell',
	'adb shell {cmd}',
	1,
	'com.daxiang.actions.android.ExcuteAdbShellCmd',
	1,
	1,
	'执行命令输出的内容',
	1,
	'[{"name": "cmd", "description": "执行的命令"}]'
);
-- 3.InstallApk

INSERT INTO `action` (
	`id`,
	`name`,
	`description`,
	`type`,
	`class_name`,
	`need_driver`,
	`has_return_value`,
	`return_value_desc`,
	`platform`,
	`params`
)
VALUES
(
	3,
	'安装Apk',
	'若已安装则先卸载，再安装。1.download apk 2.push apk to device 3.uninstall apk if apk has installed 4.pm install -r {apk}',
	1,
	'com.daxiang.actions.android.InstallApk',
	1,
	0,
	null,
	1,
	'[{"name": "apkDownloadUrl", "description": "apk下载地址"},{"name": "packageName", "description": "包名"}]'
);

-- 4.RestartApk
INSERT INTO `action` (
	`id`,
	`name`,
	`description`,
	`type`,
	`class_name`,
	`need_driver`,
	`has_return_value`,
	`return_value_desc`,
	`platform`,
	`params`
)
VALUES
(
	4,
	'启动/重启 Apk',
	'am start -S -n {packageName}/{launchActivity}',
	1,
	'com.daxiang.actions.android.RestartApk',
	1,
	0,
	null,
	1,
	'[{"name": "packageName", "description": "包名"},{"name": "launchActivity", "description": "启动Activity"}]'
);
-- actions.android------------------------end(1-100)------------------------

-- --------------------------------分割线-----------------------------------

-- actions.common------------------------start(101-200)------------------------
-- 101.Sleep
INSERT INTO `action` (
	`id`,
	`name`,
	`description`,
	`type`,
	`class_name`,
	`need_driver`,
	`has_return_value`,
	`return_value_desc`,
	`platform`,
	`params`
)
VALUES
(
	101,
	'休眠',
	'Thread.sleep',
	1,
	'com.daxiang.actions.common.Sleep',
	0,
	0,
	null,
	1,
	'[{"name": "sleep_time", "description": "休眠时长，单位：秒"}]'
);
-- actions.common------------------------end(101-200)------------------------

-- --------------------------------分割线-----------------------------------

-- actions.macaca------------------------start(201-300)------------------------
-- 201.CheckToast
INSERT INTO `action` (
	`id`,
	`name`,
	`description`,
	`type`,
	`class_name`,
	`need_driver`,
	`has_return_value`,
	`return_value_desc`,
	`platform`,
	`params`
)
VALUES
(
	201,
	'检查Toast',
	'检查最近弹出的toast',
	1,
	'com.daxiang.actions.macaca.CheckToast',
	1,
	0,
	null,
	1,
	'[{"name": "toast", "description": "toast内容"},{"name": "check_time", "description": "检查时间，单位：秒"}]'
);

-- 202.ClearText
INSERT INTO `action` (
	`id`,
	`name`,
	`description`,
	`type`,
	`class_name`,
	`need_driver`,
	`has_return_value`,
	`return_value_desc`,
	`platform`,
	`params`
)
VALUES
(
	202,
	'清除Text',
	'清除Text',
	1,
	'com.daxiang.actions.macaca.ClearText',
	1,
	1,
	'Element对象',
	1,
	'[{"name": "findBy", "description": "查找方式", "possibleValues": [{"value": "id", "description": "[uiautomator2] By.res"}, {"value": "xpath", "description": "通过xpath查找元素"}, {"value": "class name", "description": "[uiautomator2] By.clazz"}, {"value": "name", "description": "[uiautomator2] By.desc or By.text"}, {"value": "text contains", "description": "[uiautomator2] By.textContains"}, {"value": "desc contains", "description": "[uiautomator2] By.descContains"}]}, {"name": "value", "description": "查找值"}]'
);

-- 203.Click
INSERT INTO `action` (
	`id`,
	`name`,
	`description`,
	`type`,
	`class_name`,
	`need_driver`,
	`has_return_value`,
	`return_value_desc`,
	`platform`,
	`params`
)
VALUES
(
	203,
	'点击',
	'点击',
	1,
	'com.daxiang.actions.macaca.Click',
	1,
	1,
	'Element对象',
	1,
	'[{"name": "findBy", "description": "查找方式", "possibleValues": [{"value": "id", "description": "[uiautomator2] By.res"}, {"value": "xpath", "description": "通过xpath查找元素"}, {"value": "class name", "description": "[uiautomator2] By.clazz"}, {"value": "name", "description": "[uiautomator2] By.desc or By.text"}, {"value": "text contains", "description": "[uiautomator2] By.textContains"}, {"value": "desc contains", "description": "[uiautomator2] By.descContains"}]}, {"name": "value", "description": "查找值"}]'
);

-- 204. Keys
INSERT INTO `action` (
	`id`,
	`name`,
	`description`,
	`type`,
	`class_name`,
	`need_driver`,
	`has_return_value`,
	`return_value_desc`,
	`platform`,
	`params`
)
VALUES
(
	204,
	'Keys',
	'driver.keys({key})',
	1,
	'com.daxiang.actions.macaca.Keys',
	1,
	0,
	null,
	1,
	'[{"name": "key", "description": "4：返回  3：Home  更多请见https://github.com/alibaba/macaca/issues/487"}]'
);

-- 205.SendKeys
INSERT INTO `action` (
	`id`,
	`name`,
	`description`,
	`type`,
	`class_name`,
	`need_driver`,
	`has_return_value`,
	`return_value_desc`,
	`platform`,
	`params`
)
VALUES
(
	205,
	'输入',
	'输入',
	1,
	'com.daxiang.actions.macaca.SendKeys',
	1,
	1,
	'Element对象',
	1,
	'[{"name": "findBy", "description": "查找方式", "possibleValues": [{"value": "id", "description": "[uiautomator2] By.res"}, {"value": "xpath", "description": "通过xpath查找元素"}, {"value": "class name", "description": "[uiautomator2] By.clazz"}, {"value": "name", "description": "[uiautomator2] By.desc or By.text"}, {"value": "text contains", "description": "[uiautomator2] By.textContains"}, {"value": "desc contains", "description": "[uiautomator2] By.descContains"}]}, {"name": "value", "description": "查找值"}, {"name": "content", "description": "输入内容"}]'
);

-- 206.WaitForElement
INSERT INTO `action` (
	`id`,
	`name`,
	`description`,
	`type`,
	`class_name`,
	`need_driver`,
	`has_return_value`,
	`return_value_desc`,
	`platform`,
	`params`
)
VALUES
(
	206,
	'等待元素出现',
	'等待元素出现',
	1,
	'com.daxiang.actions.macaca.SendKeys',
	1,
	1,
	'Element对象',
	1,
	'[{"name": "findBy", "description": "查找方式", "possibleValues": [{"value": "id", "description": "[uiautomator2] By.res"}, {"value": "xpath", "description": "通过xpath查找元素"}, {"value": "class name", "description": "[uiautomator2] By.clazz"}, {"value": "name", "description": "[uiautomator2] By.desc or By.text"}, {"value": "text contains", "description": "[uiautomator2] By.textContains"}, {"value": "desc contains", "description": "[uiautomator2] By.descContains"}]}, {"name": "value", "description": "查找值"}, {"name": "wait_time", "description": "最大等待时间，单位：秒"}]'
);
-- actions.macaca------------------------end(201-300)------------------------



