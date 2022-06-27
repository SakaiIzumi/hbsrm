package net.bncloud.saas.sys.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import net.bncloud.saas.sys.domain.DictItem;
import net.bncloud.saas.sys.repository.DictItemRepository;
import net.bncloud.saas.sys.service.dto.DictItemDTO;
import net.bncloud.saas.sys.service.mapstruct.DictItemMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DictItemService {

    private static final String DICT_CACHE_KEY_PREFIX = "BNC:DICT:ITEMS:";

    private final DictItemRepository dictItemRepository;
    private final DictItemMapper dictItemMapper;
    private final StringRedisTemplate stringRedisTemplate;

    public DictItemService(DictItemRepository dictItemRepository,
                           DictItemMapper dictItemMapper,
                           StringRedisTemplate stringRedisTemplate) {
        this.dictItemRepository = dictItemRepository;
        this.dictItemMapper = dictItemMapper;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public Page<DictItemDTO> pageQuery(String code, Pageable pageable) {
        Page<DictItem> dictItemPage = dictItemRepository.findByDictCode(code, pageable);
        return dictItemPage.map(dictItemMapper::toDto);
    }
    public List<DictItemDTO> findAll(String code) {
        return dictItemMapper.toDto(dictItemRepository.findByDictCode(code));
    }

    @Transactional(rollbackFor = Exception.class)
    public DictItemDTO create(DictItem resources) {
        DictItem saved = dictItemRepository.save(resources);
        deleteItemCache(saved.getDict().getCode());
        return dictItemMapper.toDto(saved);
    }

    @Transactional(rollbackFor = Exception.class)
    public DictItemDTO update(DictItem resources) {
        dictItemRepository.findById(resources.getId()).ifPresent(dictItem -> {
            dictItem.setLabel(resources.getLabel());
            dictItem.setValue(resources.getValue());
            dictItem.setOrder(resources.getOrder());
            dictItemRepository.save(dictItem);
            deleteItemCache(dictItem.getDict().getCode());
        });
        return dictItemRepository.findById(resources.getId())
                .map(dictItemMapper::toDto)
                .orElse(null);
    }

    private void deleteItemCache(String code) {
        stringRedisTemplate.delete(DICT_CACHE_KEY_PREFIX + code);
    }

    public List<DictItemDTO> getDictByCode(String code) {
        final String itemsJson = stringRedisTemplate.opsForValue().get(DICT_CACHE_KEY_PREFIX + code);
        final List<DictItemDTO> dictItems = JSONArray.parseArray(itemsJson, DictItemDTO.class);
        if (dictItems == null || dictItems.isEmpty()) {
            final List<DictItemDTO> dictItemDTOS = dictItemMapper.toDto(dictItemRepository.findByDictCode(code));
            stringRedisTemplate.opsForValue().set(DICT_CACHE_KEY_PREFIX + code, JSON.toJSONString(dictItemDTOS));
            return dictItemDTOS;
        }
        return dictItems;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        dictItemRepository.findById(id).ifPresent(dictItem -> {
            deleteItemCache(dictItem.getDict().getCode());
            dictItemRepository.deleteById(id);
        });
    }
}
