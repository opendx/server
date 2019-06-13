## 环境
* java8
* mysql5.7

## 数据库
* 创建数据库（配置文件配置的数据库名为daxiang）
* 创建表（db/table.sql）
* 导入数据(data/data.sql)

## ide运行
运行src/main/java/com/daxiang/Application.java main方法即可

## 非ide打包运行
  * 打包 mvn clean package
  * 运行 java -jar server-{version}.jar --db-url={mysql ip:port/database} --db-username={mysql username} --db-password={mysql password}
  > 示例：java -jar server-0.9.0.jar --db-url=127.0.0.1:3306/daxiang --db-username=root --db-password=root

## 前端
在server-{version}.jar所处的目录下创建frontend文件夹，将前端项目编译后dist目录下的东西copy到frontend文件夹内