oldDataBase=iatc_trans
newDataBase=iatc_trans_test
user=root
dump=/root/dump.sql
container=test_timescaledb

originalPower=t_original_power
originalPowerCsv=/root/original_power.csv

realtimePower=t_realtime_power
realtimePowerCsv=/root/realtime_power.csv

statisticHourPower=t_statistic_hour_power
statisticHourPowerCsv=/root/statistic_hour_power.csv

statisticDayPower=t_statistic_day_power
statisticDayPowerCsv=/root/statistic_day_power.csv

statisticMonthPower=t_statistic_month_power
statisticMonthPowerCsv=/root/statistic_month_power.csv

createSuper=create_super.sql
createSuperPath=/root/${createSuper}

# 复制create_super.sql到docker的root目录下
docker cp ${createSuper} ${container}:/root
# 删除数据库
docker exec -i ${container} psql -U ${user} -c "DROP database ${newDataBase}"
# 创建新数据库
docker exec -i ${container} psql -U ${user} -c "create database ${newDataBase}"
# 导出数据
docker exec -i ${container} pg_dump -f ${dump} -U ${user} ${oldDataBase} --verbose  --format=c --blobs --encoding "UTF8"

# 复制超表数据
docker exec -i ${container} psql -d ${oldDataBase} -c "\COPY (SELECT * FROM ${originalPower}) TO ${originalPowerCsv} DELIMITER ',' CSV"
docker exec -i ${container} psql -d ${oldDataBase} -c "\COPY (SELECT * FROM ${realtimePower}) TO ${realtimePowerCsv} DELIMITER ',' CSV"
docker exec -i ${container} psql -d ${oldDataBase} -c "\COPY (SELECT * FROM ${statisticHourPower}) TO ${statisticHourPowerCsv} DELIMITER ',' CSV"
docker exec -i ${container} psql -d ${oldDataBase} -c "\COPY (SELECT * FROM ${statisticDayPower}) TO ${statisticDayPowerCsv} DELIMITER ',' CSV"
docker exec -i ${container} psql -d ${oldDataBase} -c "\COPY (SELECT * FROM ${statisticMonthPower}) TO ${statisticMonthPowerCsv} DELIMITER ',' CSV"

# 导入表结构
docker exec -i ${container} pg_restore -s -d ${newDataBase} --verbose ${dump}
# 导入数据
docker exec -i ${container} pg_restore -a -d ${newDataBase} --verbose ${dump}

# 删除导出的超表
docker exec -i ${container} psql -d ${newDataBase} -c "DROP table ${originalPower} CASCADE"
docker exec -i ${container} psql -d ${newDataBase} -c "DROP table ${realtimePower} CASCADE"
docker exec -i ${container} psql -d ${newDataBase} -c "DROP table ${statisticHourPower} CASCADE"
docker exec -i ${container} psql -d ${newDataBase} -c "DROP table ${statisticDayPower} CASCADE"
docker exec -i ${container} psql -d ${newDataBase} -c "DROP table ${statisticMonthPower} CASCADE"

# 建立新超表
docker exec -i  ${container} psql -d ${newDataBase} -c "\i ${createSuperPath}"

# 恢复超表数据
docker exec -i ${container} psql -d ${newDataBase} -c "\COPY ${originalPower} FROM ${originalPowerCsv} CSV"
# 由于copy后，id不会改变，需重新设置一下id
docker exec -i ${container} psql -d ${newDataBase} -c "SELECT SETVAL('${originalPower}_id_seq', (SELECT max(id) + 1 FROM ${originalPower}))"

docker exec -i ${container} psql -d ${newDataBase} -c "\COPY ${realtimePower} FROM ${realtimePowerCsv} CSV"
# 由于copy后，id不会改变，需重新设置一下id
docker exec -i ${container} psql -d ${newDataBase} -c "SELECT SETVAL('${realtimePower}_id_seq', (SELECT max(id) + 1 FROM ${realtimePower}))"

docker exec -i ${container} psql -d ${newDataBase} -c "\COPY ${statisticHourPower} FROM ${statisticHourPowerCsv} CSV"
# 由于copy后，id不会改变，需重新设置一下id
docker exec -i ${container} psql -d ${newDataBase} -c "SELECT SETVAL('${statisticHourPower}_id_seq', (SELECT max(id) + 1 FROM ${statisticHourPower}))"

docker exec -i ${container} psql -d ${newDataBase} -c "\COPY ${statisticDayPower} FROM ${statisticDayPowerCsv} CSV"
# 由于copy后，id不会改变，需重新设置一下id
docker exec -i ${container} psql -d ${newDataBase} -c "SELECT SETVAL('${statisticDayPower}_id_seq', (SELECT max(id) + 1 FROM ${statisticDayPower}))"

docker exec -i ${container} psql -d ${newDataBase} -c "\COPY ${statisticMonthPower} FROM ${statisticMonthPowerCsv} CSV"
# 由于copy后，id不会改变，需重新设置一下id
docker exec -i ${container} psql -d ${newDataBase} -c "SELECT SETVAL('${statisticMonthPower}_id_seq', (SELECT max(id) + 1 FROM ${statisticMonthPower}))"



