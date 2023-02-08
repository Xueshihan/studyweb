package cn.iatc.web.service;

import cn.iatc.database.entity.User;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserService extends IService<User> {

    public void createUser(User user);
    public void updateToUser(User user);

    public void updatePassword(Long userId, String password);

    public void deleteUser(Long userId);

    public void deleteBatchByIds(List<Long> ids);

    public User findUserByAccount(String account);

    public User findById(Long id);

    public List<User> findByRole(Long roleId);

    public IPage<User> findListLike(Long stationId, String name, String realName, Long roleId, Integer page, Integer num);
}
