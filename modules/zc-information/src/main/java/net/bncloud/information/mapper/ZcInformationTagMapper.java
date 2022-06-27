package net.bncloud.information.mapper;

import net.bncloud.information.entity.ZcInformationTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.bncloud.information.vo.ZcInformationTagVo;
import net.bncloud.information.param.ZcInformationTagParam;
import net.bncloud.common.base.domain.QueryParam;
import java.util.List;

/**
 * 智采消息标签 Mapper 接口
 * @author dr
 */
public interface ZcInformationTagMapper extends BaseMapper<ZcInformationTag> {
	/**
	 * 自定义分页
	 * @author dr
	 */
	List<ZcInformationTagVo> selectListPage(IPage page, QueryParam<ZcInformationTagParam> queryParam);
}