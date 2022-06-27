package net.bncloud.saas.utils.redis;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.bncloud.common.util.BeanUtilTwo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Redis相关常用方法
 * @author dr
 */
@Component
public class RedisUtils {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    //@Resource
    //private JedisPool jedisPool;

    /**
     * 设置数据到redis中
     *
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        // 更改在redis里面查看key编码问题
        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        vo.set(key, value);
    }

    /**
     * 设置数据到redis中
     *
     * TimeUnit.DAYS
     * @param key
     * @param value
     */
    public void set(String key, Object value, Long time, TimeUnit timeUnit) {
        // 更改在redis里面查看key编码问题
        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        vo.set(key, value, time, timeUnit);
    }

    /**
     * 判断是否有值
     *
     * @param key    key
     * @param val    val
     * @param expire 过期时间,单位:秒
     * @return
     */
    public boolean setnx(String key, Object val, long expire) {
        return redisTemplate.opsForValue().setIfAbsent(key, val, expire, TimeUnit.SECONDS);
    }

    /**
     * 从redis中获取数据
     *
     * @param key
     */
    public Object get(String key) {
        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        return vo.get(key);
    }

    /**
     * 从redis中获取数据（带对应泛型）
     *
     * @param key
     * @return Object
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> cls) {
        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        return (T) vo.get(key);
    }

    /**
     * 从redis中获取数据（List）
     *
     * @param key
     */
    //@SuppressWarnings("unchecked")
    public <T> List<T> getList(String key, Class<T> cls) {
        return getStringToList(key, cls);
    }
    /**
     * 从redis中获取数据
     *
     * @param key
     */
    public <T> List<T> getStringToList(String key, Class<T> cls) {
        String data = getString(key);
        return JSONArray.parseArray(data, cls);
    }

    /**
     * 从redis中获取数据（判断非空）
     *
     * @param key
     */
    public <T> T getStringToObject(String key, Class<T> cls) {
        String data = getString(key);
        if (StringUtils.isEmpty(data)) {
            return null;
        }
        return JSONObject.parseObject(data, cls);
    }

    /**
     * 从redis中获取Integer类型的数据
     *
     * @param key
     * @return Integer
     */
    public Integer getInteger(String key) {
        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        Object value = vo.get(key);
        if (value == null) {
            return null;
        }
        return Integer.parseInt(value.toString());
    }

    /**
     * 从redis中获取String数据
     *
     * @param key
     * @return String
     */
    public String getString(String key) {
        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        Object value = vo.get(key);
        if (value == null) {
            return "";
        }
        return value.toString();
    }

    /**
     * 从redis中获取Boolean类型数据
     *
     * @param key
     * @return Boolean
     */
    public Boolean getBoolean(String key) {
        ValueOperations<String, Object> vo = redisTemplate.opsForValue();
        Object value = vo.get(key);
        if (value == null) {
            return null;
        }
        return "true".equals(value.toString()) || "1".equals(value.toString());
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            logger.error("{}", e);
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            logger.error("{}", e);
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        final List<String> keys = Stream.of(key).collect(Collectors.toList());
        redisTemplate.delete(keys);
    }
    public boolean del(String key) {
        return redisTemplate.delete(key);
    }


    //============================String=============================

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            logger.error("{}", e);
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    public long incr(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }


    //================================hash=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            logger.error("{}", e);
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            logger.error("{}", e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            logger.error("{}", e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建 hsetJson,针对其他业务使用到的key处理
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hsetJson(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            logger.error("{}", e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            logger.error("{}", e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }


    //============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            logger.error("{}", e);
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            logger.error("{}", e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            logger.error("{}", e);
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            logger.error("{}", e);
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            logger.error("{}", e);
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            logger.error("{}", e);
            return 0;
        }
    }

    /**
     * 获取交集
     *
     * @param key1
     * @param Key2
     * @return java.util.Set<java.lang.Object>
     * @date 2020/5/25
     */
    public Set<Object> sIntersect(String key1, String Key2) {
        try {
            return redisTemplate.opsForSet().intersect(key1, Key2);
        } catch (Exception e) {
            logger.error("{}", e);
            return null;
        }
    }

    /**
     * key的元素值移到destKey
     *
     * @param key
     * @param value
     * @param destKey
     * @return boolean
     * @date 2020/5/25
     */
    public boolean sMove(String key, Object value, String destKey) {
        try {
            return redisTemplate.opsForSet().move(key, value, destKey);
        } catch (Exception e) {
            logger.error("{}", e);
            return false;
        }
    }

    /**
     * 随机获取指定个数的元素
     *
     * @param key
     * @param count
     * @return java.util.List<java.lang.Object>
     * @date 2020/5/26
     */
    public List<Object> randomMembers(String key, Integer count) {
        if (count == null || count < 1) {
            return new ArrayList<>();
        }
        try {
            return redisTemplate.opsForSet().randomMembers(key, count);
        } catch (Exception e) {
            logger.error("{}", e);
            return null;
        }
    }


    //===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            logger.error("{}", e);
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            logger.error("{}", e);
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            logger.error("{}", e);
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lPush(String key, Object value) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            return true;
        } catch (Exception e) {
            logger.error("{}", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lPush(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            logger.error("{}", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lPush(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().leftPushAll(key, value);
            return true;
        } catch (Exception e) {
            logger.error("{}", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lPushList(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().leftPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            logger.error("{}", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean rPush(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            logger.error("{}", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean rPush(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            logger.error("{}", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean rPush(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            logger.error("{}", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean rPush(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            logger.error("{}", e);
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            logger.error("{}", e);
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            logger.error("{}", e);
            return 0;
        }
    }

    /**
     * 移除某个key
     *
     * @param key 键
     */
    public boolean delete(String key) {
        try {
            return redisTemplate.delete(key);
        } catch (Exception e) {
            logger.error("{}", e);
            return false;
        }
    }

    /**
     * 从右边移除元素
     *
     * @param key
     * @return
     */
    public Object rPop(String key) {
        try {
            Object o = redisTemplate.opsForList().rightPop(key);
            return o;
        } catch (Exception e) {
            logger.error("{}", e);
            return null;
        }
    }

    /**
     * 获取锁,支持redis单机,在集群中使用有可能导致有多把锁被获取
     *
     * @param key           锁
     * @param value         值
     * @param expire        过期时间,单位:秒
     * @param sleepInterval 抢锁间隔时间,单位:毫秒
     * @param tryTimes      抢锁次数
     * @return
     */
    public boolean lock(String key, String value, long expire, long sleepInterval, int tryTimes) {
        int times = 0;
        while (times < tryTimes) {
            if (this.setnx(key, value, expire)) {
                return true;
            }
            //500毫秒抢一次
            try {
                Thread.sleep(sleepInterval);
                times++;
            } catch (InterruptedException e) {
                logger.error("{}", e);
                return false;
            }
        }
        return false;
    }

    /**
     * 释放锁
     *
     * @param key 锁
     * @param val 对应的值
     * @return
     */
    public boolean unlock(String key, String val) {
        Object o = this.get(key);
        if (o == null) {
            return true;
        }
        //判断锁是否属于当前线程的
        if (val.equals(o)) {
            return del(key);
        }
        return false;
    }

    /**
     * scan 实现
     *
     * @param pattern  表达式
     * @param consumer 对迭代到的key进行操作
     */
    public void scan(String pattern, Consumer<byte[]> consumer) {
        this.stringRedisTemplate.execute((RedisConnection connection) -> {
            try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().count(Long.MAX_VALUE).match(pattern).build())) {
                cursor.forEachRemaining(consumer);
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 模糊查找redis中的key
     *
     * @param keyPattern
     * @return
     */
    public Set<String> getKeys(String keyPattern) {
        Set<String> keys = new HashSet<>();
        this.scan(keyPattern, item -> {
            //符合条件的key
            String key = new String(item, StandardCharsets.UTF_8);
            keys.add(key);
        });
        return keys;
    }

    /**
     * 加分布式锁-请求线程粒度（简易版）
     * @param key 锁标识
     * @param val 锁值-线程ID
     * @param time 过期时间(单位：秒)
     * @param isLengthen 是否延长持有锁时间
     */
    public boolean addRedisLock(String key, String val, int time, boolean isLengthen){
        //Jedis版
//        Jedis jedis = null;
//        try {
//            jedis =jedisPool.getResource();
//            //尝试加锁
//            Long res = jedis.setnx(key, val);
//            if(res==1){//加锁成功
//                jedis.setex(key,time,val);//设置过期时间
//                return true;
//            }else{//加锁失败，判断锁被谁占有
//                String nowVal = jedis.get(key);
//                if(BeanUtil.isNotEmpty(nowVal)){
//                    if(val.equals(nowVal)){//原有锁为当前线程加的
//                        if(isLengthen){//延长占有锁的时间
//                            jedis.setex(key,time,val);
//                        }
//                        return true;
//                    }else{//原有锁不是当前线程加的
//                        return false;
//                    }
//                }else{//加锁失败后，原有锁又恰好过期
//                    return false;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;

        //redisTemplate版
        boolean execute = this.stringRedisTemplate.execute((RedisConnection connection) -> {
            try {
                //尝试加锁
                boolean aBoolean = connection.setNX(key.getBytes(), val.getBytes());
                if (aBoolean) {//加锁成功
                    connection.setEx(key.getBytes(), time, val.getBytes());//设置过期时间
                    return true;
                } else {//加锁失败，判断锁被谁占有
                    String nowVal = this.stringRedisTemplate.opsForValue().get(key);
                    if (BeanUtilTwo.isNotEmpty(nowVal)) {
                        if (val.equals(nowVal)) {//原有锁为当前线程加的
                            if (isLengthen) {//延长占有锁的时间
                                connection.setEx(key.getBytes(), time, val.getBytes());
                            }
                            return true;
                        } else {//原有锁不是当前线程加的
                            return false;
                        }
                    } else {//加锁失败后，原有锁又恰好过期
                        return false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        });

        return execute;
    }

    //手动释放锁
    public void delRedisLock(String key, String val) {
        String nowVal = this.stringRedisTemplate.opsForValue().get(key);
        if (BeanUtilTwo.isNotEmpty(nowVal)) {
            if (val.equals(nowVal)) {//原有锁为当前线程加的
                this.stringRedisTemplate.delete(key);
            }
        }
    }

    /**
     * 设置数据到redis中
     *
     * @param key
     * @param value
     */
    public void strSet(String key, String value) {
        // 更改在redis里面查看key编码问题
        ValueOperations<String, String> vo = this.stringRedisTemplate.opsForValue();
        vo.set(key, value);
    }

    /**
     * 设置数据到redis中-带时间
     *
     * @param key
     * @param value
     */
    public void strSetHasTime(String key, String value, long time, TimeUnit timeUnit) {
        // 更改在redis里面查看key编码问题
        this.stringRedisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    /**
     * 从redis中获取String数据
     *
     * @param key
     * @return String
     */
    public String strGetString(String key) {
        ValueOperations<String, String> vo = this.stringRedisTemplate.opsForValue();
        String s = vo.get(key);
        if (s == null) {
            return "";
        }
        return s;
    }
}
