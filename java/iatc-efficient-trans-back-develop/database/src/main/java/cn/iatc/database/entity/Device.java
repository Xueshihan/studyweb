package cn.iatc.database.entity;

import cn.iatc.database.constants.SqlConstants;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;
import lombok.ToString;

// 设备表
@Data
@ToString(callSuper = true)
@TableName(value = SqlConstants.TABLE_DEVICE)
public class Device extends BaseData {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "sn")
    private String sn;

    @TableField(value = "name")
    private String name;

    @TableField(value = "capacity")
    private String capacity;

    @TableField(value = "point_id")
    private String pointId;

    @TableField(value = "remark")
    private String remark;

    @TableField(value = "address")
    private String address;

    @TableField(value = "device_type_id")
    @JSONField(serialize = false)
    @Hidden
    private Long deviceTypeId;

    @TableField(value = "gateway_id")
    private String gatewayId;

    @TableField(value = "online_status")
    private Integer onlineStatus;

    @TableField(exist = false)
    private DeviceType deviceType;

}
