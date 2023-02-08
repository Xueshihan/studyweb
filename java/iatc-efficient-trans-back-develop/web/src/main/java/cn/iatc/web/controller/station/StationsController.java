package cn.iatc.web.controller.station;

import cn.iatc.database.entity.Station;
import cn.iatc.database.entity.User;
import cn.iatc.web.bean.station.StationTreeNode;
import cn.iatc.web.common.data.RestResponse;
import cn.iatc.web.common.data.Status;
import cn.iatc.web.common.data.exception.BaseException;
import cn.iatc.web.common.tree.TreeBuilder;
import cn.iatc.web.common.tree.data.BaseNode;
import cn.iatc.web.constants.CommonConstants;
import cn.iatc.web.service.StationService;
import cn.iatc.web.service.UserService;
import cn.iatc.web.utils.jwt.JWTAccessData;
import cn.iatc.web.utils.jwt.JWTUtil;
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
class BuildDeviceRequestBody {
    // 站房id
    @Schema(description = "站点id", defaultValue = "1")
    private Long stationId;
}

@Data
class StationInfosRequestBody {
    @JSONField(name = "levels")
    @Schema(name = "levels", description = "级别列表", defaultValue = "[0,1,2]", required = true)
    private List<Integer> levels = new ArrayList<>();
}
@Data
class StationTree {
    // 为了生成文档
    @JSONField(serialize = false)
    @Schema(name = "stations", description = "树形结构")
    private List<StationTreeNode<Long, Integer>> stationTrees;

    @Hidden
    private List<BaseNode<Long, Integer>> stations = new ArrayList<>();
}

@Tag(name = "stations", description = "站点相关")
@Slf4j
@RestController
@RequestMapping("/stations")
public class StationsController {

    @Autowired
    private UserService userService;

    @Autowired
    private StationService stationService;

    // 获取到站房树形结构， 只到站房
    @Operation(summary = "获取站点信息(到变压器level=4级别)",description = "登录后访问, level=0,1,2,3,4")
    @GetMapping("/buildings")
    public RestResponse<StationTree> getBuildings(@RequestHeader(value = "token") String token) {
        RestResponse<StationTree> response = new RestResponse<>();
        try {
            JWTAccessData jwtAccessData = JWTUtil.parseAccessToken(token);
            User loginUser = this.getUser(jwtAccessData.getUserId());
            if (loginUser == null) {
                throw new BaseException(Status.USER_NOT_EXIST);
            }
            Long stationId = loginUser.getStationId();
            this.handleBuildingsToLevel4(stationId, response);
        } catch (BaseException exception) {
            exception.printStackTrace();
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    // 获取到站房树形结构， 只到站房
    @Operation(summary = "获取站点信息(到站房级别)",description = "登录后访问, level=0,1,2,3,4,5")
    @GetMapping("/buildingsToLevel5")
    public RestResponse<StationTree> getBuildingsToLevel5(@RequestHeader(value = "token") String token) {
        RestResponse<StationTree> response = new RestResponse<>();
        try {
            JWTAccessData jwtAccessData = JWTUtil.parseAccessToken(token);
            User loginUser = this.getUser(jwtAccessData.getUserId());
            if (loginUser == null) {
                throw new BaseException(Status.USER_NOT_EXIST);
            }
            Long stationId = loginUser.getStationId();
            this.handleBuildingsToLevel5(stationId, response);
        } catch (BaseException exception) {
            exception.printStackTrace();
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

    private Long handleStationIdNull(Long stationId) {
        if (stationId == null) {
            List<Station> stationList = stationService.findByLevel(Station.Level.LEVEL0.getValue());
            if (stationList.size() > 0) {
                stationId = stationList.get(0).getId();
            }
        }
        return stationId;
    }
    // 返回 StationTreeNode 列表
    private void handleBuildingsToLevel4(Long stationId, RestResponse<StationTree> response) {
        stationId = this.handleStationIdNull(stationId);
        List<BaseNode<Long, Integer>> stationTreeNodes = handleTreeByStationLevels(stationId, Station.LevelConstant.stationBuildingLevels4);
        response.setSuccess(this.handleTreeBuild(stationTreeNodes));
    }
    // 返回 StationTreeNode 列表
    private void handleBuildingsToLevel5(Long stationId, RestResponse<StationTree> response) {
        stationId = this.handleStationIdNull(stationId);
        List<BaseNode<Long, Integer>> stationTreeNodes = handleTreeByStationLevels(stationId, Station.LevelConstant.stationBuildingLevels);
        response.setSuccess(this.handleTreeBuild(stationTreeNodes));
    }

    /**
     *  通过站点ID和级别列表获取树结构
     * @param stationId
     * @param levels
     * @return
     */
    private List<BaseNode<Long, Integer>> handleTreeByStationLevels(Long stationId, List<Integer> levels) {
        return stationService.findTreeListByIdLevels(stationId, levels);
    }

    private StationTree handleTreeBuild(List<BaseNode<Long, Integer>> nodeList) {
        TreeBuilder<Long, Integer> treeBuilder = new TreeBuilder<>();
        List<BaseNode<Long, Integer>> nodes = treeBuilder.buildTreeList(nodeList);
        StationTree stationTree = new StationTree();
        stationTree.setStations(nodes);
        return stationTree;
    }

    // 获取站房内的电气设备
    @Operation(summary = "获取站点设备信息",description = "登录后访问，level=6,7,8,40,41,42,43")
    @GetMapping("/build/devices")
    public RestResponse<StationTree> getBuildDevices(@RequestHeader(value = "token") String token,
                                                     @Parameter(description = "站点id")
                                                     @RequestParam Long stationId,
                                                     @Parameter(description = "是否显示当前自己结构状态")
                                                     @RequestParam(required = false, defaultValue = "0") Integer isSelf) {
        RestResponse<StationTree> response = new RestResponse<>();
        try {
            this.checkStationId(stationId);
            this.handleBuildDevices(stationId, isSelf, response);
        } catch (BaseException exception) {
            exception.printStackTrace();
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void checkStationId(Long stationId) {
        if (stationId == null || stationId <= 0) {
            throw new BaseException(Status.STATION_ID_INVALID);
        }
    }

    private void handleBuildDevices(Long stationId, Integer isSelf, RestResponse<StationTree> response) {
        List<BaseNode<Long, Integer>> stationTreeNodes = stationService.findTreeNextListById(stationId, isSelf);
        response.setSuccess(this.handleTreeBuild(stationTreeNodes));
    }

    // 获取到区的信息列表 level=0,1,2
    @Operation(summary = "获取站点信息(到区级别)",description = "登录后访问，level=0,1,2")
    @GetMapping("/departments")
    public RestResponse<StationTree> getBuildDevices(@RequestHeader(value = "token") String token) {
        RestResponse<StationTree> response = new RestResponse<>();
        try {
            this.handleDepartments(response);
        } catch (BaseException exception) {
            exception.printStackTrace();
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void handleDepartments(RestResponse<StationTree> response) {
        List<BaseNode<Long, Integer>> departmentTreeNodes = stationService.findTreeByLevels(Station.LevelConstant.regionLevels);
        response.setSuccess(this.handleTreeBuild(departmentTreeNodes));
    }

    @Operation(summary = "获取站点下一级信息",description = "登录后访问")
    @GetMapping("/next/child")
    public RestResponse<StationTree> getNextChild(@RequestHeader(value = "token") String token,
                                                  @Parameter(description = "上级id")
                                                  @RequestParam Long upperId) {
        RestResponse<StationTree> response = new RestResponse<>();
        try {
            this.checkStationId(upperId);
            this.handleNextChild(upperId, response);
        } catch (BaseException exception) {
            exception.printStackTrace();
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void handleNextChild(Long upperId, RestResponse<StationTree> response) {
        List<BaseNode<Long, Integer>> children = stationService.findTreeNextChildById(upperId);
        StationTree stationTree = new StationTree();
        stationTree.setStations(children);
        response.setSuccess(stationTree);
    }

    @Operation(summary = "获取某人管理下的，某些级别下的，站点信息",description = "登录后访问")
    @PostMapping("/infos")
    public RestResponse<StationTree> getInfos(@RequestHeader(value = "token") String token, @RequestBody StationInfosRequestBody requestBody) {
        RestResponse<StationTree> response = new RestResponse<>();
        try {
            JWTAccessData jwtAccessData = JWTUtil.parseAccessToken(token);
            User loginUser = this.getUser(jwtAccessData.getUserId());
            if (loginUser == null) {
                throw new BaseException(Status.USER_NOT_EXIST);
            }
            Long stationId = loginUser.getStationId();
            stationId = this.handleStationIdNull(stationId);
            List<BaseNode<Long, Integer>> stationTreeNodes = handleTreeByStationLevels(stationId, requestBody.getLevels());
            response.setSuccess(this.handleTreeBuild(stationTreeNodes));
        } catch (BaseException exception) {
            exception.printStackTrace();
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

}
