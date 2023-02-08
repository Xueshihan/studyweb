package cn.iatc.web.controller.role;

import cn.hutool.core.util.StrUtil;
import cn.iatc.database.entity.Role;
import cn.iatc.web.common.data.RestResponse;
import cn.iatc.web.common.data.Status;
import cn.iatc.web.common.data.exception.BaseException;
import cn.iatc.web.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
class RoleListRequestBody {
    @Schema(description = "角色名字")
    String name;
}

@Data
class RolesInfo {
    private List<Role> roles;
}

@Tag(name = "role", description = "角色相关")
@Slf4j
@RestController
@RequestMapping("/role")
public class RoleListController {

    @Autowired
    private RoleService roleService;

    @Operation(summary = "获取角色列表",description = "登录后访问")
    @PostMapping("/list")
    public RestResponse<RolesInfo> getRoleList(@RequestHeader(value = "token") String token, @RequestBody RoleListRequestBody requestBody) {
        RestResponse<RolesInfo> response = new RestResponse<>();
        try {
            this.getRoles(response, requestBody.getName());
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }

        return response;
    }

    private void getRoles(RestResponse<RolesInfo> response, String name) {
        List<Role> roles;
        if(StrUtil.isBlank(name)) {
            roles = roleService.findAll();
        } else {
            roles = roleService.findListLikeByName(name);
        }
        RolesInfo rolesInfo = new RolesInfo();
        rolesInfo.setRoles(roles);
        response.setSuccess(rolesInfo);

    }
}
