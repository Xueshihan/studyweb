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

import java.util.Date;

@Data
@ToString(callSuper = true)
@TableName(value = SqlConstants.TABLE_USER)
public class User extends BaseData {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "account")
    @Schema(description = "账号", defaultValue = "admin")
    private String account;

    @TableField(value = "password")
    @JSONField(serialize = false)
    private String password;

    @TableField(value = "role_id")
    @Schema(description = "角色id", defaultValue = "1")
    private Long roleId;

    @TableField(value = "station_id")
    @Schema(description = "站点id", defaultValue = "1")
    private Long stationId;

    @TableField(value = "real_name")
    @Schema(description = "真实名字", defaultValue = "Tom")
    private String realName;

    @TableField(value = "phone")
    @Schema(description = "电话", defaultValue = "18800000000")
    private String phone;

    @TableField(value = "email")
    @Schema(description = "邮箱", defaultValue = "test@163.com")
    private String email;

    @TableField(value = "salt")
    @Schema(description = "盐值(6位)", defaultValue = "P2z$dI")
    private String salt;

    @TableField(value = "uuid")
    @Schema(description = "uuid", defaultValue = "395967814197837824")
    private String uuid;

    @TableField(value = "navigator")
    @JSONField(serialize = false)
    @Hidden
    private String navigator;

    @TableField(value = "last_login_ip")
    @JSONField(serialize = false)
    @Hidden
    private String lastLoginIp;

    @TableField(value = "last_login_time")
    @JSONField(serialize = false)
    @Hidden
    private Date lastLoginTime;

    @TableField(value = "wrong_time")
    @JSONField(serialize = false)
    @Hidden
    private Date wrongTime;

    @TableField(value = "wrong_num")
    @JSONField(serialize = false)
    @Hidden
    private Integer wrongNum;

    @TableField(value = "lock_time")
    @JSONField(serialize = false)
    @Hidden
    private Date lockTime;

    @TableField(value = "modify_time")
    @JSONField(serialize = false)
    @Hidden
    private Date modifyTime;

    @TableField(value = "expire_time")
    @JSONField(serialize = false)
    @Hidden
    private Date expireTime;

    @TableField(exist = false)
    private Role role;

    @TableField(exist = false)
    private Station station;

    public static final String DEFAULT_PASSWORD = "ABcd1234#";
}
