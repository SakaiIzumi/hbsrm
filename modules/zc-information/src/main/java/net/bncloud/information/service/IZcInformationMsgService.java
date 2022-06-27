package net.bncloud.information.service;

import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.service.base.domain.HandlerMsgParam;
import net.bncloud.common.service.base.domain.SendMsgParam;
import net.bncloud.information.entity.ZcInformationMsg;
import net.bncloud.information.vo.InformationMsgStatisticsVo;
import net.bncloud.information.vo.ZcInformationMsgVo;
import net.bncloud.information.param.ZcInformationMsgParam;
import net.bncloud.base.BaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.information.vo.ZcInformationRouteParamVo;

import java.util.List;

/**
 * 智采消息表 服务类
 * @author dr
 */
public interface IZcInformationMsgService extends BaseService<ZcInformationMsg> {
    /**
     * 自定义分页
     */
    IPage<ZcInformationMsgVo> selectPage(IPage<ZcInformationMsgVo> page, QueryParam<ZcInformationMsgParam> queryParam);

    R saveInformationMsg(SendMsgParam sendMsgParam);

    InformationMsgStatisticsVo statistics(Long userId, String sendType, String terminalType,String code);


    List<ZcInformationRouteParamVo> getHandleUrl(Long id);

    InformationMsgStatisticsVo waitHandle(Long userId, String sendType, String terminalType);

    void changeStatus(Long id);

    void handlerInformation(HandlerMsgParam handlerMsgParam);

    InformationMsgStatisticsVo statisticsOrg(Long userId, String sendType, String terminalType, Long id);
}