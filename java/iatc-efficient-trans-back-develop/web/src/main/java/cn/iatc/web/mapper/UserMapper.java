package cn.iatc.web.mapper;

import cn.hutool.core.util.StrUtil;
import cn.iatc.database.entity.User;
import cn.iatc.web.constants.CommonConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserMapper extends BaseMapper<User> {

    public User findById(Long id);

    public default User findUserByAccount(String account) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getAccount, account);
        wrapper.eq(User::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectOne(wrapper);
    }

    public default List<User> findByRole(Long roleId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getRoleId, roleId);
        wrapper.eq(User::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectList(wrapper);
    }

    public default IPage<User> findListLike(Long stationId, String account, String realName, Long roleId, Integer pageIndex, Integer num) {
        Page<User> page = new Page<>(pageIndex, num);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (stationId != null) {
            wrapper.eq(User::getStationId, stationId);
        }
        if (StrUtil.isNotBlank(account)) {
            wrapper.eq(User::getAccount, account);
        }

        if(StrUtil.isNotBlank(realName)) {
            wrapper.eq(User::getRealName, realName);
        }

        if (roleId != null) {
            wrapper.eq(User::getRoleId, roleId);
        }
        wrapper.eq(User::getEnabledStatus, CommonConstants.Status.ENABLED.getValue());
        return this.selectPage(page, wrapper);
    }
}
