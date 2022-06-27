package net.bncloud.saas.tenant.web;

import net.bncloud.common.api.R;
import net.bncloud.saas.tenant.domain.Position;
import net.bncloud.saas.tenant.service.PositionService;
import net.bncloud.saas.tenant.service.dto.PositionDTO;
import net.bncloud.saas.tenant.service.query.PositionQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tenant/position")
public class PositionResource {

    private final PositionService positionService;

    public PositionResource(PositionService positionService) {
        this.positionService = positionService;
    }

    @PostMapping("/pageQuery")
    public R<Page<Position>> pageQuery(@RequestBody PositionQuery query, Pageable pageable) {
        return R.data(positionService.pageQuery(query, pageable));
    }

    @GetMapping("/{id}")
    public R<Position> getById(@PathVariable Long id) {
        return R.data(positionService.findById(id).orElse(null));
    }

    @PostMapping
    public R<Void> save(@RequestBody PositionDTO position) {
        positionService.save(position);
        return R.success();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        positionService.deleteById(id);
        return R.success();
    }

    @PutMapping("/status/{id}")
    public R<Position> switchStatus(@PathVariable Long id) {
        positionService.switchStatus(id);
        return R.data(positionService.findById(id).orElse(null));
    }

}
