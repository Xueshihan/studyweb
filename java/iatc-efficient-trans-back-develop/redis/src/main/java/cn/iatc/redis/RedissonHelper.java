package cn.iatc.redis;

import org.redisson.api.*;
import org.redisson.client.codec.StringCodec;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class RedissonHelper {

    // RedissonClient已经由配置类生成，这里自动装配即可
    private RedissonClient redissonClient;
    public RedissonHelper(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 锁住不设置超时时间(拿不到lock就不罢休，不然线程就一直block)
     * @param lockKey
     * @return org.redisson.api.RLock
     */
    public RLock lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    /**
     * leaseTime为加锁时间，单位为秒
     * @param lockKey
     * @param leaseTime
     * @return org.redisson.api.RLock
     */
    public RLock lock(String lockKey, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, TimeUnit.SECONDS);
        return null;
    }

    /**
     * timeout为加锁时间，时间单位由unit确定
     * @param lockKey
     * @param unit
     * @param timeout
     * @return org.redisson.api.RLock
     */
    public RLock lock(String lockKey, TimeUnit unit, long timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
        return lock;
    }

    /**
     * 尝试获取锁
     * @param lockKey
     * @param unit
     * @param waitTime
     * @param leaseTime
     * @return boolean
     */
    public boolean tryLock(String lockKey, TimeUnit unit, long waitTime, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }


    /**
     * 尝试获取锁
     * @param lockKey
     * @param unit
     * @param waitTime
     * @return boolean
     */
    public boolean tryLock(String lockKey, TimeUnit unit, long waitTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime,unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 通过lockKey解锁
     * @param lockKey
     * @return void
     */
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }

    /**
     * 直接通过锁解锁
     * @param lock
     * @return void
     */
    public void unlock(RLock lock) {
        lock.unlock();
    }

    // 存储/删除普通对象操作
    /**
     * 存入普通对象
     *
     * @param key Redis键
     * @param value 值
     */
    public void setValue(final String key, final Object value) {
        RBucket<Object> keyObject = redissonClient.getBucket(key, new StringCodec("utf-8"));
        keyObject.set(value);
    }

    /**
     * 存入普通对象
     *
     * @param key 键
     * @param value 值
     * @param timeout 有效期，单位秒
     */
    public void setValueTimeout(final String key, final Object value, final long timeout) {
        RBucket<Object> keyObject = redissonClient.getBucket(key, new StringCodec("utf-8"));
        keyObject.set(value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 获取普通对象
     *
     * @param key 键
     * @return 对象
     */
    public Object getValue(final String key) {
        RBucket<Object> keyObject = redissonClient.getBucket(key, new StringCodec("utf-8"));
        return keyObject.get();
    }

    /**
     * 删除单个key
     *
     * @param key 键
     * @return true=删除成功；false=删除失败
     */
    public boolean delKey(final String key) {
        RBucket<Object> keyObject = redissonClient.getBucket(key, new StringCodec("utf-8"));
        return keyObject.delete();
    }

    // 存储Hash操作
    /**
     * 往Hash中存入数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @param value 值
     */
    public void hashPut(final String key, final String hKey, final Object value) {
        RMap<String, Object> keyObject = redissonClient.getMap(key, new StringCodec("utf-8"));
        keyObject.put(hKey, value);
    }

    /**
     * 往Hash中存入多个数据
     *
     * @param key Redis键
     * @param values Hash键值对
     */
    public void hashPutAll(final String key, final Map<String, Object> values) {
        RMap<String, Object> keyObject = redissonClient.getMap(key, new StringCodec("utf-8"));
        keyObject.putAll(values);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public Object hashGet(final String key, final String hKey) {
        RMap<String, Object> keyObject = redissonClient.getMap(key, new StringCodec("utf-8"));
        return keyObject.get(hKey);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key Redis键
     * @return Hash对象
     */
    public Map<Object, Object> hashGetAll(final String key) {
        RMap<Object, Object> keyObject = redissonClient.getMap(key, new StringCodec("utf-8"));
        return keyObject;
    }

    // 存储Set相关操作
    /**
     * 往Set中存入数据
     *
     * @param key Redis键
     * @param values 值
     * @return 存入的个数
     */
    public boolean setSet(final String key, final Object... values) {
        RSet<Object> keyObject = redissonClient.getSet(key, new StringCodec("utf-8"));
        return keyObject.addAll(Arrays.asList(values));
    }

    /**
     * 删除Set中的数据
     *
     * @param key Redis键
     * @param values 值
     * @return 移除的个数
     */
    public boolean setDel(final String key, final Object... values) {
        RSet<Object> keyObject = redissonClient.getSet(key, new StringCodec("utf-8"));
        return keyObject.removeAll(Arrays.asList(values));
    }

    /**
     * 获取set中的所有对象
     *
     * @param key Redis键
     * @return set集合
     */
    public Set<Object> getSetAll(final String key) {
        RSet<Object> keyObject = redissonClient.getSet(key, new StringCodec("utf-8"));
        return keyObject;
    }

    // 存储ZSet相关操作
    /**
     * 往ZSet中存入数据
     *
     * @param key Redis键
     * @param values 值
     * @return 存入的个数
     */
    public boolean zsetset(final String key, final Set<ZSetOperations.TypedTuple<Object>> values) {
        RSortedSet<ZSetOperations.TypedTuple<Object>> keyObject = redissonClient.getSortedSet(key, new StringCodec("utf-8"));
        return keyObject.addAll(values);
    }

    /**
     * 删除ZSet中的数据
     *
     * @param key Redis键
     * @param values 值
     * @return 移除的个数
     */
    public boolean zsetDel(final String key, final Set<ZSetOperations.TypedTuple<Object>> values) {
        RSortedSet<ZSetOperations.TypedTuple<Object>> keyObject = redissonClient.getSortedSet(key, new StringCodec("utf-8"));
        return keyObject.removeAll(values);
    }

    // 存储List相关操作

    /**
     * 往List中存入数据
     *
     * @param key Redis键
     * @param value 数据
     * @return 存入的个数
     */
    public boolean listPush(final String key, final Object value) {
        RList<Object> keyObject = redissonClient.getList(key, new StringCodec("utf-8"));
        return keyObject.add(value);
    }

    /**
     * 往List中存入多个数据
     *
     * @param key Redis键
     * @param values 多个数据
     * @return 存入的个数
     */
    public boolean listPushAll(final String key, final Collection<Object> values) {
        RList<Object> keyObject = redissonClient.getList(key, new StringCodec("utf-8"));
        return keyObject.addAll(values);
    }

    /**
     * 从List中获取begin到end之间的元素
     *
     * @param key Redis键
     * @param start 开始位置
     * @param end 结束位置（start=0，end=-1表示获取全部元素）
     * @return List对象
     */
    public List<Object> listGet(final String key, final int start, final int end) {
        RList<Object> keyObject = redissonClient.getList(key, new StringCodec("utf-8"));
        return keyObject.range(start, end);
    }
}
