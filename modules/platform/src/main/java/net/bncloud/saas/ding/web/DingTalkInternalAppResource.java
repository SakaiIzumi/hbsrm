package net.bncloud.saas.ding.web;

import net.bncloud.common.api.R;
import net.bncloud.saas.ding.domain.DingTalkApp;
import net.bncloud.saas.ding.service.DingTalkInternalAppService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ding-talk/internal/app")
public class DingTalkInternalAppResource {

    private final DingTalkInternalAppService dingTalkInternalAppService;

    public DingTalkInternalAppResource(DingTalkInternalAppService dingTalkInternalAppService) {
        this.dingTalkInternalAppService = dingTalkInternalAppService;
    }

    @PostMapping("/add_app")
    public R<DingTalkApp> createInternalApp(DingTalkApp app) {
        DingTalkApp result = dingTalkInternalAppService.createInternalApp(app);
        return R.data(result);
    }

    @PutMapping("/update_app")
    public R<DingTalkApp> updateInternalApp(DingTalkApp app) {
        dingTalkInternalAppService.updateInternalApp(app);
        return R.success();
    }

    @GetMapping("/pageQuery")
    public R<Page<DingTalkApp>> pageQuery(Pageable pageable) {
        Page<DingTalkApp> page = dingTalkInternalAppService.pageQuery(pageable);
        return R.data(page);
    }
}
