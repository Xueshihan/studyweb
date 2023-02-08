package cn.iatc.web.controller.role;

import cn.hutool.core.util.StrUtil;
import cn.iatc.database.entity.Role;
import cn.iatc.web.common.data.RestResponse;
import cn.iatc.web.common.data.Status;
import cn.iatc.web.common.data.exception.BaseException;
import cn.iatc.web.service.RoleService;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
class CreateRoleRequestBody {
    @Schema(description = "角色名字", defaultValue = "管理员")
    private String name;

    @Schema(description = "角色备注", defaultValue = "管理员")
    private String remark;

    @Schema(description = "菜单列表", defaultValue = "[1,2,3]")
    private List<Long> menuIds;
}

@Data
class UpdateRoleRequestBody {
    @Schema(description = "角色id", defaultValue = "1")
    private Long roleId;

    @Schema(description = "角色名字", defaultValue = "管理员")
    private String name;

    @Schema(description = "角色备注", defaultValue = "管理员")
    private String remark;

    @Schema(description = "菜单列表", defaultValue = "[1,2,3]")
    private List<Long> menuIds;
}

@Data
class DeleteRoleRequestBody {

    @Schema(description = "角色id", defaultValue = "1")
    private Long roleId;
}

@Tag(name = "role", description = "角色相关")
@Slf4j
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Operation(summary = "添加角色",description = "登录后访问")
    @PostMapping("/role")
    public RestResponse<String> addRole(@RequestHeader(value = "token") String token, @RequestBody CreateRoleRequestBody requestBody) {
        RestResponse<String> response = new RestResponse<>();
        try {
            this.checkCreateRequestBody(requestBody);
            this.createRole(requestBody);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void checkCreateRequestBody(CreateRoleRequestBody requestBody) {
        String name = requestBody.getName();
        String remark = requestBody.getRemark();
        if (StrUtil.isBlank(name)) {
            throw new BaseException(Status.ROLE_NAME_INVALID);
        }
    }

    private void createRole(CreateRoleRequestBody requestBody) {
        String name = requestBody.getName();
        String remark = requestBody.getRemark();
        List<Long> menuIds = requestBody.getMenuIds();
        roleService.createRole(name, remark, menuIds);
    }

    @Operation(summary = "修改角色",description = "登录后访问")
    @PutMapping("/role")
    public RestResponse<String> updateRole(@RequestHeader(value = "token") String token, @RequestBody UpdateRoleRequestBody requestBody) {
        RestResponse<String> response = new RestResponse<>();
        try {
            this.checkUpdateRequestBody(requestBody);
            this.handleUpdateRole(requestBody);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void checkUpdateRequestBody(UpdateRoleRequestBody requestBody) {
        Long roleId = requestBody.getRoleId();
        String name = requestBody.getName();
        String remark = requestBody.getRemark();

        if (roleId == null || roleId <= 0) {
            throw new BaseException(Status.ROLE_ID_INVALID);
        }

        if (StrUtil.isBlank(name)) {
            throw new BaseException(Status.ROLE_NAME_INVALID);
        }
    }

    private void handleUpdateRole(UpdateRoleRequestBody requestBody) {
        Long roleId = requestBody.getRoleId();
        String name = requestBody.getName();
        String remark = requestBody.getRemark();
        List<Long> menuIds = requestBody.getMenuIds();
        roleService.updateRole(roleId, name, remark, menuIds);
    }

    @Operation(summary = "删除角色",description = "登录后访问")
    @PostMapping("/role/delete")
    public RestResponse<String> deleteRole(@RequestHeader(value = "token") String token, @RequestBody DeleteRoleRequestBody requestBody) {
        RestResponse<String> response = new RestResponse<>();
        try {
            this.deleteRole(requestBody);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        log.info("delete response:{}", response.toString());
        return response;
    }

    private void deleteRole(DeleteRoleRequestBody requestBody) {
        Long roleId = requestBody.getRoleId();
        Role role = roleService.findById(roleId);
        if (role.getType().equals(Role.Type.SUPER.getValue())) {
            throw new BaseException(Status.ROLE_SUPER_DELETE);
        }
        roleService.deleteRole(roleId);
    }

}
