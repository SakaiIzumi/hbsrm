package net.bncloud.quotation.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.ApiOperation;
import net.bncloud.api.feign.file.FileInfo;
import net.bncloud.base.BaseEntity;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.quotation.entity.QuotationAttRequire;
import net.bncloud.quotation.entity.QuotationAttRequireAttachment;
import net.bncloud.quotation.manager.QuotationSupplierAlert;
import net.bncloud.quotation.param.QuotationAttRequireParam;
import net.bncloud.quotation.param.SupplierAttRequireParam;
import net.bncloud.quotation.service.IQuotationAttRequireAttachmentService;
import net.bncloud.quotation.service.QuotationAttRequireService;
import net.bncloud.quotation.vo.QuotationAttRequireVo;
import net.bncloud.quotation.vo.SupplierAttachmentRequireVo;
import net.bncloud.quotation.wrapper.QuotationAttRequireWrapper;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.List;


/**
 * 附件需求清单 前端控制器
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@RestController
@RequestMapping("/quotation-att-require")
public class QuotationAttRequireController {


    @Autowired
    private QuotationAttRequireService quotationAttRequireService;
    @Autowired
    private QuotationSupplierAlert quotationSupplierAlert;

    private final IQuotationAttRequireAttachmentService attRequireAttachmentService;

    public QuotationAttRequireController(IQuotationAttRequireAttachmentService attRequireAttachmentService) {
        this.attRequireAttachmentService = attRequireAttachmentService;
    }

    /**
     * 通过id查询
     */
    @GetMapping("{id}/getById")
    @ApiOperation(value = "根据ID查询", notes = "传入QuotationAttRequire")
    public R<QuotationAttRequire> getById(@PathVariable(value = "id") Long id) {
        return R.data(quotationAttRequireService.getById(id));
    }

    /**
     * 附件需求清单新增
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入QuotationAttRequire")
    public R save(@RequestBody @Validated QuotationAttRequire quotationAttRequire) {
        quotationAttRequireService.save(quotationAttRequire);
        return R.success();
    }

    /**
     * 附件需求清单删除
     */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入id")
    public R delete(@PathVariable(value = "id") String id) {
        quotationAttRequireService.removeById(Long.parseLong(id));
        return R.success();
    }

    /**
     * 附件需求清单修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入quotationAttRequire")
    public R updateById(@RequestBody @Validated({Default.class, BaseEntity.Update.class}) QuotationAttRequire quotationAttRequire) {
        quotationAttRequireService.updateById(quotationAttRequire);
        return R.success();
    }


    /**
     * 附件需求清单查询列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入quotationAttRequire")
    public R<List<QuotationAttRequire>> list(@RequestBody QuotationAttRequire quotationAttRequire) {
        List<QuotationAttRequire> list = quotationAttRequireService.list(Condition.getQueryWrapper(quotationAttRequire));

        return R.data(list);
    }

    /**
     * 查询当前询价单附件需求清单列表
     */
    @PostMapping("/list/{quotationBaseId}")
    @ApiOperation(value = "查询当前询价单附件需求清单列表", notes = "传入询价单ID")
    public R<PageImpl<QuotationAttRequireVo>> list(@PathVariable(value = "quotationBaseId", required = true) String quotationBaseId, Pageable pageable) {
//        List<QuotationAttRequire> list = quotationAttRequireService.list(Wrappers.<QuotationAttRequire>lambdaQuery().eq(QuotationAttRequire::getQuotationBaseId,quotationBaseId));

        final IPage<QuotationAttRequire> quotationAttRequireIPage = quotationAttRequireService.page(PageUtils.toPage(pageable), Wrappers.<QuotationAttRequire>lambdaQuery().eq(QuotationAttRequire::getQuotationBaseId, quotationBaseId));
        IPage<QuotationAttRequireVo> pageVO = QuotationAttRequireWrapper.build().pageVO(quotationAttRequireIPage);
        return R.data(PageUtils.result(pageVO));
    }

    /**
     * 查询供应商的附件需求清单
     *
     * @param pageable
     * @param queryParam
     * @return
     */
    @PostMapping("/supplierAttRequireAttachment/page")
    public R<PageImpl<SupplierAttachmentRequireVo>> getSupplierAttachmentRequirePage(Pageable pageable, @RequestBody @Validated @Valid QueryParam<SupplierAttRequireParam> queryParam) {
        IPage<SupplierAttachmentRequireVo> iPage = quotationAttRequireService.selectSupplierAttRequirePage(PageUtils.toPage(pageable), queryParam);
        iPage.getRecords().forEach(record -> {
            //附件列表
            List<QuotationAttRequireAttachment> attRequireAttachments = attRequireAttachmentService.list(Wrappers.<QuotationAttRequireAttachment>lambdaQuery()
                    .eq(QuotationAttRequireAttachment::getQuotationBaseId, record.getQuotationBaseId())
                    .eq(QuotationAttRequireAttachment::getQuotationAttRequireId, record.getId())
                    .eq(QuotationAttRequireAttachment::getSupplierId, record.getSupplierId()));
            //转换成FileInfo
            ArrayList<FileInfo> fileInfos = new ArrayList<>();
            attRequireAttachments.forEach(attachment -> {
                FileInfo fileInfo = new FileInfo().setId(attachment.getFileId())
                        .setFilename(attachment.getFileName())
                        .setOriginalFilename(attachment.getFileName())
                        .setUrl(attachment.getFileUrl())
                        .setContentType(attachment.getFileType());
                fileInfos.add(fileInfo);
            });
            record.setAttRequireAttachments(fileInfos);
        });
        return R.data(PageUtils.result(iPage));
    }

    /**
     * 分页查询
     */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入QuotationAttRequireParam")
    public R<PageImpl<QuotationAttRequireVo>> page(Pageable pageable, @RequestBody QueryParam<QuotationAttRequireParam> pageParam) {
        final QuotationAttRequireParam param = pageParam.getParam();

        final IPage<QuotationAttRequire> page = quotationAttRequireService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param));
        IPage<QuotationAttRequireVo> quotationAttRequireVoIPage = QuotationAttRequireWrapper.build().pageVO(page);
        return R.data(PageUtils.result(quotationAttRequireVoIPage));
    }

    /**
     * 测试
     */
    @GetMapping("/test")
    public void test() {
        quotationSupplierAlert.earlyWarningMethod();
    }


}
