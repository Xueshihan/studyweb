package cn.iatc.database.entity;

import cn.iatc.database.constants.SqlConstants;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

// 站点关系表
@Data
@ToString(callSuper = true)
@TableName(value = SqlConstants.TABLE_STATION_RELATION)
public class StationRelation {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "cur_station_id")
    private Long curStationId;

    @TableField(value = "upper_id")
    private Long upperId;

    @TableField(value = "upper_id_set")
    private String upperIdSet;
}
