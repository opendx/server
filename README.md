## 环境
* java8
* mysql5.7
* windows || linux || mac

## 打包
mvn clean package

## 运行
java -jar server-{version}.jar --db-url={mysql ip:port/database} --db-username={mysql username} --db-password={mysql password}
> 示例：java -jar server-0.9.0.jar --db-url=127.0.0.1:3306/daxiang --db-username=root --db-password=root