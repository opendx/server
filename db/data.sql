-- ################################################################################################################################

-- -----------------------------------------------com.daxiang.action.android(1-100)-----------------------------------------------
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
	'com.daxiang.action.android.ClearApkData',
	1,
	0,
	null,
	1,
	'[{"name": "packageName", "description": "包名"}]'
);
-- 2.ExcuteAdbShellCommond
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
	'com.daxiang.action.android.ExcuteAdbShellCommond',
	1,
	1,
	'执行命令返回的结果',
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
	'1.download apk 2.install -r',
	1,
	'com.daxiang.action.android.InstallApk',
	1,
	0,
	null,
	1,
	'[{"name": "apkDownloadUrl", "description": "apk下载地址"}]'
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
	'com.daxiang.action.android.RestartApk',
	1,
	0,
	null,
	1,
	'[{"name": "packageName", "description": "包名"},{"name": "launchActivity", "description": "启动Activity名"}]'
);
-- -----------------------------------------------com.daxiang.action.android(1-100)-----------------------------------------------

-- ################################################################################################################################

-- -----------------------------------------------com.daxiang.action.common(101-200)-----------------------------------------------
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
	'com.daxiang.action.common.Sleep',
	0,
	0,
	null,
	null,
	'[{"name": "sleepTimeInSeconds", "description": "休眠时长，单位：秒"}]'
);
-- -----------------------------------------------com.daxiang.action.common(101-200)-----------------------------------------------

-- ################################################################################################################################

-- -------------------------------------------com.daxiang.action.appium.android(201-300)-------------------------------------------
-- 201.FindElementByUiautomator
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
	'通过Uiautomator查找元素',
	'AndroidDriver.findElementByAndroidUIAutomator',
	1,
	'com.daxiang.action.appium.android.FindElementByUiautomator',
	1,
	1,
	'WebElement',
	1,
	'[{"name": "uiautomator", "description": "eg. new UiSelector().text(xxx)"}]'
);
-- 202.PressKey
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
	'发送keycode到android手机执行',
	'AndroidDriver.pressKeyCode',
	1,
	'com.daxiang.action.appium.android.PressKey',
	1,
	0,
	null,
	1,
	'[{"name": "code", "description": "Android key code https://www.jianshu.com/p/f7ec856ff56f"}]'
);
-- -------------------------------------------com.daxiang.action.appium.android(201-300)-------------------------------------------

-- ################################################################################################################################

-- -----------------------------------------------com.daxiang.action.appium(301-600)-----------------------------------------------
-- 301.Click
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
	301,
	'点击',
	'WebElement.click',
	1,
	'com.daxiang.action.appium.Click',
	1,
	1,
	'WebElement',
	null,
	'[{"name": "findBy", "description": "查找方式", "possibleValues": [{"value": "id", "description": ""}, {"value": "xpath", "description": ""}]},{"name": "value", "description": "查找值"}]'
);
-- 302. ClickElement
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
  302,
	'点击元素',
	'WebElement.click',
	1,
	'com.daxiang.action.appium.ClickElement',
	0,
	1,
  'WebElement',
	null,
	'[{"name": "WebElement", "description": "元素对象"}]'
);
-- 303.FindElement
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
	303,
	'查找元素',
	'AppiumDriver.findElement',
	1,
	'com.daxiang.action.appium.FindElement',
	1,
	1,
	'WebElement',
	null,
  '[{"name": "findBy", "description": "查找方式", "possibleValues": [{"value": "id", "description": ""}, {"value": "xpath", "description": ""}]},{"name": "value", "description": "查找值"}]'
);
-- 304.SendKeys
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
  304,
	'输入',
	'WebElement.sendKeys',
	1,
	'com.daxiang.action.appium.SendKeys',
	1,
	1,
	'WebElement',
	null,
  '[{"name": "findBy", "description": "查找方式", "possibleValues": [{"value": "id", "description": ""}, {"value": "xpath", "description": ""}]},{"name": "value", "description": "查找值"},{"name": "content", "description": "输入内容"}]'
);
-- 305.SetImplicitlyWaitTime
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
  305,
  '设置隐士等待时间',
  'AppiumDriver.manage().timeouts().implicitlyWait',
  1,
  'com.daxiang.action.appium.SetImplicitlyWaitTime',
  1,
  0,
  null,
  null,
  '[{"name": "implicitlyWaitTimeInSeconds", "description": "隐士等待时间，单位：秒"}]'
);
-- 306.WaitForElementPresence
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
  306,
  '等待元素存在',
  'WebDriverWait().until(ExpectedConditions.presenceOfElementLocated)，可用于检测toast',
  1,
  'com.daxiang.action.appium.WaitForElementPresence',
  1,
  1,
  'WebElement',
  null,
  '[{"name": "findBy", "description": "查找方式", "possibleValues": [{"value": "id", "description": ""}, {"value": "xpath", "description": ""}]},{"name": "value", "description": "查找值"},{"name": "maxWaitTimeInSeconds", "description": "最大等待时间"}]'
);
-- 307.WaitForElementVisible
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
  307,
  '等待元素可见',
  'WebDriverWait().until(ExpectedConditions.visibilityOfElementLocated)',
  1,
  'com.daxiang.action.appium.WaitForElementVisible',
  1,
  1,
  'WebElement',
  null,
  '[{"name": "findBy", "description": "查找方式", "possibleValues": [{"value": "id", "description": ""}, {"value": "xpath", "description": ""}]},{"name": "value", "description": "查找值"},{"name": "maxWaitTimeInSeconds", "description": "最大等待时间"}]'
);
-- 308.ElementSendKeys
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
	308,
	'元素输入',
	'WebElement.sendKeys',
	1,
	'com.daxiang.action.appium.ElementSendKeys',
	0,
	1,
	'WebElement',
	null,
	'[{"name": "WebElement", "description": "元素对象"}, {"name": "content", "description": "输入内容"}]'
);
-- 309.FindElementByImage
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
	309,
	'通过图像查找元素',
	'AppiumDriver.findElementByImage',
	1,
	'com.daxiang.action.appium.FindElementByImage',
	1,
	1,
	'WebElement',
	null,
	'[{"name": "base64ImageTemplate", "description": "base64图片模板"}]'
);
-- -----------------------------------------------com.daxiang.action.appium(301-600)-----------------------------------------------

-- ################################################################################################################################




