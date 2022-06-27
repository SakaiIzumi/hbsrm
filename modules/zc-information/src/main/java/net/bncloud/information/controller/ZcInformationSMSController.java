package net.bncloud.information.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.common.service.base.domain.SendMsgParam;
import net.bncloud.information.service.IZcInformationSMSService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 智采发送短信消息表
 * @author dr
 */
@RestController
@RequestMapping("/zc-information-sms")
@Slf4j
public class ZcInformationSMSController {
    @Resource
    private IZcInformationSMSService iZcInformationSMSService;


    /**
    * 新增
    */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入sendMsgParam")
    public R save(@RequestBody SendMsgParam sendMsgParam){
        log.info("消息接收到了数据={}", JSONObject.toJSONString(sendMsgParam));
        iZcInformationSMSService.saveInformationSMS(sendMsgParam);
        return R.success("操作成功");
    }





}
