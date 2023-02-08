package cn.iatc.database.entity;

import cn.iatc.database.constants.SqlConstants;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@TableName(value = SqlConstants.TABLE_STATISTIC_DAY_POWER)
public class StatisticDayPower extends BaseData{
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "station_id")
    private Long stationId;

    @TableField(value = "phase")
    private Integer phase;

    @TableField(value = "high_efficiency")
    private String highEfficiency;

    @TableField(value = "low_efficiency")
    private String lowEfficiency;

    @TableField(value = "active_power")
    private String activePower;

    @TableField(value = "loss_power")
    private String lossPower;

    @TableField(value = "year")
    private Integer year;

    @TableField(value = "month")
    private Integer month;

    @TableField(value = "day")
    private Integer day;

}
