package net.bncloud.quotation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.QuotationEquipment;
import net.bncloud.quotation.mapper.QuotationEquipmentMapper;
import net.bncloud.quotation.param.QuotationEquipmentParam;
import net.bncloud.quotation.service.QuotationEquipmentService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 设备能力要求信息 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Service
public class QuotationEquipmentServiceImpl extends BaseServiceImpl<QuotationEquipmentMapper, QuotationEquipment> implements QuotationEquipmentService {

    @Override
    public IPage<QuotationEquipment> selectPage(IPage<QuotationEquipment> page, QueryParam<QuotationEquipmentParam> pageParam) {
        return page.setRecords(baseMapper.selectListPage(page, pageParam));
    }

	/**
	 * 删除设备信息
	 * @param quotationBaseId 询价单基础信息主键ID
	 */
	@Override
    public void deleteByQuotationBaseId(Long quotationBaseId) {
		baseMapper.deleteByQuotationBaseId(quotationBaseId);

    }
}
