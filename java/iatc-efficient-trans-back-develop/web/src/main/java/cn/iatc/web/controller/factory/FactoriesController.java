package cn.iatc.web.controller.factory;

import cn.iatc.database.entity.Factory;
import cn.iatc.web.common.data.RestResponse;
import cn.iatc.web.common.data.Status;
import cn.iatc.web.common.data.exception.BaseException;
import cn.iatc.web.service.FactoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Data
class FactoryListResponseData {
    @Schema(description = "厂商列表")
    List<Factory> factories = new ArrayList<>();
}
@Tag(name = "factory", description = "厂商相关")
@Slf4j
@RestController
public class FactoriesController {

    @Autowired
    private FactoryService factoryService;

    @Operation(summary = "获取厂商列表")
    @GetMapping("/factories")
    public RestResponse<FactoryListResponseData> getFactories(@RequestHeader(value = "token") String token) {
        RestResponse<FactoryListResponseData> response = new RestResponse<>();
        try {
            this.handleFactories(response);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        } catch (Exception exception) {
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void handleFactories(RestResponse<FactoryListResponseData> response) {
        FactoryListResponseData data = new FactoryListResponseData();
        data.setFactories(factoryService.findAll());
        response.setSuccess(data);
    }
}
