package cn.iatc.web.controller.user;


import cn.iatc.web.common.data.RestResponse;
import cn.iatc.web.common.data.Status;
import cn.iatc.web.utils.SnowFlakeFactory;
import cn.iatc.web.utils.ToolUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Data
class SaltUUID {
    @Schema(description = "盐值", defaultValue = "133#44")
    private String salt;

    @Schema(description = "uuid", defaultValue = "395967814197837824")
    private String UUID;

}

@Tag(name = "user", description = "用户管理")
@Slf4j
@RestController
@RequestMapping("/user")
public class SaltController {

    @Autowired
    private SnowFlakeFactory snowFlakeFactory;

    @Operation(summary = "获取盐值和uuid",description = "登录后访问")
    @GetMapping("/saltUuid")
    public RestResponse<SaltUUID> getSaltUuid(@RequestHeader("token") String token) {
        RestResponse<SaltUUID> response = new RestResponse<>();
        try {
            SaltUUID saltUUID = new SaltUUID();
            saltUUID.setSalt(ToolUtil.getSalt(6));
            saltUUID.setUUID(snowFlakeFactory.nextIdStr());
            response.setSuccess(saltUUID);
        } catch (Exception exception) {
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }
}
