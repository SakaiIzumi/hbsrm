package net.bncloud.saas.ding.web;

import net.bncloud.common.api.R;
import net.bncloud.saas.ding.domain.DingTalkApp;
import net.bncloud.saas.ding.service.DingTalkInternalAppService;
import net.bncloud.saas.ding.service.DingTalkNoticeAppService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ding-talk/internal/app")
public class DingTalkNoticeAppResource {

    private final DingTalkNoticeAppService dingTalkNoticeAppService;

    public DingTalkNoticeAppResource(DingTalkNoticeAppService dingTalkNoticeAppService) {
        this.dingTalkNoticeAppService = dingTalkNoticeAppService;
    }

}
