package cn.iatc.mqtt_server.bean.transformerInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TransformerInfoPojo {
    private Long id;

    private Long stationId;

    @Schema(description = "额定容量(kVA)")
    private String edrl;

    @Schema(description = "阻抗电压(%)")
    private String zkdy;

    @Schema(description = "空载电流(%)")
    private String kzdl;

    @Schema(description = "短路损耗(W)")
    private String dlsh;

    @Schema(description = "空载损耗(W)")
    private String kzsh;

    @Schema(description = "电压比")
    private String dyb;

    @Schema(description = "高压侧额定电流(A)")
    private String gyceddl;

    @Schema(description = "电压等级代码")
    private String dydjdm;
}
