package cn.iatc.web.bean.region;

import cn.iatc.database.entity.Region;
import cn.iatc.database.entity.Station;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class RegionPojo extends Region {

    private String upperName;

    @JSONField(serialize = false)
    @Hidden
    private Long stationId;

    @JSONField(serialize = false)
    @Hidden
    private String stationCode;

    @JSONField(serialize = false)
    @Hidden
    private String stationName;

    @Hidden
    private Station station;
}
