package cn.iatc.mqtt_server.schedule;

import cn.hutool.core.util.StrUtil;
import cn.iatc.database.constants.SqlConstants;
import cn.iatc.database.entity.*;
import cn.iatc.mqtt_server.algorithm.TransAlgorithm;
import cn.iatc.mqtt_server.bean.transformerInfo.TransformerInfoPojo;
import cn.iatc.mqtt_server.common.context.SpringContextHolder;
import cn.iatc.mqtt_server.constants.CommonConstants;
import cn.iatc.mqtt_server.service.*;
import cn.iatc.mqtt_server.utils.CalculateUtil;
import cn.iatc.mqtt_server.utils.TimeUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
class TransInfo {
    // A相设备类型id列表
    List<Long> ctADeviceTypes = new ArrayList<>();
    // A相设备sn
    List<String> ctADeviceSns = new ArrayList<>();

    // B相设备类型id列表
    List<Long> ctBDeviceTypes = new ArrayList<>();
    // B相设备sn
    List<String> ctBDeviceSns = new ArrayList<>();

    // C相设备类型id列表
    List<Long> ctCDeviceTypes = new ArrayList<>();
    // C相设备sn
    List<String> ctCDeviceSns = new ArrayList<>();

    //电表侧设备类型id列表
    List<Long> dianBiaoDeviceTypes = new ArrayList<>();
    // 电表设备id列表
    List<Long> dianBiaoDevices = new ArrayList<>();
    // 电表设备sn
    List<String> dianBiaoDeviceSns = new ArrayList<>();
}

//转换数据格式
@Data
class ChangeData {
    // 高压侧1电流
    String highCurrent1;
    // 高压侧2电流
    String highCurrent2;
    // 高压侧电流差
    String highCurrent;
    // 低压侧电流
    String lowCurrent;
    // 低压侧电压
    String lowVoltage;
    // 功率因数
    String powerFactor;
    // 数据来源
    Integer dataSource = CommonConstants.Status.ENABLED.getValue();
}
//实时数据定时器，0，15，30，45分钟整理数据
@Slf4j
@Component
public class RealTimeTask {

    private static final OriginalPowerService originalPowerService;
    private static final TransformerInfoService transformerInfoService;
    private static final BasePropertyService basePropertyService;
    private static final DeviceStationService deviceStationService;
    private static final DevicePointService devicePointService;
    private static final DeviceService deviceService;
    private static final RealTimePowerService realTimePowerService;

    static {
        originalPowerService = SpringContextHolder.getBean(OriginalPowerService.class);
        transformerInfoService = SpringContextHolder.getBean(TransformerInfoService.class);
        basePropertyService = SpringContextHolder.getBean(BasePropertyService.class);
        deviceStationService = SpringContextHolder.getBean(DeviceStationService.class);
        devicePointService = SpringContextHolder.getBean(DevicePointService.class);
        deviceService = SpringContextHolder.getBean(DeviceService.class);
        realTimePowerService = SpringContextHolder.getBean(RealTimePowerService.class);
    }
// 每15:10 15分10秒处理数据
//    @Scheduled(cron = "10 0/15 * * * ?")
    @Scheduled(cron = "10 0/15 * * * ?")
    public void handle() {
        log.info("RealTimeTask time:{}", new Date());
        this.handleData();
    }

    private void handleData() {
        List<TransformerInfoPojo> transformerInfos = findTrans();
        List<RealTimePower> realTimePowerList = new ArrayList<>();
        for (TransformerInfoPojo transformerInfo: transformerInfos) {
            Long stationId = transformerInfo.getStationId();
            TransInfo transInfo = new TransInfo();
            this.findStationDeviceInfos(stationId, transInfo);
            if (stationId != 4) {
                continue;
            }

            List<String> transAs = BaseProperty.PropertyConstant.TRANS_A;
            if (transInfo.getCtADeviceTypes().size() > 0 && transInfo.getCtADeviceSns().size() > 0 && transInfo.getDianBiaoDevices().size() > 0) {
                handleDevicePoint(transformerInfo, realTimePowerList, transAs, transInfo.getCtADeviceTypes(), transInfo.getCtADeviceSns(),
                        transInfo.getDianBiaoDevices(), SqlConstants.TransPhase.A.getValue());
            }

            List<String> transBs = BaseProperty.PropertyConstant.TRANS_B;
            if (transInfo.getCtBDeviceTypes().size() > 0 && transInfo.getCtBDeviceSns().size() > 0 && transInfo.getDianBiaoDevices().size() > 0) {
                handleDevicePoint(transformerInfo, realTimePowerList, transBs, transInfo.getCtBDeviceTypes(), transInfo.getCtBDeviceSns(),
                        transInfo.getDianBiaoDevices(), SqlConstants.TransPhase.B.getValue());
            }

            List<String> transCs = BaseProperty.PropertyConstant.TRANS_C;
            if (transInfo.getCtCDeviceTypes().size() > 0 && transInfo.getCtCDeviceSns().size() > 0 && transInfo.getDianBiaoDevices().size() > 0) {
                handleDevicePoint(transformerInfo, realTimePowerList, transCs, transInfo.getCtCDeviceTypes(), transInfo.getCtCDeviceSns(),
                        transInfo.getDianBiaoDevices(), SqlConstants.TransPhase.C.getValue());
            }
            log.info("realTimePowerList:{}", realTimePowerList.toString());

        }
        if (realTimePowerList.size() > 0) {
            realTimePowerService.insertBatch(realTimePowerList);
        }
    }

    // 找到所有变压器id
    private List<TransformerInfoPojo> findTrans() {
        return transformerInfoService.findAll();
    }

    // 找到站点下设备信息
    private void findStationDeviceInfos(Long stationId, TransInfo transInfo) {
        // A相设备类型id列表
        List<Long> ctADeviceTypes = new ArrayList<>();
        // A相设备sn
        List<String> ctADeviceSns = new ArrayList<>();

        // B相设备类型id列表
        List<Long> ctBDeviceTypes = new ArrayList<>();
        // B相设备sn
        List<String> ctBDeviceSns = new ArrayList<>();

        // C相设备类型id列表
        List<Long> ctCDeviceTypes = new ArrayList<>();
        // C相设备sn
        List<String> ctCDeviceSns = new ArrayList<>();

        //电表侧设备类型id列表
        List<Long> dianBiaoDeviceTypes = new ArrayList<>();
        // 电表设备id列表
        List<Long> dianBiaoDevices = new ArrayList<>();
        // 电表设备sn
        List<String> dianBiaoDeviceSns = new ArrayList<>();

        List<Device> devices = deviceService.findListByStation(stationId);
        for (Device device: devices) {
            Long deviceTypeId = device.getDeviceTypeId();
            if (device.getDeviceType().getProperty().equals(DeviceType.Property.ZHI_NENG_CT_10KV_A.getValue())) {
                if (!ctADeviceTypes.contains(deviceTypeId)) {
                    ctADeviceTypes.add(deviceTypeId);
                }
                ctADeviceSns.add(device.getSn());

            } else if (device.getDeviceType().getProperty().equals(DeviceType.Property.ZHI_NENG_CT_10KV_B.getValue())) {
                if (!ctBDeviceTypes.contains(deviceTypeId)) {
                    ctBDeviceTypes.add(deviceTypeId);
                }
                ctBDeviceSns.add(device.getSn());
            } else if (device.getDeviceType().getProperty().equals(DeviceType.Property.ZHI_NENG_CT_10KV_C.getValue())) {
                if (!ctCDeviceTypes.contains(deviceTypeId)) {
                    ctCDeviceTypes.add(device.getDeviceTypeId());
                }
                ctCDeviceSns.add(device.getSn());
            } else if (device.getDeviceType().getBaseDevice().getProperty().equals(BaseDevice.Property.ZHI_NENG_DIAN_BIAO.getValue())) {
                if (!dianBiaoDeviceTypes.contains(deviceTypeId)) {
                    dianBiaoDeviceTypes.add(device.getDeviceTypeId());
                }
                dianBiaoDevices.add(device.getId());
                dianBiaoDeviceSns.add(device.getSn());
            }
        }
        transInfo.setCtADeviceTypes(ctADeviceTypes);
        transInfo.setCtADeviceSns(ctADeviceSns);
        transInfo.setCtBDeviceTypes(ctBDeviceTypes);
        transInfo.setCtBDeviceSns(ctBDeviceSns);
        transInfo.setCtCDeviceTypes(ctCDeviceTypes);
        transInfo.setCtCDeviceSns(ctCDeviceSns);
        transInfo.setDianBiaoDeviceTypes(dianBiaoDeviceTypes);
        transInfo.setDianBiaoDevices(dianBiaoDevices);
        transInfo.setDianBiaoDeviceSns(dianBiaoDeviceSns);
    }

    private List<DevicePoint> getDevicePointsByType(List<Long> basePropertyIds, List<Long> deviceTypeIds) {
        return devicePointService.findByDeviceTypesBaseProperties(deviceTypeIds, basePropertyIds);
    }

    private List<DevicePoint> getDevicePointsByDevice(List<Long> basePropertyIds, List<Long> deviceIds) {
        return devicePointService.findByDevicesBaseProperties(deviceIds, basePropertyIds);
    }

    private List<Long> getBasePropertyIds(List<String> baseProperties) {
        // 电表设备类型
        List<Long> basePropertyIds = new ArrayList<>();
        for(String property: baseProperties) {
            BaseProperty baseProperty = basePropertyService.findByProperty(property);
            if (baseProperty != null) {
                basePropertyIds.add(baseProperty.getId());
            }
        }
        return basePropertyIds;
    }

    private void handleDevicePoint(TransformerInfoPojo transformerInfo, List<RealTimePower> realTimePowerList, List<String> trans,
                                   List<Long> ctDeviceTypes, List<String> ctDeviceSns, List<Long> dianBiaoDeviceIds, int phase) {
        List<Long> basePropertyIds = this.getBasePropertyIds(trans);
        List<DevicePoint> highDevicePoints = this.getDevicePointsByType(basePropertyIds, ctDeviceTypes);
        List<DevicePoint> lowDevicePoints = this.getDevicePointsByDevice(basePropertyIds, dianBiaoDeviceIds);
        calPower(transformerInfo, realTimePowerList, phase, highDevicePoints, ctDeviceSns, lowDevicePoints);
    }

    // 计算功耗
    private void calPower(TransformerInfoPojo transformerInfo, List<RealTimePower> realTimePowerList, Integer phrase,
                          List<DevicePoint> ctDevicePoints, List<String> ctDeviceSns, List<DevicePoint> dianbiaoDevicePoints) {
        ChangeData changeData = new ChangeData();
        Calendar calendar = TimeUtil.getCurCalendar();
        long curTimeSample = TimeUtil.getCurTimeSample();
        calendar.setTimeInMillis(curTimeSample - CommonConstants.COLLECT_BASE_PERIOD * CommonConstants.COLLECT_NUM);
        calendar.set(Calendar.SECOND, 0);
        // 开始时间
        Date startDate = calendar.getTime();

        calendar.setTimeInMillis(curTimeSample - CommonConstants.COLLECT_BASE_PERIOD);
        calendar.set(Calendar.SECOND, 0);
        // 结束时间
        Date endDate = calendar.getTime();
        this.handleHighChange(ctDevicePoints, ctDeviceSns, startDate, endDate, changeData);
        this.handleLowChange(dianbiaoDevicePoints, startDate, endDate, changeData);

        String highCurrent1 = changeData.getHighCurrent1();
        String highCurrent2 = changeData.getHighCurrent2();
        String lowCurrent = changeData.getLowCurrent();
        String lowVoltage = changeData.getLowVoltage();
        String powerFactor = changeData.getPowerFactor();
        if (StrUtil.isNotBlank(highCurrent1) && StrUtil.isNotBlank(highCurrent2) && StrUtil.isNotBlank(lowCurrent) &&
                StrUtil.isNotBlank(lowVoltage) && StrUtil.isNotBlank(powerFactor)) {
            BigDecimal highCurrent = CalculateUtil.subtract(highCurrent1, highCurrent2);
            highCurrent = CalculateUtil.abs(highCurrent);
            changeData.setHighCurrent(highCurrent.toString());
            this.handleTranInfo(transformerInfo, realTimePowerList, phrase, changeData, startDate);
        }
    }

    // 处理高压侧转换
    private void handleHighChange(List<DevicePoint> ctDevicePoints, List<String> ctDeviceSns, Date startDate, Date endDate, ChangeData changeData) {
        // 高压侧目前只有property='dianliu' 理论上应该点表是一条，设备是一个/两个，若有多个只用第一组
        for(DevicePoint devicePoint: ctDevicePoints) {
            if (ctDeviceSns.size() == 1) {
                String deviceSn = ctDeviceSns.get(0);
                OriginalPower originalPower = originalPowerService.findByCodeTypeTime(deviceSn, devicePoint.getProperty(), startDate, endDate);
                if (originalPower == null) {
                    originalPower = originalPowerService.findLatestByCodeType(deviceSn, devicePoint.getProperty(), startDate);
                    if (originalPower == null) {
                        continue;
                    }
                    changeData.setDataSource(CommonConstants.Status.DISABLED.getValue());
                }

                String type = originalPower.getType();
                String value = originalPower.getValue();
                if (type.equals(DevicePoint.Property.DIANLIU.getValue())) {
                    changeData.setHighCurrent1(value);
                    changeData.setHighCurrent2("0");
                }

            } else if (ctDeviceSns.size() == 2) {
                for (String deviceSn: ctDeviceSns) {
                    OriginalPower originalPower = originalPowerService.findByCodeTypeTime(deviceSn, devicePoint.getProperty(), startDate, endDate);
                    if (originalPower == null) {
                        originalPower = originalPowerService.findLatestByCodeType(deviceSn, devicePoint.getProperty(), startDate);
                        if (originalPower == null) {
                            continue;
                        }
                        changeData.setDataSource(CommonConstants.Status.DISABLED.getValue());
                    }
                    String type = originalPower.getType();
                    String value = originalPower.getValue();
                    if (type.equals(DevicePoint.Property.DIANLIU.getValue())) {
                        if(StrUtil.isBlank(changeData.getHighCurrent1())) {
                            changeData.setHighCurrent1(value);
                            continue;
                        }
                        changeData.setHighCurrent2(value);
                        break;
                    }
                }
            }
            break;
        }
    }

    // 处理低压侧转换
    private void handleLowChange(List<DevicePoint> dianbiaoDevicePoints, Date startDate, Date endDate, ChangeData changeData) {
        for (DevicePoint devicePoint: dianbiaoDevicePoints) {
            Device device = deviceService.findById(devicePoint.getDeviceId());
            if (device == null) {
                continue;
            }
            OriginalPower originalPower = originalPowerService.findByCodeTypeTime(device.getSn(), devicePoint.getProperty(), startDate, endDate);
            if (originalPower == null) {
                originalPower = originalPowerService.findLatestByCodeType(device.getSn(), devicePoint.getProperty(), startDate);
                if (originalPower == null) {
                    continue;
                }
                changeData.setDataSource(CommonConstants.Status.DISABLED.getValue());
            }
            String type = originalPower.getType();
            String value = originalPower.getValue();
            if (type.equals(DevicePoint.Property.PHV_PHSA.getValue()) || type.equals(DevicePoint.Property.PHV_PHSB.getValue()) ||
                    type.equals(DevicePoint.Property.PHV_PHSC.getValue())) {
                changeData.setLowVoltage(value);
            } else if (type.equals(DevicePoint.Property.A_PHSA.getValue()) || type.equals(DevicePoint.Property.A_PHSB.getValue()) ||
                    type.equals(DevicePoint.Property.A_PHSC.getValue())) {
                changeData.setLowCurrent(value);
            } else if (type.equals(DevicePoint.Property.PHPF_PHSA.getValue()) || type.equals(DevicePoint.Property.PHPF_PHSB.getValue()) ||
                    type.equals(DevicePoint.Property.PHPF_PHSC.getValue())) {
                changeData.setPowerFactor(value);
            }
        }
    }

    private void handleTranInfo(TransformerInfoPojo transformerInfo, List<RealTimePower> realTimePowerList, Integer phrase,
                                ChangeData changeData, Date startDate) {
        String highCurrent = changeData.getHighCurrent();
        String lowCurrent = changeData.getLowCurrent();
        String lowVoltage = changeData.getLowVoltage();
        String powerFactor = changeData.getPowerFactor();
        Integer dataSource = changeData.getDataSource();

        String k = calDyb(transformerInfo.getDyb());
        String uk = transformerInfo.getZkdy();
        TransformerInfo.Dydjdm dydjdm = TransformerInfo.Dydjdm.fromKey(transformerInfo.getDydjdm());
        String u1n = dydjdm.getValue();
        String snKVA = transformerInfo.getEdrl();
        BigDecimal snKVABigD = CalculateUtil.multiply(snKVA, "1000");
        String sn = snKVABigD.toString();
        String i0 = transformerInfo.getKzdl();

        String highVoltage = TransAlgorithm.calHighVoltage(lowCurrent, lowVoltage, powerFactor, k, uk, u1n, sn, 6);
        String lossPower = TransAlgorithm.calLossPower(highCurrent, highVoltage, i0, uk, sn, 6);
        String highActivePower = TransAlgorithm.calHighActivePower(highCurrent, highVoltage, lowCurrent, lowVoltage, powerFactor, lossPower, 6);
        String lowActivePower = TransAlgorithm.calLowActivePower(lowCurrent, lowVoltage, powerFactor);

        RealTimePower realTimePower = new RealTimePower();
        realTimePower.setStationId(transformerInfo.getStationId());
        realTimePower.setPhase(phrase);
        realTimePower.setHighCurrent(highCurrent);
        realTimePower.setHighVoltage(highVoltage);
        realTimePower.setLowCurrent(lowCurrent);
        realTimePower.setLowVoltage(lowVoltage);
        realTimePower.setPowerFactor(powerFactor);
        realTimePower.setHighEfficiency(highActivePower);
        realTimePower.setLowEfficiency(lowActivePower);
        BigDecimal activePower = CalculateUtil.abs(CalculateUtil.subtract(highActivePower, lowActivePower));
        realTimePower.setActivePower(activePower.toString());
        realTimePower.setLossPower(lossPower);
        Calendar calendar = TimeUtil.getCurCalendar();
        calendar.setTime(startDate);
        realTimePower.setYear(calendar.get(Calendar.YEAR));
        realTimePower.setMonth(calendar.get(Calendar.MONTH) + 1);
        realTimePower.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        realTimePower.setHour(calendar.get(Calendar.HOUR));
        realTimePower.setMinute(calendar.get(Calendar.MINUTE));
        realTimePower.setDataSource(dataSource);
        realTimePower.setEnabledStatus(CommonConstants.Status.ENABLED.getValue());
        realTimePower.setCreatedTime(startDate);
        realTimePower.setUpdatedTime(new Date());
        realTimePowerList.add(realTimePower);
    }

    private String calDyb(String dyb) {
        List<String> k = StrUtil.split(dyb, "/");
        String k1 = k.get(0);
        String k2 = k.get(1);
        BigDecimal result = CalculateUtil.divide(k1, k2, 6);
        return result.toString();
    }
}
