package net.bncloud.oem.controller;

import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.oem.domain.entity.OperationLog;
import net.bncloud.oem.domain.param.OperationLogParam;
import net.bncloud.oem.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * 操作记录 控制器liyh
 *
 * @author Auto-generator
 * @since 2022-04-24
 */
@RestController
@RequestMapping("/operationLog")
public class OperationLogController {
    @Autowired
    private OperationLogService operationLogService;

    /**
     * 分页查询日志
     */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询")
    public R<PageImpl<OperationLog>> page(Pageable pageable,@RequestBody QueryParam<OperationLogParam> param) {
        return R.data(operationLogService.selectPageList(PageUtils.toPage(pageable),param));
    }

    /**
     * 分页查询地址维护日志
     */
    @PostMapping("/pageForAddress")
    @ApiOperation(value = "分页查询")
    public R<PageImpl<OperationLog>> pageForAddressLog(Pageable pageable) {
        return R.data(operationLogService.selectpageForAddressLog(PageUtils.toPage(pageable)));
    }

    /**
     * 编辑保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "编辑保存", notes = "")
    public R<Void> saveAddress(@RequestBody OperationLog operationLog) {
        return R.success();
    }
}
