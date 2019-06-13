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
		'点击',
		'点击元素',
		1,
		'com.daxiang.actions.macaca.Click',
		1,
		1,
		'Element对象',
		1,
		'[{"name": "findBy", "description": "查找方式", "possibleValues": [{"value": "id", "description": "[uiautomator2] By.res"}, {"value": "xpath", "description": "通过xpath查找元素"}, {"value": "class name", "description": "[uiautomator2] By.clazz"}, {"value": "name", "description": "[uiautomator2] By.desc or By.text"}, {"value": "text contains", "description": "[uiautomator2] By.textContains"}, {"value": "desc contains", "description": "[uiautomator2] By.descContains"}]}, {"name": "value", "description": "查找值"}]'
	);

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
		'点击元素',
		'点击元素',
		1,
		'com.daxiang.actions.macaca.ClickElement',
		1,
		1,
		'Element对象',
		1,
		'[{"name": "element", "description": "element对象"}]'
	);