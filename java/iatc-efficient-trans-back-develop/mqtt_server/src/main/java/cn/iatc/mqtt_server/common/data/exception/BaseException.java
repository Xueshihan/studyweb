package cn.iatc.mqtt_server.common.data.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 异常处理基类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseException extends RuntimeException {

    private String code;

    private String message;

    public BaseException(IStatus iStatus) {
        super(iStatus.getMessage());
        this.code = iStatus.getCode();
        this.message = iStatus.getMessage();
    }

    public BaseException(IStatus iStatus, String message) {
        super(iStatus.getMessage());
        this.code = iStatus.getCode();
        if (message.isEmpty()) {
            this.message = iStatus.getMessage();
        } else {
            this.message = message;
        }
    }
}
