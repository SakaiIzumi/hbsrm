package net.bncloud.saas.user.web;


import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.saas.user.strategy.selector.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user-center/complexes")
@Slf4j
@AllArgsConstructor
public class UserComplexesResource {

    private final SideTreeSelectorStrategyContext sideTreeSelectorStrategyContext;

    @PostMapping("/getTreeByTypeAndParentId")
    @ApiOperation(value = "分类选择树", notes = "通用接口")
    public R getTreeByTypeAndParentId(@RequestBody SideTreeQuery condition) {
        return R.data(sideTreeSelectorStrategyContext.getTreeData(condition));
    }


    @PostMapping("/getMemberTableData")
    @ApiOperation(value = "选择器-表格数据")
    public R getTableData(@RequestBody TableQuery query, Pageable pageable) {
        return R.data(sideTreeSelectorStrategyContext.getTableData(query, pageable));
    }


    @PostMapping("/getMemberDataEcho")
    @ApiOperation(value = "选择器-回显数据")
    public R getDataEcho(@RequestBody DataEchoQuery query) {
        return R.data(sideTreeSelectorStrategyContext.getDataEcho(query));
    }

}
