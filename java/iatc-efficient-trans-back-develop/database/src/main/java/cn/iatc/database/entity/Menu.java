package cn.iatc.database.entity;

import cn.iatc.database.constants.SqlConstants;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

// 菜单表
@Data
@ToString(callSuper = true)
@TableName(value = SqlConstants.TABLE_MENU)
public class Menu extends BaseData {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "title")
    private String title;

    @TableField(value = "name")
    private String name;

    @TableField(value = "type")
    private Integer type;

    @TableField(value = "path")
    private String path;

    @TableField(value = "icon")
    private String icon;

    @TableField(value = "upper_id")
    private Long upperId;

    @TableField(value = "upper_id_set")
    private String upperIdSet;

}
