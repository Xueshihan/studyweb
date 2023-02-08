package cn.iatc.database.entity;

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

import java.util.Arrays;
import java.util.List;

// 设备类型表
@Data
@ToString(callSuper = true)
@TableName(value = SqlConstants.TABLE_DEVICE_TYPE)
public class DeviceType extends BaseData {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "base_device_id")
    @JSONField(serialize = false)
    @Hidden
    private Long baseDeviceId;

    @TableField(value = "name")
    @Schema(description = "名字")
    private String name;

    @TableField(value = "property")
    @Schema(name = "property", description = "属性")
    private String property;

    @TableField(value = "source")
    private String source;

    @TableField(value = "type_classify")
    private String typeClassify;

    @TableField(value = "factory_id")
    @JSONField(serialize = false)
    @Hidden
    private Long factoryId;

    @TableField(value = "mark")
    private String mark;

    @TableField(value = "period")
    private Integer period;

    @TableField(value = "delay")
    private Integer delay;

    @TableField(value = "timeout")
    private Integer timeout;

    @TableField(value = "func_flag")
    private Integer funcFlag;

    @TableField(value = "comm_flag")
    private Integer commFlag;

    @TableField(exist = false)
    private BaseDevice baseDevice;

    @TableField(exist = false)
    @JSONField(name = "factory")
    @Schema(name = "factory")
    private Factory factory;

    public enum Property {
        ZHI_NENG_DIAN_BIAO_IATC("zhinengdianbiao_iatc"),
        ZHI_NENG_CT_10KV_A("zhinengct_10kv_A"), // 变压器A相
        ZHI_NENG_CT_10KV_B("zhinengct_10kv_B"), // 变压器B相
        ZHI_NENG_CT_10KV_C("zhinengct_10kv_C"); // 变压器C相

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
            return ZHI_NENG_DIAN_BIAO_IATC;
        }
    }

    public static class PropertyConstant {
        // CT 10kv属性
        public static final List<String> CT_10KV = Arrays.asList(Property.ZHI_NENG_CT_10KV_A.getValue(), Property.ZHI_NENG_CT_10KV_B.getValue(),
                Property.ZHI_NENG_CT_10KV_C.getValue());

        // 智能电表属性
        public static final List<String> DIAN_BIAO_IATC = Arrays.asList(Property.ZHI_NENG_DIAN_BIAO_IATC.getValue());

    }
}
