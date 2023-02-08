package cn.iatc.web.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.iatc.database.entity.User;
import cn.iatc.web.constants.CommonConstants;
import cn.iatc.web.mapper.UserMapper;
import cn.iatc.web.service.UserService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void createUser(User user) {
        userMapper.insert(user);
    }

    @Override
    public void updateToUser(User user) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        if (StrUtil.isNotBlank(user.getAccount())) {
            wrapper.set(User::getAccount, user.getAccount());
        }
        if (StrUtil.isNotBlank(user.getRealName())) {
            wrapper.set(User::getRealName, user.getRealName());
        }
        if (user.getStationId() != null) {
            wrapper.set(User::getStationId, user.getStationId());
        }
        if (user.getRoleId() != null) {
            wrapper.set(User::getRoleId, user.getRoleId());
        }
        wrapper.set(User::getPhone, user.getPhone());
        wrapper.set(User::getEmail, user.getEmail());
        wrapper.eq(User::getId, user.getId());
        userMapper.update(null, wrapper);
    }

    @Override
    public void updatePassword(Long userId, String password) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(User::getPassword, password);
        wrapper.set(User::getUpdatedTime, new Date());
        userMapper.update(null, wrapper);
    }

    @Override
    public void deleteUser(Long userId) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(User::getEnabledStatus, CommonConstants.Status.DISABLED.getValue());
        wrapper.set(User::getUpdatedTime, new Date());
        wrapper.eq(User::getId, userId);
        userMapper.update(null, wrapper);
    }

    @Override
    public void deleteBatchByIds(List<Long> ids) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(User::getEnabledStatus, CommonConstants.Status.DISABLED.getValue());
        wrapper.set(User::getUpdatedTime, new Date());
        wrapper.in(User::getId, ids);
        userMapper.update(null, wrapper);
    }

    @Override
    public User findUserByAccount(String account) {
        return userMapper.findUserByAccount(account);
    }

    @Override
    public User findById(Long id) {
        return userMapper.findById(id);
    }

    @Override
    public List<User> findByRole(Long roleId) {
        return userMapper.findByRole(roleId);
    }

    @Override
    public IPage<User> findListLike(Long stationId, String account, String realName, Long roleId, Integer pageIndex, Integer num) {
        return userMapper.findListLike(stationId, account, realName, roleId, pageIndex, num);
    }
}
