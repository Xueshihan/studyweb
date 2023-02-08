package cn.iatc.web.controller.login;

import cn.hutool.core.util.StrUtil;
import cn.iatc.redis.RedisHelper;
import cn.iatc.web.common.data.RestResponse;
import cn.iatc.web.common.data.Status;
import cn.iatc.web.constants.RedisConstant;
import cn.iatc.web.utils.RandomValidUtil;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Data
class CodeResponseData {
    String base64Img;
    String code;
}

@Tag(name = "login", description = "登陆")
@Slf4j
@RestController
public class LoginCodeController {

    @Autowired
    private RedisHelper redisHelper;

    @Operation(summary = "获取登陆数字code码",description = "登录前访问")
    @GetMapping("/login/code")
    public RestResponse<CodeResponseData> getCode() {
        RestResponse<CodeResponseData> response = new RestResponse<>();
        CodeResponseData codeResponseData = new CodeResponseData();
        try {
            Map<String, String> codeImgInfo = RandomValidUtil.getValidCode();
            codeResponseData.setBase64Img(codeImgInfo.get("imageBase64"));

            String code = codeImgInfo.get("code");
            String verifyCodeKey = StrUtil.format("{}{}/{}", RedisConstant.KEY_BASE, RedisConstant.KEY_LOGIN_VERIFY_CODE, code.toLowerCase());
            redisHelper.increment(verifyCodeKey);
            redisHelper.expire(verifyCodeKey, 5 * 60);
            codeResponseData.setCode(code);
            response.setSuccess(codeResponseData);
        }  catch (Exception exception) {
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }
}
