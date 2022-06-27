package net.bncloud.saas.ding.web;

import net.bncloud.common.api.R;
import net.bncloud.saas.ding.service.DingTalkUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/ding-talk/user/app")
public class DingTalkUserAppResource {


    @Autowired
    private DingTalkUserService dingTalkUserService;

    @GetMapping("/findAllDingUserIds")
    public R findAllDingUserIds(){
      return R.data(dingTalkUserService.findAllUserIds());
    }
}
