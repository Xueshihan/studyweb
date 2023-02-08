package cn.iatc.database.entity;

import cn.iatc.database.constants.Constants;
import cn.iatc.database.constants.SqlConstants;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@TableName(value = SqlConstants.TABLE_REALTIME_POWER)
public class RealTimePower extends BaseData {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "station_id")
    private Long stationId;

    @TableField(value = "phase")
    private Integer phase;

    @TableField(value = "high_current")
    private String highCurrent;

    @TableField(value = "high_voltage")
    private String highVoltage;

    @TableField(value = "low_current")
    private String lowCurrent;

    @TableField(value = "low_voltage")
    private String lowVoltage;

    @TableField(value = "power_factor")
    private String powerFactor;

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

    @TableField(value = "hour")
    private Integer hour;

    @TableField(value = "minute")
    private Integer minute;

    // 数据来源1-真实，0-虚拟
    @TableField(value = "data_source")
    private Integer dataSource = Constants.Status.ENABLED.getValue();

    public enum DataSource {
        VIRTUAL(0), // 虚拟
        REAL(1); // 真实

        private Integer value;

        DataSource(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return this.value;
        }
    }

}
