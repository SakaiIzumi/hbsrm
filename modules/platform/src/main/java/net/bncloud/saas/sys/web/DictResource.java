package net.bncloud.saas.sys.web;

import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.saas.sys.domain.Dict;
import net.bncloud.saas.sys.service.DictItemService;
import net.bncloud.saas.sys.service.DictService;
import net.bncloud.saas.sys.service.dto.DictDTO;
import net.bncloud.saas.sys.service.query.DictQuery;
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

import java.util.Set;

@RestController
@RequestMapping("/sys/dict")
public class DictResource {

    private final DictService dictService;

    private final DictItemService dictItemService;

    public DictResource(DictService dictService,DictItemService dictItemService) {
        this.dictItemService = dictItemService;
        this.dictService = dictService;
    }

    @ApiOperation("查询字典")
    @PostMapping(value = "/pageQuery")
    public R<Page<DictDTO>> pageQuery(@RequestBody QueryParam<DictQuery> resources, Pageable pageable) {
        return R.data(dictService.pageQuery(resources.getParam(), pageable));
    }

    @ApiOperation("新增字典")
    @PostMapping
    public R<Void> create(@RequestBody Dict resources) {
        dictService.create(resources);
        return R.success();
    }

    @GetMapping("/{code}")
    public R<DictDTO> items(@PathVariable String code) {
        DictDTO dictDTO = dictService.getDictByCode(code);
        //dictDTO.setItems(dictItemService.getDictByCode(code));
        return R.data(dictDTO);
    }

    @GetMapping("/getById/{id}")
    public R<DictDTO> getById(@PathVariable String id) {
        DictDTO dictDTO = dictService.getById(id);
        return R.data(dictDTO);
    }

    @ApiOperation("修改字典")
    @PutMapping
    public R<Void> update(@RequestBody Dict resources) {
        dictService.update(resources);
        return R.success();
    }

    @ApiOperation("删除字典")
    @DeleteMapping
    public R<Void> delete(@RequestBody Set<String> codes) {
        dictService.delete(codes);
        return R.success();
    }
}
