package net.bncloud.delivery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.FactoryInfo;
import net.bncloud.delivery.param.FactoryInfoParam;
import net.bncloud.delivery.vo.FactoryInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liyh
 * @description 工厂主数据mapper
 * @since 2022/5/16
 */
public interface FactoryInfoMapper extends BaseMapper<FactoryInfo> {


    IPage<FactoryInfoVo> selectPlantListPage(IPage<FactoryInfoParam> page, @Param("queryParam") QueryParam<FactoryInfoParam> queryParam);

    void deleteAllByIds(@Param("ids")List<Long> ids);
}
