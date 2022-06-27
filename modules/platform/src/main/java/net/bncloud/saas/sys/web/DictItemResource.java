package net.bncloud.saas.sys.web;

import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.saas.sys.domain.DictItem;
import net.bncloud.saas.sys.service.DictItemService;
import net.bncloud.saas.sys.service.dto.DictItemDTO;
import net.bncloud.saas.sys.service.query.DictQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/dict-item")
public class DictItemResource {

    private final DictItemService dictItemService;

    public DictItemResource(DictItemService dictItemService) {
        this.dictItemService = dictItemService;
    }

    @ApiOperation("查询字典详情")
    @PostMapping(value = "/pageQuery")
    public R<Page<DictItemDTO>> query(Pageable pageable,@RequestBody QueryParam<DictQuery> queryParam){
        DictQuery dictQuery = queryParam.getParam();
        return R.data(dictItemService.pageQuery(dictQuery.getCode(),pageable));
    }

    @ApiOperation("查询多个字典详情")
    @GetMapping(value = "/map")
    public R<Map<String, List<DictItemDTO>>> getDictDetailMaps(@RequestParam List<String> codes){
        Map<String, List<DictItemDTO>> dictMap = new HashMap<>(16);
        for (String code : codes) {
            dictMap.put(code, dictItemService.getDictByCode(code));
        }
        return R.data(dictMap);
    }

    @GetMapping("/items/{code}")
    public R<List<DictItemDTO>> items(@PathVariable String code) {
        List<DictItemDTO> data = dictItemService.getDictByCode(code);
        return R.data(data);
    }

    @ApiOperation("新增字典详情")
    @PostMapping
    public R<DictItemDTO> create(@Validated @RequestBody DictItem resources){
        if (resources.getId() != null) {
            throw new IllegalArgumentException("新增时id必须为空"); // TODO
        }
        return R.data(dictItemService.create(resources));
    }

    @ApiOperation("修改字典详情")
    @PutMapping
    public R<DictItemDTO> update(@RequestBody DictItem resources){
        if (resources.getId() == null) {
            throw new IllegalArgumentException("修改时id不能为空"); // TODO
        }
        return R.data(dictItemService.update(resources));
    }

    @ApiOperation("删除字典详情")
    @DeleteMapping(value = "/{id}")
    public R<Void> delete(@PathVariable Long id){
        dictItemService.delete(id);
        return R.success();
    }
}
