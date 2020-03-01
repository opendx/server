## 环境
* java8
* mysql5.7

## 数据库
* 数据库（db/database.sql）
* 表（db/table.sql）
* 初始数据（db/data.sql, https://github.com/opendx/agent/blob/master/src/main/java/com/daxiang/action/sql/basic_action.sql）

## ide运行
运行src/main/java/com/daxiang/Application.java main方法即可

## 非ide打包运行
  * 打包 mvn clean package
  * 运行 java -jar server-{version}.jar --db-url={mysql ip:port/database} --db-username={mysql username} --db-password={mysql password}
  > 示例：java -jar server-0.9.0.jar --db-url=127.0.0.1:3306/daxiang --db-username=root --db-password=root
