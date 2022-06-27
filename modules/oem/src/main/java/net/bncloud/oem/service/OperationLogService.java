package net.bncloud.oem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.oem.domain.entity.OperationLog;
import net.bncloud.oem.domain.param.OperationLogParam;
import org.springframework.data.domain.PageImpl;

/**
 * @author ddh
 * @description
 * @since 2022/4/24
 */

public interface OperationLogService extends BaseService<OperationLog> {

    PageImpl<OperationLog> selectPageList(IPage<OperationLog> page, QueryParam<OperationLogParam> param);

    PageImpl<OperationLog> selectpageForAddressLog(IPage<OperationLog> toPage);
}
