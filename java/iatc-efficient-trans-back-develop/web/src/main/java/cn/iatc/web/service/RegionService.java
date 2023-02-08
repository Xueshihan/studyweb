package cn.iatc.web.service;

import cn.iatc.database.entity.Region;
import cn.iatc.web.bean.region.RegionPojo;
import cn.iatc.web.common.tree.data.BaseNode;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface RegionService extends IService<Region> {

    public void createBatch(JSONArray jsonArray, Region upperRegion, Boolean isInit);

    public void createBatch(List<BaseNode<String, Integer>> regionList, Region upperRegion, Boolean isInit);

    public Region createRegion(RegionPojo regionPojo);

    public void deleteById(Long id);

    public void deleteBatchByIds(List<Long> ids);

    public Long countRegion();

    public Long countNextRegionByCode(String code);

    public Region findById(Long id);

    public List<Region> findListByIds(List<Long> ids);

    public IPage<RegionPojo> findListLike(Integer level, String name, Integer pageIndex, Integer num);
}
