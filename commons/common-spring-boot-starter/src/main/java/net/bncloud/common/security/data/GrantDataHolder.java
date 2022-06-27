package net.bncloud.common.security.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 武书静 wusj4 shujing.wu@meicloud.com
 */
public class GrantDataHolder {

    private final String cachePrefix;

    private final StringRedisTemplate stringRedisTemplate;

    public GrantDataHolder(String cachePrefix, StringRedisTemplate stringRedisTemplate) {
        this.cachePrefix = cachePrefix;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void cache(String username, DataDimensionGrant dataDimensionGrant) {
        if (StringUtils.isBlank(username) || dataDimensionGrant == null) {
            return;
        }
        HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
        opsForHash.put(dataGrantCacheKey(username, dataDimensionGrant.getSubjectId()), dataDimensionGrant.getDimensionCode(), JSON.toJSONString(dataDimensionGrant));
    }


    public void delete(String username, String subjectId) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(subjectId)) {
            return;
        }
        stringRedisTemplate.delete(dataGrantCacheKey(username, subjectId));
    }

    public void delete(String username, String subjectId, String dimensionCode) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(subjectId)) {
            return;
        }
        HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
        opsForHash.delete(dataGrantCacheKey(username, subjectId), dimensionCode);
    }

    public DataDimensionGrant get(String username, String subjectId, String dimensionCode) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(subjectId)) {
            return null;
        }
        HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
        String json = opsForHash.get(dataGrantCacheKey(username, subjectId), dimensionCode);
        return JSONObject.parseObject(json, DataDimensionGrant.class);
    }

    public List<DataDimensionGrant> get(String username, String subjectId) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(subjectId)) {
            return null;
        }
        HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
        return opsForHash.entries(dataGrantCacheKey(username, subjectId))
                .values()
                .stream()
                .filter(StringUtils::isNotBlank)
                .map(s -> JSONObject.parseObject(s, DataDimensionGrant.class))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 数据授权缓存KEY
     *
     * @param username  用户账号
     * @param subjectId 数据主题表 主键
     */
    private String dataGrantCacheKey(String username, String subjectId) {
        return cachePrefix + ":GRANT:" + username + ":" + subjectId;
    }

    /**
     * 数据授权缓存KEY 通配符
     *
     * @param username 用户账号
     */
    private String dataGrantCacheKeyPrefix(String username) {
        return cachePrefix + ":GRANT:" + username + ":" + "*";
    }

    /**
     * 删除用户数据维度授权
     *
     * @param username
     */
    public void delete(String username) {
        String keyPrefix = dataGrantCacheKeyPrefix(username);
        Set<String> keys = stringRedisTemplate.keys(keyPrefix);
        stringRedisTemplate.delete(keys);
    }
}
