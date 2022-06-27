package net.bncloud.saas.authorize.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.saas.authorize.domain.DataAppConfig;
import net.bncloud.saas.authorize.service.DataAppConfigService;
import net.bncloud.saas.authorize.service.query.DataAppConfigQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authorize/data/app_config")
@AllArgsConstructor
@Slf4j
public class DataAppConfigResource {

    private final DataAppConfigService dataAppConfigService;


    @GetMapping("/all")
    public R queryAll(DataAppConfigQuery appConfigQuery) {
        return R.data(dataAppConfigService.queryAll(appConfigQuery));
    }

    @GetMapping
    public R queryPage(DataAppConfigQuery appConfigQuery, Pageable pageable) {
        return R.data(dataAppConfigService.queryPage(appConfigQuery, pageable));
    }

    @PostMapping
    public R create(@RequestBody DataAppConfig resources) {
        dataAppConfigService.create(resources);
        return R.success();
    }

    @GetMapping("/load")
    public R loadDataAppConfig() {
        dataAppConfigService.loadDataAppConfig();
        return R.success();
    }
}
