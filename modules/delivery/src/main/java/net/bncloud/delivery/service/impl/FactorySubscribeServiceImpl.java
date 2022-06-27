package net.bncloud.delivery.service.impl;

import net.bncloud.base.BaseServiceImpl;
import net.bncloud.delivery.entity.FactorySubscribe;
import net.bncloud.delivery.mapper.FactorySubscribeMapper;
import net.bncloud.delivery.service.FactorySubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FactorySubscribeServiceImpl extends BaseServiceImpl<FactorySubscribeMapper, FactorySubscribe> implements FactorySubscribeService {
    @Resource
    private FactorySubscribeMapper factorySubscribeMapper;

    @Override
    public List<FactorySubscribe> listAllSubscribeForLocalYear(String code, String year) {
        List<FactorySubscribe> list=factorySubscribeMapper.listAllSubscribeForLocalYear(code,year);
        return list;
    }
}
