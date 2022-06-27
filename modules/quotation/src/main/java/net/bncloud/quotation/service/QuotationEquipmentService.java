package net.bncloud.quotation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.QuotationEquipment;
import net.bncloud.quotation.param.QuotationEquipmentParam;

/**
 * <p>
 * 设备能力要求信息 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface QuotationEquipmentService extends BaseService<QuotationEquipment> {

    /**
     * 自定义分页
     *
     * @param page
     * @param pageParam
     * @return
     */
    IPage<QuotationEquipment> selectPage(IPage<QuotationEquipment> page, QueryParam<QuotationEquipmentParam> pageParam);


	/**
	 * 删除设备信息
	 * @param quotationBaseId 询价单基础信息主键ID
	 */
	void deleteByQuotationBaseId(Long quotationBaseId);
}
