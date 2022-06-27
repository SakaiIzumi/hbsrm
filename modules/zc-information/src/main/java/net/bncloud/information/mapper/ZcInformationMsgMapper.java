package net.bncloud.information.mapper;

import net.bncloud.information.entity.ZcInformationMsg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.bncloud.information.vo.ZcInformationMsgVo;
import net.bncloud.information.param.ZcInformationMsgParam;
import net.bncloud.common.base.domain.QueryParam;
import java.util.List;

/**
 * 智采消息表 Mapper 接口
 * @author dr
 */
public interface ZcInformationMsgMapper extends BaseMapper<ZcInformationMsg> {
	List<ZcInformationMsgVo> selectListPage(IPage page, QueryParam<ZcInformationMsgParam> queryParam);

    ZcInformationMsgVo selectMsgOnce(ZcInformationMsg msg);
}