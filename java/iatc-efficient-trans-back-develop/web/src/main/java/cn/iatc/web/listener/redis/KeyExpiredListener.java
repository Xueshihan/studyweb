package cn.iatc.web.listener.redis;

import cn.hutool.core.util.StrUtil;
import cn.iatc.redis.RedisHelper;
import cn.iatc.web.constants.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

//创建监听类 key失效监听类
@Slf4j
@Component
public class KeyExpiredListener extends KeyExpirationEventMessageListener {

    @Autowired
    private RedisHelper redisHelper;

    public KeyExpiredListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    /**
     * 使用该方法监听，当Redis的key失效的时候执行该方法
     * @param message message must not be {@literal null}.
     * @param pattern pattern matching the channel (if specified) - can be {@literal null}.
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 过期的Key
        String expireKey = message.toString();
        log.info("########该Key已失效:{}", expireKey);
        String lockTimeBaseKey = StrUtil.format("{}{}", RedisConstant.KEY_BASE, RedisConstant.KEY_LOGIN_LOCK_TIME);
        if (expireKey.startsWith(lockTimeBaseKey)) {
            String[] expireKeyArray = expireKey.split("/");
            String lastStr = expireKeyArray[expireKeyArray.length - 1];
            String errNumKey = StrUtil.format("{}{}/{}", RedisConstant.KEY_BASE, RedisConstant.KEY_LOGIN_ERROR_NUM, lastStr);
            log.info("lock time out errNumKey:{}", errNumKey);
            redisHelper.delKey(errNumKey);
        }
    }

}