package net.bncloud.bis.service.impl;

import com.alibaba.nacos.common.utils.Objects;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.bncloud.bis.dao.K3ERPEnumDao;
import net.bncloud.bis.model.entity.K3ERPEnum;
import net.bncloud.bis.service.K3ERPEnumService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static net.bncloud.bis.constant.DatasourceConstants.BIS;


/**
 * 枚举是手动添加到自己的表的，
 *   因此现在存在的问题是：如果正式ERP环境枚举手动添加， 那么这边 则需要手动添加 到表里 bis.k3_erp_enum
 * @author lijiaju
 * @date 2022/2/23 14:50
 */
@Service
@DS(BIS)
public class K3ERPEnumServiceImpl extends ServiceImpl<K3ERPEnumDao,K3ERPEnum> implements K3ERPEnumService{

    @Override
    @Cacheable(cacheNames="cache::erpEnumValue",key = "#fieldKey + ':' + #code")
    public String getEnumValue(String fieldKey,String code){
        K3ERPEnum k3ERPEnum = getOne(new LambdaQueryWrapper<K3ERPEnum>().eq(K3ERPEnum::getFieldKey,fieldKey).eq(K3ERPEnum::getCode,code));
        if(Objects.isNull(k3ERPEnum)){
            return "";
        }else{
            return k3ERPEnum.getValue();
        }
    }

    @Override
    @CacheEvict(cacheNames = "cache::erpEnumValue", allEntries = true)
    public void evictK3ErpCache() {}
}
