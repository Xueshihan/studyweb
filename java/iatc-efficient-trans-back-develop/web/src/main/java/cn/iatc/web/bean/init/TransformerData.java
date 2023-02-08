package cn.iatc.web.bean.init;

import cn.iatc.database.entity.TransformerInfo;
import com.baomidou.mybatisplus.annotation.TableField;

public class TransformerData extends TransformerInfo {
    @TableField(exist = false)
    private String stationCode;
}
