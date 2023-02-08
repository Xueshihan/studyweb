package cn.iatc.database.entity;

import cn.iatc.database.constants.SqlConstants;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

// 角色表
@Data
@ToString(callSuper = true)
@TableName(value = SqlConstants.TABLE_ROLE)
public class Role extends BaseData {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "name")
    private String name;

    @TableField(value = "type")
    private Integer type;

    @TableField(value = "remark")
    private String remark;

    public enum Type {
        SUPER(1), //超级管理员
        COMMON(2); // 普通管理员

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
            return COMMON;
        }
    }
}
