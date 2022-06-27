package net.bncloud.delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.api.ResultCode;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.exception.Asserts;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.common.util.StringUtil;
import net.bncloud.delivery.entity.FactoryInfo;
import net.bncloud.delivery.entity.FactoryTransportationDuration;
import net.bncloud.delivery.enums.FactoryBelongTypeEnum;
import net.bncloud.delivery.mapper.FactoryTransportationDurationMapper;
import net.bncloud.delivery.param.FactoryInfoParam;
import net.bncloud.delivery.param.FactoryTransportationDurationParam;
import net.bncloud.delivery.service.FactoryInfoService;
import net.bncloud.delivery.service.FactoryTransportationDurationService;
import net.bncloud.delivery.vo.FactoryTransportationDurationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Service
public class FactoryTransportationDurationServiceImpl extends BaseServiceImpl<FactoryTransportationDurationMapper, FactoryTransportationDuration> implements FactoryTransportationDurationService {
    @Resource
    private FactoryTransportationDurationMapper factoryTransportationDurationMapper;
    @Autowired
    @Lazy
    private FactoryInfoService factoryInfoService;


    @Override
    public PageImpl<FactoryTransportationDurationVo> selectPageList(IPage<FactoryTransportationDurationVo> page, QueryParam<FactoryTransportationDurationParam> queryParam) {
        List<FactoryTransportationDurationVo> transportationDurationVos = factoryTransportationDurationMapper.selectListPage(page, queryParam);
        if (CollectionUtil.isNotEmpty(transportationDurationVos)) {
            //将省市区街道拼接成字符串
            transportationDurationVos.forEach(vo -> {
                ArrayList<String> list = new ArrayList<String>();
                list.add( vo.getDeliveryFactoryProvince());
                list.add(  vo.getDeliveryFactoryCity());
                list.add( vo.getDeliveryFactoryDistrict());
                list.add(  vo.getDeliveryFactoryStreet());
                vo.setAreas(list);

                String area = vo.getDeliveryFactoryProvince() +
                        vo.getDeliveryFactoryCity() +
                        vo.getDeliveryFactoryDistrict() +
                        vo.getDeliveryFactoryStreet();
                vo.setDeliveryFactoryArea(area);
            });
        }
        return PageUtils.result(page.setRecords(transportationDurationVos));
    }

    @Override
    public void batchDeleteTransportationDuration(List<Long> ids) {
        if (CollectionUtil.isNotEmpty(ids)) {
            deleteLogic(ids);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addOrEditTransportationDuration(List<FactoryTransportationDuration> factoryTransportationDurationList) {
        if (CollectionUtil.isNotEmpty(factoryTransportationDurationList)) {
            factoryTransportationDurationList.forEach(factoryTransportationDuration -> {
                if (StringUtil.isEmpty(factoryTransportationDuration.getTransportDuration())) {
                    throw new ApiException(ResultCode.FAILURE.getCode(), "新增或修改供应商运输时长信息的时候，运输时长不能为空！");
                }

                // 一个采购方工厂对应一个供应商的工厂、一个运输方式只有一条数据
                FactoryTransportationDuration whetherExist = this.getOne(Wrappers.<FactoryTransportationDuration>lambdaQuery()
                        .ne( factoryTransportationDuration.getId() != null ,FactoryTransportationDuration::getId, factoryTransportationDuration.getId() )
                        .eq(FactoryTransportationDuration::getReceiptFactoryId, factoryTransportationDuration.getReceiptFactoryId())
                        .eq(FactoryTransportationDuration::getDeliveryFactoryId, factoryTransportationDuration.getDeliveryFactoryId())
                        .eq( FactoryTransportationDuration::getTransportWay, factoryTransportationDuration.getTransportWay())
                );

                Asserts.isNull( whetherExist,"已存在该采购与供应商和运输方式的数据！" );

                saveOrUpdate(factoryTransportationDuration);
            });
        }
    }

    @Override
    public List<FactoryInfo> getFactoryInfoList(QueryParam<FactoryInfoParam> queryParam) {
        @Valid FactoryInfoParam param = queryParam.getParam();
        LambdaQueryWrapper<FactoryInfo> queryWrapper = Wrappers.<FactoryInfo>lambdaQuery().eq(FactoryInfo::getBelongType, param.getBelongType());
        if (StringUtil.isNotBlank(queryParam.getSearchValue())) {
            queryWrapper.and(query -> {
                query.like(FactoryInfo::getBelongName, queryParam.getSearchValue())
                        .or()
                        .like(FactoryInfo::getBelongCode, queryParam.getSearchValue())
                        .or()
                        .like(FactoryInfo::getName,queryParam.getSearchValue());
            });
        }
        return factoryInfoService.list(queryWrapper);
    }

    @Override
    public Integer getTransportationDuration(String purchaserCode, String supplierCode) {

        // 目前是 一个采购方一个工厂，工厂的code等于采购方的code
        FactoryInfo purchaserFactoryInfo = factoryInfoService.getOne(
                Wrappers.<FactoryInfo>lambdaQuery()
                        .eq(FactoryInfo::getBelongType, FactoryBelongTypeEnum.PURCHASE.getCode() )
                        .eq(FactoryInfo::getBelongCode, purchaserCode)
        );
        if( purchaserFactoryInfo == null) return 0;

        // 一个供应商只有一个工厂，工厂的code不等于供应商的code
        FactoryInfo supplierFactoryInfo = factoryInfoService.getOne(
                Wrappers.<FactoryInfo>lambdaQuery()
                        .eq(FactoryInfo::getBelongType, FactoryBelongTypeEnum.SUPPLIER.getCode() )
                        .eq(FactoryInfo::getBelongCode, supplierCode)
        );

        if(supplierFactoryInfo == null ) return 0;

        FactoryTransportationDuration factoryTransportationDuration = this.getOne(
                Wrappers.<FactoryTransportationDuration>lambdaQuery()
                        .eq(FactoryTransportationDuration::getDeliveryFactoryId, supplierFactoryInfo.getId())
                        .eq(FactoryTransportationDuration::getReceiptFactoryId, purchaserFactoryInfo.getId())
                        .orderByDesc( FactoryTransportationDuration::getTransportDuration )
                        .last("limit 1")
        );

        return factoryTransportationDuration != null ? Integer.parseInt( factoryTransportationDuration.getTransportDuration() ) : 0;

    }
}
