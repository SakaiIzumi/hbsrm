package net.bncloud.bis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.bncloud.bis.model.entity.K3ERPEnum;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

/**
 * @author lijiaju
 * @date 2022/2/23 14:50
 */
public interface K3ERPEnumService extends IService<K3ERPEnum> {
    public String getEnumValue(String field_key, String code);


    public void evictK3ErpCache();
}
