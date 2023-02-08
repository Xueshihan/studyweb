package cn.iatc.web.service;

import cn.iatc.database.entity.Station;
import cn.iatc.database.entity.StationRelation;
import cn.iatc.web.bean.station.StationPojo;
import cn.iatc.web.common.tree.data.BaseNode;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface StationService extends IService<Station> {

    public void createBatch(List<BaseNode<String, Integer>> stationDataList, List<BaseNode<String, Integer>> treeNodes, StationRelation upperStationRelation, Boolean isInit);

    // 创建站点 level 0-5
    public void createBuilding(StationPojo stationPojo);
    // 更新站点 level 0-5
    public void updateBuilding(StationPojo stationPojo);

    public void createDevice(StationPojo stationPojo);

    public void updateDevice(StationPojo stationPojo);

    public void updateCountByStation(Long stationId, Integer count);

    public void deleteById(Long id);

    public void deleteByIds(List<Long> ids);

    public Station findById(Long id);

    public Long countStation();

    public Long countByLevels(List<Integer> levels);

    public Long countByRegion(Long regionId);

    public List<Long> findIdsByRegion(Long regionId);

    public Station findByCode(String code);

    public List<Station> findByLevel(Integer level);

    public List<BaseNode<Long, Integer>> findTreeByLevels(List<Integer> levels);

    public List<BaseNode<Long, Integer>> findTreeListByIdLevels(Long id, List<Integer> levels);

    /**
     * 获取该级别下所有结构
     * @param id
     * @param isSelf 是否需要id这层信息， 0-不要，1-要
     * @return
     */
    public List<BaseNode<Long, Integer>> findTreeNextListById(Long id, Integer isSelf);

    /**
     * 获取该id下级(孩子)节点
     * @param id
     * @return
     */
    public List<BaseNode<Long, Integer>> findTreeNextChildById(Long id);

}
