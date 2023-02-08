package cn.iatc.web.init;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.iatc.web.common.tree.TreeBuilder;
import cn.iatc.web.common.tree.data.BaseNode;
import cn.iatc.web.bean.init.RegionTreeNode;
import cn.iatc.web.service.RegionService;
import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RegionInitHelper {

    public void handle(RegionService regionService, String filePath) {
        if (regionService.countRegion() > 0) {
            return;
        }
        log.info("======= insert region start");
        this.handleData(regionService, filePath);
        log.info("======= insert region end");
    }

    private void handleData(RegionService regionService, String filePath) {
        List<BaseNode<String, Integer>> regionDataList = new ArrayList<>();
        CSVReader csvReader = null;
        try {
            ClassPathResource resource = new ClassPathResource(filePath);
            InputStream inputStream = resource.getStream();
            csvReader = new CSVReader(new InputStreamReader(inputStream, "utf-8"));
            String[] strArr = null;
            while ((strArr = csvReader.readNext())!= null) {
                if (strArr[0].equals("name")) {
                    continue;
                }
                RegionTreeNode<String, Integer> regionData = new RegionTreeNode<>();
                regionData.setName(strArr[0]);
                regionData.setPinyin(strArr[1]);
                regionData.setId(strArr[2]);
                regionData.setParentId(strArr[3]);
                regionData.setLevel(Integer.valueOf(strArr[4]));
                regionData.setSort(Integer.valueOf(strArr[5]));
                regionData.setNationalCode(strArr[6]);
                regionDataList.add(regionData);
            }

            TreeBuilder<String, Integer> treeBuilder = new TreeBuilder<>();
            List<BaseNode<String, Integer>> nodes = treeBuilder.buildTreeList(regionDataList);
            regionService.createBatch(nodes, null, true);
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
