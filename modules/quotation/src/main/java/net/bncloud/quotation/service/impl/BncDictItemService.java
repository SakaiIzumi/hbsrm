package net.bncloud.quotation.service.impl;

import com.alibaba.fastjson.JSONArray;
import net.bncloud.api.feign.saas.sys.DictItemDTO;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.common.util.StringUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;


/**
 * @author Toby
 */
@Component
public class BncDictItemService {

    private static final String DICT_CACHE_KEY_PREFIX = "BNC:DICT:ITEMS:";

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取字典标签
     * @param dictCode 字典编码
     * @param dictItemValue 字典项值
     * @return 字典项标签
     */
    public String getDictItemLabel(String dictCode,String dictItemValue){
        if(StringUtil.isBlank(dictCode) || StringUtil.isBlank(dictItemValue)){
            return null;
        }
        final String itemsJson = stringRedisTemplate.opsForValue().get(DICT_CACHE_KEY_PREFIX + dictCode);
        final List<DictItemDTO> dictItems = JSONArray.parseArray(itemsJson, DictItemDTO.class);
        if(CollectionUtil.isNotEmpty(dictItems)){
            Optional<DictItemDTO> optional = dictItems.stream().filter(dictItemDTO -> dictItemDTO.getValue().equals(dictItemValue)).findFirst();
            if(optional.isPresent()){
                return optional.get().getLabel();
            }
        }
        return null;
    }

}
