package net.bncloud.saas.authorize.web;

import lombok.AllArgsConstructor;
import net.bncloud.common.api.R;
import net.bncloud.saas.authorize.domain.DataDimension;
import net.bncloud.saas.authorize.service.DataDimensionService;
import net.bncloud.saas.authorize.service.query.DataDimensionQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 维度
 */
@RestController
@RequestMapping("/authorize/data/dimension")
@AllArgsConstructor
public class DataDimensionResource {

    private DataDimensionService dataDimensionService;

    /**
     * 查询维度
     *
     * @param query
     * @return
     */
    @GetMapping("/all")
    public R queryAll(@RequestBody DataDimensionQuery query) {
        return R.data(dataDimensionService.queryAll(query));
    }

    @GetMapping
    public R queryPage(@RequestBody DataDimensionQuery query, Pageable pageable) {
        return R.data(dataDimensionService.queryPage(query, pageable));
    }
}
