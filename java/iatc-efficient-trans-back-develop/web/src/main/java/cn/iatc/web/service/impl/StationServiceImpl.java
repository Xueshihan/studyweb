package cn.iatc.web.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.iatc.database.entity.Region;
import cn.iatc.database.entity.Station;
import cn.iatc.database.entity.StationRelation;
import cn.iatc.database.entity.StationType;
import cn.iatc.web.bean.station.StationPojo;
import cn.iatc.web.common.data.Status;
import cn.iatc.web.common.data.exception.BaseException;
import cn.iatc.web.common.tree.data.BaseNode;
import cn.iatc.web.constants.CommonConstants;
import cn.iatc.web.bean.init.StationTreeNode;
import cn.iatc.web.mapper.RegionMapper;
import cn.iatc.web.mapper.StationMapper;
import cn.iatc.web.mapper.StationRelationMapper;
import cn.iatc.web.mapper.StationTypeMapper;
import cn.iatc.web.service.StationService;
import cn.iatc.web.utils.SnowFlakeFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional
public class StationServiceImpl extends ServiceImpl<StationMapper, Station> implements StationService {

    @Autowired
    private SnowFlakeFactory snowFlakeFactory;

    @Autowired
    private StationMapper stationMapper;

    @Autowired
    private StationTypeMapper stationTypeMapper;

    @Autowired
    private RegionMapper regionMapper;

    @Autowired
    private StationRelationMapper stationRelationMapper;

    @Override
    public void createBatch(List<BaseNode<String, Integer>> stationDataList, List<BaseNode<String, Integer>> treeNodes, StationRelation upperStationRelation, Boolean isInit) {
        if (isInit) {
            UpdateWrapper<Station> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().set(Station::getEnabledStatus, CommonConstants.Status.DISABLED.getValue());
            this.update(updateWrapper);

            QueryWrapper<StationRelation> srWrapper = new QueryWrapper<>();
            srWrapper.lambda().gt(StationRelation::getId, 0L);
            stationRelationMapper.delete(srWrapper);
        }
        this.createStations(stationDataList);
        log.info("======= createStations start");
        List<StationRelation> stationRelationList = new ArrayList<>();
        log.info("======= createStationRelation start ");
        this.createStationRelation(treeNodes, upperStationRelation, stationRelationList);
        if (stationDataList.size() > 0) {
            stationRelationMapper.createBatch(stationRelationList);
        }
        log.info("======= createStationRelation end ");

//        this.createList(treeNodes, upperStationRelation);
    }

    private void createStations(List<BaseNode<String, Integer>> stationDataList) {
        List<Station> addStationList = new ArrayList<>();
        for (BaseNode<String, Integer> data: stationDataList) {
            StationTreeNode<String, Integer> stationData = (StationTreeNode<String, Integer>) data;
            boolean createStatus = false;
            Date createdTime = new Date();
            String code = stationData.getId();
            String name = stationData.getName();
            Integer level = stationData.getLevel();
            String regionCode = stationData.getRegionCode();

            QueryWrapper<Station> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Station::getCode, code);
            Station station = stationMapper.selectOne(queryWrapper);
            if (station == null) {
                createStatus = true;
                station = new Station();
                station.setCode(code);
                station.setCreatedTime(createdTime);
            }

            station.setName(name);
            station.setLevel(level);

            Region region = regionMapper.findByCode(regionCode);
            Long regionId = null;
            if (region != null) {
                regionId = region.getId();
            }
            station.setRegionId(regionId);

            station.setEnabledStatus(CommonConstants.Status.ENABLED.getValue());
            station.setUpdatedTime(createdTime);
            if (createStatus) {
                addStationList.add(station);
            } else {
                UpdateWrapper<Station> stationUpdateWrapper = new UpdateWrapper<>();
                stationUpdateWrapper.lambda().set(Station::getName, station.getName());
                stationUpdateWrapper.lambda().set(Station::getLevel, station.getLevel());
                stationUpdateWrapper.lambda().set(Station::getStationCount, station.getStationCount());
                stationUpdateWrapper.lambda().set(Station::getStationTypeId, station.getStationTypeId());
                stationUpdateWrapper.lambda().set(Station::getRegionId, station.getRegionId());
                stationUpdateWrapper.lambda().set(Station::getEnabledStatus, station.getEnabledStatus());
                stationUpdateWrapper.lambda().set(Station::getUpdatedTime, station.getUpdatedTime());
                stationUpdateWrapper.lambda().eq(Station::getId, station.getId());
                this.update(stationUpdateWrapper);
            }
        }
        if (addStationList.size() > 0) {
            stationMapper.createStations(addStationList);
        }
    }

    private void createStationRelation(List<BaseNode<String, Integer>> treeNodes, StationRelation upperStationRelation, List<StationRelation> stationRelationList) {

        for (BaseNode<String, Integer> node: treeNodes) {
            StationTreeNode<String, Integer> stationNode = (StationTreeNode<String, Integer>) node;
            String code = stationNode.getId();
            Station station = stationMapper.findByCode(code);

            StationRelation stationRelation = new StationRelation();
            stationRelation.setCurStationId(station.getId());
            if (upperStationRelation != null) {
                Long upperId = upperStationRelation.getCurStationId();
                stationRelation.setUpperId(upperId);
                String upperIdSet = upperStationRelation.getUpperIdSet();
                if (upperIdSet == null) {
                    upperIdSet = Long.toString(upperId);
                } else {
                    upperIdSet = StrUtil.format("{},{}", upperIdSet, upperId);
                }
                stationRelation.setUpperIdSet(upperIdSet);
            } else {
                stationRelation.setUpperId(null);
                stationRelation.setUpperIdSet(null);
            }
            stationRelationList.add(stationRelation);

            List<BaseNode<String, Integer>> children = stationNode.getChildren();
            if (children.size() > 0) {
                createStationRelation(children, stationRelation, stationRelationList);
            }
        }
    }

    private void createList(List<BaseNode<String, Integer>> treeNodes, StationRelation upperStationRelation) {
        for (BaseNode<String, Integer> node: treeNodes) {
            StationTreeNode<String, Integer> stationNode = (StationTreeNode<String, Integer>) node;
            boolean createStatus = false;
            Date createdTime = new Date();
            String code = stationNode.getId();
            String name = stationNode.getName();
            Integer level = stationNode.getLevel();
            String regionCode = stationNode.getRegionCode();

            QueryWrapper<Station> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Station::getCode, code);
            Station station = stationMapper.selectOne(queryWrapper);
            if (station == null) {
                createStatus = true;
                station = new Station();
                station.setCode(code);
                station.setCreatedTime(createdTime);
            }

            station.setName(name);
            station.setLevel(level);

            Region region = regionMapper.findByCode(regionCode);
            Long regionId = null;
            if (region != null) {
                regionId = region.getId();
            }
            station.setRegionId(regionId);

            station.setEnabledStatus(CommonConstants.Status.ENABLED.getValue());
            station.setUpdatedTime(createdTime);

            List<BaseNode<String, Integer>> children = stationNode.getChildren();
            if (createStatus) {
                stationMapper.insert(station);
            } else {
                // 解决当某个字段设置null时，不能够改变其值问题 https://blog.csdn.net/Boy_Martin/article/details/126264676
                UpdateWrapper<Station> stationUpdateWrapper = new UpdateWrapper<>();
                stationUpdateWrapper.lambda().set(Station::getName, station.getName());
                stationUpdateWrapper.lambda().set(Station::getLevel, station.getLevel());
                stationUpdateWrapper.lambda().set(Station::getStationCount, station.getStationCount());
                stationUpdateWrapper.lambda().set(Station::getStationTypeId, station.getStationTypeId());
                stationUpdateWrapper.lambda().set(Station::getRegionId, station.getRegionId());
                stationUpdateWrapper.lambda().set(Station::getEnabledStatus, station.getEnabledStatus());
                stationUpdateWrapper.lambda().set(Station::getUpdatedTime, station.getUpdatedTime());
                stationUpdateWrapper.lambda().eq(Station::getId, station.getId());
                this.update(stationUpdateWrapper);
            }

            StationRelation stationRelation = new StationRelation();
            stationRelation.setCurStationId(station.getId());
            if (upperStationRelation != null) {
                Long upperId = upperStationRelation.getCurStationId();
                stationRelation.setUpperId(upperId);
                String upperIdSet = upperStationRelation.getUpperIdSet();
                if (upperIdSet == null) {
                    upperIdSet = Long.toString(upperId);
                } else {
                    upperIdSet = StrUtil.format("{},{}", upperIdSet, upperId);
                }
                stationRelation.setUpperIdSet(upperIdSet);
            } else {
                stationRelation.setUpperId(null);
                stationRelation.setUpperIdSet(null);
            }
            stationRelationMapper.insert(stationRelation);

            if (children.size() > 0) {
                createList(children, stationRelation);
            }
        }
    }

    @Override
    public void createBuilding(StationPojo stationPojo) {
        Integer level = stationPojo.getLevel();
        String code = stationPojo.getCode();
        String name = stationPojo.getName();
        String address = stationPojo.getAddress();
        Long upperId = stationPojo.getUpperId();
        Long stationTypeId = stationPojo.getStationTypeId();
        Integer stationCount = stationPojo.getStationCount();
        String pmsId = stationPojo.getPmsId();

        Date createdTime = new Date();
        Station station = new Station();
        if(StrUtil.isBlank(code)) {
            code = snowFlakeFactory.nextIdStr();
        }
        station.setCode(code);
        station.setName(name);
        station.setEnabledStatus(CommonConstants.Status.ENABLED.getValue());
        station.setCreatedTime(createdTime);
        station.setUpdatedTime(createdTime);
        Station upperStation = stationMapper.findById(upperId);

        if (level.equals(Station.Level.LEVEL0.getValue())) {
            if(upperId != null) {
                throw new BaseException(Status.STATION_LEVEL_INVALID);
            }
            station.setLevel(Station.Level.LEVEL0.getValue());
        } else if (level.equals(Station.Level.LEVEL1.getValue()) || level.equals(Station.Level.LEVEL2.getValue()) ||
                level.equals(Station.Level.LEVEL3.getValue()) || level.equals(Station.Level.LEVEL4.getValue()) ||
                level.equals(Station.Level.LEVEL5.getValue())) {

            if (upperStation == null) {
                throw new BaseException(Status.STATION_UPPER_ID_INVALID);
            }

            Integer upperLevel = upperStation.getLevel();
            Integer nextLevel = Station.LevelRelation.levelMap.get(upperLevel);

            if (!level.equals(nextLevel)) {
                throw new BaseException(Status.STATION_LEVEL_INVALID);
            }
            station.setLevel(nextLevel);
            if (!level.equals(Station.Level.LEVEL5.getValue())) {
                station.setStationCount(stationCount);
            }

            if (level.equals(Station.Level.LEVEL4.getValue())) {
                StationType stationType = stationTypeMapper.findById(stationTypeId);
                if (stationType != null) {
                    station.setStationTypeId(stationTypeId);
                    station.setName(stationType.getName());
                }
            }

            if (level.equals(Station.Level.LEVEL5.getValue())) {
                code = snowFlakeFactory.nextIdStr();
                station.setCode(code);
                station.setAddress(address);
                station.setPmsId(pmsId);
                station.setLatitude(stationPojo.getLatitude());
                station.setLongitude(stationPojo.getLongitude());
                station.setContactName(stationPojo.getContactName());
                station.setContactPhone(stationPojo.getContactPhone());
                station.setSmartTime(stationPojo.getSmartTime());
                station.setPanoramacaUrl(stationPojo.getPanoramacaUrl());
                Integer powerEnvFlag = Station.OptionHandle.transform(stationPojo.getPowerEnvFlag(), Station.Option.POWER_ENV);
                Integer gatewayFlag = Station.OptionHandle.transform(stationPojo.getGatewayFlag(), Station.Option.GATEWAY);
                Integer onlandsFlag = Station.OptionHandle.transform(stationPojo.getOnlandsFlag(), Station.Option.ON_LANDS);
                Integer options = powerEnvFlag | gatewayFlag | onlandsFlag;
                station.setOptions(options);
            }
        }
        stationMapper.insert(station);

        // 添加站点关系表
        if (upperStation != null) {
            List<StationRelation> upperStationRelations = stationRelationMapper.findByCurStation(upperId);
            for(StationRelation upperStationRelation: upperStationRelations) {

                StationRelation stationRelation = new StationRelation();
                stationRelation.setCurStationId(station.getId());
                stationRelation.setUpperId(upperId);

                String upperIdSet = upperStationRelation.getUpperIdSet();
                if (upperIdSet == null) {
                    upperIdSet = Long.toString(upperId);
                } else {
                    upperIdSet = StrUtil.format("{},{}", upperIdSet, upperId);
                }
                stationRelation.setUpperIdSet(upperIdSet);
                stationRelationMapper.insert(stationRelation);
            }
        }

    }

    @Override
    public void updateBuilding(StationPojo stationPojo) {
        Long stationId = stationPojo.getId();
        Station updateStation = stationMapper.findById(stationId);
        Integer level = updateStation.getLevel();

        UpdateWrapper<Station> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(Station::getId, stationId);

        Date updatedTime = new Date();
        Station station = new Station();
        station.setUpdatedTime(updatedTime);
        station.setName(stationPojo.getName());
        if (level.equals(Station.Level.LEVEL0.getValue()) || level.equals(Station.Level.LEVEL1.getValue()) || level.equals(Station.Level.LEVEL2.getValue()) ||
                level.equals(Station.Level.LEVEL3.getValue()) || level.equals(Station.Level.LEVEL4.getValue())) {
            station.setStationCount(stationPojo.getStationCount());
            if (level.equals(Station.Level.LEVEL4.getValue())) {
                Long stationTypeId = stationPojo.getStationTypeId();
                StationType stationType = stationTypeMapper.findById(stationTypeId);
                if (stationType != null) {
                    station.setStationTypeId(stationTypeId);
                    station.setName(stationType.getName());
                }
            }
        } else if (level.equals(Station.Level.LEVEL5.getValue())) {
            station.setAddress(stationPojo.getAddress());
            station.setPmsId(stationPojo.getPmsId());
            station.setLatitude(stationPojo.getLatitude());
            station.setLongitude(stationPojo.getLongitude());
            station.setContactName(stationPojo.getContactName());
            station.setContactPhone(stationPojo.getContactPhone());
            station.setSmartTime(stationPojo.getSmartTime());
            station.setPanoramacaUrl(stationPojo.getPanoramacaUrl());
            Integer powerEnvFlag = Station.OptionHandle.transform(stationPojo.getPowerEnvFlag(), Station.Option.POWER_ENV);
            Integer gatewayFlag = Station.OptionHandle.transform(stationPojo.getGatewayFlag(), Station.Option.GATEWAY);
            Integer onlandsFlag = Station.OptionHandle.transform(stationPojo.getOnlandsFlag(), Station.Option.ON_LANDS);
            Integer options = powerEnvFlag | gatewayFlag | onlandsFlag;
            station.setOptions(options);
        }

        stationMapper.update(station, wrapper);
    }

    @Override
    public void createDevice(StationPojo stationPojo) {
        Long upperId = stationPojo.getUpperId();
        String code = snowFlakeFactory.nextIdStr();

        Date createdTime = new Date();
        Station station = new Station();
        station.setCode(code);
        station.setName(stationPojo.getName());
        station.setSort(stationPojo.getSort());
        station.setCapacity(stationPojo.getCapacity());
        station.setSmartTime(stationPojo.getSmartTime());
        station.setPmsId(stationPojo.getPmsId());
        station.setEnabledStatus(CommonConstants.Status.ENABLED.getValue());
        station.setCreatedTime(createdTime);
        station.setUpdatedTime(createdTime);

        Station upperStation = stationMapper.findById(upperId);
        Integer upperLevel = upperStation.getLevel();
        Integer nextLevel = Station.LevelRelation.levelMap.get(upperLevel);
        station.setLevel(nextLevel);
        if (nextLevel.equals(Station.Level.LEVEL8.getValue())) {
            station.setGeneralTypeId(stationPojo.getGeneralTypeId());
        }

        stationMapper.insert(station);

        List<StationRelation> upperStationRelations = stationRelationMapper.findByCurStation(upperId);
        for(StationRelation upperStationRelation: upperStationRelations) {
            StationRelation stationRelation = new StationRelation();
            stationRelation.setCurStationId(station.getId());
            stationRelation.setUpperId(upperId);

            String upperIdSet = upperStationRelation.getUpperIdSet();
            if (upperIdSet == null) {
                upperIdSet = Long.toString(upperId);
            } else {
                upperIdSet = StrUtil.format("{},{}", upperIdSet, upperId);
            }
            stationRelation.setUpperIdSet(upperIdSet);
            stationRelationMapper.insert(stationRelation);
        }
    }

    @Override
    public void updateDevice(StationPojo stationPojo) {
        Long stationId = stationPojo.getId();
        Station curStation = stationMapper.findById(stationId);
        Integer level = curStation.getLevel();

        UpdateWrapper<Station> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(Station::getId, stationId);

        Date updatedTime = new Date();
        Station station = new Station();
        station.setUpdatedTime(updatedTime);
        station.setName(stationPojo.getName());
        station.setCapacity(stationPojo.getCapacity());
        station.setSmartTime(stationPojo.getSmartTime());
        if (level.equals(Station.Level.LEVEL8.getValue())) {
            station.setGeneralTypeId(stationPojo.getGeneralTypeId());
        }

        station.setPmsId(stationPojo.getPmsId());
        station.setUpdatedTime(updatedTime);
        stationMapper.update(station, wrapper);
    }

    @Override
    public void updateCountByStation(Long stationId, Integer count) {
        stationMapper.updateCountByStation(stationId, count);
    }

    @Override
    public void deleteById(Long id) {
        stationMapper.deleteById(id);
        stationRelationMapper.deleteByCurStation(id);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        for(Long id: ids) {
            stationMapper.deleteById(id);
            stationRelationMapper.deleteByCurStation(id);
        }
    }

    @Override
    public Station findById(Long id) {
        return stationMapper.selectById(id);
    }

    @Override
    public Long countStation() {
        return stationMapper.selectCount(null);
    }

    @Override
    public Long countByLevels(List<Integer> levels) {
        return stationMapper.countByLevels(levels);
    }

    @Override
    public Long countByRegion(Long regionId) {
        return stationMapper.countByRegion(regionId);
    }

    @Override
    public List<Long> findIdsByRegion(Long regionId) {
        return stationMapper.findIdsByRegion(regionId);
    }

    @Override
    public Station findByCode(String code) {
        return stationMapper.findByCode(code);
    }

    @Override
    public List<Station> findByLevel(Integer level) {
        return stationMapper.findByLevel(level);
    }

    @Override
    public List<BaseNode<Long, Integer>> findTreeByLevels(List<Integer> levels) {
        return stationMapper.findTreeByLevels(levels);
    }

    @Override
    public List<BaseNode<Long, Integer>> findTreeListByIdLevels(Long id, List<Integer> levels) {
        return stationMapper.findTreeListByIdLevels(id, levels);
    }

    @Override
    public List<BaseNode<Long, Integer>> findTreeNextListById(Long id, Integer isSelf) {
        try {
            return stationMapper.findTreeNextListById(id, isSelf);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public List<BaseNode<Long, Integer>> findTreeNextChildById(Long id) {
        return stationMapper.findTreeNextChildById(id);
    }
}
