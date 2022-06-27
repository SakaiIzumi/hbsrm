package net.bncloud.quotation.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.quotation.entity.QuotationAttRequire;
import net.bncloud.quotation.service.QuotationAttRequireService;
import net.bncloud.quotation.vo.QuotationAttRequireAttachmentSaveVo;
import net.bncloud.quotation.vo.QuotationAttRequireVo;
import net.bncloud.service.api.file.dto.FileInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.support.Condition;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import net.bncloud.common.pageable.PageUtils;

import net.bncloud.quotation.service.IQuotationAttRequireAttachmentService;
import net.bncloud.quotation.entity.QuotationAttRequireAttachment;
import net.bncloud.quotation.param.QuotationAttRequireAttachmentParam;
import net.bncloud.quotation.vo.QuotationAttRequireAttachmentVo;
import net.bncloud.quotation.wrapper.QuotationAttRequireAttachmentWrapper;

import java.util.*;


/**
 * 附件需求上传文件表
 *
 * @author Auto-generator
 * @since 2022-03-04
 */
@RestController
@RequestMapping("/quotation-att-require-attachment")
public class QuotationAttRequireAttachmentController {


    @Autowired
    private IQuotationAttRequireAttachmentService iQuotationAttRequireAttachmentService;
    @Autowired
    private QuotationAttRequireService quotationAttRequireService;


    /**
     * 通过id查询
     */
    @GetMapping("/getById")
    @ApiOperation(value = "根据ID查询", notes = "传入QuotationAttRequireAttachment")
    public R<QuotationAttRequireAttachment> getById(@PathVariable(value = "id") Long id) {
        return R.data(iQuotationAttRequireAttachmentService.getById(id));
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增", notes = "传入QuotationAttRequireAttachment")
    public R save(@RequestBody QuotationAttRequireAttachment quotationAttRequireAttachment) {
        iQuotationAttRequireAttachmentService.save(quotationAttRequireAttachment);
        return R.success();
    }

    /**
     * 通过id删除
     */
    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "通过id删除", notes = "传入ids")
    public R delete(@PathVariable(value = "id") String ids) {
        String[] idsStrs = ids.split(",");
        for (String id : idsStrs) {
            iQuotationAttRequireAttachmentService.removeById(Long.parseLong(id));
        }
        return R.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改", notes = "传入quotationAttRequireAttachment")
    public R updateById(@RequestBody QuotationAttRequireAttachment quotationAttRequireAttachment) {
        iQuotationAttRequireAttachmentService.updateById(quotationAttRequireAttachment);
        return R.success();
    }


    /**
     * 查询列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "查询列表", notes = "传入quotationAttRequireAttachment")
    public R list(@RequestBody QuotationAttRequireAttachment quotationAttRequireAttachment) {
        List<QuotationAttRequireAttachment> list = iQuotationAttRequireAttachmentService.list(Condition.getQueryWrapper(quotationAttRequireAttachment));

        return R.data(list);
    }

    /**
     * 分页查询
     */
    @PostMapping("/page")
    @ApiOperation(value = "查询列表", notes = "传入QuotationAttRequireAttachmentParam")
    public R page(Pageable pageable, @RequestBody QueryParam<QuotationAttRequireAttachmentParam> pageParam) {
        final QuotationAttRequireAttachmentParam param = pageParam.getParam();

        final IPage<QuotationAttRequireAttachment> page = iQuotationAttRequireAttachmentService.page(PageUtils.toPage(pageable), Condition.getQueryWrapper(param));
        IPage<QuotationAttRequireAttachmentVo> quotationAttRequireAttachmentVoIPage = QuotationAttRequireAttachmentWrapper.build().pageVO(page);
        return R.data(PageUtils.result(quotationAttRequireAttachmentVoIPage));
    }


    /**
     * 保存需求单文件
     * @return
     */
    @PostMapping("/saveFiles")
    public R<List<QuotationAttRequireVo>> uploadFiles(@RequestBody QuotationAttRequireAttachmentSaveVo quotationAttRequireAttachmentSaveVo) throws Exception {
        Long quotationAttRequireId =  quotationAttRequireAttachmentSaveVo.getQuotationAttRequireId();
        Long quotationId = quotationAttRequireAttachmentSaveVo.getQuotationId();
        List<FileInfoDto> fileInfoDtos = quotationAttRequireAttachmentSaveVo.getFileInfoDtos();
        List<QuotationAttRequireAttachment> quotationAttRequireAttachments = iQuotationAttRequireAttachmentService.saveFile(quotationId, quotationAttRequireId, fileInfoDtos);

        //重新取出供应商附件需求清单字段以及每个清单下的所有附件
        List<QuotationAttRequire> quotationAttRequireList = quotationAttRequireService.list(Wrappers.<QuotationAttRequire>lambdaQuery()
                .eq(QuotationAttRequire::getQuotationBaseId, quotationAttRequireAttachmentSaveVo.getQuotationId())
                .orderByAsc(QuotationAttRequire::getCreatedDate));
        List<QuotationAttRequireVo> quotationAttRequireVos = new ArrayList<>();
        for (int i = 0; i < quotationAttRequireList.size(); i++) {
            QuotationAttRequireVo quotationAttRequireVo = new QuotationAttRequireVo();
            BeanUtil.copyProperties(quotationAttRequireList.get(i), quotationAttRequireVo);
            //添加标号
            quotationAttRequireVo.setItemNo(i + 1);
            //添加附件列表
            quotationAttRequireVo.setQuotationAttRequireAttachmentVos(iQuotationAttRequireAttachmentService.getFilesByQuotationIdAndAttRequireId(quotationAttRequireVo.getQuotationBaseId(), quotationAttRequireVo.getId()));
            quotationAttRequireVos.add(quotationAttRequireVo);
        }

        return R.data(quotationAttRequireVos);
//        return R.success();
    }

    /**
     * 获取需求单上传过的文件列表
     * @param quotationId 询价单主键ID
     * @param quotationAttRequireId 附件需求清单主键ID
     * @return
     */
    @GetMapping("/getFilesByQuotationIdAndAttRequireId/{quotationId}/{quotationAttRequireId}")
    public R<List<QuotationAttRequireAttachmentVo>> getFilesByQuotationIdAndAttRequireId(@PathVariable("quotationId") Long quotationId, @PathVariable("quotationAttRequireId") Long quotationAttRequireId) {
        List<QuotationAttRequireAttachmentVo> filesByQuotationIdAndAttRequireId = iQuotationAttRequireAttachmentService.getFilesByQuotationIdAndAttRequireId(quotationId, quotationAttRequireId);
        return R.data(filesByQuotationIdAndAttRequireId);
    }

    /**
     * 删除需求附件清单文件
     * @param quotationAttRequireId 附件需求清单主键ID
     * @param quotationId 询价单主键ID
     * @return
     */
    @DeleteMapping("/deleteByAttRequireIdAndQuotationId/{quotationAttRequireId}/{quotationId}")
    public R<String> deleteByAttRequireId(@PathVariable("quotationAttRequireId") Long quotationAttRequireId,@PathVariable("quotationId") Long quotationId) {
        iQuotationAttRequireAttachmentService.deleteByAttRequireId(quotationAttRequireId,quotationId);
        return R.success();
    }

}
