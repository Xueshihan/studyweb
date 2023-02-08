package cn.iatc.web.bean.token;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Token {
    private String accessToken;
}
