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

// 通用类型
@Data
@ToString(callSuper = true)
@TableName(value = SqlConstants.TABLE_GENERAL_TYPE)
public class GeneralType extends BaseData {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "name")
    @Schema(description = "类型名字")
    private String name;

    @TableField(value = "code")
    @Schema(description = "类型code")
    private String code;

    @TableField(value = "base_elec_id")
    @Schema(description = "基础电力设备类型id")
    private Long baseElecId;

    @TableField(value = "sort")
    @Schema(description = "排序")
    private Integer sort;

    @TableField(exist = false)
    @JSONField(serialize = false)
    @Hidden
    private BaseElec baseElec;
}
