package cn.iatc.web.controller.role;

import cn.iatc.database.entity.Menu;
import cn.iatc.database.entity.RoleMenu;
import cn.iatc.web.bean.menu.MenuTreeNode;
import cn.iatc.web.bean.menu.Meta;
import cn.iatc.web.common.data.RestResponse;
import cn.iatc.web.common.data.Status;
import cn.iatc.web.common.data.exception.BaseException;
import cn.iatc.web.common.tree.TreeBuilder;
import cn.iatc.web.common.tree.data.BaseNode;
import cn.iatc.web.constants.CommonConstants;
import cn.iatc.web.service.MenuService;
import cn.iatc.web.service.RoleMenuService;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Data
class MenuTree {

    // 就是为了文档
    @JSONField(serialize = false)
    @Schema(name = "menus")
    private List<MenuTreeNode<Long, Integer>> menuTrees;

    @Hidden
    private List<BaseNode<Long, Integer>> menus;
}

@Data
class AddMenuRequestBody {
    @Schema(description = "角色id", defaultValue = "1")
    private Long roleId;

    @Schema(description = "菜单id列表", defaultValue = "[1,2,3]")
    private List<Long> menuIds = new ArrayList<>();
}

@Tag(name = "role", description = "角色相关")
@Slf4j
@RestController
@RequestMapping("/role")
public class RoleMenusController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleMenuService roleMenuService;

    @Operation(summary = "获取某个角色下菜单列表",description = "登录后访问")
    @GetMapping("/menus")
    public RestResponse<MenuTree> getRoleMenus(@RequestHeader(value = "token") String token,
                                               @RequestParam(required = false)
                                               @Parameter(description = "角色id") Long roleId) {
        RestResponse<MenuTree> response = new RestResponse<>();
        try {
            this.handleRoleMenus(roleId, response);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }

        return response;
    }

    private void handleRoleMenus(Long roleId, RestResponse<MenuTree> response) {
        List<BaseNode<Long, Integer>> treeNodes = new ArrayList<>();
        List<Menu> menuList = menuService.findAll();
        List<Long> menuIds = new ArrayList<>();
        if(roleId != null && roleId > 0) {
            List<RoleMenu> roleMenus = roleMenuService.findListByRole(roleId);
            for (RoleMenu item: roleMenus) {
                menuIds.add(item.getMenuId());
            }
        }
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
            int isSelect = CommonConstants.Status.DISABLED.getValue();
            if (menuIds.contains(item.getId())) {
                isSelect = CommonConstants.Status.ENABLED.getValue();
            }
            node.setIsSelect(isSelect);
            treeNodes.add(node);
        }

        TreeBuilder<Long, Integer> treeBuilder = new TreeBuilder<>();
        List<BaseNode<Long, Integer>> nodes = treeBuilder.buildTreeList(treeNodes);
        MenuTree menuTree = new MenuTree();
        menuTree.setMenus(nodes);
        response.setSuccess(menuTree);
    }

    @Operation(summary = "修改某个角色下菜单列表",description = "登录后访问")
    @PutMapping("/menus")
    public RestResponse<String> updateRoleMenus(@RequestHeader(value = "token") String token, @RequestBody AddMenuRequestBody requestBody) {
        RestResponse<String> response = new RestResponse<>();
        try {
            this.checkUpdateRequestBody(requestBody);
            this.updateRoleMenus(requestBody);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }

        return response;
    }

    private void checkUpdateRequestBody(AddMenuRequestBody requestBody) {
        Long roleId = requestBody.getRoleId();
        if (roleId == null || roleId <= 0) {
            throw new BaseException(Status.ROLE_ID_INVALID);
        }
    }

    private void updateRoleMenus(AddMenuRequestBody requestBody) {
        Long roleId = requestBody.getRoleId();
        List<Long> menuIds = requestBody.getMenuIds();
        roleMenuService.updateRoleMenus(roleId, menuIds);
    }
}
