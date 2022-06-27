package net.bncloud.saas.authorize.web;

import lombok.AllArgsConstructor;
import net.bncloud.common.api.R;
import net.bncloud.saas.authorize.service.DesensitizeFieldMappingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authorize/data/desensitize_field")
@AllArgsConstructor
public class DesensitizeFieldMappingResource {

    private final DesensitizeFieldMappingService desensitizeFieldMappingService;

    @GetMapping("/load")
    public R load() {
        desensitizeFieldMappingService.load();
        return R.success();
    }
}
