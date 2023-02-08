# 执行步骤

## 1.准备工作

```
1.在服务器上新建文件夹  mkdir efficient_trans

2.将Dockerfile和docker-compose.yml放入efficient_trans文件夹下

3.将配置文件放入efficient_trans/config下

4.给数据库创建网段 docker network create iatc_network
```

## 2.创建数据库及表 -postgresql

```
执行create_sql.sql 文件

1、将该文件传到postgresql容器中的/root目录下，docker cp create_sql.sql 容器id:/root/
2、进入容器后， psql -U root 进入数据库
3、\i /root/create_sql.sql
```

## 3.指定超表

```
创建超表，不能有数据，同时超表中列名也得是唯一或联合唯一

select create_hypertable(表名, 列名);

如：

select create_hypertable('company', 'time');

注：
1.无索引条件
2.若表中有主键的话，可以设置联合主键，包含时间字段
3.
```

## 4.执行代码