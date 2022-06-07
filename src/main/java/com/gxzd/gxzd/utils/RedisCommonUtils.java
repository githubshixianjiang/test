package com.gxzd.gxzd.utils;

import com.gxzd.gxzd.enums.RedisCommonEnum;
import com.gxzd.gxzd.exception.ExceptionUtils;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author coderFu
 * @create 2021/10/17
 */
@ApiModel("redis常用工具类")
@Component
@Slf4j
public class RedisCommonUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    //================common=================

    /**
     * 指定缓存的失效时间
     *
     * @param key
     * @param time
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=====获取键值的失效时间失败=====");
            return false;
        }
    }

    /**
     * 获取key的缓存失效时间
     *
     * @param key key不能为null
     * @return 0 代表永久有效
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key key不能为空
     * @return true 存在 false 不存在
     */
    public Boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=====判断key值是否存在失败=====");
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key key可以是一个值也可是一个数组
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }


    /**
     * 普通键值存入
     *
     * @param key   键
     * @param value 值
     * @return true 成功 false 失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=====设置String类型的key和值失败=====");
            return false;
        }
    }

    /**
     * 普通缓存放入并设置过期时间
     *
     * @param key   键
     * @param value 值
     * @param time  过期时间 单位是s 如果time<0 则表示 无限期
     * @return true 成功 false 失败
     */
    public boolean set(String key, Object value, long time, TimeUnit unit) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, unit);
            } else {
                redisTemplate.opsForValue().set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.info("=====设置String类型的key和值失败=====");
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 递增
     *
     * @param key   键
     * @param delta 每次增加几 （大于0）
     * @return 递增后的结果
     */
    public Long incr(String key, long delta) {
        if (delta < 0) ExceptionUtils.error(RedisCommonEnum.INCREASE_FACTOR_NOT_VALID);
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 每次减小几 （大于0）
     * @return 递增后的结果
     */
    public Long decr(String key, long delta) {
        if (delta < 0) {
            ExceptionUtils.error(RedisCommonEnum.INCREASE_FACTOR_NOT_VALID);
        }
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    //=====================hash================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return
     */
    public Object hget(String key, Object item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey 对应所有的键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        try {
            return redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=====设置hash类型的key和值失败=====");
            return false;
        }
    }

    /**
     * HashSet设置过期时间
     *
     * @param key  键
     * @param map  对应的多个键值
     * @param time 过期时间s
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=====设置多个hash类型过期时间的key和值失败=====");
            return false;
        }
    }

    /**
     * 同一张hash表中放入数据，如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false 失败
     */
    public boolean hset(String key, Object item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=====设置hash类型的key和值失败=====");
            return false;
        }
    }

    /**
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  过期时间 单位s
     * @return true 成功 false 失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=====设置hash类型过期时间的key和值失败=====");
            return false;
        }
    }

    public void hdel(String outKey, String innerKey) {
        if (StringUtils.isNotBlank(outKey) && StringUtils.isNotBlank(innerKey)) {
            try {
                redisTemplate.opsForHash().delete(outKey, innerKey);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("=====删除hash类型key和值失败=====");
            }
        }

    }

    /**
     * hash递增
     *
     * @param key  键
     * @param item 项
     * @param by   每次增加的值
     * @return 增加之后的结果
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   每次减少的值
     * @return 减少之后的结果
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    //=======================set======================

    /**
     * 根据key获取set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=====获取set类型的值失败=====");
            return null;
        }
    }

    /**
     * 从set集合中随机移除并获取一个元素
     *
     * @param key
     * @return
     */
    public Object sGetOneValueRandom(String key) {
        try {
            return redisTemplate.opsForSet().pop(key);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=====获取set类型的值");
            return null;
        }
    }

    /**
     * 根据value 从一个set中查询是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 成功 false 失败
     */
    public Boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=====判断set类型的值是否存在失败=====");
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 返回成功个数
     */
    public Long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=====添加set类型的键值失败=====");
            return 0L;
        }
    }

    /**
     * 将数据放入set缓存中并指定过期时间
     *
     * @param key    键
     * @param time   过期时间
     * @param values 值 可以是多个
     * @return 返回成功个数
     */
    public Long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=====添加set类型过期的键值失败=====");
            return 0L;
        }
    }

    /**
     * 获取set集合中的元素个数
     *
     * @param key 键
     * @return 返回元素个数
     */
    public Long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=====获取set类型的键个数失败=====");
            return 0L;
        }
    }

    /**
     * 移除set中值为value的
     *
     * @param key    键
     * @param values 值
     * @return 移除的个数
     */
    public Long setRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=====移除set类型的键失败=====");
            return 0L;
        }
    }

    //=======================list==========

    /**
     * 获取list缓存中制定区间的内容
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置  从0到-1代表所有值
     * @return list缓存中的内容
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=====获取list类型指定区间的值失败=====");
            return null;
        }
    }

    /**
     * 获取list缓存中的长度
     *
     * @param key 键
     * @return 缓存中的长度
     */
    public Long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=====获取list类型的长度失败=====");
            return 0L;
        }
    }

    /**
     * 通过索引获取list缓存中的值
     *
     * @param key   键
     * @param index 索引位置
     * @return 该索引对应的值 index>0时 0表头 1 第二个元素 index<0时，-1表尾 -2倒数第二个元素
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=====获取list中索引的值失败=====");
            return null;
        }
    }

    /**
     * 将数据存入list缓存中
     *
     * @param key   键
     * @param value 值 一个对象
     * @return true 成功 false 失败
     */
    public boolean ISet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=====list中添加键值失败=====");
            return false;
        }
    }

    /**
     * 将值存入list缓存中 含过期时间
     *
     * @param key   键
     * @param value 值 一个对象
     * @param time  过期时间 秒
     * @return true 成功 false 失败
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=====list中添加过期键值失败=====");
            return false;
        }
    }

    /**
     * 将list放入缓存中
     *
     * @param key   键
     * @param value 值 一个集合
     * @return true 成功 false 失败
     */
    public boolean lSetList(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=====list中添加多个键值失败=====");
            return false;
        }
    }

    /**
     * 将list放入缓存中 含过期时间
     *
     * @param key   键
     * @param value 值 集合
     * @param time  过期时间 s
     * @return true 成功 false 失败
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=====list中添加多个过期键值失败=====");
            return false;
        }
    }

    /**
     * 将索引位置list缓存中的值修改
     *
     * @param key   键
     * @param index 索引位置
     * @param value 值
     * @return true 成功 false 失败
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=====list中更新键值失败=====");
            return false;
        }
    }

    /**
     * 移除 n个值为value
     *
     * @param key   键
     * @param count 数目
     * @param value 值
     * @return 成功个数
     */
    public Long lRemove(String key, long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=====list中更新键值失败=====");
            return 0L;
        }
    }

    /**
     * 限制list中值的个数
     *
     * @param key 键
     * @param min 最小位
     * @param max 最大位
     * @return
     */
    public Boolean ltrimList(String key, Integer min, Integer max) {
        try {
            redisTemplate.opsForList().trim(key, min, max);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("====list中限制值个数失败====");
            return false;
        }
    }

    /**
     * 将数据添加到bitMap中缓存
     *
     * @param key    字段
     * @param offset 位置
     * @param value  数值
     * @return
     */
    public boolean setBit(String key, Integer offset, boolean value) {
        try {
            redisTemplate.execute((RedisCallback) con -> con.setBit(key.getBytes(), offset, value));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("====添加BitMap失败====");
            return false;
        }
    }

    /**
     * 判断该key字段offset位是否为1
     *
     * @param key    字段
     * @param offset 位置
     * @return
     */
    public Integer getBit(String key, Integer offset) {
        try {
            return (Integer) redisTemplate.execute((RedisCallback) con -> con.getBit(key.getBytes(), offset));
        } catch (Exception e) {
            e.printStackTrace();
            log.info("====添加BitMap失败====");
            return -1;
        }
    }

    /**
     * 统计key字段value值为1的字段
     *
     * @param key 字段
     * @return
     */
    public Integer bitCount(String key) {
        try {
            return (Integer) redisTemplate.execute((RedisCallback) con -> con.bitCount(key.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            log.info("====获取总数失败====");
            return -1;
        }
    }

    /**
     * 将数据添加到zSet中缓存
     *
     * @param key   字段
     * @param value 元素
     * @param score 分数
     * @return
     */
    public boolean zSet(String key, Object value, Double score) {
        try {
            redisTemplate.opsForZSet().add(key, value, score);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("====添加zSet类型数据失败====");
            return false;
        }
    }

    /**
     * 将数据添加到zSet中缓存并设置过去时间
     *
     * @param key   字段
     * @param value 元素
     * @param score 分数
     * @param time  过期时间
     * @return
     */
    public boolean zSet(String key, Object value, Double score, long time) {
        try {
            redisTemplate.opsForZSet().add(key, value, score);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("====添加zSet过期时间类型数据失败====");
            return false;
        }
    }

    /**
     * 根据key和value删除指定元素value
     *
     * @param key   字段
     * @param value 元素
     * @return
     */
    public boolean removeZSetValue(String key, Object value) {
        try {
            redisTemplate.opsForZSet().remove(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("====删除zSet类型数据失败====");
            return false;
        }
    }

    /**
     * 根据key 删除 zset中第一条元素 默认是score的正序
     *
     * @param key
     * @return
     */
    public boolean removeFirstZSetValue(String key) {
        try {
            redisTemplate.opsForZSet().removeRange(key, 0, 0);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=====删除zset中的第一条数据失败====");
            return false;
        }
    }

    /**
     * 获取zset中的元素个数
     *
     * @param key
     * @return
     */
    public Long getZsetSize(String key) {
        try {
            return redisTemplate.opsForZSet().zCard(key);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("====获取zSet元素个数失败====");
            return 0L;
        }
    }

    public boolean judgeZsetExistValue(String key, Object value) {
        try {
            Long rank = redisTemplate.opsForZSet().rank(key, value);
            return rank != null;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=========判断是否存在key失败========");
        }
        return false;
    }

    /**
     * 为指定元素加分
     *
     * @param key   字段
     * @param value 元素
     * @param score 分数值
     * @return
     */
    public Double incrementScoreForZSet(String key, Object value, Double score) {
        try {
            return redisTemplate.opsForZSet().incrementScore(key, value, score);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("====为指定元素增加分数失败=====");
            return -1.0;
        }
    }

    /**
     * 获取指定元素的分数值
     *
     * @param key   字段
     * @param value 元素
     * @return
     */
    public Double getScore(String key, Object value) {
        try {
            return redisTemplate.opsForZSet().score(key, value);
        } catch (Exception e) {
            log.info("====获取指定元素的分数====");
            return -1.0;
        }
    }

    /**
     * 获取ZSet中元素的排名（倒序)
     *
     * @param key 字段
     * @return
     */
    public Set<Object> rangeByZSetAll(String key) {
        try {
            return redisTemplate.opsForZSet().reverseRange(key, 0, -1);
        } catch (Exception e) {
            log.info("====获取集合内元素排名失败====");
            return null;
        }
    }

    /**
     * 根据索引获取ZSet中元素的排名（倒序)
     *
     * @param key 字段
     * @return
     */
    public Set<Object> rangeByZSet(String key, int startIndex, int endIndex) {
        try {
            return redisTemplate.opsForZSet().reverseRange(key, startIndex, endIndex);
        } catch (Exception e) {
            log.info("====获取集合内元素排名失败====");
            return null;
        }
    }

    /**
     * 根据索引获取ZSet中元素的排名（倒序)含score
     *
     * @param key 字段
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> rangeByZSetWithScore(String key, int startIndex, int endIndex) {
        try {
            return redisTemplate.opsForZSet().reverseRangeWithScores(key, startIndex, endIndex);
        } catch (Exception e) {
            log.info("====获取集合内元素排名失败====");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取ZSet中元素的排名（倒序)
     *
     * @param key      字段
     * @param minScore 最小分数值
     * @param maxScore 最大分数值
     * @return
     */
    public Set<Object> rangeByZSet(String key, long minScore, long maxScore) {
        try {
            return redisTemplate.opsForZSet().reverseRangeByScore(key, minScore, maxScore);
        } catch (Exception e) {
            log.info("====获取集合内元素排名失败(指定范围)====");
            return null;
        }
    }

    /**
     * 倒序查询成员在zset中的索引
     *
     * @param key    zset的可以
     * @param member 成员
     * @return {@link Object}
     * @author 廖振辉
     */
    public Long zrevrank(String key, Object member) {
        try {
            Long rank = redisTemplate.opsForZSet().reverseRank(key, member);
            return rank == null ? -1L : rank;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1L;
    }

}
