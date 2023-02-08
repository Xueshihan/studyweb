package cn.iatc.web.init;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.iatc.database.entity.Station;
import cn.iatc.database.entity.TransformerInfo;
import cn.iatc.web.common.context.SpringContextHolder;
import cn.iatc.web.constants.CommonConstants;
import cn.iatc.web.service.StationService;
import cn.iatc.web.service.TransformerInfoService;
import cn.iatc.web.utils.TimeUtil;
import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class TransformerInitHelper {

    private static final StationService stationService;
    static {
        stationService = SpringContextHolder.getBean(StationService.class);
    }
    public void handle(TransformerInfoService transformerInfoService, String filePath) {
        if (transformerInfoService.countTrans() > 0) {
            return;
        }
        this.handleData(transformerInfoService, filePath);
    }

    private void handleData(TransformerInfoService transformerInfoService, String filePath) {
        CSVReader csvReader = null;
        try {
            ClassPathResource resource = new ClassPathResource(filePath);
            InputStream inputStream = resource.getStream();
            csvReader = new CSVReader(new InputStreamReader(inputStream, "utf-8"));
            List<TransformerInfo> transformerInfoList = new ArrayList<>();
            String[] strArr = null;
            while ((strArr = csvReader.readNext())!= null) {
                if (strArr[0].equals("station_code")) {
                    continue;
                }
                TransformerInfo transformerInfo = new TransformerInfo();
                String stationCode = strArr[0];
                Station station = stationService.findByCode(stationCode);
                Long stationId = null;
                if (station != null) {
                    stationId = station.getId();
                }
                transformerInfo.setStationId(stationId);
                this.fromData(strArr, transformerInfo);
                transformerInfoList.add(transformerInfo);
            }

            transformerInfoService.createBatch(transformerInfoList);
        } catch(Exception exception){
            exception.printStackTrace();
        } finally {
            if (csvReader != null) {
                try {
                    csvReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void fromData(String[] strArr, TransformerInfo transformerInfo) {
        transformerInfo.setSbmc(strArr[1]);
        transformerInfo.setEdrl(strArr[2]);
        transformerInfo.setZkdy(strArr[3]);
        transformerInfo.setKzdl(strArr[4]);
        transformerInfo.setDlsh(strArr[5]);
        transformerInfo.setKzsh(strArr[6]);
        transformerInfo.setDyb(strArr[7]);
        transformerInfo.setGyceddl(strArr[8]);
        transformerInfo.setYxbh(strArr[9]);
        transformerInfo.setZxmc(strArr[10]);
        transformerInfo.setSsgt(strArr[11]);
        transformerInfo.setSyxz(strArr[12]);
        transformerInfo.setSsds(strArr[13]);
        transformerInfo.setYwdw(strArr[14]);
        transformerInfo.setWhbz(strArr[15]);
        transformerInfo.setSsdkx(strArr[16]);
        transformerInfo.setGdqy(strArr[17]);
        transformerInfo.setDydj(strArr[18]);
        transformerInfo.setDkxzx(strArr[19]);
        transformerInfo.setSbzt(strArr[20]);
        Date tyrqDate = TimeUtil.parseDate(strArr[21], "yyyy-MM-dd");
        transformerInfo.setTyrq(tyrqDate);
        transformerInfo.setSfdw(strArr[22]);
        transformerInfo.setSfnw(strArr[23]);
        transformerInfo.setZycd(strArr[24]);
        transformerInfo.setZcxz(strArr[25]);
        transformerInfo.setZcdw(strArr[26]);
        transformerInfo.setZcbh(strArr[27]);
        transformerInfo.setSbzjfs(strArr[28]);
        transformerInfo.setXh(strArr[29]);
        transformerInfo.setSccj(strArr[30]);
        transformerInfo.setCcbh(strArr[31]);
        transformerInfo.setDxbm(strArr[32]);
        Date ccrqDate = TimeUtil.parseDate(strArr[33], "yyyy-MM-dd");
        transformerInfo.setCcrq(ccrqDate);
        transformerInfo.setDxmc(strArr[34]);
        transformerInfo.setJyjz(strArr[35]);
        transformerInfo.setSffjb(strArr[36]);
        transformerInfo.setWzkgfjds(strArr[37]);
        transformerInfo.setWzkgfjwz(strArr[38]);
        transformerInfo.setXmmc(strArr[39]);
        transformerInfo.setDyceddl(strArr[40]);
        transformerInfo.setXmbm(strArr[41]);
        transformerInfo.setJddz(strArr[42]);
        transformerInfo.setYh(strArr[43]);
        transformerInfo.setYz(strArr[44]);
        transformerInfo.setZz(strArr[45]);
        transformerInfo.setLx(strArr[46]);
        transformerInfo.setJynrdj(strArr[47]);
        transformerInfo.setSbbm(strArr[48]);
        Date djsjDate = TimeUtil.parseDate(strArr[49], "yyyy-MM-dd");
        transformerInfo.setDjsj(djsjDate);
        transformerInfo.setBz(strArr[50]);
        transformerInfo.setLjzbh(strArr[51]);
        transformerInfo.setGnwz(strArr[52]);
        transformerInfo.setSwid(strArr[53]);
        transformerInfo.setTzcjfs(strArr[54]);
        transformerInfo.setYwsbzl(strArr[55]);
        transformerInfo.setSfctsb(strArr[56]);
        transformerInfo.setPmbm(strArr[57]);
        transformerInfo.setSsxlmc(strArr[58]);
        transformerInfo.setZyfl(strArr[59]);
        transformerInfo.setZltxsl(strArr[60]);
        transformerInfo.setSblxbm(strArr[62]);
        transformerInfo.setDydjmc(strArr[62]);
        transformerInfo.setDydjdm(strArr[63]);
        transformerInfo.setSsdkxid(strArr[64]);
        transformerInfo.setDqtz(strArr[65]);
        transformerInfo.setDkxzxid(strArr[66]);
        transformerInfo.setDxmpyxkid(strArr[67]);
        transformerInfo.setObjId(strArr[68]);
        transformerInfo.setEnabledStatus(CommonConstants.Status.ENABLED.getValue());
        Date createdDate = new Date();
        transformerInfo.setCreatedTime(createdDate);
        transformerInfo.setUpdatedTime(createdDate);
    }
}
