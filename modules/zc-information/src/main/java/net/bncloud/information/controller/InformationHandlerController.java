package net.bncloud.information.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.common.service.base.domain.HandlerMsgParam;
import net.bncloud.information.service.IZcInformationMsgService;
import net.bncloud.information.service.IZcInformationTagService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 智采消息表 前端控制器
 * @author dr
 */
@RestController
@RequestMapping("/information_handler")
@Slf4j
public class InformationHandlerController {
    @Resource
    private IZcInformationMsgService iZcInformationMsgService;




    /**
    * 代办消息结束
    */
    @PostMapping("/handlerInformation")
    @ApiOperation(value = "代办消息结束", notes = "handlerInformation")
    public R handlerInformation(@RequestBody HandlerMsgParam handlerMsgParam){

        iZcInformationMsgService.handlerInformation(handlerMsgParam);
        return R.success("操作成功");
    }


}
