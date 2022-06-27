package net.bncloud.common.security.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.*;
import java.util.stream.Collectors;

public class DesensitizeFieldHolder {
    private final String cachePrefix;

    private final StringRedisTemplate stringRedisTemplate;

    public DesensitizeFieldHolder(String cachePrefix, StringRedisTemplate stringRedisTemplate) {
        this.cachePrefix = cachePrefix;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void cache(String app, List<DataDesensitizeField> desensitizeFields) {
        if (StringUtils.isBlank(app) || desensitizeFields == null || desensitizeFields.isEmpty()) {
            return;
        }
        Map<String, List<DataDesensitizeField>> map = desensitizeFields.stream().collect(Collectors.groupingBy(DataDesensitizeField::getSubjectId));
        HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
        if (map != null) {
            for (Map.Entry<String, List<DataDesensitizeField>> entry : map.entrySet()) {
                opsForHash.put(key(app), entry.getKey(), JSON.toJSONString(entry.getValue()));
            }
        }
    }

    public void delete(String app) {
        if (StringUtils.isBlank(app)) {
            return;
        }
        this.stringRedisTemplate.delete(key(app));
    }

    public void delete(String app, String subject) {
        if (StringUtils.isBlank(app) || StringUtils.isBlank(subject)) {
            return;
        }
        HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
        opsForHash.delete(key(app), subject);
    }

    public List<DataDesensitizeField> get(String app, String subject) {
        if (StringUtils.isBlank(app) || StringUtils.isBlank(subject)) {
            return null;
        }
        HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
        String json = opsForHash.get(key(app), subject);
        return JSONArray.parseArray(json, DataDesensitizeField.class);
    }

    public DataDesensitizeField get(String app, String subject, String dimensionCode) {
        List<DataDesensitizeField> desensitizeFields = get(app, subject);
        if (desensitizeFields != null) {
            return desensitizeFields.stream().filter(s -> StringUtils.equals(s.getDimensionCode(), dimensionCode)).findFirst().orElse(null);
        }
        return null;
    }

    public Map<String, List<DataDesensitizeField>> get(String app) {
        if (StringUtils.isBlank(app)) {
            return null;
        }
        HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();

        Map<String, List<DataDesensitizeField>> map = new HashMap<>();
        for (Map.Entry<String, String> entry : opsForHash.entries(key(app)).entrySet()) {
            map.put(entry.getKey(), JSONArray.parseArray(entry.getValue(), DataDesensitizeField.class));
        }
        return map;
    }


    private String key(String app) {
        return cachePrefix + ":DESENSITIZEFIELD:" + app;
    }
}
