# SQL Light Rail

## 简介



### 介绍

SQL Light Rail (SQL 轻轨)

1. class SqlLightRail
2. class ShardingKey = sharding 计算
3. class RepositoryManager. shardingDB 管理.

License 为 Apache2.0

## Download

### Maven

```xml

<dependency>
    <groupId>com.neko233</groupId>
    <artifactId>sql-light-rail</artifactId>
    <version>1.0.1</version>
</dependency>

```

### Gradle

```groovy
implementation group: 'com.neko233', name: 'sql-light-rail', version: '1.0.1'
```

## 初衷 / 痛点

因 mybatis-plus/flux 等好用的 ORM 框架, 都依赖于 mybatis. 

但一旦离开了 mybatis 生态圈, 很多好用的机制不能拿出来独立使用. 

1. 独立的 SQL stream 写法, 直接生成 sql 语句. 无需 mybatis-plus 重量级依赖.
2. 独立的 ORM 机制.
3. 独立的 Sharding DB 机制
4. Plugin Chain 处理 SQL 执行过程.


# Use
Dependency
maven
```xml
<!-- default use druid as DataSource -->
<dependency>
    <groupId>com.neko233</groupId>
    <artifactId>sql-light-rail</artifactId>
    <version>1.0.1</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.25</version>
</dependency>
```

Java
```java

public static DataSource configDbDataSource() throws Exception {
        Properties properties = new Properties();
        properties.put(PROP_URL, "jdbc:mysql://localhost:3306/sql_light_rail");
        properties.put(PROP_USERNAME, "root");
        properties.put(PROP_PASSWORD, "root");
        properties.put(PROP_INITIALSIZE, "5");
        properties.put(PROP_MINIDLE, "5");
        properties.put(PROP_MAXACTIVE, "10");
        properties.put(PROP_MAXWAIT, "10000");
        return createDataSource(properties);
}

/**
 * How to use multi DataSource
 *
 * @throws Exception 异常
 */
@Test
public void testInit() throws Exception {

        // auto init config | see 'DDL-for-manager.sql'
        Db configDb = new Db(configDbDataSource());
        new RepositoryManagerInitializerByMysql().initDbGroup(configDb, "template");

        // use 
        Db db = RepositoryManager.instance
        .getDbGroup("template")
        .getDb(0L);

        System.out.println(db.getDbName());

        // check
        Integer number1 = db.executeQuerySingle("select 1 from dual", Integer.class);
        System.out.println(number1);
}

```


