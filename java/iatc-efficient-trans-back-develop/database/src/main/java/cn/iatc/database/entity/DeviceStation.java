package cn.iatc.database.entity;

import cn.iatc.database.constants.SqlConstants;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

// 设备与站点关联表
@Data
@ToString(callSuper = true)
@TableName(value = SqlConstants.TABLE_DEVICE_STATION)
public class DeviceStation {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "device_id")
    private Long deviceId;

    @TableField(value = "station_id")
    private Long stationId;
}
