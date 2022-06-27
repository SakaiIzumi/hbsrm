package net.bncloud.oem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.oem.domain.entity.OperationLog;
import net.bncloud.oem.domain.param.OperationLogParam;
import net.bncloud.oem.enums.AddressOperationLogEnum;
import net.bncloud.oem.mapper.OperationLogMapper;
import net.bncloud.oem.service.OperationLogService;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @author ddh
 * @description
 * @since 2022/4/24
 */
@Service
public class OperationLogServiceImpl extends BaseServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {
    @Autowired
    private OperationLogMapper operationLogMapper;
    @Override
    public PageImpl<OperationLog> selectPageList(IPage<OperationLog> page, QueryParam<OperationLogParam> param) {
        return PageUtils.result(page.setRecords(operationLogMapper.selectPageList(page,param,
                AddressOperationLogEnum.CREATE_LOG.getName(),
                AddressOperationLogEnum.EDIT_LOG.getName(),
                AddressOperationLogEnum.IMPORT.getName())));
    }

    @Override
    public PageImpl<OperationLog> selectpageForAddressLog(IPage<OperationLog> toPage) {
        LambdaQueryWrapper<OperationLog> eq = Condition.getQueryWrapper(new OperationLog())
                .lambda()
                .eq(OperationLog::getOperatorContent, AddressOperationLogEnum.CREATE_LOG.getName())
                .or()
                .eq(OperationLog::getOperatorContent, AddressOperationLogEnum.EDIT_LOG.getName())
                .or()
                .eq(OperationLog::getOperatorContent, AddressOperationLogEnum.IMPORT.getName())
                .orderByDesc(OperationLog::getCreatedDate);

        return PageUtils.result(operationLogMapper.selectPage(toPage, eq));
    }
}
