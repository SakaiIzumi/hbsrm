package net.bncloud.quotation.mapper;

import net.bncloud.quotation.entity.QuotationEquipment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;


import net.bncloud.quotation.param.QuotationEquipmentParam;
import net.bncloud.common.base.domain.QueryParam;

import java.util.List;

/**
 * <p>
 * 设备能力要求信息 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface QuotationEquipmentMapper extends BaseMapper<QuotationEquipment> {
    /**
     * <p>
     * 自定义分页
     * </p>
     *
     * @author Auto-generator
     * @since 2022-02-14
     */
    List<QuotationEquipment> selectListPage(IPage page, QueryParam<QuotationEquipmentParam> pageParam);

	/**
	 * 删除设备信息
	 * @param quotationBaseId 询价单基础信息主键
	 */
	void deleteByQuotationBaseId(Long quotationBaseId);
}
