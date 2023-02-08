package cn.iatc.web.service;

import cn.iatc.database.entity.TransformerInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface TransformerInfoService extends IService<TransformerInfo> {

    public void createBatch(List<TransformerInfo> transformerInfos);

    public Long countTrans();

    public TransformerInfo findById(Long id);

    public TransformerInfo findByStationId(Long stationId);

    public IPage<TransformerInfo> findListLike(String name, Integer pageIndex, Integer num);

    public String getEdrlByStationId(Long stationId);

    public List<TransformerInfo> getSimilarTransformer(String sbxh,String edrl,String zkdy,String ljzbh);
}
