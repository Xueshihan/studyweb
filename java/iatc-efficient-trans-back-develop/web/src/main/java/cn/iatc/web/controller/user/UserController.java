package cn.iatc.web.controller.user;

import cn.hutool.core.util.StrUtil;
import cn.iatc.database.entity.Role;
import cn.iatc.database.entity.User;
import cn.iatc.web.common.data.RestResponse;
import cn.iatc.web.common.data.Status;
import cn.iatc.web.common.data.exception.BaseException;
import cn.iatc.web.service.RoleService;
import cn.iatc.web.service.UserService;
import cn.iatc.web.utils.MD5Util;
import cn.iatc.web.utils.jwt.JWTAccessData;
import cn.iatc.web.utils.jwt.JWTUtil;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Data
class UserRequestBody {
    @Schema(description = "用户id，创建不传；修改必须传", defaultValue = "1")
    private Long userId;

    @Schema(description = "账号", defaultValue = "admin")
    private String account;

    @Schema(description = "角色id", defaultValue = "1")
    private Long roleId;

    @Schema(description = "站点id", defaultValue = "1")
    private Long stationId;

    private String realName;

    private String phone;

    private String email;

    private String salt;

    private String uuid;
}

@Data
class DeleteUserRequestBody {
    @Schema(description = "用户id列表", defaultValue = "[1]")
    List<Long> userIds;
}

@Data
class UpdatePasswordRequestBody {

    @Schema(description = "用户id")
    Long userId;

    @Schema(description = "旧密码", defaultValue = "123456")
    String oldPassWord;

    @Schema(description = "新密码", defaultValue = "123456")
    String newPassword;
}

@Data
class UserGetResponseData {

    private User user;
}
@Tag(name = "user", description = "用户管理")
@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Operation(summary = "获取用户信息",description = "登录后访问")
    @GetMapping("/user")
    public RestResponse<UserGetResponseData> getUser(@RequestHeader(value = "token") String token) {
        RestResponse<UserGetResponseData> response = new RestResponse<>();
        try {
            JWTAccessData jwtAccessData = JWTUtil.parseAccessToken(token);
            User loginUser = this.getUser(jwtAccessData.getUserId());
            if (loginUser == null) {
                throw new BaseException(Status.USER_NOT_EXIST);
            }
            UserGetResponseData data = new UserGetResponseData();
            data.setUser(loginUser);
            response.setSuccess(data);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private User getUser(Long userId) throws BaseException {
        return userService.findById(userId);
    }

    @Operation(summary = "创建或更新用户",description = "登录后访问")
    @PostMapping("/user")
    public RestResponse<String> createOrUpdateUser(@RequestHeader(value = "token") String token, @RequestBody UserRequestBody requestBody) {
        RestResponse<String> response = new RestResponse<>();
        try {

            this.checkRequestBody(requestBody);
            this.createOrUpdateUserInfo(requestBody);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void checkRequestBody(UserRequestBody requestBody) {
        String account = requestBody.getAccount();
        Long roleId = requestBody.getRoleId();
        Long stationId = requestBody.getStationId();
        String realName = requestBody.getRealName();
        String salt = requestBody.getSalt();
        String uuid = requestBody.getUuid();
        this.checkAccount(account);
        this.checkRole(roleId);
        this.checkStation(stationId);
        this.checkRealName(realName);
        this.checkSalt(salt);
        this.checkUUID(uuid);
    }

    private void checkAccount(String account) {
        if (StrUtil.isBlank(account)) {
            throw new BaseException(Status.USER_NAME_INVALID);
        }
    }

    private void checkRole(Long roleId) {
        if (roleId == null || roleId <= 0) {
            throw new BaseException(Status.ROLE_ID_INVALID);
        }
    }

    private void checkRealName(String realName) {
        if (StrUtil.isBlank(realName)) {
            throw new BaseException(Status.USER_REAL_NAME_INVALID);
        }
    }

    private void checkSalt(String salt) {
        if (StrUtil.isBlank(salt)) {
            throw new BaseException(Status.USER_SALT_INVALID);
        }
    }

    private void checkUUID(String uuid) {
        if (StrUtil.isBlank(uuid)) {
            throw new BaseException(Status.USER_UUID_INVALID);
        }
    }

    private void checkStation(Long stationId) {
        if (stationId == null || stationId <= 0) {
            throw new BaseException(Status.STATION_ID_INVALID);
        }
    }

    @Operation(summary = "更新自己用户信息",description = "登录后访问")
    @PutMapping("/user/self")
    public RestResponse<String> updateUserSelf(@RequestHeader(value = "token") String token, @RequestBody UserRequestBody requestBody) {
        RestResponse<String> response = new RestResponse<>();
        try {
            this.checkUpdateRequestBody(requestBody);
            JWTAccessData jwtAccessData = JWTUtil.parseAccessToken(token);
            this.handleUpdateSelf(jwtAccessData.getUserId(), requestBody);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    public void checkUpdateRequestBody(UserRequestBody requestBody) {
        String realName = requestBody.getRealName();
        this.checkRealName(realName);
    }

    public void handleUpdateSelf(Long userId, UserRequestBody requestBody) {
        User user = new User();
        user.setId(userId);
        user.setRealName(requestBody.getRealName());
        user.setPhone(requestBody.getPhone());
        user.setEmail(requestBody.getEmail());
        userService.updateToUser(user);
    }

    @Operation(summary = "删除用户",description = "登录后访问")
    @PostMapping("/user/delete")
    public RestResponse<String> deleteUser(@RequestHeader(value = "token") String token, @RequestBody DeleteUserRequestBody requestBody) {
        RestResponse<String> response = new RestResponse<>();
        try {

            this.handleDeleteUser(token, requestBody.getUserIds());
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void handleDeleteUser(String token, List<Long> userIds) {

        // 不能删除自己
        JWTAccessData jwtAccessData = JWTUtil.parseAccessToken(token);
        log.info("jwtAccessData:{}", jwtAccessData.toString());
        if (userIds.contains(jwtAccessData.getUserId())) {
            throw new BaseException(Status.USER_SELF_NO_DELETE);
        }

        // 不能删除超级用户
        for (Long userId: userIds) {
            User user = userService.findById(userId);
            if (user != null) {
                Role role = roleService.findById(user.getRoleId());
                if (role != null && role.getType().equals(Role.Type.SUPER.getValue())) {
                    throw new BaseException(Status.USER_SUPER_NO_DELETE);
                }
            }
        }
        userService.deleteBatchByIds(userIds);
    }

    private void createOrUpdateUserInfo(UserRequestBody requestBody) {

        Long userId = requestBody.getUserId();
        String account = requestBody.getAccount();
        User user = userService.findUserByAccount(account);
        if (user != null) {
            if (!user.getId().equals(userId)) {
                throw new BaseException(Status.USER_EXIST);
            }
        }

        Long roleId = requestBody.getRoleId();
        Long stationId = requestBody.getStationId();
        String realName = requestBody.getRealName();
        String salt = requestBody.getSalt();
        String uuid = requestBody.getUuid();

        user = new User();
        user.setAccount(account);
        user.setRoleId(roleId);
        user.setStationId(stationId);
        user.setRealName(realName);
        user.setPhone(requestBody.getPhone());
        user.setEmail(requestBody.getEmail());
        user.setSalt(salt);
        user.setUuid(uuid);
        if(userId == null) {
//            user.setPassword(MD5Util.encodeMd5("123456" + salt));
            user.setPassword(User.DEFAULT_PASSWORD);
            userService.createUser(user);
        } else {
            user.setId(userId);
            user.setUpdatedTime(new Date());
            userService.updateToUser(user);
        }
    }

    @Operation(summary = "修改用户密码",description = "登录后访问")
    @PostMapping("/user/password")
    public RestResponse<String> updatePassword(@RequestHeader(value = "token") String token, @RequestBody UpdatePasswordRequestBody requestBody) {
        RestResponse<String> response = new RestResponse<>();
        try {
            this.checkPasswordRequestBody(requestBody);
            this.handlePassword(requestBody);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void checkPasswordRequestBody(UpdatePasswordRequestBody requestBody) {
        Long userId = requestBody.getUserId();
        String oldPassword = requestBody.getOldPassWord();
        String newPassword = requestBody.getNewPassword();
        if (userId == null || userId <= 0) {
            throw new BaseException(Status.USER_ID_INVALID);
        }

        User user = userService.findById(userId);
        if (user == null) {
            throw new BaseException(Status.USER_NOT_EXIST);
        }

        String oldPasswordMd5 = MD5Util.encodeMd5(oldPassword + user.getSalt()).toLowerCase();
        if (oldPasswordMd5.equals(user.getPassword()) ||
                oldPasswordMd5.equals(MD5Util.encodeMd5(user.getPassword() + user.getSalt()).toLowerCase())) {
            if (StrUtil.isBlank(newPassword)) {
                throw new BaseException(Status.USER_PASSWORD_ERROR, "新密码无效");
            }
        } else {
            throw new BaseException(Status.USER_PASSWORD_ERROR, "原密码不正确");
        }
        requestBody.setNewPassword(MD5Util.encodeMd5(newPassword + user.getSalt()));
    }

    private void handlePassword(UpdatePasswordRequestBody requestBody) {
        Long userId = requestBody.getUserId();
        String newPassword = requestBody.getNewPassword();
        userService.updatePassword(userId, newPassword);
    }

}
