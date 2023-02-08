package cn.iatc.mqtt_server.common.data;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;

/**
 * 返回的响应内容
 * @param <T>
 */
@Data
public class RestResponse<T> extends BaseResponse{

    private T data;

    private String callBack;

    @Hidden //为了解决swagger文档里生成success字段
    public void setSuccess(T data) {
        this.setSuccess(Status.SUCCESS.getCode(), Status.SUCCESS.getMessage(), data);
    }

    @Hidden
    public void setSuccess(String code, String message) {
        this.setSuccess(code, message, null);
    }

    @Hidden
    public void setSuccess(String code, String message, T data) {
        this.setCode(code);
        this.setMessage(message);
        this.setData(data);
        this.setStatus(StatusRes.SUCCESS.getValue());
    }

    public void setFail(String code, String message) {
        this.setCode(code);
        this.setMessage(message);
        this.setStatus(StatusRes.FAIL.getValue());
    }
}
