package net.bncloud.delivery.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.SupplyDemandDetailView;
import net.bncloud.delivery.param.SupplyDemandDetailViewParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author ddh
 * @since 2022/4/8
 * @description 报表明细
 */
@Mapper
public interface SupplyDemandDetailViewMapper{

    List<SupplyDemandDetailView> selectPageList(IPage<SupplyDemandDetailView> page, @Param("queryParam") QueryParam<SupplyDemandDetailViewParam> queryParam);

    /**
     * 获取去重后的所有的物料编码
     * @return
     */
    List<String> getProductCodeByCondition(@Param("supplier") String supplier);

    List<SupplyDemandDetailView> getDetailsByCode(@Param("productCode") String productCode);

}