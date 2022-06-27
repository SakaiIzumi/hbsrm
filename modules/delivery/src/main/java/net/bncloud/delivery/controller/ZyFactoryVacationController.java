package net.bncloud.delivery.controller;

import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.delivery.config.DownloadFactoryVacationModel;
import net.bncloud.delivery.enums.FactoryBelongTypeEnum;
import net.bncloud.delivery.param.FactoryVacationParam;
import net.bncloud.delivery.service.FactoryVacationService;
import net.bncloud.delivery.vo.FactoryVacationImportVo;
import net.bncloud.delivery.vo.FactoryVacationVo;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author ddh
 * @description
 * @since 2022/5/18
 */
@RestController
@RequestMapping("/zy/factory-vacation")
public class ZyFactoryVacationController {

    private final FactoryVacationService factoryVacationService;
    private final DownloadFactoryVacationModel downloadFactoryVacationModel;
    public ZyFactoryVacationController(FactoryVacationService factoryVacationService,DownloadFactoryVacationModel downloadFactoryVacationModel) {
        this.factoryVacationService = factoryVacationService;
        this.downloadFactoryVacationModel = downloadFactoryVacationModel;
    }

    /**
     * 分页查询工厂假期
     *
     * @param pageable
     * @param queryParam
     * @return
     */
    @PostMapping("/page")
    public R<PageImpl<FactoryVacationVo>> page(Pageable pageable, @RequestBody QueryParam<FactoryVacationParam> queryParam) {
        //数据过滤
        @Valid FactoryVacationParam param = queryParam.getParam();
        if (param!=null){
            param.setBelongType(FactoryBelongTypeEnum.SUPPLIER.getCode());
            SecurityUtils.getCurrentSupplier().ifPresent(supplier -> {
                param.setBelongCode(supplier.getSupplierCode());
            });
        }
        return R.data(PageUtils.result(factoryVacationService.selectListPage(PageUtils.toPage(pageable), queryParam)));
    }

    /**
     * 获取附件模板id
     *
     * @return
     */
    @GetMapping("/getModelId")
    public R<Long> getModelId() {
        return R.data(downloadFactoryVacationModel.getSupModelId());
    }

    /**
     * 导入节假日发生冲突提示接口
     *
     * @param file
     * @return
     */
    @PostMapping("/importVacationConflictMessage")
    public R<FactoryVacationImportVo> importVacationConflictMessage(@RequestParam("file") MultipartFile file, @RequestParam("workBench")String workBench) {
        FactoryVacationImportVo factoryVacationImportVo = factoryVacationService.importSupplierVacationConflictMessage(file,workBench);
        return R.data(factoryVacationImportVo);
    }


}
