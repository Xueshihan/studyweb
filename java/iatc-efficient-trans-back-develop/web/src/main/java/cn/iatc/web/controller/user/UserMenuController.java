package cn.iatc.web.controller.user;

import cn.iatc.database.entity.Menu;
import cn.iatc.database.entity.User;
import cn.iatc.web.bean.menu.MenuTreeNode;
import cn.iatc.web.bean.menu.Meta;
import cn.iatc.web.common.data.RestResponse;
import cn.iatc.web.common.data.Status;
import cn.iatc.web.common.data.exception.BaseException;
import cn.iatc.web.common.tree.TreeBuilder;
import cn.iatc.web.common.tree.data.BaseNode;
import cn.iatc.web.service.MenuService;
import cn.iatc.web.service.UserService;
import cn.iatc.web.utils.jwt.JWTAccessData;
import cn.iatc.web.utils.jwt.JWTUtil;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Data
class UserMenusResponseData {
    @JSONField(serialize = false)
    @Schema(name = "menus")
    private List<MenuTreeNode<Long, Integer>> menuTrees;

    @Hidden
    private List<BaseNode<Long, Integer>> menus = new ArrayList<>();
}
@Tag(name = "user", description = "用户管理")
@Slf4j
@RestController
public class UserMenuController {

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @Operation(summary = "用户菜单列表",description = "登录后访问")
    @PostMapping("/user/menus")
    public RestResponse<UserMenusResponseData> getMenus(@RequestHeader(value = "token") String token) {
        RestResponse<UserMenusResponseData> response = new RestResponse<>();
        try {
            JWTAccessData jwtAccessData = JWTUtil.parseAccessToken(token);
            User loginUser = this.getUser(jwtAccessData.getUserId());
            if (loginUser == null) {
                throw new BaseException(Status.USER_NOT_EXIST);
            }
            this.handleUserMenus(loginUser, response);
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

    private void handleUserMenus(User user, RestResponse<UserMenusResponseData> response) {
        List<BaseNode<Long, Integer>> treeNodes = new ArrayList<>();
        UserMenusResponseData data = new UserMenusResponseData();
        if (user.getRoleId() == null) {
            response.setData(data);
            return;
        }
        List<Menu> menuList = menuService.findListByRole(user.getRoleId());
        for (Menu item: menuList) {
            MenuTreeNode<Long, Integer> node = new MenuTreeNode<>();
            node.setId(item.getId());
            node.setParentId(item.getUpperId());
            Meta mt = new Meta();mt.setTitle(item.getTitle());mt.setIcon(item.getIcon());
            node.setMeta(mt);
            String name = item.getName();
            node.setName(name.substring(0,1).toUpperCase() + name.substring(1));
            node.setPath(item.getPath());
            node.setType(item.getType());
            treeNodes.add(node);
        }
        TreeBuilder<Long, Integer> treeBuilder = new TreeBuilder<>();
        List<BaseNode<Long, Integer>> nodes = treeBuilder.buildTreeList(treeNodes);
        data.setMenus(nodes);
        response.setSuccess(data);
    }
}
