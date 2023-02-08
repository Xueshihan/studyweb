package cn.iatc.web.controller.login;

import cn.hutool.core.util.StrUtil;
import cn.iatc.database.entity.Role;
import cn.iatc.database.entity.User;
import cn.iatc.redis.RedisHelper;
import cn.iatc.web.bean.token.Token;
import cn.iatc.web.bean.user.UserPojo;
import cn.iatc.web.common.data.RestResponse;
import cn.iatc.web.common.data.Status;
import cn.iatc.web.common.data.exception.BaseException;
import cn.iatc.web.constants.CommonConstants;
import cn.iatc.web.constants.RedisConstant;
import cn.iatc.web.service.RoleService;
import cn.iatc.web.service.UserService;
import cn.iatc.web.utils.IPUtil;
import cn.iatc.web.utils.MD5Util;
import cn.iatc.web.utils.TimeUtil;
import cn.iatc.web.utils.jwt.JWTAccessData;
import cn.iatc.web.utils.jwt.JWTUtil;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Data
class LoginRequestBody {
    private String userName;

    private String password;

    //private String code;
    private Boolean rememberMe = false;
}

@Data
class LoginResponseData {
    private Token token;

    private UserPojo user;
}
@Tag(name = "login", description = "登陆")
@Slf4j
@RestController
public class LoginController {

    @Autowired
    private RedisHelper redisHelper;
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Operation(summary = "登陆操作",description = "登录前访问")
    @PostMapping("/login")
    public RestResponse<LoginResponseData> doLogin(@RequestBody LoginRequestBody requestBody, HttpServletRequest request) {
        RestResponse<LoginResponseData> response = new RestResponse<>();
        LoginResponseData loginResponseData = new LoginResponseData();
        try {
            User user = this.checkRequestBody(requestBody);
            this.handleSuccess(user, request, loginResponseData);
            this.getToken(user, requestBody.getRememberMe(), loginResponseData);
            response.setSuccess(loginResponseData);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private User checkRequestBody(LoginRequestBody loginRequestBody) {
        String account = loginRequestBody.getUserName();
        String password = loginRequestBody.getPassword();
        //String code = loginRequestBody.getCode();
        if (StrUtil.isBlank(account)) {
            throw new BaseException(Status.USER_NAME_INVALID);
        }

        if (StrUtil.isBlank(password)) {
            throw new BaseException(Status.USER_PASSWORD_INVALID);
        }

        /*if (StrUtil.isBlank(code)) {
            throw new BaseException(Status.LOGIN_VERIFY_CODE_ERROR);
        }*/

        return this.checkLogin(account, password);
    }

    private User checkLogin(String account, String password) {

        /*String verifyCodeKey = StrUtil.format("{}{}/{}", RedisConstant.KEY_BASE, RedisConstant.KEY_LOGIN_VERIFY_CODE, code.toLowerCase());
        String existCode = (String) redisHelper.getValue(verifyCodeKey);
        log.info("====== existCode:{}", existCode);
        if (StrUtil.isBlank(existCode)) {
            throw new BaseException(Status.LOGIN_VERIFY_CODE_ERROR);
        }

        int finalCode = Integer.parseInt(existCode);
        if(finalCode <= 0) {
            throw new BaseException(Status.LOGIN_VERIFY_CODE_ERROR);
        }*/

        User user = userService.findUserByAccount(account);
        if (user == null) {
            throw new BaseException(Status.USER_NOT_EXIST);
        }

        String errNumKey = StrUtil.format("{}{}/{}", RedisConstant.KEY_BASE, RedisConstant.KEY_LOGIN_ERROR_NUM, user.getId());
        String lockTimeKey = StrUtil.format("{}{}/{}", RedisConstant.KEY_BASE, RedisConstant.KEY_LOGIN_LOCK_TIME, user.getId());

        String lockTimeStr = (String) redisHelper.getValue(lockTimeKey);

        if (StrUtil.isNotBlank(lockTimeStr)) {
            long lockTime = Long.parseLong(lockTimeStr);
            long curTime = TimeUtil.dateToTime(new Date());
            if (lockTime >= curTime) {
                // 账号被锁定，无论密码正确与否都锁定
                throw new BaseException(Status.LOGIN_LOCK);
            }
        }

        String passwordMd5 = MD5Util.encodeMd5(password + user.getSalt()).toLowerCase();
        if (passwordMd5.equals(user.getPassword()) ||
                passwordMd5.equals(MD5Util.encodeMd5(user.getPassword() + user.getSalt()).toLowerCase())) {
            redisHelper.delKey(errNumKey);
            redisHelper.delKey(lockTimeKey);
            //redisHelper.decrement(verifyCodeKey);

        } else {
            int count = redisHelper.increment(errNumKey).intValue();
            if(count >= CommonConstants.LOGIN_ERR_NUM) {
                long lockTime = TimeUtil.dateToTime(new Date()) + CommonConstants.LOGIN_LOCK_TIME;
                redisHelper.setValueTimeout(lockTimeKey, String.valueOf(lockTime), CommonConstants.LOGIN_LOCK_TIME / 1000);
                user.setLockTime(TimeUtil.timeToDate(lockTime));
            }
            user.setWrongNum(count);
            userService.updateToUser(user);

            throw new BaseException(Status.USER_PASSWORD_ERROR);
        }

        return user;
    }

    private void handleSuccess(User user, HttpServletRequest request, LoginResponseData loginResponseData) {
        Date updatedTime = new Date();
        user.setWrongNum(0);
        user.setLockTime(null);
        user.setLastLoginTime(updatedTime);
        user.setLastLoginIp(IPUtil.getIpAddr(request));
        user.setUpdatedTime(updatedTime);
        userService.updateToUser(user);

        UserPojo userPojo = new UserPojo();
        userPojo.setId(user.getId());
        userPojo.setAccount(user.getAccount());
        userPojo.setRealName(user.getRealName());
        userPojo.setPhone(user.getPhone());
        userPojo.setEmail(user.getEmail());
        userPojo.setSalt(user.getSalt());
        userPojo.setUuid(user.getUuid());
        userPojo.setCreatedTime(user.getCreatedTime());
        userPojo.setUpdatedTime(user.getUpdatedTime());
        userPojo.setRole(this.getRole(user));
        loginResponseData.setUser(userPojo);
    }

    private void getToken(User user, boolean rememberMe, LoginResponseData loginResponseData) {
        JWTAccessData jwtAccessData = new JWTAccessData();
        jwtAccessData.setUserId(user.getId());
        String accessToken = JWTUtil.sign(jwtAccessData, rememberMe);
        Token token = new Token();
        token.setAccessToken(accessToken);
        loginResponseData.setToken(token);
    }

    private Role getRole(User user) {
        return roleService.findById(user.getRoleId());

    }
}
