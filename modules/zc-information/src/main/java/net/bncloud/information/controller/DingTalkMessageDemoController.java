package net.bncloud.information.controller;

import com.dingtalk.api.request.OapiCspaceAddToSingleChatRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.swytec.ding.oapi.DingTalkApiService;
import com.swytec.ding.oapi.file.DingFileApiService;
import com.swytec.ding.oapi.message.DingCorpConversationApiService;
import com.taobao.api.ApiException;
import net.bncloud.common.api.R;
import net.bncloud.information.config.ApplicationProperties;
import net.bncloud.information.feign.DingUserServiceFeignClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/information/demo/dd/message")
public class DingTalkMessageDemoController {

    private final ApplicationProperties applicationProperties;
    private final DingTalkApiService dingTalkApiService;

    @Autowired
    private DingUserServiceFeignClient dingUserServiceFeignClient;


    public DingTalkMessageDemoController(ApplicationProperties applicationProperties, DingTalkApiService dingTalkApiService) {
        this.applicationProperties = applicationProperties;
        this.dingTalkApiService = dingTalkApiService;
    }


    @GetMapping("/test")
    public R sendMessageToAll() {
        return dingUserServiceFeignClient.findAllDingUserIds();
    }

    @PostMapping("/send")
    public R sendMessage(String title, String content, String url, String users) {
        if (StringUtils.isBlank(content)) {
            return R.fail("content不能为空");
        }
        if (StringUtils.isBlank(url)) {
            return R.fail("url不能为空");
        }
        if (StringUtils.isBlank(users)) {
            return R.fail("users不能为空");
        }
        doSendMessage(title, content, url, users);
        return R.success();
    }

    @PostMapping("/sendToAll")
    public R sendMessageToAll(String title, String content, String url) {
        if (StringUtils.isBlank(content)) {
            return R.fail("content不能为空");
        }
        if (StringUtils.isBlank(url)) {
            return R.fail("url不能为空");
        }
        R userIdsResult = dingUserServiceFeignClient.findAllDingUserIds();
        Object data = userIdsResult.getData();
        if (data != null) {
            List userIds = (List) data;
            String users = Joiner.on(",").join(userIds);
            doSendMessage(title, content, url, users);
            return R.success();
        }
        return R.fail("用户数据不存在,无法完成发送!");
    }

    private void doSendMessage(String title, String content, String url, String users) {
        final DingCorpConversationApiService apiService = dingTalkApiService.getCorpConversationApiService();

        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        final OapiMessageCorpconversationAsyncsendV2Request.ActionCard actionCard =
                new OapiMessageCorpconversationAsyncsendV2Request.ActionCard();
        actionCard.setTitle(title);
        actionCard.setMarkdown(content);
        if (StringUtils.isNotBlank(url)) {
            actionCard.setSingleTitle("查看详情");
            actionCard.setSingleUrl(url);
        }

        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        msg.setActionCard(actionCard);
        msg.setMsgtype("action_card");
        request.setMsg(msg);
        final ApplicationProperties.Ding ding = applicationProperties.getDing();
        request.setAgentId(ding.getAgentId());
        request.setUseridList(users);
        request.setToAllUser(false);
        apiService.asyncSendMessage(request);
    }

    @PostMapping("/send/file")
    public R sendFileMessage(@RequestParam("file") MultipartFile file,
                             String title, String content, String users,
                             @RequestParam(required = false) String filename) throws IOException, ApiException {
        if (StringUtils.isBlank(content)) {
            return R.fail("content不能为空");
        }
        if (StringUtils.isBlank(users)) {
            return R.fail("users不能为空");
        }
        final DingCorpConversationApiService apiService = dingTalkApiService.getCorpConversationApiService();
        OapiMessageCorpconversationAsyncsendV2Request msgRequest = new OapiMessageCorpconversationAsyncsendV2Request();
        final OapiMessageCorpconversationAsyncsendV2Request.Markdown text = new OapiMessageCorpconversationAsyncsendV2Request.Markdown();
        text.setTitle(title);
        text.setText(content);
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        msg.setMarkdown(text);
        msg.setMsgtype("markdown");
        msgRequest.setMsg(msg);
        msgRequest.setAgentId(applicationProperties.getDing().getAgentId());
        msgRequest.setUseridList(users);
        msgRequest.setToAllUser(false);
        apiService.asyncSendMessage(msgRequest);

        final String[] userIds = users.split(",");
        final DingFileApiService fileApiService = dingTalkApiService.getFileApiService();
        final String mediaId = fileApiService.uploadFile(StringUtils.isBlank(filename) ? file.getOriginalFilename() : filename, file.getSize(), file.getBytes(), applicationProperties.getDing().getAgentId() + "");
        for (String userId : userIds) {
            OapiCspaceAddToSingleChatRequest request = new OapiCspaceAddToSingleChatRequest();
            request.setAgentId(applicationProperties.getDing().getAgentId() + "");
            request.setFileName(StringUtils.isBlank(filename) ? file.getOriginalFilename() : filename);
            request.setMediaId(mediaId);
            request.setUserid(userId);
            fileApiService.addMediaToSingleChat(request);
        }
        return R.success();
    }
}
