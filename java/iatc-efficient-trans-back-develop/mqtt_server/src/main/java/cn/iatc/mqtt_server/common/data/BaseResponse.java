package cn.iatc.mqtt_server.common.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 基础返回响应
 * 成功响应：status=1,code=200
 * 失败响应：status=0, code=非200的值都是失败
 * 默认是成功响应
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {

    private Integer status = StatusRes.SUCCESS.getValue();

    private String code = Status.SUCCESS.getCode();

    private String message = Status.SUCCESS.getMessage();


    public enum StatusRes {
        SUCCESS(1),
        FAIL(0);

        private Integer value;
        StatusRes(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return this.value;
        }
    }
}
