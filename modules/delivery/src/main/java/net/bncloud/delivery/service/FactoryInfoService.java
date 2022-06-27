package net.bncloud.delivery.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.FactoryInfo;
import net.bncloud.delivery.param.FactoryInfoParam;
import net.bncloud.delivery.param.OverAllAdjustParam;
import net.bncloud.delivery.param.OverallsituationConfigChangeParam;
import net.bncloud.delivery.vo.FactoryInfoVo;
import org.springframework.data.domain.PageImpl;

import java.util.List;

/**
 * 工厂主数据service 接口
 *
 * @author liyh
 * @since 2021-05-16
 */
public interface FactoryInfoService extends BaseService<FactoryInfo> {


    PageImpl<FactoryInfoVo> getPageList(IPage<FactoryInfoParam> pageable, QueryParam<FactoryInfoParam> queryParam);


    void batchDelete(List<Long> ids);

    void batchUpdate(List<FactoryInfo> factoryInfos);

    List<FactoryInfoVo> selectForCreate(String code);

    void overallsituationConfigChange(OverallsituationConfigChangeParam param);

    /**
     * 同步采购方信息到 工厂
     */
    void syncPurchaseInfoToFactoryInfo();

    /**
     * 采购方全局默认工作日配置调整接口
     *
     * 全局配置调整后给没有设置自定义默认工作日的采购工厂主数据使用的
     */
    void overAllAdjustment(OverAllAdjustParam param);
}
