package cn.iatc.web.init;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.iatc.database.entity.Station;
import cn.iatc.web.bean.init.StationTreeNode;
import cn.iatc.web.common.tree.TreeBuilder;
import cn.iatc.web.common.tree.data.BaseNode;
import cn.iatc.web.service.StationService;
import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class StationInitHelper {

    // 处理公司数据
    public void handle(StationService stationService, String filePath) {
        if (stationService.countStation() > 0) {
            return;
        }
        log.info("======= insert station start");
        this.handleData(stationService, filePath);
        log.info("======= insert station end");
    }

    public void handleData(StationService stationService, String filePath) {
        List<BaseNode<String, Integer>> stationDataList = new ArrayList<>();
        CSVReader csvReader = null;
        try {
            ClassPathResource resource = new ClassPathResource(filePath);
            InputStream inputStream = resource.getStream();
            csvReader = new CSVReader(new InputStreamReader(inputStream, "utf-8"));
            String[] strArr = null;
            while ((strArr = csvReader.readNext())!= null) {
                if (strArr[0].equals("code")) {
                    continue;
                }
                StationTreeNode<String, Integer> stationData = new StationTreeNode<>();
                stationData.setId(strArr[0]);
                stationData.setName(strArr[1]);
                stationData.setLevel(Integer.valueOf(strArr[2]));
                stationData.setParentId(strArr[3]);
                stationData.setRegionCode(strArr[4]);
                stationData.setSort(1);
                stationDataList.add(stationData);
            }

            TreeBuilder<String, Integer> treeBuilder = new TreeBuilder<>();
            List<BaseNode<String, Integer>> treNodes = treeBuilder.buildTreeList(stationDataList);
            stationService.createBatch(stationDataList, treNodes, null, true);
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
}
