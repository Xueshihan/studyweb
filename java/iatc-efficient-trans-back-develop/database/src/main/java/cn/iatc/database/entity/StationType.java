package cn.iatc.database.entity;

import cn.iatc.database.constants.SqlConstants;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

// 站点类型表
@Data
@ToString(callSuper = true)
@TableName(value = SqlConstants.TABLE_STATION_TYPE)
public class StationType extends BaseData {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "name")
    private String name;

    @TableField(value = "type")
    private Integer type;

    public enum Type {
        BOX_SUBSTATION(1), //箱式变电站
        ELEC_DIS_ROOM(2), // 配电室
        SWITCH_STATION(3), //开关站
        RING_CAGE(4); //环网箱

        private Integer value;

        Type(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }

        public static Type fromValue(Integer value) {
            for (Type type : Type.values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return BOX_SUBSTATION;
        }
    }
}
