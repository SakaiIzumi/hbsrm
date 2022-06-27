package net.bncloud.financial.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.financial.entity.FinancialAttachment;
import net.bncloud.financial.param.FinancialAttachmentParam;
import net.bncloud.financial.service.FinancialAttachmentService;
import net.bncloud.financial.vo.FinancialAttachmentVo;
import net.bncloud.financial.wrapper.AccountAttachmentWrapper;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 * 对账附件信息表 前端控制器
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
@RestController
@RequestMapping("/financial/attachment")
public class FinancialAttachmentController {


    @Autowired
    private FinancialAttachmentService financialAttachmentService;


    /**
     * 通过id查询
     */
    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询", notes = "传入AccountAttachment")
    public R<FinancialAttachment> getById(@PathVariable(value = "id") Long id) {
        return R.data(financialAttachmentService.getById(id));
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入AccountAttachment")
    public R save(@RequestBody FinancialAttachment financialAttachment) {
        financialAttachmentService.save(financialAttachment);
        return R.success();
    }

    /**
     * 通过id删除
     */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String id) {
        financialAttachmentService.removeById(Long.parseLong(id));
        return R.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入accountAttachment")
    public R updateById(@RequestBody FinancialAttachment financialAttachment) {
        financialAttachmentService.updateById(financialAttachment);
        return R.success();
    }


    /**
     * 查询列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入accountAttachment")
    public R list(@RequestBody FinancialAttachment financialAttachment) {
        List<FinancialAttachment> list = financialAttachmentService.list(Condition.getQueryWrapper(financialAttachment));

        return R.data(list);
    }

    /**
     * 分页查询
     */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入AccountAttachmentParam")
    public R page(Pageable pageable, @RequestBody QueryParam<FinancialAttachmentParam> pageParam) {
        final FinancialAttachmentParam param = pageParam.getParam();

        final IPage<FinancialAttachment> page = financialAttachmentService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param));
        IPage<FinancialAttachmentVo> accountAttachmentVoIPage = AccountAttachmentWrapper.build().pageVO(page);
        return R.data(PageUtils.result(accountAttachmentVoIPage));
    }


}
