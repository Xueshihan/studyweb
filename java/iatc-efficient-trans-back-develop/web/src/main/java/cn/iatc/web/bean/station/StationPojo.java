package cn.iatc.web.bean.station;

import cn.iatc.database.entity.Station;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class StationPojo extends Station {
    // 父id
    private Long upperId;

}
