package cn.iatc.web.bean.device;

import cn.iatc.database.entity.Device;
import cn.iatc.database.entity.Station;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DevicePojo extends Device {

    @JSONField(serialize = false)
    @Hidden
    private Long stationId;

    @Schema(description = "站点信息")
    private Station station;
}
