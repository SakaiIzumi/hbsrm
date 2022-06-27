package net.bncloud.quotation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.quotation.entity.BidResponse;
import net.bncloud.quotation.entity.QuotationSupplier;
import net.bncloud.quotation.mapper.BidResponseMapper;
import net.bncloud.quotation.mapper.QuotationSupplierMapper;
import net.bncloud.quotation.param.QuotationSupplierParam;
import net.bncloud.quotation.service.BidResponseService;
import net.bncloud.quotation.service.QuotationSupplierService;
import net.bncloud.quotation.vo.QuotationSupplierVo;
import net.bncloud.support.Condition;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 询价供应商信息 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-18
 */
@Service
public class BidResponseServiceImpl extends BaseServiceImpl<BidResponseMapper, BidResponse> implements BidResponseService {

}
