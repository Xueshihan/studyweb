package cn.iatc.web.controller.type;

import cn.iatc.database.entity.BaseElec;
import cn.iatc.database.entity.GeneralType;
import cn.iatc.web.common.data.RestResponse;
import cn.iatc.web.common.data.Status;
import cn.iatc.web.common.data.exception.BaseException;
import cn.iatc.web.service.BaseElecService;
import cn.iatc.web.service.GeneralTypeService;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Data
class GeneralTypeResponseData {
    List<GeneralType> generalTypes = new ArrayList<>();
}

@Tag(name = "type", description = "类型相关")
@Slf4j
@RestController
@RequestMapping("/general_type")
public class GeneralTypeController {

    @Autowired
    private BaseElecService baseElecService;

    @Autowired
    private GeneralTypeService generalTypeService;

    // 根据basetype 查询通用类型列表
    @Operation(summary = "获取通用类型列表",description = "登录后访问, level=0,1,2,3,4,5")
    @GetMapping("/list")
    public RestResponse<GeneralTypeResponseData> getGeneralTypes(@RequestHeader(value = "token") String token, @RequestParam(name = "base_type") String baseType) {
        RestResponse<GeneralTypeResponseData> response = new RestResponse<>();
        try {

            this.handleGeneralTypes(baseType, response);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        }catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void handleGeneralTypes(String baseTypeStr, RestResponse<GeneralTypeResponseData> response) {
        BaseElec baseElec = baseElecService.findByType(baseTypeStr);
        if (baseElec == null) {
            return;
        }
        GeneralTypeResponseData data = new GeneralTypeResponseData();
        data.setGeneralTypes(generalTypeService.findListByBaseType(baseElec.getId()));
        response.setSuccess(data);
    }
}
