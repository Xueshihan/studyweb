package cn.iatc.database.entity;

import cn.iatc.database.constants.Constants;
import cn.iatc.database.constants.SqlConstants;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.*;

// 站点表
@Data
@ToString(callSuper = true)
@TableName(value = SqlConstants.TABLE_STATION)
public class Station extends BaseData {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "code")
    @Schema(description = "站点编号")
    private String code;

    @TableField(value = "name")
    @Schema(description = "站点名字")
    private String name;

    //级别 0-省公司，1-市公司，2-区供电公司，3-线路，4-站房类型，5-具体站房，6-高压进线柜，7-高压出线柜，8-变压器，
    // 40-低压进线柜，41-低压出线柜，42-低压开关，43-开关三相(A,B,C)
    @TableField(value = "level")
    @Schema(description = "站点级别")
    private Integer level;

    //区域id, 只有level=0,1,2,3,4才有
    @TableField(value = "region_id")
    @Schema(description = "区域id")
    private Long regionId;

    //站房数量 当level=0,1,2,3,4时候有值'
    @TableField(value = "station_count")
    @Schema(description = "站房数量")
    private Integer stationCount;

    @TableField(value = "capacity")
    @Schema(description = "容量")
    private String capacity;

    @TableField(value = "remark")
    @Schema(description = "备注")
    private String remark;

    @TableField(value = "pms_id")
    @Schema(description = "pms_id")
    private String pmsId;

    @TableField(value = "address")
    @Schema(description = "地址")
    private String address;

    @TableField(value = "longitude")
    @Schema(description = "经度")
    private String longitude;

    @TableField(value = "latitude")
    @Schema(description = "纬度")
    private String latitude;

    //站房类型id,level=4才有
    @TableField(value = "station_type_id")
    @Schema(description = "站房类型id，level=4")
    private Long stationTypeId;

    @TableField(value = "general_type_id")
    @Schema(description = "通用类型id")
    private Long generalTypeId;

    //智能化建设时间 level=5，6，7，8，40，41，42，43才有值
    @TableField(value = "smart_time")
    @Schema(description = "智能化建立时间")
    private Date smartTime;

    @TableField(value = "running_status")
    @Schema(description = "运行状态")
    private Integer runningStatus;

    // 联系人名
    @TableField(value = "contact_name")
    @Schema(description = "联系人名")
    private String contactName;

    // 联系电话
    @TableField(value = "contact_phone")
    @Schema(description = "联系人手机号")
    private String contactPhone;

    // 全景url
    @TableField(value = "panoramaca_url")
    @Schema(description = "全景url")
    private String panoramacaUrl;

    // 操作类型 用8421码，目前只有三级；0-什么也不操作(000)，1-动环状态(001)，2-网关状态(010)，4-是否在地上状态(100), 3-动环和网关状态(011)，以此类推,其他组合
    @TableField(value = "options")
    @JSONField(serialize = false)
    @Hidden
    private Integer options;

    // 动环状态
    @TableField(exist = false)
    @Schema(description = "动环状态")
    private Integer powerEnvFlag;

    // 网管状态
    @TableField(exist = false)
    @Schema(description = "网管状态")
    private Integer gatewayFlag;

    // 是否是地上状态
    @TableField(exist = false)
    @Schema(description = "是否是地上状态")
    private Integer onlandsFlag;

    @TableField(value = "sort")
    @Schema(description = "排序序号")
    private Integer sort;

    // level=42，当为智能开关，在t_device添加一条记录
    @TableField(value = "smart_status")
    @Schema(description = "智能状态 level=42才有")
    private Integer smartStatus;

    @TableField(exist = false)
    @JSONField(serialize = false)
    @Hidden
    private StationType stationType;

    @TableField(exist = false)
    @JSONField(serialize = false)
    @Hidden
    private Region region;

    @TableField(exist = false)
    @JSONField(serialize = false)
    @Hidden
    private GeneralType generalType;

    // 站房级别
    public enum Level {
        LEVEL0(0), // 级别0-省公司
        LEVEL1(1), // 级别1-市公司
        LEVEL2(2), // 级别2-区公司
        LEVEL3(3), // 级别3-线路
        LEVEL4(4), // 级别4-站房类型
        LEVEL5(5), // 级别5-站房信息
        LEVEL6(6), // 级别6-高压进线柜
        LEVEL7(7), // 级别7-高压出线柜
        LEVEL8(8), // 级别8-变压器
        LEVEL40(40), // 级别40-低压进线柜
        LEVEL41(41), // 级别41-低压出线柜
        LEVEL42(42), // 级别42-低压开关
        LEVEL43(43); // 级别43-互感器，开关三相

        private Integer value;

        Level(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }

        public static Level fromValue(Integer value) {
            for (Level level : Level.values()) {
                if (level.getValue().equals(value)) {
                    return level;
                }
            }
            return LEVEL0;
        }
    }

    // 针对ognl方式处理，只能是静态变量或静态方法
    public static Integer POWER_ENV = Option.POWER_ENV.getValue();
    public static Integer GATEWAY = Option.GATEWAY.getValue();
    public static Integer ON_LANDS = Option.ON_LANDS.getValue();

    // 操作类型
    public enum Option {
        DEFAULT(0), // 未知
        POWER_ENV(1), // 动环
        GATEWAY(2), // 网关
        ON_LANDS(4); // 地上

        private Integer value;

        Option(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }

        public static Option fromValue(Integer value) {
            for(Option option: Option.values()) {
                if (option.getValue().equals(value)) {
                    return option;
                }
            }
            return DEFAULT;
        }
    }
    public static class OptionHandle {
        public static Integer transform(Integer enabledStatus, Option option) {
            Integer result = Option.DEFAULT.getValue();
            if(enabledStatus != null && enabledStatus.equals(Constants.Status.ENABLED.getValue())) {
                switch (option) {
                    case POWER_ENV:
                        result = Option.POWER_ENV.getValue();
                        break;
                    case GATEWAY:
                        result = Option.GATEWAY.getValue();
                        break;
                    case ON_LANDS:
                        result = Option.ON_LANDS.getValue();
                        break;
                }
            }
            return result;
        }
    }

    public static class LevelConstant {
        // 区域级别
        public static final List<Integer> regionLevels = Arrays.asList(Level.LEVEL0.getValue(), Level.LEVEL1.getValue(), Level.LEVEL2.getValue());

        // 到变压器level=4级别
        public static final List<Integer> stationBuildingLevels4 = Arrays.asList(Level.LEVEL0.getValue(), Level.LEVEL1.getValue(), Level.LEVEL2.getValue(),
                Level.LEVEL3.getValue(), Level.LEVEL4.getValue());

        // 到站房级别
        public static final List<Integer> stationBuildingLevels = Arrays.asList(Level.LEVEL0.getValue(), Level.LEVEL1.getValue(), Level.LEVEL2.getValue(),
                Level.LEVEL3.getValue(), Level.LEVEL4.getValue(), Level.LEVEL5.getValue());

        public static final List<Integer> stationDeviceLevels = Arrays.asList(Level.LEVEL6.getValue(), Level.LEVEL7.getValue(), Level.LEVEL8.getValue(),
                Level.LEVEL40.getValue(), Level.LEVEL41.getValue(), Level.LEVEL42.getValue(), Level.LEVEL43.getValue());
    }

    // level层级关系
    public static class LevelRelation {
        public static final Map<Integer, Integer> levelMap = new HashMap<Integer, Integer>() {{
            put(null, Level.LEVEL0.getValue());
            put(Level.LEVEL0.getValue(), Level.LEVEL1.getValue());
            put(Level.LEVEL1.getValue(), Level.LEVEL2.getValue());
            put(Level.LEVEL2.getValue(), Level.LEVEL3.getValue());
            put(Level.LEVEL4.getValue(), Level.LEVEL5.getValue());
            put(Level.LEVEL5.getValue(), Level.LEVEL6.getValue());
            put(Level.LEVEL6.getValue(), Level.LEVEL7.getValue());
            put(Level.LEVEL7.getValue(), Level.LEVEL8.getValue());
            put(Level.LEVEL8.getValue(), Level.LEVEL40.getValue());
            put(Level.LEVEL40.getValue(), Level.LEVEL41.getValue());
            put(Level.LEVEL41.getValue(), Level.LEVEL42.getValue());
            put(Level.LEVEL42.getValue(), Level.LEVEL43.getValue());
        }};
    }
}
