package cn.iatc.web.controller.user;

import cn.iatc.database.entity.Role;
import cn.iatc.database.entity.Station;
import cn.iatc.database.entity.User;
import cn.iatc.web.bean.user.UserPojo;
import cn.iatc.web.common.data.RestResponse;
import cn.iatc.web.common.data.Status;
import cn.iatc.web.common.data.exception.BaseException;
import cn.iatc.web.service.RoleService;
import cn.iatc.web.service.StationService;
import cn.iatc.web.service.UserService;
import cn.iatc.web.utils.jwt.JWTAccessData;
import cn.iatc.web.utils.jwt.JWTUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Data
class UserListRequestBody{
    private Long stationId;

    private String account;

    private String realName;

    private Long roleId;

    @Schema(defaultValue = "1")
    private Integer page= 1;

    @Schema(defaultValue = "10")
    private Integer num = 10;

}
@Data
class UserGetData {
    @JSONField(name = "users")
    @Schema(name = "users")
    List<UserPojo> userPojoList;

    Long pages;

    Long curPage;

    Long total;

    Long size;
}

@Tag(name = "user", description = "用户管理")
@Slf4j
@RestController
public class UsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private StationService stationService;

    @Operation(summary = "获取用户列表",description = "登录后访问")
    @PostMapping("/users")
    public RestResponse<UserGetData> getUserList(@RequestHeader(value = "token") String token, @RequestBody(required = false) UserListRequestBody requestBody) {
        RestResponse<UserGetData> response = new RestResponse<>();
        try {
            JWTAccessData jwtAccessData = JWTUtil.parseAccessToken(token);
            User loginUser = this.getUser(jwtAccessData.getUserId());
            this.handleUserList(requestBody, response);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private User getUser(Long userId) throws BaseException {
        return userService.findById(userId);
    }

    private void handleUserList(UserListRequestBody requestBody, RestResponse<UserGetData> response) {

        Long stationId = null;
        String account = null;
        String realName = null;
        Long roleId = null;
        Integer pageIndex = 1;
        Integer num = 10;
        if (requestBody != null) {
            stationId = requestBody.getStationId();
            account = requestBody.getAccount();
            realName = requestBody.getRealName();
            roleId = requestBody.getRoleId();
            pageIndex = requestBody.getPage();
            num = requestBody.getNum();
        }

        IPage<User> userPage = userService.findListLike(stationId, account, realName, roleId, pageIndex, num);
        log.info("====== userPage:{}", userPage.getRecords());
        List<User> userList = userPage.getRecords();
        List<UserPojo> userPojoList = new ArrayList<>();
        for (User user: userList) {
            UserPojo userPojo = this.createUserPojo(user);
            userPojoList.add(userPojo);
        }
        UserGetData userGetData = new UserGetData();
        userGetData.setUserPojoList(userPojoList);
        userGetData.setPages(userPage.getPages());
        userGetData.setCurPage(userPage.getCurrent());
        userGetData.setTotal(userPage.getTotal());
        userGetData.setSize(userPage.getSize());
        response.setSuccess(userGetData);
    }

    private UserPojo createUserPojo(User user) {
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
        Role role = roleService.findById(user.getRoleId());
        userPojo.setRole(role);
        Station station = stationService.findById(user.getStationId());
        userPojo.setStation(station);
        return userPojo;
    }
}
