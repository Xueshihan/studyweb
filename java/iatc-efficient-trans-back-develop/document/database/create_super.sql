CREATE TABLE IF NOT EXISTS t_original_power (
    id       BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    code     VARCHAR NOT NULL,
    type     VARCHAR,
    value    VARCHAR,
    created_time    TIMESTAMP(3) NOT NULL,
    PRIMARY KEY (id, created_time)
);
comment on table t_original_power is '原始数据功耗表';
comment on column t_original_power.code is '设备编号';
comment on column t_original_power.type is '数据类型';
comment on column t_original_power.value is '数据值';
select create_hypertable('t_original_power', 'created_time');

CREATE TABLE IF NOT EXISTS t_realtime_power (
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    station_id  BIGINT NOT NULL,
    phase   SMALLINT NOT NULL,
    high_current     VARCHAR,
    high_voltage     VARCHAR,
    low_current     VARCHAR,
    low_voltage     VARCHAR,
    power_factor VARCHAR,
    high_efficiency VARCHAR,
    low_efficiency  VARCHAR,
    active_power   VARCHAR,
    loss_power     VARCHAR,
    year INT NOT NULL,
    month INT NOT NULL,
    day INT NOT NULL,
    hour INT NOT NULL,
    minute INT NOT NULL,
    data_source SMALLINT NOT NULL DEFAULT 1,
    enabled_status  SMALLINT NOT NULL DEFAULT 1,
    created_time    TIMESTAMP(3) NOT NULL,
    updated_time    TIMESTAMP(3) NOT NULL DEFAULT now(),
    PRIMARY KEY (id, created_time)
);
comment on table t_realtime_power is '实时转换功耗表，根据电表15分钟一传，再转换';
comment on column t_realtime_power.station_id is '变压器id';
comment on column t_realtime_power.phase is '变压器相位A，B，C三相,1-A相，2-B相，3-C相';
comment on column t_realtime_power.high_current is '高压侧电流';
comment on column t_realtime_power.high_voltage is '高压侧电压';
comment on column t_realtime_power.low_current is '低压侧电流';
comment on column t_realtime_power.low_voltage is '低压侧电压';
comment on column t_realtime_power.power_factor is '功率因数 低压方设备才有，即电表设备才传';
comment on column t_realtime_power.data_source is '数据来源，0-虚拟，1-真实';
select create_hypertable('t_realtime_power', 'created_time');


CREATE TABLE IF NOT EXISTS t_statistic_hour_power (
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    station_id  BIGINT NOT NULL,
    phase   SMALLINT NOT NULL,
    high_efficiency VARCHAR,
    low_efficiency  VARCHAR,
    active_power   VARCHAR,
    loss_power     VARCHAR,
    year INT NOT NULL,
    month INT NOT NULL,
    day INT NOT NULL,
    hour INT NOT NULL,
    enabled_status  SMALLINT NOT NULL DEFAULT 1,
    created_time    TIMESTAMP(3) NOT NULL,
    updated_time    TIMESTAMP(3) NOT NULL DEFAULT now(),
    PRIMARY KEY (id, created_time)

);
comment on table t_statistic_hour_power is '统计每时损耗表 以一小时统计';
comment on column t_statistic_hour_power.station_id is '变压器id';
comment on column t_statistic_hour_power.phase is '变压器相位A，B，C三相，1-A相，2-B相，3-C相';
comment on column t_statistic_hour_power.high_efficiency is '高压侧有功功率 W 计算出来 累加';
comment on column t_statistic_hour_power.low_efficiency is '低压侧有功功率 W 计算出来 累加';
comment on column t_statistic_hour_power.active_power is '有功损耗 计算出来 高压侧-低压侧';
comment on column t_statistic_hour_power.high_efficiency is '无功损耗 计算出来 累加';
select create_hypertable('t_statistic_hour_power', 'created_time');


CREATE TABLE IF NOT EXISTS t_statistic_day_power (
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    station_id  BIGINT NOT NULL,
    phase   SMALLINT NOT NULL,
    high_efficiency VARCHAR,
    low_efficiency  VARCHAR,
    active_power   VARCHAR,
    loss_power     VARCHAR,
    year INT NOT NULL,
    month INT NOT NULL,
    day INT NOT NULL,
    enabled_status  SMALLINT NOT NULL DEFAULT 1,
    created_time    TIMESTAMP(3) NOT NULL,
    updated_time    TIMESTAMP(3) NOT NULL DEFAULT now(),
    PRIMARY KEY (id, created_time)
);
comment on table t_statistic_day_power is '统计每时损耗表 以一小时统计';
comment on column t_statistic_day_power.station_id is '变压器id';
comment on column t_statistic_day_power.phase is '变压器相位A，B，C三相，1-A相，2-B相，3-C相';
comment on column t_statistic_day_power.high_efficiency is '高压侧有功功率 W 累加';
comment on column t_statistic_day_power.low_efficiency is '低压侧有功功率 W 累加';
comment on column t_statistic_day_power.active_power is '有功损耗 累加';
comment on column t_statistic_day_power.high_efficiency is '无功损耗 累加';
select create_hypertable('t_statistic_day_power', 'created_time');


CREATE TABLE IF NOT EXISTS t_statistic_month_power (
    id          BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    station_id  BIGINT NOT NULL,
    phase   SMALLINT NOT NULL,
    high_efficiency VARCHAR,
    low_efficiency  VARCHAR,
    active_power   VARCHAR,
    loss_power     VARCHAR,
    year INT NOT NULL,
    month INT NOT NULL,
    day_num INT NOT NULL,
    enabled_status  SMALLINT NOT NULL DEFAULT 1,
    created_time    TIMESTAMP(3) NOT NULL,
    updated_time    TIMESTAMP(3) NOT NULL DEFAULT now(),
    PRIMARY KEY (id, created_time)
);
comment on table t_statistic_month_power is '统计每月损耗表 以月统计';
comment on column t_statistic_month_power.station_id is '变压器id';
comment on column t_statistic_month_power.phase is '变压器相位A，B，C三相，1-A相，2-B相，3-C相';
comment on column t_statistic_month_power.high_efficiency is '高压侧有功功率 W 累加';
comment on column t_statistic_month_power.low_efficiency is '低压侧有功功率 W 累加';
comment on column t_statistic_month_power.active_power is '有功损耗 累加';
comment on column t_statistic_month_power.high_efficiency is '无功损耗 累加';
comment on column t_statistic_month_power.day_num is '每个月的天数';
select create_hypertable('t_statistic_month_power', 'created_time');