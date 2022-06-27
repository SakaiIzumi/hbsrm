package net.bncloud.delivery.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.bncloud.delivery.entity.FactorySubscribe;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FactorySubscribeMapper extends BaseMapper<FactorySubscribe> {

    List<FactorySubscribe> listAllSubscribeForLocalYear(@Param("code") String code,@Param("year")  String year);
}