package net.bncloud.information.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.service.base.domain.HandlerMsgParam;
import net.bncloud.common.service.base.domain.SendMsgParam;
import net.bncloud.information.entity.ZcInformationMsg;
import net.bncloud.information.entity.ZcInformationSMS;
import net.bncloud.information.param.ZcInformationMsgParam;
import net.bncloud.information.vo.InformationMsgStatisticsVo;
import net.bncloud.information.vo.ZcInformationMsgVo;
import net.bncloud.information.vo.ZcInformationRouteParamVo;

import java.util.List;

/**
 * 智采消息表 服务类
 * @author dr
 */
public interface IZcInformationSMSService extends BaseService<ZcInformationSMS> {


    R saveInformationSMS(SendMsgParam sendMsgParam);


}