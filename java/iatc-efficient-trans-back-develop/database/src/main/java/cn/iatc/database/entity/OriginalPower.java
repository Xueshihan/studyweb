package cn.iatc.database.entity;

import cn.iatc.database.constants.SqlConstants;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString(callSuper = true)
@TableName(value = SqlConstants.TABLE_ORIGINAL_POWER)
public class OriginalPower {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 设备唯一码
    @TableField(value = "code")
    private String code;

    // 电流
    @TableField(value = "type")
    private String type;

    // 电压 高压设备无电压
    @TableField(value = "value")
    private String value;

    @TableField(value = "created_time")
    private Date createdTime;
}
