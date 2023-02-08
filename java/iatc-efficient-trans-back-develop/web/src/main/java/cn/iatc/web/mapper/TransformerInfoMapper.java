package cn.iatc.web.mapper;

import cn.hutool.core.util.StrUtil;
import cn.iatc.database.entity.TransformerInfo;
import cn.iatc.web.constants.CommonConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;

@Component
public interface TransformerInfoMapper extends BaseMapper<TransformerInfo> {

    public default void deleteById(Long id) {
        LambdaUpdateWrapper<TransformerInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(TransformerInfo::getEnabledStatus, CommonConstants.Status.DISABLED.getValue());
        wrapper.set(TransformerInfo::getUpdatedTime, new Date());
        wrapper.eq(TransformerInfo::getId, id);
        this.update(null, wrapper);
    }

    public TransformerInfo findById(Long id);

    public TransformerInfo findByStationId(Long stationId);

    public default IPage<TransformerInfo> findListLike(String name, Integer pageIndex, Integer num) {
        Page<TransformerInfo> page = new Page<>(pageIndex, num);
        LambdaQueryWrapper<TransformerInfo> wrapper = new LambdaQueryWrapper<>();
        if (name != null && StrUtil.isNotBlank(name)) {
            wrapper.like(TransformerInfo::getSbmc, "%"+name+"%");
        }
        wrapper.eq(TransformerInfo::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectPage(page, wrapper);
    }
    public String getEdrlByStationId(Long stationId);

    public default List<TransformerInfo> getSimilarTransformer(String sbxh, String edrl, String zkdy, String ljzbh) {
        LambdaQueryWrapper<TransformerInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(TransformerInfo::getLx, "%"+sbxh+"%");
        wrapper.ge(TransformerInfo::getEdrl, edrl);
        wrapper.ge(TransformerInfo::getZkdy, zkdy);
        wrapper.ge(TransformerInfo::getLjzbh, ljzbh);
        wrapper.eq(TransformerInfo::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        wrapper.orderByAsc(TransformerInfo::getStationId);
        return this.selectList(wrapper);
    }

}
