package cn.iatc.database.entity;

import cn.iatc.database.constants.SqlConstants;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

// 设备采集点点表
@Data
@ToString(callSuper = true)
@TableName(value = SqlConstants.TABLE_DEVICE_POINT)
public class DevicePoint extends BaseData {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "device_type_id")
    private Long deviceTypeId;

    @TableField(value = "device_id")
    private Long deviceId;

    @TableField(value = "property")
    private String property;

    @TableField(value = "base_property_id")
    private Long basePropertyId;

    @TableField(value = "point")
    private String point;

    @TableField(value = "point_key")
    private String pointKey;

    @TableField(value = "value_key")
    private Long valueKey;

    @TableField(value = "period")
    private Integer period;

    @TableField(value = "remark")
    private String remark;

    public enum Property {
        PHV_PHSA("PhV_phsA"), // A相电压
        PHV_PHSB("PhV_phsB"), // B相电压
        PHV_PHSC("PhV_phsC"), // C相电压
        A_PHSA("A_phsA"), // A相电流
        A_PHSB("A_phsB"), // B相电流
        A_PHSC("A_phsC"), // C相电流
        PHPF_PHSA("PhPF_phsA"), // A相功率因数
        PHPF_PHSB("PhPF_phsB"), // B相功率因数
        PHPF_PHSC("PhPF_phsC"), // C相功率因数
        DIANLIU("dianliu"); // 高压侧电流


        private String value;

        Property(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Property fromValue(String value) {
            for (Property property : Property.values()) {
                if (property.getValue().equals(value)) {
                    return property;
                }
            }
            return PHV_PHSA;
        }
    }

    public enum Point {
        UNKNOWN(null),
        HIGH("0"), // 只有一侧
        HIGH_1("1"), // 高压1侧
        HIGH_2("2"); // 低压2侧

        private String value;

        Point(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }
}
