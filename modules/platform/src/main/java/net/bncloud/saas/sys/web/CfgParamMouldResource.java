package net.bncloud.saas.sys.web;

import lombok.AllArgsConstructor;
import net.bncloud.saas.sys.service.CfgParamMouldService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/cfg/param/mould")
@AllArgsConstructor
public class CfgParamMouldResource {

    private CfgParamMouldService cfgParamMouldService;
}
