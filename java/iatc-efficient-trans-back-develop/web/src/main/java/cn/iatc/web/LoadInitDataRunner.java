package cn.iatc.web;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.iatc.database.entity.Role;
import cn.iatc.database.entity.SystemConfiguration;
import cn.iatc.database.entity.User;
import cn.iatc.web.constants.CommonConstants;
import cn.iatc.web.init.RegionInitHelper;
import cn.iatc.web.init.StationInitHelper;
import cn.iatc.web.init.TransformerInitHelper;
import cn.iatc.web.service.*;
import cn.iatc.web.utils.SnowFlakeFactory;
import cn.iatc.web.utils.TimeUtil;
import cn.iatc.web.utils.ToolUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class LoadInitDataRunner implements ApplicationRunner {

    @Autowired
    private SystemConfigurationService systemConfigurationService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserService userService;

    @Autowired
    private RegionService regionService;

    @Autowired
    private FactoryService factoryService;

    @Autowired
    private StationTypeService stationTypeService;

    @Autowired
    private StationService stationService;

    @Autowired
    private BaseDeviceService baseDeviceService;

    @Autowired
    private BasePropertyService basePropertyService;

    @Autowired
    private BaseElecService baseElecService;

    @Autowired
    private GeneralTypeService generalTypeService;

    @Autowired
    private TransformerInfoService transformerInfoService;

    @Autowired
    private SnowFlakeFactory snowFlakeFactory;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.loadSystem();
        this.loadMenu();
        this.loadRole();
        this.loadSuperAdmin();
        this.loadStationType();
        this.loadBaseDevice();
        this.loadBaseProperty();
        this.loadBaseElec();
        this.loadGeneralType();

        this.loadRegion();
        this.loadFactory();
        this.loadStation();
        this.loadTrans();
    }

    private void loadSystem() {
        try {
            SystemConfiguration systemConfiguration = systemConfigurationService.findByType(SystemConfiguration.Type.CREATE_TIME.getValue());
            if (systemConfiguration == null) {
                Date createdTime = new Date();
                systemConfiguration = new SystemConfiguration();
                systemConfiguration.setType(SystemConfiguration.Type.CREATE_TIME.getValue());
                systemConfiguration.setValue(TimeUtil.dateFormat(createdTime, "yyyy-MM-dd HH:mm:ss"));
                systemConfiguration.setEnabledStatus(CommonConstants.Status.ENABLED.getValue());
                systemConfiguration.setCreatedTime(createdTime);
                systemConfiguration.setUpdatedTime(createdTime);
                systemConfigurationService.create(systemConfiguration);
            }
        } catch (Exception e) {
            log.info("===== Exception:{}", e);
        }

    }

    private void loadRole() {
        try {
            Long count = roleService.countRoleByType(Role.Type.SUPER.getValue());
            if (count > 0) {
                return;
            }

            String json = this.getInitJson("init/data/roleType.json");
            JSONArray jsonArray = JSON.parseArray(json);
            if (jsonArray.size() > 0) {
                roleService.createBatch(jsonArray);
            }
        } catch (Exception e) {
            log.info("===== Exception:{}", e);
        }
    }

    private void loadMenu() {
        try {
            String json = this.getInitJson("init/data/menu.json");
            JSONArray jsonArray = JSON.parseArray(json);
            if (jsonArray.size() > 0) {
                menuService.createBatch(jsonArray, null, true);
            }
        } catch (Exception e) {
            log.info("===== Exception:{}", e);
        }
    }

    private void loadSuperAdmin() {
        try {
            List<Role> superRoles = roleService.findByType(Role.Type.SUPER.getValue());
            log.info("===== superRoles:{}", superRoles);
            if (superRoles.size() == 0) {
                return;
            }

            Role superRole = superRoles.get(0);
            List<User> superUsers = userService.findByRole(superRole.getId());
            if (superUsers.size() > 0) {
                return;
            }

            User user = new User();
            user.setAccount("admin");
            user.setPassword("123456");
            user.setRealName("超级管理员");
            user.setRoleId(superRole.getId());
            user.setSalt(ToolUtil.getSalt(6));
            user.setUuid(snowFlakeFactory.nextIdStr());
            userService.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("===== Exception:{}", e);
        }
    }

    private void loadRegion() {
        try {
            RegionInitHelper regionInitHelper = new RegionInitHelper();
            regionInitHelper.handle(regionService, "init/data/csv/region.csv");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("===== Exception:{}", e);
        }
    }

    private void loadFactory() {
        try {
            Long count = factoryService.countAll();
            if (count > 0) {
                return;
            }

            String json = this.getInitJson("init/data/factory.json");
            JSONArray jsonArray = JSON.parseArray(json);
            if (jsonArray.size() > 0) {
                factoryService.createBatch(jsonArray);
            }
        } catch (Exception e) {
            log.info("===== Exception:{}", e);
        }
    }

    private void loadStationType() {
        try {
            String json = this.getInitJson("init/data/stationType.json");
            JSONArray jsonArray = JSON.parseArray(json);
            if (jsonArray.size() > 0) {
                stationTypeService.createBatch(jsonArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("===== Exception:{}", e);
        }
    }

    private void loadStation() {
        try {
            StationInitHelper stationInitHelper = new StationInitHelper();
            stationInitHelper.handle(stationService, "init/data/csv/station.csv");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("===== Exception:{}", e);
        }
    }

    private void loadBaseDevice() {
        try {
            Long count = baseDeviceService.countAll();
            if (count > 0) {
                return;
            }
            log.info("===== loadBaseDevice 1");

            String json = this.getInitJson("init/data/baseDevice.json");
            JSONArray jsonArray = JSON.parseArray(json);
            log.info("===== loadBaseDevice jsonArray size:{}", jsonArray.size());
            if (jsonArray.size() > 0) {
                baseDeviceService.createBatch(jsonArray);
            }
        } catch (Exception e) {
            log.info("===== Exception:{}", e);
        }
    }

    private void loadBaseProperty() {
        try {
            Long count = basePropertyService.countAll();
            if (count > 0) {
                return;
            }
            log.info("===== loadBaseProperty 1");

            String json = this.getInitJson("init/data/baseProperty.json");
            JSONArray jsonArray = JSON.parseArray(json);
            log.info("===== loadBaseProperty jsonArray size:{}", jsonArray.size());
            if (jsonArray.size() > 0) {
                basePropertyService.createBatch(jsonArray);
            }
        } catch (Exception e) {
            log.info("===== Exception:{}", e);
        }
    }

    private void loadBaseElec() {
        try {
            Long count = baseElecService.countAll();
            if (count > 0) {
                return;
            }
            log.info("===== loadTransformerType 1");

            String json = this.getInitJson("init/data/baseElec.json");
            JSONArray jsonArray = JSON.parseArray(json);
            log.info("===== loadTransformerType jsonArray size:{}", jsonArray.size());
            if (jsonArray.size() > 0) {
                baseElecService.createBatch(jsonArray);
            }
        } catch (Exception e) {
            log.info("===== Exception:{}", e);
        }
    }

    private void loadGeneralType() {
        try {
            Long count = generalTypeService.countAll();
            if (count > 0) {
                return;
            }
            log.info("===== loadTransformerType 1");

            String json = this.getInitJson("init/data/generalType.json");
            JSONArray jsonArray = JSON.parseArray(json);
            log.info("===== loadTransformerType jsonArray size:{}", jsonArray.size());
            if (jsonArray.size() > 0) {
                generalTypeService.createBatch(jsonArray);
            }
        } catch (Exception e) {
            log.info("===== Exception:{}", e);
        }
    }

    private void loadTrans() {
        try {
            TransformerInitHelper transformerInitHelper = new TransformerInitHelper();
            transformerInitHelper.handle(transformerInfoService, "init/data/csv/transformer_info.csv");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("===== Exception:{}", e);
        }
    }

    private String getInitJson(String path) throws IOException {
        // 打成jar包后，可以读取resources下的值
        ClassPathResource resource = new ClassPathResource(path);
        InputStream inputStream = resource.getStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
        String jsonStr = "";
        StringBuilder json = new StringBuilder();
        while ((jsonStr=bufferedReader.readLine()) != null) {
            json.append(jsonStr);
        }

        // 开发环境，可以读取
//            File jsonFile = ResourceUtils.getFile("classpath:init/data/sampleCategory.json");
//            String json = FileUtils.readFileToString(jsonFile, "utf-8");
        return json.toString();
    }

}
