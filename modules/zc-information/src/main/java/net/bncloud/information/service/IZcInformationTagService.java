package net.bncloud.information.service;

import net.bncloud.information.entity.ZcInformationTag;
import net.bncloud.information.vo.ZcInformationTagVo;
import net.bncloud.information.param.ZcInformationTagParam;
import net.bncloud.base.BaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;

/**
 * 智采消息标签 服务类
 * @author Auto-generator
 * @since 2021-03-22
 */
public interface IZcInformationTagService extends BaseService<ZcInformationTag> {
	/**
	 * 自定义分页
	 */
	IPage<ZcInformationTagVo> selectPage(IPage<ZcInformationTagVo> page, QueryParam<ZcInformationTagParam> queryParam);

	ZcInformationTagVo findByTag(String tag);
}