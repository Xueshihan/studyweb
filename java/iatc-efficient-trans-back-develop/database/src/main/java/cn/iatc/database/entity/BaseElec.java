package cn.iatc.database.entity;

import cn.iatc.database.constants.SqlConstants;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

// 基础电力设备类型表
@Data
@ToString(callSuper = true)
@TableName(value = SqlConstants.TABLE_BASE_ELEC)
public class BaseElec extends BaseData {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "name")
    @Schema(description = "基础类型名字")
    private String name;

    @TableField(value = "type")
    @Schema(description = "基础类型")
    private String type;

    @TableField(value = "sort")
    @Schema(description = "排序")
    private Integer sort;

    public enum Type {
        ZHI_NENG_MEN_CI("zhinengmenci"),
        ZHI_NENG_MEN_JIN("zhinengmenjin"),
        ZAO_SHENG("zaosheng"),
        YAN_WU("yanwu"),
        ZHI_NENG_CHU_SHI("zhinengchushi"),
        DI_YA_FEN_ZHI("diyafenzhi"),
        DIAN_LAN_GOU_SHUI_WEI("dianlangoushuiwei"),
        HEI_XIA_ZI("heixiazi"),
        ZHI_NENG_JING_GAI("zhinengjinggai"),
        ZHI_NENG_ZHAO_MING("zhinengzhaoming"),
        ZHI_NENG_SHENG_GUANG("zhinengshengguang"),
        ZHI_NENG_KAI_GUAN("zhinengkaiguan"),
        ZHI_NENG_FENG_JI("zhinengfengji"),
        ZHI_NENG_SHUI_BENG("zhinengshuibeng"),
        REN_LIAN_SHI_BIE("renlianshibie"),
        SHE_XIANG_TOU("SHEXIANGTOU"),
        DIAN_LAN_TOU_WEN_DU("dianlantouwendu"),
        SHUI_JIN("shuijin"),
        SF6("SF6"),
        ZHI_NENG_DI_YA_KAI_GUAN("zhinengdiyakaiguan"),
        JU_FANG("jufang"),
        WEI_DUAN("weiduan"),
        ZHI_NENG_DIAN_BIAO("zhinengdianbiao"),
        XU_DIAN_CHI("xudianchi"),
        BIAN_YA_QI("bianyaqi");

        private String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Type fromValue(String value) {
            for (Type type : Type.values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return BIAN_YA_QI;
        }
    }
}
