package cn.iatc.web.mapper;

import cn.iatc.database.entity.Region;
import cn.iatc.web.bean.region.RegionPojo;
import cn.iatc.web.constants.CommonConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RegionMapper extends BaseMapper<Region> {

    public Region findById(Long id);

    public IPage<RegionPojo> findListLike(IPage<RegionPojo> page, Integer level, String name);

    public default Region findByCode(String code) {
        LambdaQueryWrapper<Region> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Region::getCode, code);
        wrapper.eq(Region::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectOne(wrapper);
    }

    public default Long countAll() {
        LambdaQueryWrapper<Region> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Region::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectCount(wrapper);
    }

    public default Long countNextRegionByCode(String code) {
        LambdaQueryWrapper<Region> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(Region::getCode, code);
        wrapper.likeRight(Region::getCode, code);
        return this.selectCount(wrapper);
    }

    public default List<Region> findListByIds(List<Long> ids) {
        LambdaQueryWrapper<Region> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Region::getId, ids);
        return this.selectList(wrapper);
    }
}
