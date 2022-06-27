package net.bncloud.saas.authorize.web;

import lombok.AllArgsConstructor;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.saas.authorize.service.DesensitizeTypeService;
import net.bncloud.saas.authorize.service.query.DesensitizeTypeQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping("/authorize/desensitize_type")
@AllArgsConstructor
public class DesensitizeTypeResource {

    private final DesensitizeTypeService desensitizeTypeService;

    @PostMapping("/desensitizes/pageQuery")
    public R queryPage(@RequestBody QueryParam<DesensitizeTypeQuery> queryParam, Pageable pageable) {
        DesensitizeTypeQuery param = queryParam.getParam();
        return R.data(desensitizeTypeService.queryPage(param, pageable));
    }
}
