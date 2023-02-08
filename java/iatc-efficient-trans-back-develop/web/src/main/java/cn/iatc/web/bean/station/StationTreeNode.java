package cn.iatc.web.bean.station;

import cn.iatc.database.entity.GeneralType;
import cn.iatc.database.entity.Region;
import cn.iatc.database.entity.StationType;
import cn.iatc.web.common.tree.data.BaseNode;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class StationTreeNode<T, E> extends BaseNode<T, E> {

    // 父类已有，就用父类的
//    private Long id;

    @Schema(description = "站点编号")
    private String code;

    @Schema(description = "站点名字")
    private String name;

    //级别 0-省公司，1-市公司，2-区供电公司，3-线路，4-站房类型，5-具体站房，6-高压进线柜，7-高压出线柜，8-变压器，
    // 40-低压进线柜，41-低压出线柜，42-低压开关，43-开关三相(A,B,C)
    // 父类已有，就用父类的
//    private Integer level;

    //区域id, 只有level=0,1,2,3,4才有
    @Schema(description = "区域id")
    private Long regionId;

    //站房数量 当level=0,1,2,3,4时候有值'
    @Schema(description = "站房数量")
    private Integer stationCount;

    @Schema(description = "容量")
    private String capacity;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "pms_id")
    private String pmsId;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "经度")
    private String longitude;

    @Schema(description = "纬度")
    private String latitude;

    //站房类型id,level=4才有
    @Schema(description = "站房类型id，level=4")
    private Long stationTypeId;

    @Schema(description = "通用类型id")
    private Long generalTypeId;

    //智能化建设时间 level=5，6，7，8，40，41，42，43才有值
    @Schema(description = "智能化建立时间")
    private Date smartTime;

    @Schema(description = "运行状态")
    private Integer runningStatus;

    // 联系人名
    @Schema(description = "联系人名")
    private String contactName;

    // 联系电话
    @Schema(description = "联系人手机号")
    private String contactPhone;

    // 全景url
    @Schema(description = "全景url")
    private String panoramacaUrl;

    @JSONField(serialize = false)
    @Hidden
    private Integer options;

    // 动环状态
    @Schema(description = "动环状态")
    private Integer powerEnvFlag;

    // 网管状态
    @Schema(description = "网管状态")
    private Integer gatewayFlag;

    // 是否是地上状态
    @Schema(description = "是否是地上状态")
    private Integer onlandsFlag;

    @Schema(description = "排序序号")
    private Integer sort;

    @Schema(description = "智能状态 level=42才有")
    private Integer smartStatus;

    private Date createdTime;

    private Date updatedTime;

    private Region region;

    @Hidden
    private StationType stationType;

    @Hidden
    private GeneralType generalType;


}
