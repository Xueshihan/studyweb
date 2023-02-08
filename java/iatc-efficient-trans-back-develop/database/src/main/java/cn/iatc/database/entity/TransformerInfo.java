package cn.iatc.database.entity;

import cn.iatc.database.constants.SqlConstants;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

// 台账表——高效节能变压器试点情况统计excel
@Data
@ToString(callSuper = true)
@TableName(value = SqlConstants.TABLE_TRANSFORMER_INFO)
public class TransformerInfo extends BaseData {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "station_id")
    @Schema(description = "站点id")
    private Long stationId;

    @TableField(value = "sbmc")
    @Schema(description = "设备名称")
    private String sbmc;

    @TableField(value = "edrl")
    @Schema(description = "额定容量(kVA)")
    private String edrl;

    @TableField(value = "zkdy")
    @Schema(description = "阻抗电压(%)")
    private String zkdy;

    @TableField(value = "kzdl")
    @Schema(description = "空载电流(%)")
    private String kzdl;

    @TableField(value = "dlsh")
    @Schema(description = "短路损耗(W)")
    private String dlsh;

    @TableField(value = "kzsh")
    @Schema(description = "空载损耗(W)")
    private String kzsh;

    @TableField(value = "dyb")
    @Schema(description = "电压比")
    private String dyb;

    @TableField(value = "gyceddl")
    @Schema(description = "高压侧额定电流(A)")
    private String gyceddl;

    @TableField(value = "yxbh")
    @Schema(description = "运行编号")
    private String yxbh;

    @TableField(value = "zxmc")
    @Schema(description = "主线名称")
    private String zxmc;

    @TableField(value = "ssgt")
    @Schema(description = "所属杆塔")
    private String ssgt;

    @TableField(value = "syxz")
    @Schema(description = "使用性质")
    private String syxz;

    @TableField(value = "ssds")
    @Schema(description = "所属地市")
    private String ssds;

    @TableField(value = "ywdw")
    @Schema(description = "运维单位")
    private String ywdw;

    @TableField(value = "whbz")
    @Schema(description = "维护班组")
    private String whbz;

    @TableField(value = "ssdkx")
    @Schema(description = "所属大馈线")
    private String ssdkx;

    @TableField(value = "gdqy")
    @Schema(description = "供电区域")
    private String gdqy;

    @TableField(value = "dydj")
    @Schema(description = "电压等级")
    private String dydj;

    @TableField(value = "dkxzx")
    @Schema(description = "大馈线支线")
    private String dkxzx;

    @TableField(value = "sbzt")
    @Schema(description = "设备状态")
    private String sbzt;

    @TableField(value = "tyrq")
    @Schema(description = "投运日期")
    private Date tyrq;

    @TableField(value = "sfdw")
    @Schema(description = "是否代维")
    private String sfdw;

    @TableField(value = "sfnw")
    @Schema(description = "是否农网")
    private String sfnw;

    @TableField(value = "zycd")
    @Schema(description = "重要程度")
    private String zycd;

    @TableField(value = "zcxz")
    @Schema(description = "资产性质")
    private String zcxz;

    @TableField(value = "zcdw")
    @Schema(description = "资产单位")
    private String zcdw;

    @TableField(value = "zcbh")
    @Schema(description = "资产编号")
    private String zcbh;

    @TableField(value = "sbzjfs")
    @Schema(description = "设备增加方式")
    private String sbzjfs;

    @TableField(value = "xh")
    @Schema(description = "型号")
    private String xh;

    @TableField(value = "sccj")
    @Schema(description = "生产厂家")
    private String sccj;

    @TableField(value = "ccbh")
    @Schema(description = "出厂编号")
    private String ccbh;

    @TableField(value = "dxbm")
    @Schema(description = "单项编码")
    private String dxbm;

    @TableField(value = "ccrq")
    @Schema(description = "出厂日期")
    private Date ccrq;

    @TableField(value = "dxmc")
    @Schema(description = "单项名称")
    private String dxmc;

    @TableField(value = "jyjz")
    @Schema(description = "绝缘介质")
    private String jyjz;

    @TableField(value = "sffjb")
    @Schema(description = "是否非晶变")
    private String sffjb;

    @TableField(value = "wzkgfjds")
    @Schema(description = "无载开关分接档数")
    private String wzkgfjds;

    @TableField(value = "wzkgfjwz")
    @Schema(description = "无载开关分接位置")
    private String wzkgfjwz;

    @TableField(value = "xmmc")
    @Schema(description = "项目名称")
    private String xmmc;

    @TableField(value = "dyceddl")
    @Schema(description = "低压侧额定电流(A)")
    private String dyceddl;

    @TableField(value = "xmbm")
    @Schema(description = "项目编码")
    private String xmbm;

    @TableField(value = "jddz")
    @Schema(description = "接地电阻(Ω)")
    private String jddz;

    @TableField(value = "yh")
    @Schema(description = "油号")
    private String yh;

    @TableField(value = "yz")
    @Schema(description = "油重(kg)")
    private String yz;

    @TableField(value = "zz")
    @Schema(description = "总重(kg)")
    private String zz;

    @TableField(value = "lx")
    @Schema(description = "类型")
    private String lx;

    @TableField(value = "jynrdj")
    @Schema(description = "绝缘耐热等级")
    private String jynrdj;

    @TableField(value = "sbbm")
    @Schema(description = "设备编码")
    private String sbbm;

    @TableField(value = "djsj")
    @Schema(description = "登记时间")
    private Date djsj;

    @TableField(value = "bz")
    @Schema(description = "备注")
    private String bz;

    @TableField(value = "ljzbh")
    @Schema(description = "联接组标号")
    private String ljzbh;

    @TableField(value = "gnwz")
    @Schema(description = "功能位置")
    private String gnwz;

    @TableField(value = "swid")
    @Schema(description = "实物ID")
    private String swid;

    @TableField(value = "tzcjfs")
    @Schema(description = "台账创建方式")
    private String tzcjfs;

    @TableField(value = "ywsbzl")
    @Schema(description = "有无设备资料")
    private String ywsbzl;

    @TableField(value = "sfctsb")
    @Schema(description = "是否成套设备")
    private String sfctsb;

    @TableField(value = "pmbm")
    @Schema(description = "PM编码")
    private String pmbm;

    @TableField(value = "ssxlmc")
    @Schema(description = "所属线路名称")
    private String ssxlmc;

    @TableField(value = "zyfl")
    @Schema(description = "专业分类")
    private String zyfl;

    @TableField(value = "zltxsl")
    @Schema(description = "资料图像数量")
    private String zltxsl;

    @TableField(value = "sblxbm")
    @Schema(description = "设备类型编码")
    private String sblxbm;

    @TableField(value = "dydjmc")
    @Schema(description = "电压等级名称")
    private String dydjmc;

    @TableField(value = "dydjdm")
    @Schema(description = "电压等级代码")
    private String dydjdm;

    @TableField(value = "ssdkxid")
    @Schema(description = "所属大馈线id")
    private String ssdkxid;

    @TableField(value = "dqtz")
    @Schema(description = "地区特征")
    private String dqtz;

    @TableField(value = "dkxzxid")
    @Schema(description = "大馈线支线id")
    private String dkxzxid;

    @TableField(value = "dxmpyxkid")
    @Schema(description = "电系铭牌运行库id")
    private String dxmpyxkid;

    @TableField(value = "obj_id")
    @Schema(description = "obj_id")
    private String objId;

    // 电压等级代码对应的额定电压
    public enum Dydjdm {
        KV_10("22", "100000");
        private String key;
        private String value;

        Dydjdm(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return this.key;
        }

        public String getValue() {
            return this.value;
        }

        public static Dydjdm fromKey(String key) {
            for (Dydjdm dydjdm : Dydjdm.values()) {
                if (dydjdm.getKey().equals(key)) {
                    return dydjdm;
                }
            }
            return KV_10;
        }

        public static Dydjdm fromValue(String value) {
            for (Dydjdm dydjdm : Dydjdm.values()) {
                if (dydjdm.getValue().equals(value)) {
                    return dydjdm;
                }
            }
            return KV_10;
        }
    }


}
