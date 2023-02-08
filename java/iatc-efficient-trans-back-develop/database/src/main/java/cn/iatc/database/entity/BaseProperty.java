package cn.iatc.database.entity;

import cn.iatc.database.constants.SqlConstants;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

// 基础设备表
@Data
@ToString(callSuper = true)
@TableName(value = SqlConstants.TABLE_BASE_PROPERTY)
public class BaseProperty extends BaseData {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "name")
    @Schema(description = "基础属性名字")
    private String name;

    @TableField(value = "property")
    @Schema(description = "属性，唯一")
    private String property;

    @TableField(value = "accuracy")
    @Schema(description = "精度")
    private String accuracy;

    @TableField(value = "unit")
    @Schema(description = "单位")
    private String unit;

    @TableField(value = "sort")
    @Schema(description = "排序")
    private Integer sort;

    public enum Property {

        PHV_PHSA("PhV_phsA"), // A相电压
        PHV_PHSB("PhV_phsB"), // B相电压
        PHV_PHSC("PhV_phsC"), // C相电压
        A_PHSA("A_phsA"), // A相电流
        A_PHSB("A_phsB"), // B相电流
        A_PHSC("A_phsC"), // C相电流
        PHPF_PHSA("PhPF_phsA"), // A相功率因数
        PHPF_PHSB("PhPF_phsB"), // B相功率因数
        PHPF_PHSC("PhPF_phsC"); // C相功率因数

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

    public static class PropertyConstant {
        public static final List<String> TRANSPROPERTIES = Arrays.asList(Property.PHV_PHSA.getValue(),Property.PHV_PHSB.getValue(),
                Property.PHV_PHSC.getValue(), Property.A_PHSA.getValue(), Property.A_PHSB.getValue(), Property.A_PHSC.getValue(),
                Property.PHPF_PHSA.getValue(), Property.PHPF_PHSB.getValue(), Property.PHPF_PHSC.getValue());

        // A相
        public static final List<String> TRANS_A = Arrays.asList(Property.PHV_PHSA.getValue(), Property.A_PHSA.getValue(), Property.PHPF_PHSA.getValue());

        // B相
        public static final List<String> TRANS_B = Arrays.asList(Property.PHV_PHSB.getValue(), Property.A_PHSB.getValue(), Property.PHPF_PHSB.getValue());

        // C相
        public static final List<String> TRANS_C = Arrays.asList(Property.PHV_PHSC.getValue(), Property.A_PHSC.getValue(), Property.PHPF_PHSC.getValue());
    }

}
