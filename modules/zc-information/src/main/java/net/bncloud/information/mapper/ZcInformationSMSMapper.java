package net.bncloud.information.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.information.entity.ZcInformationMsg;
import net.bncloud.information.entity.ZcInformationSMS;
import net.bncloud.information.param.ZcInformationMsgParam;
import net.bncloud.information.vo.ZcInformationMsgVo;

import java.util.List;

/**
 * 智采消息表 Mapper 接口
 * @author dr
 */
public interface ZcInformationSMSMapper extends BaseMapper<ZcInformationSMS> {

}