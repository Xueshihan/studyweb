## 数据库迁移

### 0.导入表结构
```
将create_super.sql 放到容器docker下
```
### 1. 导出表信息及数据

* a.导出表
```
docker exec -i {容器名} pg_dump  -U {用户名} {数据库名} -f {文件名}

docker exec -i test_timescaledb pg_dump -f /root/new2.sql -U root iatc_trans --verbose  --format=c --blobs --encoding "UTF8"
```

* b.导出数据
```
docker exec -i {容器名} psql -d {数据库}  -c "\COPY (SELECT * FROM {产生数据的表}) TO {导出的数据文件} DELIMITER ',' CSV"

docker exec -i test_timescaledb psql -d iatc_trans -c "\COPY (SELECT * FROM t_original_power) TO /root/data.csv DELIMITER ',' CSV"
```

### 2.创建数据库
```
docker exec -i {容器名} psql -U {用户名} -c "create database {要创建的库}"

docker exec -i test_timescaledb psql -U root -c "create database iatc_trans_test"
```

### 3.恢复数据表结构信息
```
docker exec -i {容器名} pg_restore -s -d {数据库} --verbose {导出文件名}

docker exec -i test_timescaledb pg_restore -s -d iatc_trans_test --verbose /root/new1.sql
```

### 4.恢复数据表数据信息
```
docker exec -i {容器名} pg_restore -a -d {数据库} --verbose {导出文件名}

docker exec -i test_timescaledb pg_restore -a -d iatc_trans_test --verbose /root/new1.sql
```

### 5.删除超表
```
docker exec -i {容器名} psql -U {用户名} -d {数据库名} -c "DROP table {要删除的表} CASCADE"

docker exec -i test_timescaledb psql -d iatc_trans_test -c "DROP table t_original_power CASCADE"
```

### 6.建立超表
```
docker exec -i  {容器名} psql -d {数据库名} -c "\i {创建的超表文件}"
docker exec -i  test_timescaledb psql -d iatc_trans_test -c "\i /root/create_super.sql"
```

### 7.导入超表数据
```
docker exec -i {容器名} psql -d {数据库名} -c "\COPY {表名} FROM {csv数据文件} CSV"

docker exec -i test_timescaledb psql -d iatc_trans_test -c "\COPY t_original_power FROM /root/data.csv CSV"
```