package cn.iatc.web.service.impl;

import cn.iatc.database.entity.TransformerInfo;
import cn.iatc.web.mapper.TransformerInfoMapper;
import cn.iatc.web.service.TransformerInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class TransformerInfoServiceImpl extends ServiceImpl<TransformerInfoMapper, TransformerInfo> implements TransformerInfoService {

    @Autowired
    private TransformerInfoMapper transformerInfoMapper;

    @Override
    public void createBatch(List<TransformerInfo> transformerInfos) {
        for (TransformerInfo transformerInfo: transformerInfos) {
            transformerInfoMapper.insert(transformerInfo);
        }
    }

    @Override
    public Long countTrans() {
        return transformerInfoMapper.selectCount(null);
    }

    @Override
    public TransformerInfo findById(Long id) {
        return transformerInfoMapper.findById(id);
    }

    @Override
    public TransformerInfo findByStationId(Long stationId) { return transformerInfoMapper.findByStationId(stationId);}

    @Override
    public IPage<TransformerInfo> findListLike(String name, Integer pageIndex, Integer num) {
        return transformerInfoMapper.findListLike(name, pageIndex, num);
    }

    @Override
    public String getEdrlByStationId(Long stationId) { return transformerInfoMapper.getEdrlByStationId(stationId);}

    @Override
    public List<TransformerInfo> getSimilarTransformer(String sbxh, String edrl, String zkdy, String ljzbh) {
        return transformerInfoMapper.getSimilarTransformer(sbxh, edrl, zkdy, ljzbh);
    }
}
