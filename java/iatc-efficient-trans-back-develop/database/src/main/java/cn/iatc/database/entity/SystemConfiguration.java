package cn.iatc.database.entity;

import cn.iatc.database.constants.SqlConstants;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

// 系统配置表
@Data
@ToString(callSuper = true)
@TableName(value = SqlConstants.TABLE_SYSTEM_CONFIGURATION)
public class SystemConfiguration extends BaseData{
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //配置类型，唯一  producer, logo, title,expired, phone, create_time, word_path
    @TableField(value = "type")
    private String type;

    @TableField(value = "value")
    private String value;

    public enum Type {
        PRODUCER("producer"),
        LOGO("logo"),
        TITLE("title"),
        EXPIRED("expired"),
        PHONE("phone"),
        CREATE_TIME("create_time"),
        WORD_PATH("word_path");

        String value;

        public String getValue() {
            return this.value;
        }

        Type(String value) {
            this.value = value;
        }

        public static Type fromValue(String value) {
            for (Type type : Type.values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            return PRODUCER;
        }
    }
}
