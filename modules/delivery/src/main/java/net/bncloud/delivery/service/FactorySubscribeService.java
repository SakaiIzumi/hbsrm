package net.bncloud.delivery.service;

import net.bncloud.base.BaseService;
import net.bncloud.delivery.entity.FactorySubscribe;

import java.util.List;


public interface FactorySubscribeService extends BaseService<FactorySubscribe> {

    List<FactorySubscribe> listAllSubscribeForLocalYear(String code, String year);
}
