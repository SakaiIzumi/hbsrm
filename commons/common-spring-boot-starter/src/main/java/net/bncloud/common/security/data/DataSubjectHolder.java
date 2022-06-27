package net.bncloud.common.security.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 武书静 wusj4 shujing.wu@meicloud.com
 */
public class DataSubjectHolder {

    private final String cachePrefix;

    private final StringRedisTemplate stringRedisTemplate;

    public DataSubjectHolder(String cachePrefix, StringRedisTemplate stringRedisTemplate) {
        this.cachePrefix = cachePrefix;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void cache(String app, DataSubject subject) {
        if (StringUtils.isBlank(app) || subject == null) {
            return;
        }
        List<DataSubject> dataSubjects = get(app, subject.getKey());
        if (dataSubjects == null) {
            dataSubjects = new ArrayList<>();
        }
        Set<DataSubject> set = new HashSet<>(dataSubjects);
        set.add(subject);

        cache(app, new ArrayList<>(set));
    }

    public void cache(String app, List<DataSubject> subjects) {
        if (StringUtils.isBlank(app) || subjects == null || subjects.isEmpty()) {
            return;
        }
        Map<String, List<DataSubject>> map = subjects.stream().collect(Collectors.groupingBy(DataSubject::getKey));
        HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
        if (map != null) {
            for (Map.Entry<String, List<DataSubject>> entry : map.entrySet()) {
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

    public List<DataSubject> get(String app, String subject) {
        if (StringUtils.isBlank(app) || StringUtils.isBlank(subject)) {
            return null;
        }
        HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
        String json = opsForHash.get(key(app), subject);
        return JSONArray.parseArray(json, DataSubject.class);
    }

    public DataSubject get(String app, String subject, String id) {
        List<DataSubject> dataSubjects = get(app, subject);
        if (dataSubjects != null) {
            return  dataSubjects.stream().filter(s -> StringUtils.equals(s.getId(), id)).findFirst().orElse(null);
        }
        return null;
    }

    public Map<String, List<DataSubject>> get(String app) {
        if (StringUtils.isBlank(app)) {
            return null;
        }
        HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();

        Map<String, List<DataSubject>> map = new HashMap<>();
        for (Map.Entry<String, String> entry : opsForHash.entries(key(app)).entrySet()) {
            map.put(entry.getKey(), JSONArray.parseArray(entry.getValue(), DataSubject.class));
        }
        return map;
    }


    private String key(String app) {
        return cachePrefix + ":SUBJECT:" + app;
    }
}
