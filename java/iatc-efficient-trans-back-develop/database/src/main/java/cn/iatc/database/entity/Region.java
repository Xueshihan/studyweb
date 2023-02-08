package cn.iatc.database.entity;

import cn.iatc.database.constants.SqlConstants;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;
import lombok.ToString;

// 区域表
@Data
@ToString(callSuper = true)
@TableName(value = SqlConstants.TABLE_REGION)
public class Region extends BaseData {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "name")
    private String name;

    @TableField(value = "pinyin")
    private String pinyin;

    @TableField(value = "code")
    private String code;

    @TableField(value = "adcode")
    private String adCode;

    @TableField(value = "upper_id")
    private Long upperId;

    @TableField(value = "upper_id_set")
    @JSONField(serialize = false)
    @Hidden
    private String upperIdSet;

    @TableField(value = "level")
    private Integer level;

    @TableField(value = "sort")
    private Integer sort;

    @TableField(value = "national_code")
    private String nationalCode;

    @TableField(value = "longitude")
    private String longitude;

    @TableField(value = "latitude")
    private String latitude;

    public enum Level {
        PROVINCE(0), // 省
        CITY(1), // 市
        AREA(2); // 区

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
            return PROVINCE;
        }
    }
}
