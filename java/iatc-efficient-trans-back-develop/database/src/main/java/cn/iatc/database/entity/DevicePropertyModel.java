package cn.iatc.database.entity;

import cn.iatc.database.constants.SqlConstants;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

// 设备属性模型表
@Data
@ToString(callSuper = true)
@TableName(value = SqlConstants.TABLE_DEVICE_PROPERTY_MODEL)
public class DevicePropertyModel {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "device_type_id")
    @Schema(description = "设备类型id")
    private Long deviceTypeId;

    @TableField(value = "base_property_id")
    @Schema(description = "基础属性id")
    private Long basePropertyId;

    @TableField(value = "point_flag")
    @Schema(description = "点类型, 1：遥信；2：遥测；3：遥控；4：遥调；5：遥脉；6：系统遥控；7：用户遥控")
    private Integer pointFlag;

    public enum PointFlag {
        TELESIGNALLING(1), // 遥信
        TELEMETERING(2), // 遥测
        REMOTE_CONTROL(3), // 遥控
        TELEMODULATION(4), // 遥调
        TELEPULSE(5), // 遥脉
        SYSTEM_REMOTE_CONTROL(6), // 系统遥控
        USER_REMOTE_CONTROL(6); // 用户遥控

        private Integer value;

        PointFlag(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return this.value;
        }

        public static PointFlag fromValue(Integer value) {
            for (PointFlag flag : PointFlag.values()) {
                if (flag.getValue().equals(value)) {
                    return flag;
                }
            }
            return TELESIGNALLING;
        }
    }
}
