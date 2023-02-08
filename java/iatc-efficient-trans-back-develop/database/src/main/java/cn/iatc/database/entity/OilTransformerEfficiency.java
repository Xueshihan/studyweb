package cn.iatc.database.entity;

import cn.iatc.database.constants.SqlConstants;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

// 等级表——10kv油浸式三相双绕组无励磁调压配电变压器能效等级excel
@Data
@ToString(callSuper = true)
@TableName(value = SqlConstants.TABLE_OIL_TRANSFORMER_EFFICIENCY)
public class OilTransformerEfficiency extends BaseData {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "level")
    @Schema(description = "变压器台账等级")
    private Integer level;

    @TableField(value = "sffjb")
    @Schema(description = "是否非晶变")
    private String sffjb;

    @TableField(value = "edrl")
    @Schema(description = "额定容量(kVA)")
    private String edrl;

    @TableField(value = "kzsh")
    @Schema(description = "空载损耗(W)")
    private String kzsh;

    @TableField(value = "ljzbh")
    @Schema(description = "联接组标号")
    private String ljzbh;

    @TableField(value = "fzsh")
    @Schema(description = "负载损耗(W)")
    private Integer fzsh;

    @TableField(value = "dlzk")
    @Schema(description = "短路阻抗(%)")
    private String dlzk;


}
