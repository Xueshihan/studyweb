CREATE DATABASE iatc_trans;

\c iatc_trans;

CREATE TABLE IF NOT EXISTS t_menu (
   id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
   title      VARCHAR NOT NULL,
   name       VARCHAR NOT NULL,
   type        INT,
   path        VARCHAR,
   icon        VARCHAR,
   upper_id    BIGINT,
   upper_id_set   VARCHAR,
   enabled_status  SMALLINT NOT NULL DEFAULT 1,
   created_time    TIMESTAMP(3) NOT NULL DEFAULT now(),
   updated_time    TIMESTAMP(3) NOT NULL DEFAULT now()
);
comment on table t_menu is '菜单表';
comment on column t_menu.title is 'title，汉语';
comment on column t_menu.name is '名字，英文';
comment on column t_menu.type is '类型';
comment on column t_menu.path is '前端路径';
comment on column t_menu.icon is '前端图标';
comment on column t_menu.upper_id is '父id';

CREATE TABLE IF NOT EXISTS t_role (
   id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
   name        VARCHAR NOT NULL,
   type        INT,
   remark        VARCHAR NOT NULL,
   enabled_status  SMALLINT NOT NULL DEFAULT 1,
   created_time    TIMESTAMP(3) NOT NULL DEFAULT now(),
   updated_time    TIMESTAMP(3) NOT NULL DEFAULT now()
);
comment on table t_role is '角色表';
comment on column t_role.name is '角色名字';
comment on column t_role.type is '角色类型';
comment on column t_role.remark is '备注';

CREATE TABLE IF NOT EXISTS t_role_menu (
   id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
   role_id        BIGINT NOT NULL,
   menu_id        BIGINT NOT NULL
);
comment on table t_role_menu is '角色菜单关联表';
comment on column t_role_menu.role_id is '角色id';
comment on column t_role_menu.menu_id is '菜单id';
create index IF NOT EXISTS t_role_menu_role_id on t_role_menu(role_id);
create index IF NOT EXISTS t_role_menu_menu_id on t_role_menu(menu_id);


CREATE TABLE IF NOT EXISTS t_user (
   id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
   account        VARCHAR(200) NOT NULL,
   password       VARCHAR NOT NULL,
   role_id        BIGINT,
   station_id     BIGINT,
   real_name      VARCHAR,
   phone          VARCHAR(50),
   email          VARCHAR,
   salt           VARCHAR,
   uuid           VARCHAR,
   navigator      VARCHAR,
   last_login_ip  VARCHAR,
   last_login_time TIMESTAMP(3),
   wrong_time   TIMESTAMP(3),
   wrong_num    INT,
   lock_time    TIMESTAMP(3),
   modify_time  TIMESTAMP(3),
   expire_time  TIMESTAMP(3),
   enabled_status  SMALLINT NOT NULL DEFAULT 1,
   created_time    TIMESTAMP(3) NOT NULL DEFAULT now(),
   updated_time    TIMESTAMP(3) NOT NULL DEFAULT now()
);
comment on table t_user is '用户表';
comment on column t_user.account is '账号';
comment on column t_user.password is '密码';
comment on column t_user.role_id is '角色id';
comment on column t_user.station_id is '站点id';
comment on column t_user.real_name is '真实名字';
comment on column t_user.phone is '手机号';
comment on column t_user.email is '邮箱';
comment on column t_user.wrong_time is '错误时间';
comment on column t_user.wrong_num is '错误次数';
comment on column t_user.modify_time is '修改时间';
comment on column t_user.expire_time is '密码过期时间';
create index IF NOT EXISTS t_user_role_id on t_user(role_id);
create index IF NOT EXISTS t_user_station_id on t_user(station_id);


CREATE TABLE IF NOT EXISTS t_region (
   id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
   name        VARCHAR NOT NULL,
   pinyin      VARCHAR NOT NULL,
   code        VARCHAR UNIQUE NOT NULL,
   adcode      VARCHAR,
   upper_id    BIGINT,
   upper_id_set  VARCHAR,
   level       INT,
   sort          INT,
   national_code VARCHAR,
   longitude     VARCHAR,
   latitude      VARCHAR,
   enabled_status  SMALLINT NOT NULL DEFAULT 1,
   created_time    TIMESTAMP(3) NOT NULL DEFAULT now(),
   updated_time    TIMESTAMP(3) NOT NULL DEFAULT now()
);
comment on table t_region is '区域表';
comment on column t_region.name is '名字';
comment on column t_region.code is '自定义区域编码';
comment on column t_region.level is '级别';
comment on column t_region.sort is '排序';
comment on column t_region.national_code is '国标';
comment on column t_region.longitude is '经度';
comment on column t_region.latitude is '纬度';


CREATE TABLE IF NOT EXISTS t_station (
   id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
   code        VARCHAR NOT NULL,
   name        VARCHAR NOT NULL,
   level       INT NOT NULL,
   region_id   BIGINT,
   station_count INT,
   capacity    VARCHAR,
   remark      VARCHAR,
   pms_id      VARCHAR,
   address     VARCHAR,
   longitude     VARCHAR,
   latitude      VARCHAR,
   station_type_id  BIGINT,
   general_type_id   BIGINT,
   smart_time  TIMESTAMP(3),
   running_status    SMALLINT NOT NULL DEFAULT 0,
   contact_name    VARCHAR,
   contact_phone   VARCHAR,
   panoramaca_url  VARCHAR,
   options         INT NOT NULL DEFAULT 0,
   sort            INT NOT NULL DEFAULT 1,
   smart_status    INT NOT NULL DEFAULT 0,
   enabled_status  SMALLINT NOT NULL DEFAULT 1,
   created_time    TIMESTAMP(3) NOT NULL DEFAULT now(),
   updated_time    TIMESTAMP(3) NOT NULL DEFAULT now()
);
comment on table t_station is '站点表';
comment on column t_station.name is '名字';
comment on column t_station.code is '编码, 唯一，靠代码逻辑验证';
comment on column t_station.level is '级别 0-省公司，1-市公司，2-区供电公司，3-线路，4-站房类型，5-具体站房，6-高压进线柜，7-高压出线柜，8-变压器，40-低压进线柜，41-低压出线柜，42-低压开关，43-开关三相(A,B,C)';
comment on column t_station.region_id is '区域id, 只有level=0,1,2,3,4才有,可以为空';
comment on column t_station.station_count is '站房数量 当level=0,1,2,3,4时候有值';
comment on column t_station.capacity is '容量';
comment on column t_station.pms_id is '这是随意写的字符串';
comment on column t_station.address is '站房位置 level=5才有';
comment on column t_station.longitude is '经度';
comment on column t_station.latitude is '纬度';
comment on column t_station.station_type_id is '站房类型id,level=4才有 ';
comment on column t_station.general_type_id is '通用类型id,level=8才有';
comment on column t_station.smart_time is '智能化时间 level=5，6，7，8，40，41，42，43才有值';
comment on column t_station.running_status is '运行态 0-未运行，1-运行';
comment on column t_station.contact_name is '联系人名';
comment on column t_station.contact_phone is '联系电话';
comment on column t_station.panoramaca_url is '全景图url';
comment on column t_station.options is '操作 用8421码，目前只有三级；0-什么也不操作(000)，1-动环状态(001)，2-网关状态(010)，4-是否在地上状态(100), 3-动环和网关状态(011)，以此类推';
comment on column t_station.sort is '排序序号';
comment on column t_station.smart_status is '智能状态，针对level=42, 0-非智能，1-智能。';
create index IF NOT EXISTS t_station_region_id on t_station(region_id);
create index IF NOT EXISTS t_station_station_type_id on t_station(station_type_id);


CREATE TABLE IF NOT EXISTS t_station_relation (
   id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
   cur_station_id        BIGINT NOT NULL,
   upper_id        BIGINT,
   upper_id_set    VARCHAR
);
comment on table t_station_relation is '站点关系表';
comment on column t_station_relation.cur_station_id is '当前站点id';
comment on column t_station_relation.upper_id is '父id';
comment on column t_station_relation.upper_id_set is '父id及以上';
create index IF NOT EXISTS t_station_relation_cur_station_id on t_station_relation(cur_station_id);
create index IF NOT EXISTS t_station_relation_upper_id on t_station_relation(upper_id);


CREATE TABLE IF NOT EXISTS t_station_type (
   id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
   name        VARCHAR NOT NULL,
   type        SMALLINT NOT NULL,
   enabled_status  SMALLINT NOT NULL DEFAULT 1,
   created_time    TIMESTAMP(3) NOT NULL DEFAULT now(),
   updated_time    TIMESTAMP(3) NOT NULL DEFAULT now()
);
comment on table t_station_type is '站点类型表';
comment on column t_station_type.name is '类型名字';
comment on column t_station_type.type is '类型 1-箱式变电站，2-配电室，3-开关站，4-环网箱';


CREATE TABLE IF NOT EXISTS t_station_video (
   id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
   station_id  BIGINT NOT NULL,
   name        VARCHAR NOT NULL,
   url         VARCHAR NOT NULL,
   type        INT NOT NULL DEFAULT 1,
   enabled_status  SMALLINT NOT NULL DEFAULT 1,
   created_time    TIMESTAMP(3) NOT NULL DEFAULT now(),
   updated_time    TIMESTAMP(3) NOT NULL DEFAULT now()
);
comment on table t_station_video is '站点视频表';
comment on column t_station_video.station_id is '站点id';
comment on column t_station_video.name is '视频名字';
comment on column t_station_video.url is '视频地址';
comment on column t_station_video.type is '视频类型，目前只有一个，1-萤石云';
create index IF NOT EXISTS t_station_video_station_id on t_station_video(station_id);



CREATE TABLE IF NOT EXISTS t_factory (
   id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
   name        VARCHAR NOT NULL,
   type        SMALLINT NOT NULL,
   enabled_status  SMALLINT NOT NULL DEFAULT 1,
   created_time    TIMESTAMP(3) NOT NULL DEFAULT now(),
   updated_time    TIMESTAMP(3) NOT NULL DEFAULT now()
);
comment on table t_factory is '厂商表';
comment on column t_factory.name is '厂商名字';
comment on column t_factory.type is '厂商类别';


CREATE TABLE IF NOT EXISTS t_base_device (
   id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
   name        VARCHAR NOT NULL,
   property    VARCHAR NOT NULL,
   sort        INT NOT NULL DEFAULT 1,
   enabled_status  SMALLINT NOT NULL DEFAULT 1,
   created_time    TIMESTAMP(3) NOT NULL DEFAULT now(),
   updated_time    TIMESTAMP(3) NOT NULL DEFAULT now()
);
comment on table t_base_device is '基础设备类型表 包括传感器';
comment on column t_base_device.name is '基础设备名字';
comment on column t_base_device.property is '属性，唯一';
comment on column t_base_device.sort is '排序';


CREATE TABLE IF NOT EXISTS t_base_property (
   id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
   name        VARCHAR NOT NULL,
   property    VARCHAR NOT NULL,
   accuracy    VARCHAR,
   unit        VARCHAR,
   sort        INT NOT NULL DEFAULT 1,
   enabled_status  SMALLINT NOT NULL DEFAULT 1,
   created_time    TIMESTAMP(3) NOT NULL DEFAULT now(),
   updated_time    TIMESTAMP(3) NOT NULL DEFAULT now()
);
comment on table t_base_property is '基础属性类型表';
comment on column t_base_property.name is '基础属性名字';
comment on column t_base_property.property is '属性，唯一';
comment on column t_base_property.accuracy is '精度';
comment on column t_base_property.unit is '单位';
comment on column t_base_property.sort is '排序';



CREATE TABLE IF NOT EXISTS t_device_type (
   id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
   base_device_id        BIGINT NOT NULL,      
   name             VARCHAR NOT NULL,
   property         VARCHAR NOT NULL,
   source                VARCHAR NOT NULL,
   type_classify         VARCHAR,
   factory_id            BIGINT NOT NULL,
   mark                  VARCHAR NOT NULL,
   period                INT NOT NULL,
   delay                 INT NOT NULL,
   timeout               INT NOT NULL,
   func_flag             INT NOT NULL,
   comm_flag             INT NOT NULL,
   enabled_status  SMALLINT NOT NULL DEFAULT 1,
   created_time    TIMESTAMP(3) NOT NULL DEFAULT now(),
   updated_time    TIMESTAMP(3) NOT NULL DEFAULT now()
);
comment on table t_device_type is '设备类型表';
comment on column t_device_type.base_device_id is '基础设备id';
comment on column t_device_type.factory_id is '厂商id';
comment on column t_device_type.source is '来源，device,claa 目前这两种，数据处理逻辑不同';
comment on column t_device_type.type_classify is '旧数据可能有用，先保留，后期删除';
create index IF NOT EXISTS t_device_type_base_device_id on t_device_type(base_device_id);
create index IF NOT EXISTS t_device_type_factory_id on t_device_type(factory_id);



CREATE TABLE IF NOT EXISTS t_device_property_model (
   id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
   device_type_id          BIGINT NOT NULL,
   base_property_id        BIGINT NOT NULL,
   point_flag              INT
);
comment on table t_device_property_model is '设备属性模型表，某个设备类型有一些基础属性';
comment on column t_device_property_model.device_type_id is '设备类型id';
comment on column t_device_property_model.base_property_id is '基础属性id';
comment on column t_device_property_model.point_flag is '点类型，1：遥信；2：遥测；3：遥控；4：遥调；5：遥脉；6：系统遥控；7：用户遥控';
create index IF NOT EXISTS t_device_property_model_device_type_id on t_device_property_model(device_type_id);
create index IF NOT EXISTS t_device_property_model_base_property_id on t_device_property_model(base_property_id);



CREATE TABLE IF NOT EXISTS t_device (
   id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
   sn        VARCHAR NOT NULL,
   name        VARCHAR NOT NULL,
   capacity    VARCHAR,
   point_id    VARCHAR,
   remark      VARCHAR,
   address     VARCHAR,
   device_type_id  BIGINT NOT NULL,
   gateway_id  VARCHAR,
   online_status  SMALLINT,
   enabled_status  SMALLINT NOT NULL DEFAULT 1,
   created_time    TIMESTAMP(3) NOT NULL DEFAULT now(),
   updated_time    TIMESTAMP(3) NOT NULL DEFAULT now()
);
comment on table t_device is '设备表';
comment on column t_device.sn is '设备唯一码';
comment on column t_device.name is '设备名字';
comment on column t_device.capacity is '设备容量';
comment on column t_device.device_type_id is '设备类型id';
create index IF NOT EXISTS t_device_device_type_id on t_device(device_type_id);


CREATE TABLE IF NOT EXISTS t_device_station (
   id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
   device_id        BIGINT NOT NULL,
   station_id        BIGINT NOT NULL
);
comment on table t_device_station is '设备绑定关联表';
comment on column t_device_station.device_id is '设备id';
comment on column t_device_station.station_id is '站点id，一个设备，可以绑在多个、站点上， 通过station_id能查出父级及以上各个级别信息';
create index IF NOT EXISTS t_device_station_device_id on t_device_station(device_id);
create index IF NOT EXISTS t_device_station_station_id on t_device_station(station_id);



CREATE TABLE IF NOT EXISTS t_device_point (
   id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
   device_type_id   BIGINT NOT NULL,
   device_id        BIGINT,
   property         VARCHAR,
   base_property_id  BIGINT,
   point            VARCHAR,
   point_key        VARCHAR,
   value_key        VARCHAR,
   period           INT,
   remark           VARCHAR,
   enabled_status   SMALLINT NOT NULL DEFAULT 1,
   created_time     TIMESTAMP(3) NOT NULL DEFAULT now(),
   updated_time     TIMESTAMP(3) NOT NULL DEFAULT now()
);
comment on table t_device_point is '设备点点表';
comment on column t_device_point.device_type_id is '设备类型id';
comment on column t_device_point.device_id is '设备id';
comment on column t_device_point.property is '设备给的属性，找到我们自己定义的属性就是base_property';
comment on column t_device_point.base_property_id is '基础属性id, 自己定义的基础属性表';
comment on column t_device_point.point is '字符串，点id，如：basic_property_id中是Ia， 其点为1，相当于Ia-1, 可为空';
create index IF NOT EXISTS t_device_point_device_type_id on t_device_point(device_type_id);
create index IF NOT EXISTS t_device_point_device_id on t_device_point(device_id);
create index IF NOT EXISTS t_device_point_base_property_id on t_device_point(base_property_id);



CREATE TABLE IF NOT EXISTS t_device_data_format (
   id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
   device_type_id   BIGINT NOT NULL,
   base_property_id  BIGINT,
   data_prefix      VARCHAR,
   data_postfix     VARCHAR,
   start_idx        INT,
   byte_cnt         INT,
   data_scale       INT,
   ishbyte_right    SMALLINT,
   need_complement  SMALLINT,
   ratio_val        VARCHAR,
   revise_val       VARCHAR,
   ishex            SMALLINT,
   isbin            SMALLINT,
   bin_idx          SMALLINT,
   bin_cnt          SMALLINT,
   hbit_only_sign   SMALLINT,
   scale_type       VARCHAR,
   value_range      VARCHAR,
   options          INT,
   remark           VARCHAR,
   enabled_status   SMALLINT NOT NULL DEFAULT 1,
   created_time     TIMESTAMP(3) NOT NULL DEFAULT now(),
   updated_time     TIMESTAMP(3) NOT NULL DEFAULT now()
);
comment on table t_device_data_format is '设备数据格式表';
comment on column t_device_data_format.device_type_id is '设备类型id';
comment on column t_device_data_format.base_property_id is '基础属性id';
create index IF NOT EXISTS t_device_data_format_device_type_id on t_device_data_format(device_type_id);
create index IF NOT EXISTS t_device_data_format_base_property_id on t_device_data_format(base_property_id);



CREATE TABLE IF NOT EXISTS t_base_elec (
   id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
   name        VARCHAR NOT NULL,
   type        VARCHAR NOT NULL,
   sort        INT NOT NULL DEFAULT 1,
   enabled_status  SMALLINT NOT NULL DEFAULT 1,
   created_time    TIMESTAMP(3) NOT NULL DEFAULT now(),
   updated_time    TIMESTAMP(3) NOT NULL DEFAULT now()
);
comment on table t_base_elec is '基础电力设备类型表';
comment on column t_base_elec.name is '类型名字';
comment on column t_base_elec.type is '类型';
comment on column t_base_elec.sort is '排序';



CREATE TABLE IF NOT EXISTS t_general_type (
   id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
   name        VARCHAR NOT NULL,
   code        VARCHAR NOT NULL,
   base_elec_id        BIGINT NOT NULL,
   sort        INT NOT NULL DEFAULT 1,
   enabled_status  SMALLINT NOT NULL DEFAULT 1,
   created_time    TIMESTAMP(3) NOT NULL DEFAULT now(),
   updated_time    TIMESTAMP(3) NOT NULL DEFAULT now()
);
comment on table t_general_type is '通用类型表';
comment on column t_general_type.name is '通用类型名字';
comment on column t_general_type.code is '通用类型编码，不能重复';
comment on column t_general_type.base_elec_id is '基础电力设备id';
comment on column t_general_type.sort is '排序';
create index IF NOT EXISTS t_general_type_base_elec_id on t_general_type(base_elec_id);



CREATE TABLE IF NOT EXISTS t_system_configuration (
   id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
   type        VARCHAR NOT NULL,
   value       VARCHAR NOT NULL,
   enabled_status  SMALLINT NOT NULL DEFAULT 1,
   created_time    TIMESTAMP(3) NOT NULL DEFAULT now(),
   updated_time    TIMESTAMP(3) NOT NULL DEFAULT now()
);
comment on table t_system_configuration is '系统配置表';
comment on column t_system_configuration.type is '配置类型，唯一  producer, logo, title,expired, phone, create_time, word_path';
comment on column t_system_configuration.value is '根据属性不同，值不一样';

CREATE TABLE t_energy_transformer_info (
	id int8  GENERATED ALWAYS AS IDENTITY,
	station_id int8 ,
	sbmc varchar(70) ,
	edrl varchar(20) ,
	zkdy varchar(20) ,
	kzdl varchar(20) ,
	dlsh varchar(20) ,
	kzsh varchar(20) ,
	dyb varchar(70) ,
	gyceddl varchar(20) ,
	yxbh varchar(70) ,
	zxmc varchar(70) ,
	ssgt varchar(50) ,
	syxz varchar(10) ,
	ssds varchar(70) ,
	ywdw varchar(60) ,
	whbz varchar ,
	ssdkx varchar ,
	gdqy varchar ,
	dydj varchar ,
	dkxzx varchar ,
	sbzt varchar ,
	tyrq timestamp(3)  DEFAULT now(),
	sfdw varchar ,
	sfnw varchar ,
	zycd varchar ,
	zcxz varchar ,
	zcdw varchar ,
	zcbh varchar ,
	sbzjfs varchar ,
	xh varchar ,
	sccj varchar ,
	ccbh varchar ,
	dxbm varchar ,
	ccrq timestamp(3)  DEFAULT now(),
	dxmc varchar ,
	jyjz varchar ,
	sffjb varchar ,
	wzkgfjds varchar ,
	wzkgfjwz varchar ,
	xmmc varchar ,
	dyceddl varchar(20) ,
	xmbm varchar ,
	jddz varchar(20) ,
	yh varchar ,
	yz varchar(20) ,
	zz varchar(20) ,
	lx varchar ,
	jynrdj varchar ,
	sbbm varchar ,
	djsj timestamp(3)  DEFAULT now(),
	bz varchar ,
	ljzbh varchar ,
	gnwz varchar ,
	swid varchar ,
	tzcjfs varchar ,
	ywsbzl varchar ,
	sfctsb varchar ,
	pmbm varchar ,
	ssxlmc varchar ,
	zyfl varchar ,
	zltxsl varchar(20) ,
	sblxbm varchar ,
	dydjmc varchar ,
	dydjdm varchar ,
	ssdkxid varchar ,
	dqtz varchar ,
	dkxzxid varchar ,
	dxmpyxkid varchar ,
	obj_id varchar ,
	enabled_status int2  DEFAULT 1,
	created_time timestamp(3)  DEFAULT now(),
	updated_time timestamp(3)  DEFAULT now(),
	CONSTRAINT t_energy_transformer_info_pkey PRIMARY KEY (id)
);

COMMENT ON TABLE t_energy_transformer_info IS '高效节能变压器试点情况统计';
COMMENT ON COLUMN t_energy_transformer_info.id IS '主键';
COMMENT ON COLUMN t_energy_transformer_info.station_id IS '站点表外键';
COMMENT ON COLUMN t_energy_transformer_info.sbmc IS '设备名称';
COMMENT ON COLUMN t_energy_transformer_info.edrl IS '额定容量(kVA)';
COMMENT ON COLUMN t_energy_transformer_info.zkdy IS '阻抗电压(%)';
COMMENT ON COLUMN t_energy_transformer_info.kzdl IS '空载电流(%)';
COMMENT ON COLUMN t_energy_transformer_info.dlsh IS '短路损耗(W)';
COMMENT ON COLUMN t_energy_transformer_info.kzsh IS '空载损耗(W)';
COMMENT ON COLUMN t_energy_transformer_info.dyb IS '电压比';
COMMENT ON COLUMN t_energy_transformer_info.gyceddl IS '高压侧额定电流(A)';
COMMENT ON COLUMN t_energy_transformer_info.yxbh IS '运行编号';
COMMENT ON COLUMN t_energy_transformer_info.zxmc IS '主线名称';
COMMENT ON COLUMN t_energy_transformer_info.ssgt IS '所属杆塔';
COMMENT ON COLUMN t_energy_transformer_info.syxz IS '使用性质';
COMMENT ON COLUMN t_energy_transformer_info.ssds IS '所属地市';
COMMENT ON COLUMN t_energy_transformer_info.ywdw IS '运维单位';
COMMENT ON COLUMN t_energy_transformer_info.whbz IS '维护班组';
COMMENT ON COLUMN t_energy_transformer_info.ssdkx IS '所属大馈线';
COMMENT ON COLUMN t_energy_transformer_info.gdqy IS '供电区域';
COMMENT ON COLUMN t_energy_transformer_info.dydj IS '电压等级';
COMMENT ON COLUMN t_energy_transformer_info.dkxzx IS '大馈线支线';
COMMENT ON COLUMN t_energy_transformer_info.sbzt IS '设备状态';
COMMENT ON COLUMN t_energy_transformer_info.tyrq IS '投运日期';
COMMENT ON COLUMN t_energy_transformer_info.sfdw IS '是否代维';
COMMENT ON COLUMN t_energy_transformer_info.sfnw IS '是否农网';
COMMENT ON COLUMN t_energy_transformer_info.zycd IS '重要程度';
COMMENT ON COLUMN t_energy_transformer_info.zcxz IS '资产性质';
COMMENT ON COLUMN t_energy_transformer_info.zcdw IS '资产单位';
COMMENT ON COLUMN t_energy_transformer_info.zcbh IS '资产编号';
COMMENT ON COLUMN t_energy_transformer_info.sbzjfs IS '设备增加方式';
COMMENT ON COLUMN t_energy_transformer_info.xh IS '型号';
COMMENT ON COLUMN t_energy_transformer_info.sccj IS '生产厂家';
COMMENT ON COLUMN t_energy_transformer_info.ccbh IS '出厂编号';
COMMENT ON COLUMN t_energy_transformer_info.dxbm IS '单项编码';
COMMENT ON COLUMN t_energy_transformer_info.ccrq IS '出厂日期';
COMMENT ON COLUMN t_energy_transformer_info.dxmc IS '单项名称';
COMMENT ON COLUMN t_energy_transformer_info.jyjz IS '绝缘介质';
COMMENT ON COLUMN t_energy_transformer_info.sffjb IS '是否非晶变';
COMMENT ON COLUMN t_energy_transformer_info.wzkgfjds IS '无载开关分接档数';
COMMENT ON COLUMN t_energy_transformer_info.wzkgfjwz IS '无载开关分接位置';
COMMENT ON COLUMN t_energy_transformer_info.xmmc IS '项目名称';
COMMENT ON COLUMN t_energy_transformer_info.dyceddl IS '低压侧额定电流(A)';
COMMENT ON COLUMN t_energy_transformer_info.xmbm IS '项目编码';
COMMENT ON COLUMN t_energy_transformer_info.jddz IS '接地电阻(Ω)';
COMMENT ON COLUMN t_energy_transformer_info.yh IS '油号';
COMMENT ON COLUMN t_energy_transformer_info.yz IS '油重(kg)';
COMMENT ON COLUMN t_energy_transformer_info.zz IS '总重(kg)';
COMMENT ON COLUMN t_energy_transformer_info.lx IS '类型';
COMMENT ON COLUMN t_energy_transformer_info.jynrdj IS '绝缘耐热等级';
COMMENT ON COLUMN t_energy_transformer_info.sbbm IS '设备编码';
COMMENT ON COLUMN t_energy_transformer_info.djsj IS '登记时间';
COMMENT ON COLUMN t_energy_transformer_info.bz IS '备注';
COMMENT ON COLUMN t_energy_transformer_info.ljzbh IS '联接组标号';
COMMENT ON COLUMN t_energy_transformer_info.gnwz IS '功能位置';
COMMENT ON COLUMN t_energy_transformer_info.swid IS '实物ID';
COMMENT ON COLUMN t_energy_transformer_info.tzcjfs IS '台账创建方式';
COMMENT ON COLUMN t_energy_transformer_info.ywsbzl IS '有无设备资料';
COMMENT ON COLUMN t_energy_transformer_info.sfctsb IS '是否成套设备';
COMMENT ON COLUMN t_energy_transformer_info.pmbm IS 'PM编码';
COMMENT ON COLUMN t_energy_transformer_info.ssxlmc IS '所属线路名称';
COMMENT ON COLUMN t_energy_transformer_info.zyfl IS '专业分类';
COMMENT ON COLUMN t_energy_transformer_info.zltxsl IS '资料图像数量';
COMMENT ON COLUMN t_energy_transformer_info.sblxbm IS '设备类型编码';
COMMENT ON COLUMN t_energy_transformer_info.dydjmc IS '电压等级名称';
COMMENT ON COLUMN t_energy_transformer_info.dydjdm IS '电压等级代码';
COMMENT ON COLUMN t_energy_transformer_info.ssdkxid IS '所属大馈线id';
COMMENT ON COLUMN t_energy_transformer_info.dqtz IS '地区特征';
COMMENT ON COLUMN t_energy_transformer_info.dkxzxid IS '大馈线支线id';
COMMENT ON COLUMN t_energy_transformer_info.dxmpyxkid IS '电系铭牌运行库id';
COMMENT ON COLUMN t_energy_transformer_info.obj_id IS 'obj_id';

CREATE TABLE t_oil_transformer_efficiency (
	id int8  GENERATED ALWAYS AS IDENTITY,
	level SMALLINT NOT NULL,
	sffjb VARCHAR(20) NOT NULL,
	edrl VARCHAR NULL,
	kzsh VARCHAR NULL,
	ljzbh VARCHAR(40) NULL,
	fzsh VARCHAR NULL,
	dlzk VARCHAR(40) NULL,
	enabled_status int2  DEFAULT 1,
	created_time timestamp(3)  DEFAULT now(),
	updated_time timestamp(3)  DEFAULT now(),
	CONSTRAINT t_oil_transformer_efficiency_pkey PRIMARY KEY (id)
);

COMMENT ON TABLE t_oil_transformer_efficiency IS '10kv油浸式三相双绕组无励磁调压配电变压器能效等级';

COMMENT ON COLUMN t_oil_transformer_efficiency.id IS '主键';
COMMENT ON COLUMN t_oil_transformer_efficiency.level IS '变压器能效等级';
COMMENT ON COLUMN t_oil_transformer_efficiency.sffjb IS '是否非晶变';
COMMENT ON COLUMN t_oil_transformer_efficiency.edrl IS '额定容量(kVA)';
COMMENT ON COLUMN t_oil_transformer_efficiency.kzsh IS '空载损耗(W)';
COMMENT ON COLUMN t_oil_transformer_efficiency.ljzbh IS '联接组标号';
COMMENT ON COLUMN t_oil_transformer_efficiency.fzsh IS '负载损耗(W)';
COMMENT ON COLUMN t_oil_transformer_efficiency.dlzk IS '短路阻抗(%)';

INSERT INTO t_oil_transformer_efficiency ("level",sffjb,edrl,kzsh,ljzbh,fzsh,enabled_status,created_time,updated_time,dlzk) VALUES
	 (1,'否','30','65','Yzn1','455',1,'2023-01-29 11:30:57.987','2023-01-29 11:30:57.987','4.0'),
	 (1,'否','30','65','Yzn0','430',1,'2023-01-29 11:30:57.987','2023-01-29 11:30:57.987','4.0');


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