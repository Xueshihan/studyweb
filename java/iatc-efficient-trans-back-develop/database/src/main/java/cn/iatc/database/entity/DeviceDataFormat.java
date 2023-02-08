package cn.iatc.database.entity;

import cn.iatc.database.constants.SqlConstants;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

// 数据格式表
@Data
@ToString(callSuper = true)
@TableName(value = SqlConstants.TABLE_DEVICE_DATA_FORMAT)
public class DeviceDataFormat extends BaseData {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "device_type_id")
    private Long deviceTypeId;

    @TableField(value = "base_property_id")
    private Long basePropertyId;

    @TableField(value = "data_prefix")
    private String dataPrefix;

    @TableField(value = "data_postfix")
    private String dataPostfix;

    @TableField(value = "start_idx")
    private Integer startIdx;

    @TableField(value = "byte_cnt")
    private Integer byteCnt;

    @TableField(value = "data_scale")
    private Integer dataScale;

    @TableField(value = "ishbyte_right")
    private Integer ishbyteRight;

    @TableField(value = "need_complement")
    private Integer needComplement;

    @TableField(value = "ratio_val")
    private String ratioVal;

    @TableField(value = "revise_val")
    private String reviseVal;

    @TableField(value = "ishex")
    private Integer ishex;

    @TableField(value = "isbin")
    private Integer isbin;

    @TableField(value = "bin_idx")
    private Integer binIdx;

    @TableField(value = "bin_cnt")
    private Integer binCnt;

    @TableField(value = "hbit_only_sign")
    private Integer hbitOnlySign;

    @TableField(value = "scale_type")
    private String scaleType;

    @TableField(value = "value_range")
    private String valueRange;

    @TableField(value = "options")
    private Integer options;

    @TableField(value = "remark")
    private String remark;
}
