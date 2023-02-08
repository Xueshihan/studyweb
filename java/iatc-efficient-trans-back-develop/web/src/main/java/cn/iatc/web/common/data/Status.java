package cn.iatc.web.common.data;

import cn.iatc.web.common.data.exception.IStatus;
import lombok.Getter;

@Getter
public enum Status implements IStatus {

    EXCEPTION("0", "请求失败"),
    SUCCESS("200", "操作成功！"),

    DELETE_ERROR("290", "删除失败，请先删除下属子集"),

    /**
     * token 解析失败，请尝试重新登录！
     */
    TOKEN_INVALID("300", "token无效，请重新登录！"),

    /**
     * token 为空！
     */
    TOKEN_EMPTY("301", "token 不能为空！"),
    /**
     * token 已过期，请重新登录！
     */
    TOKEN_EXPIRED("302", "token 已过期，请重新登录"),

    LOGIN_LOCK("340", "账号被锁定，请5分钟后再重试"),

    LOGIN_VERIFY_CODE_ERROR("341", "登陆验证码无效"),

    USER_ID_INVALID("350", "用户id无效"),
    USER_NAME_INVALID("351", "用户名无效"),

    USER_REAL_NAME_INVALID("352", "用户真实姓名无效"),

    USER_PASSWORD_INVALID("353", "用户密码无效"),
    USER_PASSWORD_ERROR("354", "用户密码错误"),
    USER_NOT_EXIST("355", "用户不存在"),

    USER_EXIST("356", "用户已存在"),

    USER_ROLE_NOT_EXIST("357", "用户无角色"),

    USER_SALT_INVALID("358", "盐值无效"),

    USER_UUID_INVALID("359", "uuid无效"),

    USER_SUPER_NO_DELETE("360", "超级用户不能删除"),

    USER_SELF_NO_DELETE("361", "用户不能删除自己"),

    ROLE_ID_INVALID("380", "角色id无效"),
    ROLE_NAME_INVALID("381", "角色名无效"),
    ROLE_REMARK_INVALID("382", "角色描述无效"),
    ROLE_SUPER_DELETE("383", "超级用户不能删除"),

    MENU_ID_INVALID("400", "菜单id无效"),

    REGION_ID_INVALID("420", "区域id无效"),

    REGION_NAME_INVALID("421", "区域名字无效"),

    REGION_NAME_PINYIN_INVALID("422", "区域名字拼音无效"),

    REGION_CODE_INVALID("423", "区域编码无效"),

    REGION_CODE_EXIST("424", "区域编码已存在"),

    REGION_LEVEL_INVALID("425", "区域level无效"),

    REGION_NATIONAL_CODE_INVALID("426", "区域国标列无效"),

    REGION_SORT_INVALID("427", "区域顺序无效"),

    REGION_UPPER_INVALID("428", "父级id无效"),
    STATION_ID_INVALID("450", "站点id无效"),

    STATION_CODE_INVALID("451", "站点编码无效"),
    STATION_NAME_INVALID("452", "站点名字无效"),
    STATION_ADDRESS_INVALID("453", "站点地址无效"),
    STATION_COUNT_INVALID("454", "站点数量无效"),
    STATION_UPPER_ID_INVALID("455", "站点父id无效"),
    STATION_UPPER_ID_ERROR("456", "站点父id错误"),
    STATION_LEVEL_INVALID("457", "站点level无效"),
    STATION_PMSID_INVALID("458", "站点pmsId无效"),
    STATION_SORT_INVALID("459", "站点序号无效"),
    STATION_CAPACITY_INVALID("460", "站点设备容量无效"),
    STATION_EXIST("461", "站点已存在"),
    STATION_NEXT_LEVEL_EXIST("462", "下属子集存在，请先删除下属子集"),
    STATION_MIGRATE_LEVEL("463", "站房迁移级别错误，请选择正确迁移点"),
    STATION_VIDEO_ID_INVALID("464", "站点视频id无效"),
    STATION_VIDEO_NAME_INVALID("465", "站点视频名字无效"),
    STATION_VIDEO_URL_INVALID("466", "站点视频地址无效"),
    FACTORY_ID_INVALID("480", "厂商id无效"),
    DEVICE_ID_INVALID("500", "设备id无效"),
    DEVICE_EXIST("501", "设备已存在"),
    DEVICE_NAME_INVALID("502", "设备名字无效"),
    DEVICE_SN_INVALID("503", "设备sn无效"),
    DEVICE_TYPE_ID_INVALID("504", "设备类型id无效"),

    PARAMETER_INVALID("801", "传参错误"),

    ;

    private String code;

    private String message;

    Status(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Status fromCode(String code) {
        Status[] statuses = Status.values();
        for (Status status : statuses) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return SUCCESS;
    }
}
