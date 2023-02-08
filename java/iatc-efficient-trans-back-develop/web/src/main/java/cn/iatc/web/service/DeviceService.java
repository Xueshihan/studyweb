package cn.iatc.web.service;

import cn.iatc.database.entity.Device;
import cn.iatc.web.bean.device.DevicePojo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface DeviceService extends IService<Device> {

    public Device createDevice(Long stationId, Device device);

    public void updateDevice(Device device);

    public void deleteByIds(List<Long> id);

    public Device findById(Long id);
    public Device findBySn(String sn);

    public IPage<DevicePojo> findListLike(String name, Long baseDeviceId, Integer pageIndex, Integer num);

    public List<DevicePojo> findListByStation(Long stationId);
}
