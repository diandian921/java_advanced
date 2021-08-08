### 第七周作业

## 作业一
>按自己设计的表结构，插入 100 万订单模拟数据，测试不同方式的插入效率

**为了保证测试的准确性，尽量排除一些干扰因素，order_info表除主键外没有其他索引，每种测试开始前都会将数据清除**

本次采用了三种方式进行测试，分别是：
1. 使用存储过程（initData）一条一条插入，一个事务
2. 使用存储过程（initData_1）一条一条插入，一条一个事务
3. 将1或2的结果使用 mysqldump 导出后，再使用source导入 ,导出格式为insert 多个values
```
mysqldump -hlocalhost -P3306 -uroot --add-locks=0 --no-create-info --single-transaction --set-grid-purged=OFF shopping --result-file=/Users/wuweihua/Desktop/order.sql
```
```
source /Users/wuweihua/Desktop/order.sql;
```
测试结果速度从快到慢依次为：3>1>2
- 方法1花费时间约为 35.51s
- 方法2花费时间为 7min 55.07s  最慢
- 方法3花费时间约22s 最快

![alt 测试结果](1.png)


以下是存储过程的创建
```sql
DROP PROCEDURE if exists initData;
DELIMITER $
CREATE PROCEDURE initData()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE now TIMESTAMP DEFAULT now();
    SET AUTOCOMMIT = 0;
    START TRANSACTION;
    WHILE i <= 1000000
        DO
            insert into order_info (id, order_no, user_info_id, booking_time, pay_time, shipping_time, receive_time,
                                    order_status, order_amount, discount_amount, shipping_amount, payment_amount,
                                    shipping_user, province, city, district, address, payment_method, invoice_title,
                                    order_point, shipping_comp_name, shipping_no, gmt_create, gmt_modify)
            values (i, '8888888888', i, now, now, now, now, 0, 100, 0, 0, 100, 'Tom', 1, 1, 1, 'abc', 1, 'Tom', 100,
                    'SF', '12345678', now, now);
            SET i = i + 1;
        END WHILE;
    COMMIT;
END $



DROP PROCEDURE IF EXISTS initData_1;
DELIMITER $
CREATE PROCEDURE initData_1()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE now TIMESTAMP DEFAULT now();
    WHILE i <= 1000000
        DO
            insert into order_info (id, order_no, user_info_id, booking_time, pay_time, shipping_time, receive_time,
                                    order_status, order_amount, discount_amount, shipping_amount, payment_amount,
                                    shipping_user, province, city, district, address, payment_method, invoice_title,
                                    order_point, shipping_comp_name, shipping_no, gmt_create, gmt_modify)
            values (i, '8888888888', i, now, now, now, now, 0, 100, 0, 0, 100, 'Tom', 1, 1, 1, 'abc', 1, 'Tom', 100,
                    'SF', '12345678', now, now);
            SET i = i + 1;
        END WHILE;
END $
```

## 作业二
>读写分离 - 动态切换数据源版本 1.0

### 解决思路
1. 借助spring提供的抽象类AbstractRoutingDataSource，通过继承抽象类，并覆盖determineCurrentLookupKey方法。
2. 增加Aspect环绕服务，在业务方法执行前设置好数据源本地线程变量、执行之后在finally中清理掉数据源本地线程变量。
3. 每次获取的时候，若数据源本地线程变量为空，则默认走主dataSource。

代码：
    https://github.com/diandian921/java_advanced/tree/main/week07/homework2/src/main/java

## 作业三
>读写分离 - 数据库框架版本 2.0

Sharding-Sphere是一套开源的分布式数据库中间件解决方案组成的生态圈，它由Sharding-JDBC、Sharding-Proxy和Sharding-Sidecar这3款产品组成。3款产品提供标准化的数据分片、读写分离、柔性事务和数据治理功能，可适用于如Java同构、异构语言、容器、云原生等各种多样化的应用场景。
我们这里主要使用sharding-jdbc来实现读写分离，当然框架还支持分库分表，我们也会根据业务需要增加分表功能的使用。
sharding-jdbc的使用十分便捷，基本上没有代码侵入，直接在springboot中进行配置即可。

创建一个maven 工程，pom文件如下
```sql
<parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.5.RELEASE</version>
        <relativePath/>
    </parent>
    <groupId>sharding-master-slave</groupId>
    <artifactId>sharding-master-slave</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>spring-boot-sharding-table</name>
    <description>基于 Spring Boot 2.1.5 使用sharding-sphere + JdbcTemplate 实现读写分离</description>
 
    <properties>
        <java.version>1.8</java.version>
    </properties>
 
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>io.shardingsphere</groupId>
            <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
            <version>3.1.0</version>
        </dependency>
        <dependency>
            <groupId>io.shardingsphere</groupId>
            <artifactId>sharding-jdbc-spring-namespace</artifactId>
            <version>3.1.0</version>
        </dependency>
    </dependencies>
```


配置文件
```
# 配置真实数据源
sharding.jdbc.datasource.names=master,slave
#主数据库
sharding.jdbc.datasource.master.type=com.zaxxer.hikari.HikariDataSource
sharding.jdbc.datasource.master.hikari.driver-class-name=com.mysql.cj.jdbc.Driver
sharding.jdbc.datasource.master.jdbc-url=jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true
sharding.jdbc.datasource.master.username=root
sharding.jdbc.datasource.master.password=root
# 从数据库
sharding.jdbc.datasource.slave.type=com.zaxxer.hikari.HikariDataSource
sharding.jdbc.datasource.slave.hikari.driver-class-name=com.mysql.cj.jdbc.Driver
sharding.jdbc.datasource.slave.jdbc-url=jdbc:mysql://localhost:3307/test?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true
sharding.jdbc.datasource.slave.username=root
sharding.jdbc.datasource.slave.password=root
# 配置读写分离
# 配置从库选择策略，提供轮询与随机，这里选择用轮询
sharding.jdbc.config.masterslave.load-balance-algorithm-type=round_robin
sharding.jdbc.config.masterslave.name=ms
sharding.jdbc.config.masterslave.master-data-source-name=master
sharding.jdbc.config.masterslave.slave-data-source-names=slave
# 开启SQL显示，默认值: false，注意：仅配置读写分离时不会打印日志
sharding.jdbc.config.props.sql.show=true
spring.main.allow-bean-definition-overriding=true
```
    