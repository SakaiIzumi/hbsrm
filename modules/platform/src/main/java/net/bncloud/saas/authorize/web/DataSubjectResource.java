package net.bncloud.saas.authorize.web;

import lombok.AllArgsConstructor;
import net.bncloud.common.api.R;
import net.bncloud.saas.authorize.domain.DataSubject;
import net.bncloud.saas.authorize.service.DataSubjectService;
import net.bncloud.saas.authorize.service.query.DataSubjectQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authorize/data/subject")
@AllArgsConstructor
public class DataSubjectResource {

    private final DataSubjectService dataSubjectService;

    @GetMapping("/all")
    public R queryAll(@RequestBody DataSubjectQuery query) {
        return R.data(dataSubjectService.queryAll(query));
    }

    @GetMapping
    public R queryPage(@RequestBody DataSubjectQuery query, Pageable pageable) {
        return R.data(dataSubjectService.queryPage(query, pageable));
    }


    @PostMapping
    public R create(@RequestBody DataSubject resources) {

        return R.success();
    }
}
