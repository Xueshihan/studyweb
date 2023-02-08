package cn.iatc.web.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.iatc.database.entity.Region;
import cn.iatc.database.entity.Station;
import cn.iatc.web.bean.region.RegionPojo;
import cn.iatc.web.common.tree.data.BaseNode;
import cn.iatc.web.constants.CommonConstants;
import cn.iatc.web.bean.init.RegionTreeNode;
import cn.iatc.web.mapper.RegionMapper;
import cn.iatc.web.mapper.StationMapper;
import cn.iatc.web.service.RegionService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {
    @Autowired
    private RegionMapper regionMapper;

    @Autowired
    private StationMapper stationMapper;

    @Override
    public void createBatch(JSONArray jsonArray, Region upperRegion, Boolean isInit) {
        if (isInit) {
            UpdateWrapper<Region> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().set(Region::getEnabledStatus, CommonConstants.Status.DISABLED.getValue());
            this.update(updateWrapper);
        }

        for (Object jsonObject: jsonArray) {
            boolean createStatus = false;
            Date createdTime = new Date();
            String name = ((JSONObject)jsonObject).getString("name");
            String pinyin = ((JSONObject)jsonObject).getString("pinyin");
            String code = ((JSONObject)jsonObject).getString("code");
            Integer level = ((JSONObject)jsonObject).getInteger("level");
            Integer sort = ((JSONObject)jsonObject).getInteger("sort");
            String nationalCode = ((JSONObject)jsonObject).getString("national_code");

            QueryWrapper<Region> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Region::getCode, code);
            Region region = regionMapper.selectOne(queryWrapper);
            if (region == null) {
                createStatus = true;
                region = new Region();
                region.setCreatedTime(createdTime);
            }
            region.setName(name);
            region.setPinyin(pinyin);
            region.setCode(code);
            region.setLevel(level);
            region.setSort(sort);
            region.setNationalCode(nationalCode);
            region.setEnabledStatus(CommonConstants.Status.ENABLED.getValue());
            region.setUpdatedTime(createdTime);

            if(upperRegion != null) {
                Long upperId = upperRegion.getId();
                region.setUpperId(upperId);
                String upperIdSet = upperRegion.getUpperIdSet();
                if (upperIdSet == null) {
                    upperIdSet = Long.toString(upperId);
                } else {
                    upperIdSet = StrUtil.format("{},{}", upperIdSet, upperId);
                }
                region.setUpperIdSet(upperIdSet);
            } else {
                region.setUpperId(null);
                region.setUpperIdSet(null);
            }
            JSONArray children = ((JSONObject)jsonObject).getJSONArray("children");

            if (createStatus) {
                regionMapper.insert(region);
            } else {
                regionMapper.updateById(region);
            }

            if (children.size() > 0) {
                createBatch(children, region, false);
            }
        }
    }

    @Override
    public void createBatch(List<BaseNode<String, Integer>> regionList, Region upperRegion, Boolean isInit) {
        if (isInit) {
            UpdateWrapper<Region> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().set(Region::getEnabledStatus, CommonConstants.Status.DISABLED.getValue());
            this.update(updateWrapper);
        }
        this.createList(regionList, upperRegion);
    }

    public void createList(List<BaseNode<String, Integer>> regionList, Region upperRegion) {
        for (BaseNode<String, Integer> node: regionList) {
            boolean createStatus = false;
            Date createdTime = new Date();
            RegionTreeNode<String, Integer> regionNode = (RegionTreeNode<String, Integer>) node;
            String name = regionNode.getName();
            String pinyin = regionNode.getPinyin();
            String code = regionNode.getId();
            Integer level = regionNode.getLevel();
            Integer sort = regionNode.getSort();
            String nationalCode = regionNode.getNationalCode();

            QueryWrapper<Region> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Region::getCode, code);
            Region region = regionMapper.selectOne(queryWrapper);
            if (region == null) {
                createStatus = true;
                region = new Region();
                region.setCreatedTime(createdTime);
            }

            region.setName(name);
            region.setPinyin(pinyin);
            region.setCode(code);
            region.setLevel(level);
            region.setSort(sort);
            region.setNationalCode(nationalCode);
            region.setEnabledStatus(CommonConstants.Status.ENABLED.getValue());
            region.setUpdatedTime(createdTime);

            if(upperRegion != null) {
                Long upperId = upperRegion.getId();
                region.setUpperId(upperId);
                String upperIdSet = upperRegion.getUpperIdSet();
                if (upperIdSet == null) {
                    upperIdSet = Long.toString(upperId);
                } else {
                    upperIdSet = StrUtil.format("{},{}", upperIdSet, upperId);
                }
                region.setUpperIdSet(upperIdSet);
            } else {
                region.setUpperId(null);
                region.setUpperIdSet(null);
            }

            List<BaseNode<String, Integer>> children = regionNode.getChildren();

            if (createStatus) {
                regionMapper.insert(region);
            } else {
                regionMapper.updateById(region);
            }

            if (children.size() > 0) {
                createBatch(children, region, false);
            }
        }
    }

    @Override
    public Region createRegion(RegionPojo regionPojo) {
        String name = regionPojo.getName();
        String pinyin = regionPojo.getPinyin();
        String code = regionPojo.getCode();
        String adcode = regionPojo.getAdCode();
        Integer level = regionPojo.getLevel();
        Long upperId = regionPojo.getUpperId();
        String stationCode = regionPojo.getStationCode();
        String nationalCode = regionPojo.getNationalCode();
        Integer sort = regionPojo.getSort();

        Date createdTime = new Date();
        Region region = regionMapper.findByCode(code);
        boolean createStatus = false;
        if (region == null) {
            createStatus = true;
            region = new Region();
            region.setCode(code);
            region.setCreatedTime(createdTime);
        }
        region.setName(name);
        region.setPinyin(pinyin);
        region.setAdCode(adcode);
        region.setLevel(level);
        region.setUpperId(upperId);
        Region upperRegion = regionMapper.findById(upperId);
        if(upperRegion != null) {
            String upperIdSet = upperRegion.getUpperIdSet();
            if (StrUtil.isBlank(upperIdSet)) {
                upperIdSet = Long.toString(upperId);
            } else {
                upperIdSet = StrUtil.format("{},{}", upperIdSet, upperId);
            }
            region.setUpperIdSet(upperIdSet);
        }
        region.setNationalCode(nationalCode);
        region.setSort(sort);
        region.setEnabledStatus(CommonConstants.Status.ENABLED.getValue());
        region.setUpdatedTime(createdTime);
        if (createStatus) {
            regionMapper.insert(region);
        } else {
            regionMapper.updateById(region);
        }

        Station station = stationMapper.findByCode(stationCode);
        if (station != null) {
            station.setRegionId(region.getId());
            station.setUpdatedTime(createdTime);
            stationMapper.updateById(station);
        }

        return region;
    }

    @Override
    public void deleteById(Long id) {
        QueryWrapper<Region> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Region::getId, id);
        Region region = new Region();
        region.setEnabledStatus(CommonConstants.Status.DISABLED.getValue());
        regionMapper.update(region, wrapper);

        UpdateWrapper<Station> stationUpdateWrapper = new UpdateWrapper<>();
        stationUpdateWrapper.lambda().set(Station::getRegionId, null);
        stationUpdateWrapper.lambda().eq(Station::getRegionId, id);
        stationMapper.update(new Station(), stationUpdateWrapper);

    }

    @Override
    public void deleteBatchByIds(List<Long> ids) {
        QueryWrapper<Region> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(Region::getId, ids);
        Region region = new Region();
        region.setEnabledStatus(CommonConstants.Status.DISABLED.getValue());
        regionMapper.update(region, wrapper);

        UpdateWrapper<Station> stationUpdateWrapper = new UpdateWrapper<>();
        stationUpdateWrapper.lambda().set(Station::getRegionId, null);
        stationUpdateWrapper.lambda().in(Station::getRegionId, ids);
        stationMapper.update(new Station(), stationUpdateWrapper);
    }

    @Override
    public Long countRegion() {
        return regionMapper.countAll();
    }

    @Override
    public Long countNextRegionByCode(String code) {
        return regionMapper.countNextRegionByCode(code);
    }

    @Override
    public Region findById(Long id) {
        return regionMapper.selectById(id);
    }

    @Override
    public List<Region> findListByIds(List<Long> ids) {
        return regionMapper.findListByIds(ids);
    }

    @Override
    public IPage<RegionPojo> findListLike(Integer level, String name, Integer pageIndex, Integer num) {
        IPage<RegionPojo> page = new Page<>(pageIndex, num);
        return regionMapper.findListLike(page, level, name);
    }
}
