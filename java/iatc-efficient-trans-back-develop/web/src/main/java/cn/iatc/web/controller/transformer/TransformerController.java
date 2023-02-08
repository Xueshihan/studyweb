package cn.iatc.web.controller.transformer;

import cn.hutool.core.date.DateUtil;
import cn.iatc.database.entity.*;
import cn.iatc.web.bean.transformer.OilTransformerEfficiencyPojo;
import cn.iatc.web.common.data.RestResponse;
import cn.iatc.web.common.data.Status;
import cn.iatc.web.common.data.exception.BaseException;
import cn.iatc.web.service.*;
import cn.iatc.web.utils.TimeUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Data
class SelectBaseRequestBody {
    @Schema(description = "左侧树选中的站点id")
    private Long stationId;
}
@Data
class SelectYyyyMMRequestBody extends SelectBaseRequestBody{
    @Schema(description = "月度2023-01", defaultValue = "new Date()")
    private Date yyyyMM = new Date();
}
@Data
class SelectYyyyRequestBody extends SelectBaseRequestBody{
    @Schema(description = "年度2023", defaultValue = "new Date()")
    private Date yyyy = new Date();
}
@Data
class SelectPeriodRequestBody extends SelectBaseRequestBody{
    @Schema(description = "功率损耗曲线开始日期2023-01-30", defaultValue = "new Date()")
    private Date startDate = new Date();

    @Schema(description = "功率损耗曲线结束日期2023-01-31", defaultValue = "new Date()")
    private Date endDate = new Date();
}

@Data
class TransformerInfoRequestBody {
    private String name;

    @Schema(description = "页数", defaultValue = "1")
    private Integer page = 1;

    @Schema(description = "数量", defaultValue = "10")
    private Integer num = 10;
}
@Data
class TransformerInfoResponseData {
    @JSONField(name = "devices")
    @Schema(name = "devices")
    List<TransformerInfo> transformerInfoList;

    Long pages;

    Long curPage;

    Long total;

    Long size;
}
@Data
class LevelFzshKzshResponseData {
    private String level;
    private String fzsh;
    private String kzsh;
}
@Data
class TotalABCResponseData {
    private String total;
    private String A;
    private String B;
    private String C;
}
@Data
class CurveDetailCreateTimeResponseData {
    private List<String> curve = new ArrayList<>();
    private List<String[]> detail = new ArrayList<>();
    private List<String> createTime = new ArrayList<>();
}
@Data
class HighLowFuzaiCreateTimeResponseData {
    private List<String> high = new ArrayList<>();
    private List<String> low = new ArrayList<>();
    private List<String> fuzai = new ArrayList<>();
    private List<String> createTime = new ArrayList<>();
}
@Data
class ThisDeviceSimilarDeviceCreateTimeResponseData {
    private List<String> thisDevice = new ArrayList<>();
    private List<String> similarDevice = new ArrayList<>();
    private List<String> createTime = new ArrayList<>();
}
@Data
class GraphWasteResponseData {
    private String edHighVoltage1 = "10000";
    private String edLowVoltage1 = "220";
    private String edHighVoltage2 = "10000";
    private String edLowVoltage2 = "220";
    private String edHighCurrent = "0";
    private String edLowCurrent = "0";

    private List<String> AHighCurrent = new ArrayList<>();
    private List<String> BHighCurrent = new ArrayList<>();
    private List<String> CHighCurrent = new ArrayList<>();
    private List<String> AHighVoltage = new ArrayList<>();
    private List<String> BHighVoltage = new ArrayList<>();
    private List<String> CHighVoltage = new ArrayList<>();
    private List<String> ALowCurrent = new ArrayList<>();
    private List<String> BLowCurrent = new ArrayList<>();
    private List<String> CLowCurrent = new ArrayList<>();
    private List<String> ALowVoltage = new ArrayList<>();
    private List<String> BLowVoltage = new ArrayList<>();
    private List<String> CLowVoltage = new ArrayList<>();

    private List<String> createTimeHighCurrent = new ArrayList<>();
    private List<String> createTimeHighVoltage = new ArrayList<>();
    private List<String> createTimeLowCurrent = new ArrayList<>();
    private List<String> createTimeLowVoltage = new ArrayList<>();

    public GraphWasteResponseData(String edHighVoltage1, String edLowVoltage1, String edHighVoltage2, String edLowVoltage2, String edHighCurrent, String edLowCurrent, List<String> AHighCurrent, List<String> BHighCurrent, List<String> CHighCurrent, List<String> AHighVoltage, List<String> BHighVoltage, List<String> CHighVoltage, List<String> ALowCurrent, List<String> BLowCurrent, List<String> CLowCurrent, List<String> ALowVoltage, List<String> BLowVoltage, List<String> CLowVoltage, List<String> createTimeHighCurrent, List<String> createTimeHighVoltage, List<String> createTimeLowCurrent, List<String> createTimeLowVoltage) {
        this.edHighVoltage1 = edHighVoltage1;
        this.edLowVoltage1 = edLowVoltage1;
        this.edHighVoltage2 = edHighVoltage2;
        this.edLowVoltage2 = edLowVoltage2;
        this.edHighCurrent = edHighCurrent;
        this.edLowCurrent = edLowCurrent;
        this.AHighCurrent = AHighCurrent;
        this.BHighCurrent = BHighCurrent;
        this.CHighCurrent = CHighCurrent;
        this.AHighVoltage = AHighVoltage;
        this.BHighVoltage = BHighVoltage;
        this.CHighVoltage = CHighVoltage;
        this.ALowCurrent = ALowCurrent;
        this.BLowCurrent = BLowCurrent;
        this.CLowCurrent = CLowCurrent;
        this.ALowVoltage = ALowVoltage;
        this.BLowVoltage = BLowVoltage;
        this.CLowVoltage = CLowVoltage;
        this.createTimeHighCurrent = createTimeHighCurrent;
        this.createTimeHighVoltage = createTimeHighVoltage;
        this.createTimeLowCurrent = createTimeLowCurrent;
        this.createTimeLowVoltage = createTimeLowVoltage;
    }
}

@Tag(name = "transformer", description = "设备相关")
@Slf4j
@RestController
@RequestMapping("/transformer")
public class TransformerController {

    @Autowired
    private TransformerInfoService transformerInfoService;
    @Autowired
    private OilTransformerEfficiencyService oilTransformerEfficiencyService;
    @Autowired
    private StatisticMonthPowerService statisticMonthPowerService;
    @Autowired
    private StatisticDayPowerService statisticDayPowerService;
    @Autowired
    private StatisticHourPowerService statisticHourPowerService;
    @Autowired
    private RealTimePowerService realTimePowerService;
    @Autowired
    private SystemConfigurationService systemConfigurationService;

    private void checkStationId(Long stationId) {
        if (stationId == null || stationId <= 0) {
            throw new BaseException(Status.PARAMETER_INVALID);
        }
    }

    // StationId查询台账表
    @Operation(summary = "根据StationId查询该台账")
    @PostMapping("/info/one")
    public RestResponse<TransformerInfo> getOneInfo(@RequestHeader(value = "token") String token, @RequestBody SelectBaseRequestBody requestBody) {
        RestResponse<TransformerInfo> response = new RestResponse<>();
        try {
            this.checkStationId(requestBody.getStationId());
            TransformerInfo data = transformerInfoService.findByStationId(requestBody.getStationId());
            response.setSuccess(data);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    // 查询台账表
    @Operation(summary = "根据条件查询所有台账表")
    @PostMapping("/info/all")
    public RestResponse<TransformerInfoResponseData> getAllInfo(@RequestHeader(value = "token") String token, @RequestBody TransformerInfoRequestBody requestBody) {
        RestResponse<TransformerInfoResponseData> response = new RestResponse<>();
        try {
            this.handleTransformerInfoAll(requestBody, response);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    private void handleTransformerInfoAll(TransformerInfoRequestBody requestBody, RestResponse<TransformerInfoResponseData> response ) {
        TransformerInfoResponseData data = new TransformerInfoResponseData();
        String name = requestBody.getName();
        Integer page = requestBody.getPage();
        Integer num = requestBody.getNum();
        Page<TransformerInfo> devicePojoPage = (Page<TransformerInfo>) transformerInfoService.findListLike(name,page,num);
        data.setTransformerInfoList(devicePojoPage.getRecords());
        data.setPages(devicePojoPage.getPages());
        data.setCurPage(devicePojoPage.getCurrent());
        data.setTotal(devicePojoPage.getTotal());
        data.setSize(devicePojoPage.getSize());
        response.setSuccess(data);
    }

    // 查询台账表
    @Operation(summary = "根据条件查询所有能效等级")
    @PostMapping("/efficiency/all")
    public RestResponse<List<OilTransformerEfficiency>> getAllEfficiency(@RequestHeader(value = "token") String token, @RequestBody SelectBaseRequestBody requestBody) {
        RestResponse<List<OilTransformerEfficiency>> response = new RestResponse<>();
        try {
            List<OilTransformerEfficiency> data = oilTransformerEfficiencyService.findByLevel(1);
            response.setSuccess(data);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    @Operation(summary = "系统运行多少天了")
    @GetMapping("/system/days")
    public RestResponse<String> getSystemDayCount(@RequestHeader(value = "token") String token) {
        RestResponse<String> response = new RestResponse<>();
        try {
            String createTime = systemConfigurationService.findByType(SystemConfiguration.Type.CREATE_TIME.getValue()).getValue();
            response.setSuccess(String.valueOf(TimeUtil.getDiffDays(new Date(), DateUtil.parse(createTime,"yyyy-MM-dd"))));
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }


    @Operation(summary = "变压器能耗等级")
    @PostMapping("/efficiency/oil")
    public RestResponse<LevelFzshKzshResponseData> getOilTransformerEfficiency(@RequestHeader(value = "token") String token, @RequestBody SelectBaseRequestBody requestBody) {
        RestResponse<LevelFzshKzshResponseData> response = new RestResponse<>();
        try {
            this.checkStationId(requestBody.getStationId());
            LevelFzshKzshResponseData pd = new LevelFzshKzshResponseData();
            String maxFzsh = statisticHourPowerService.getMaxActivePower(requestBody.getStationId());
            List<OilTransformerEfficiencyPojo> oilTransformerEfficiency = oilTransformerEfficiencyService.getKzsh(requestBody.getStationId());
            pd.setLevel(""+oilTransformerEfficiency.get(0).getLevel());pd.setFzsh(maxFzsh);pd.setKzsh(oilTransformerEfficiency.get(0).getKzsh());
            response.setSuccess(pd);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    @Operation(summary = "昨日电能损耗")
    @PostMapping("/waste/yesterday")
    public RestResponse<TotalABCResponseData> getYesterdayWaste(@RequestHeader(value = "token") String token, @RequestBody SelectBaseRequestBody requestBody) {
        RestResponse<TotalABCResponseData> response = new RestResponse<>();
        try {
            this.checkStationId(requestBody.getStationId());
            TotalABCResponseData pd = new TotalABCResponseData();
            int year = TimeUtil.getDateFormatByType(TimeUtil.getYesterday(),"year");
            int month = TimeUtil.getDateFormatByType(TimeUtil.getYesterday(),"month");
            int day = TimeUtil.getDateFormatByType(TimeUtil.getYesterday(),"day");
            List<StatisticDayPower> statisticDayPowerList = statisticDayPowerService.getStatisticDayPowerOfDay(requestBody.getStationId(), year, month, day);
            String AValue = "0";
            String BValue = "0";
            String CValue = "0";
            if(statisticDayPowerList!=null&&statisticDayPowerList.size()>0){
                for(StatisticDayPower smp:statisticDayPowerList){
                    if(smp.getPhase().equals(1)){
                        AValue = smp.getActivePower();
                    }else if(smp.getPhase().equals(2)){
                        BValue = smp.getActivePower();
                    }else if(smp.getPhase().equals(3)){
                        CValue = smp.getActivePower();
                    }
                }
            }
            String sum = String.valueOf(Float.valueOf(AValue)+Float.valueOf(BValue)+Float.valueOf(CValue));
            pd.setTotal(sum);pd.setA(AValue);pd.setB(BValue);pd.setC(CValue);
            response.setSuccess(pd);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    @Operation(summary = "上月电能损耗")
    @PostMapping("/waste/lastMonth")
    public RestResponse<TotalABCResponseData> getLastMonthWaste(@RequestHeader(value = "token") String token, @RequestBody SelectBaseRequestBody requestBody) {
        RestResponse<TotalABCResponseData> response = new RestResponse<>();
        try {
            this.checkStationId(requestBody.getStationId());
            TotalABCResponseData pd = new TotalABCResponseData();
            int year = TimeUtil.getDateFormatByType(TimeUtil.getLastMonth(),"year");
            int month = TimeUtil.getDateFormatByType(TimeUtil.getLastMonth(),"month");
            List<StatisticMonthPower> statisticMonthPowerList = statisticMonthPowerService.getStatisticMonthPowerOfMonth(requestBody.getStationId(), year, month);
            String AValue = "0";
            String BValue = "0";
            String CValue = "0";
            if(statisticMonthPowerList!=null&&statisticMonthPowerList.size()>0){
                for(StatisticMonthPower smp:statisticMonthPowerList){
                    if(smp.getPhase().equals(1)){
                        AValue = smp.getActivePower();
                    }else if(smp.getPhase().equals(2)){
                        BValue = smp.getActivePower();
                    }else if(smp.getPhase().equals(3)){
                        CValue = smp.getActivePower();
                    }
                }
            }
            String sum = String.valueOf(Float.valueOf(AValue)+Float.valueOf(BValue)+Float.valueOf(CValue));
            pd.setTotal(sum);pd.setA(AValue);pd.setB(BValue);pd.setC(CValue);
            response.setSuccess(pd);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    @Operation(summary = "累计电能损耗")
    @PostMapping("/waste/total")
    public RestResponse<TotalABCResponseData> getTotalWaste(@RequestHeader(value = "token") String token, @RequestBody SelectBaseRequestBody requestBody) {
        RestResponse<TotalABCResponseData> response = new RestResponse<>();
        try {
            this.checkStationId(requestBody.getStationId());
            TotalABCResponseData pd = new TotalABCResponseData();
            List<String> statisticMonthPowerOfAll = statisticMonthPowerService.getStatisticMonthPowerOfAll(requestBody.getStationId());
            if(statisticMonthPowerOfAll!=null&&statisticMonthPowerOfAll.size()>0){
                pd.setA(statisticMonthPowerOfAll.get(0));
                pd.setB(statisticMonthPowerOfAll.get(1));
                pd.setC(statisticMonthPowerOfAll.get(2));
                BigDecimal A = new BigDecimal(statisticMonthPowerOfAll.get(0));
                BigDecimal B = new BigDecimal(statisticMonthPowerOfAll.get(1));
                BigDecimal C = new BigDecimal(statisticMonthPowerOfAll.get(2));
                pd.setTotal(""+A.add(B).add(C));
            }
            response.setSuccess(pd);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    @Operation(summary = "月度电能损耗")
    @PostMapping("/waste/monthly")
    public RestResponse<CurveDetailCreateTimeResponseData> getMonthlyWaste(@RequestHeader(value = "token") String token, @RequestBody SelectYyyyMMRequestBody requestBody) {
        RestResponse<CurveDetailCreateTimeResponseData> response = new RestResponse<>();
        try {
            this.checkStationId(requestBody.getStationId());
            int year = TimeUtil.getDateFormatByType(requestBody.getYyyyMM(),"year");
            int month = TimeUtil.getDateFormatByType(requestBody.getYyyyMM(),"month");
            List<StatisticDayPower> statisticDayPowerList = statisticDayPowerService.getStatisticDayPowerOfMonth(requestBody.getStationId(), year, month);
            List<String> curve = new ArrayList<>();
            List<String[]> detail = new ArrayList<>();
            List<String> createTime = new ArrayList<>();
            if(statisticDayPowerList!=null&&statisticDayPowerList.size()>0){
                int day = 1;
                String AValue = "";
                String BValue = "";
                String CValue = "";
                for(StatisticDayPower sdp:statisticDayPowerList){
                    day = sdp.getDay();
                    if(!createTime.contains(String.valueOf(day))){
                        createTime.add(String.valueOf(day));
                        for(StatisticDayPower sdptemp:statisticDayPowerList){
                            if(sdptemp.getDay().equals(day)){
                                if(sdptemp.getPhase().equals(1)){
                                    AValue = sdptemp.getActivePower();
                                }else if(sdptemp.getPhase().equals(2)){
                                    BValue = sdptemp.getActivePower();
                                }else if(sdptemp.getPhase().equals(3)){
                                    CValue = sdptemp.getActivePower();
                                }
                            }
                        }
                        String ABC = AValue+"|"+BValue+"|"+CValue;
                        String sum = String.valueOf(Float.valueOf(AValue)+Float.valueOf(BValue)+Float.valueOf(CValue));
                        curve.add(sum);System.out.println(day+"的ABC结果是"+ABC+"的总和是"+sum);
                        detail.add(ABC.split("[|]"));
                    }else{
                        continue;
                    }
                }
            }
            CurveDetailCreateTimeResponseData pd = new CurveDetailCreateTimeResponseData();
            pd.setCurve(curve);pd.setDetail(detail);pd.setCreateTime(createTime);
            response.setSuccess(pd);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    @Operation(summary = "日度电能损耗")
    @PostMapping("/waste/daily")
    public RestResponse<HighLowFuzaiCreateTimeResponseData> getDailyWaste(@RequestHeader(value = "token") String token, @RequestBody SelectPeriodRequestBody requestBody) {
        RestResponse<HighLowFuzaiCreateTimeResponseData> response = new RestResponse<>();
        try {
            this.checkStationId(requestBody.getStationId());
            TimeUtil.judgeTimePeriodValid(requestBody.getStartDate(),requestBody.getEndDate());
            List<StatisticHourPower> statisticHourPowerList = statisticHourPowerService.getStatisticHourPowerOfDay(requestBody.getStationId(), TimeUtil.formatStartDate(requestBody.getStartDate()), TimeUtil.formatEndDate(requestBody.getEndDate()));
            List<String> high = new ArrayList<>();
            List<String> low = new ArrayList<>();
            List<String> fuzai = new ArrayList<>();
            List<String> createTime = new ArrayList<>();
            if(statisticHourPowerList!=null&&statisticHourPowerList.size()>0){
                int hour = 1;
                String AHigh = "";
                String BHigh = "";
                String CHigh = "";
                String ALow = "";
                String BLow = "";
                String CLow = "";
                String AFuZai = "";
                String BFuZai = "";
                String CFuZai = "";
                for(StatisticHourPower sdp:statisticHourPowerList){
                    hour = sdp.getHour();
                    if(!createTime.contains(String.valueOf(hour))){
                        createTime.add(String.valueOf(hour));
                        for(StatisticHourPower sdptemp:statisticHourPowerList){
                            if(sdptemp.getHour().equals(hour)){
                                if(sdptemp.getPhase().equals(1)){
                                    AHigh = sdptemp.getHighEfficiency();
                                    ALow = sdptemp.getLowEfficiency();
                                    AFuZai = sdptemp.getActivePower();
                                }else if(sdptemp.getPhase().equals(2)){
                                    BHigh = sdptemp.getHighEfficiency();
                                    BLow = sdptemp.getLowEfficiency();
                                    BFuZai = sdptemp.getActivePower();
                                }else if(sdptemp.getPhase().equals(3)){
                                    CHigh = sdptemp.getHighEfficiency();
                                    CLow = sdptemp.getLowEfficiency();
                                    CFuZai = sdptemp.getActivePower();
                                }
                            }
                        }
                        String ABCHighSum = String.valueOf(Float.valueOf(AHigh)+Float.valueOf(BHigh)+Float.valueOf(CHigh));
                        String ABCLowSum = String.valueOf(Float.valueOf(ALow)+Float.valueOf(BLow)+Float.valueOf(CLow));
                        String ABCFuZaiSum = String.valueOf(Float.valueOf(AFuZai)+Float.valueOf(BFuZai)+Float.valueOf(CFuZai));
                        System.out.println(hour+"的ABCHighSum是"+ABCHighSum+"的ABCLowSum是"+ABCLowSum+"的ABCFuZaiSum是"+ABCFuZaiSum);
                        high.add(ABCHighSum);
                        low.add(ABCLowSum);
                        fuzai.add(ABCFuZaiSum);
                    }else{
                        continue;
                    }
                }
            }
            HighLowFuzaiCreateTimeResponseData pd = new HighLowFuzaiCreateTimeResponseData();
            pd.setHigh(high);pd.setLow(low);pd.setFuzai(fuzai);pd.setCreateTime(createTime);
            response.setSuccess(pd);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }


    @Operation(summary = "同类电能损耗对比")
    @PostMapping("/waste/similar")
    public RestResponse<ThisDeviceSimilarDeviceCreateTimeResponseData> getSimilarWaste(@RequestHeader(value = "token") String token, @RequestBody SelectYyyyRequestBody requestBody) {
        RestResponse<ThisDeviceSimilarDeviceCreateTimeResponseData> response = new RestResponse<>();
        try {
            this.checkStationId(requestBody.getStationId());
            int year = TimeUtil.getDateFormatByType(requestBody.getYyyy(),"year");
            List<StatisticMonthPower> statisticMonthPowerList = statisticMonthPowerService.getStatisticMonthPowerOfYear(Arrays.asList(requestBody.getStationId()), year);
            List<String> thisDevice = new ArrayList<>();
            List<String> similarDevice = new ArrayList<>();
            List<String> createTime = new ArrayList<>();
            List<Long> stationIdList = new ArrayList<>();
            if(statisticMonthPowerList!=null&&statisticMonthPowerList.size()>0){
                int month = 1;
                String AValue = "";
                String BValue = "";
                String CValue = "";
                for(StatisticMonthPower sdp:statisticMonthPowerList){
                    month = sdp.getMonth();
                    if(!createTime.contains(String.valueOf(month))){
                        createTime.add(String.valueOf(month));
                        for(StatisticMonthPower sdptemp:statisticMonthPowerList){
                            if(sdptemp.getMonth().equals(month)){
                                if(sdptemp.getPhase().equals(1)){
                                    AValue = sdptemp.getActivePower();
                                }else if(sdptemp.getPhase().equals(2)){
                                    BValue = sdptemp.getActivePower();
                                }else if(sdptemp.getPhase().equals(3)){
                                    CValue = sdptemp.getActivePower();
                                }
                            }
                        }
                        String ABC = String.valueOf(Float.valueOf(AValue)+Float.valueOf(BValue)+Float.valueOf(CValue));
                        System.out.println(month+"的AValue是"+AValue+"的BValue是"+BValue+"的CValue是"+CValue);
                        thisDevice.add(ABC);
                    }else{
                        continue;
                    }
                }
            }
            //------------查同类型设备------------
            TransformerInfo transformerInfo = transformerInfoService.findByStationId(requestBody.getStationId());
            int deleteLength = transformerInfo.getXh().split("[-]").length;
            String sbxh = transformerInfo.getXh().replace(transformerInfo.getXh().split("[-]")[deleteLength-1],"");
            sbxh = sbxh.substring(0,sbxh.length()-1);   System.out.println("从"+transformerInfo.getXh()+"截取为"+sbxh);
            List<TransformerInfo> similarTransformerList = transformerInfoService.getSimilarTransformer(sbxh,transformerInfo.getEdrl(),transformerInfo.getZkdy(),transformerInfo.getLjzbh());

            if(similarTransformerList!=null&&similarTransformerList.size()>0){
                List<Long> similarTransIdList = similarTransformerList.stream().map(e -> e.getStationId()).collect(Collectors.toList());
                for(long i:similarTransIdList){
                    if(i==(requestBody.getStationId())){
                        similarTransIdList.remove(requestBody.getStationId());
                    }
                }System.out.println("已从"+similarTransIdList.toString()+"中去掉"+requestBody.getStationId());
                List<StatisticMonthPower> similarMonthPowerList = statisticMonthPowerService.getStatisticMonthPowerOfYear(similarTransIdList, year);
                int month = 1;
                Long stationId;
                Double AValueSum = 0.0;
                Double BValueSum = 0.0;
                Double CValueSum = 0.0;
                for(StatisticMonthPower sdp:statisticMonthPowerList){
                    month = sdp.getMonth();
                    if(!createTime.contains(String.valueOf(month))){
                        createTime.add(String.valueOf(month));
                        for(StatisticMonthPower sdpMonth:statisticMonthPowerList){
                            Double AValue = 0.0;
                            Double BValue = 0.0;
                            Double CValue = 0.0;
                            stationId = sdpMonth.getStationId();
                            if(!stationIdList.contains(stationId)) {
                                stationIdList.add(stationId);
                                for (StatisticMonthPower sdptempABC : statisticMonthPowerList) {
                                    if (sdptempABC.getMonth().equals(month) && sdptempABC.getStationId().equals(stationId)) {
                                        if (sdpMonth.getPhase().equals(1)) {
                                            AValue = Double.valueOf(sdptempABC.getActivePower());AValueSum += AValue;
                                        } else if (sdptempABC.getPhase().equals(2)) {
                                            BValue = Double.valueOf(sdptempABC.getActivePower());BValueSum += BValue;
                                        } else if (sdptempABC.getPhase().equals(3)) {
                                            CValue = Double.valueOf(sdptempABC.getActivePower());CValueSum += CValue;break;
                                        }
                                    }
                                }
                                System.out.println(month+"月，站房ID="+stationId+"，AValue是"+AValue+"，BValue是"+BValue+"，CValue是"+CValue);
                            }
                            similarDevice.add(""+new BigDecimal(AValueSum+BValueSum+CValueSum).divide(new BigDecimal(stationIdList.size()),2,BigDecimal.ROUND_HALF_UP));
                        }
                    }else{
                        continue;
                    }
                }
            }
            ThisDeviceSimilarDeviceCreateTimeResponseData pd = new ThisDeviceSimilarDeviceCreateTimeResponseData();
            pd.setThisDevice(thisDevice);pd.setSimilarDevice(similarDevice);pd.setCreateTime(createTime);
            response.setSuccess(pd);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }

    /*普通10千伏变压器额定电流的计算方法如下： 一次侧就是高压侧，二次侧就是低压侧
    假设10千伏变压器容量为200KVA，则一次侧额定电流计算公式如下：I=200/（1.732×10）=11.55A；二次侧额定电流计算公式如下：I=200/（1.732×0.4）=288.68A。*/
    @Operation(summary = "电流电压监测弹窗")
    @PostMapping("/waste/graph")
    public RestResponse<GraphWasteResponseData> getGraphWaste(@RequestHeader(value = "token") String token, @RequestBody SelectPeriodRequestBody requestBody) {
        RestResponse<GraphWasteResponseData> response = new RestResponse<>();
        try {
            this.checkStationId(requestBody.getStationId());
            TimeUtil.judgeTimePeriodValid(requestBody.getStartDate(),requestBody.getEndDate());
            List<RealTimePower> realTimePowerList = realTimePowerService.getRealTimePowerOfPeriod(requestBody.getStationId(), TimeUtil.formatStartDate(requestBody.getStartDate()), TimeUtil.formatEndDate(requestBody.getEndDate()));
            String edrl = transformerInfoService.getEdrlByStationId(requestBody.getStationId());
            List<String> AHighCurrentList = new ArrayList<>();
            List<String> BHighCurrentList = new ArrayList<>();
            List<String> CHighCurrentList = new ArrayList<>();
            List<String> AHighVoltageList = new ArrayList<>();
            List<String> BHighVoltageList = new ArrayList<>();
            List<String> CHighVoltageList = new ArrayList<>();
            List<String> ALowCurrentList = new ArrayList<>();
            List<String> BLowCurrentList = new ArrayList<>();
            List<String> CLowCurrentList = new ArrayList<>();
            List<String> ALowVoltageList = new ArrayList<>();
            List<String> BLowVoltageList = new ArrayList<>();
            List<String> CLowVoltageList = new ArrayList<>();
            String edHighVoltage1 = ""+(10000+10000*0.07);
            String edLowVoltage1 = ""+(220+220*0.07);
            String edHighVoltage2 = ""+(10000-10000*0.07);
            String edLowVoltage2 = ""+(220-220*0.1);
            String edHighCurrent = ""+new BigDecimal(edrl).divide(new BigDecimal(1.732*10),2,BigDecimal.ROUND_HALF_UP);
            String edLowCurrent = ""+new BigDecimal(edrl).divide(new BigDecimal(1.732*0.4),2,BigDecimal.ROUND_HALF_UP);
            List<String> createTime = new ArrayList<>();
            if(realTimePowerList!=null&&realTimePowerList.size()>0){
                String hour = "";
                String AHighCurrentT = "";
                String BHighCurrentT = "";
                String CHighCurrentT = "";
                String AHighVoltageT = "";
                String BHighVoltageT = "";
                String CHighVoltageT = "";
                String ALowCurrentT = "";
                String BLowCurrentT = "";
                String CLowCurrentT = "";
                String ALowVoltageT = "";
                String BLowVoltageT = "";
                String CLowVoltageT = "";

                for(RealTimePower sdp:realTimePowerList){
                    hour = TimeUtil.dateFormat(sdp.getCreatedTime(),"yyyy-MM-dd HH:mm");
                    if(!createTime.contains(hour)){
                        createTime.add(hour);
                        for(RealTimePower sdptemp:realTimePowerList){
                            String tempHour = TimeUtil.dateFormat(sdptemp.getCreatedTime(),"yyyy-MM-dd HH:mm");
                            if(tempHour.equals(hour)){
                                if(sdptemp.getPhase().equals(1)){
                                    AHighVoltageT = sdptemp.getHighVoltage(); AHighVoltageList.add(AHighVoltageT);
                                    AHighCurrentT = sdptemp.getHighCurrent(); AHighCurrentList.add(AHighCurrentT);
                                    ALowVoltageT = sdptemp.getLowVoltage(); ALowVoltageList.add(ALowVoltageT);
                                    ALowCurrentT = sdptemp.getLowCurrent(); ALowCurrentList.add(ALowCurrentT);
                                }else if(sdptemp.getPhase().equals(2)){
                                    BHighVoltageT = sdptemp.getHighVoltage(); BHighVoltageList.add(BHighVoltageT);
                                    BHighCurrentT = sdptemp.getHighCurrent(); BHighCurrentList.add(BHighCurrentT);
                                    BLowVoltageT = sdptemp.getLowVoltage(); BLowVoltageList.add(BLowVoltageT);
                                    BLowCurrentT = sdptemp.getLowCurrent(); BLowCurrentList.add(BLowCurrentT);
                                }else if(sdptemp.getPhase().equals(3)){
                                    CHighVoltageT = sdptemp.getHighVoltage(); CHighVoltageList.add(CHighVoltageT);
                                    CHighCurrentT = sdptemp.getHighCurrent(); CHighCurrentList.add(CHighCurrentT);
                                    CLowVoltageT = sdptemp.getLowVoltage(); CLowVoltageList.add(CLowVoltageT);
                                    CLowCurrentT = sdptemp.getLowCurrent(); CLowCurrentList.add(CLowCurrentT);
                                }
                            }
                        }
                    }else{
                        continue;
                    }
                }
            }
            GraphWasteResponseData pd = new GraphWasteResponseData(edHighVoltage1, edLowVoltage1,edHighVoltage2, edLowVoltage2, edHighCurrent, edLowCurrent, AHighCurrentList, BHighCurrentList, CHighCurrentList, AHighVoltageList, BHighVoltageList, CHighVoltageList, ALowCurrentList, BLowCurrentList, CLowCurrentList, ALowVoltageList, BLowVoltageList, CLowVoltageList, createTime, createTime, createTime, createTime);
            response.setSuccess(pd);
        } catch (BaseException exception) {
            response.setFail(exception.getCode(), exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
            response.setFail(Status.EXCEPTION.getCode(), Status.EXCEPTION.getMessage());
        }
        return response;
    }
}
