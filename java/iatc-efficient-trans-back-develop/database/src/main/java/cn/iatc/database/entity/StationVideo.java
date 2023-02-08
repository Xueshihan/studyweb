package cn.iatc.database.entity;

import cn.iatc.database.constants.SqlConstants;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

// 站点视频表
@Data
@ToString(callSuper = true)
@TableName(value = SqlConstants.TABLE_STATION_VIDEO)
public class StationVideo extends BaseData {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "station_id")
    private Long stationId;

    @TableField(value = "name")
    private String name;

    @TableField(value = "url")
    private String url;

    // 视频类型，目前只有一个，1-萤石云
    @TableField(value = "type")
    private Integer type;

    public enum Type {
        EZVIZ(1);// 萤石云视频

        private Integer value;

        public Integer getValue() {
            return this.value;
        }

        Type(Integer value) {
            this.value = value;
        }

        public Type fromValue(Integer value) {
            for (Type type : Type.values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return EZVIZ;
        }
    }
}
