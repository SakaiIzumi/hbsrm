package net.bncloud.delivery.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.pageable.PageUtils;
import net.bncloud.delivery.config.DownloadFactoryVacationModel;
import net.bncloud.delivery.param.AutoSubscribeParam;
import net.bncloud.delivery.param.FactoryVacationParam;
import net.bncloud.delivery.param.FactoryVacationSetParam;
import net.bncloud.delivery.service.FactoryVacationService;
import net.bncloud.delivery.vo.FactoryVacationImportVo;
import net.bncloud.delivery.vo.FactoryVacationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;


/**
 * 收货/送货工厂 节假日 controller
 *
 * @author liyh
 * @since 2022-05-16
 */
@Validated
@Slf4j
@RestController
@RequestMapping("/zc/factory-vacation")
@Api(tags = "货/送货工厂 节假日 controller")
public class ZcFactoryVacationController {


    @Autowired
    private FactoryVacationService factoryVacationService;
    @Resource
    private DownloadFactoryVacationModel downloadFactoryVacationModel;

    /**
     * 分页查询
     *
     * 接口支持采购方的  采购节假日查询的tab和销售的tab
     *
     * 销售端是反过来的,在zy那边的controller
     *
     * belongType 标记类型
     *
     * 但是业务层方法用的是同一个
     *
     * 注意采购/销售不同tab的按钮控制
     *
     * 注意高查
     *
     * 所有假期数据都有开启和关闭状态
     */
    @PostMapping("/page")
    @ApiOperation(value = "分页查询列表", notes = "传入DeliveryDetailParam")
    public R<PageImpl<FactoryVacationVo>> page(Pageable pageable, @RequestBody QueryParam<FactoryVacationParam> queryParam) {
        IPage<FactoryVacationVo> factoryVacationVoIPage = factoryVacationService.selectListPage(PageUtils.toPage(pageable), queryParam);
        return R.data(PageUtils.result(factoryVacationVoIPage));
    }


    /**
     * 删除  (供应/采购都适用)
     *
     *
     * @param ids
     * @return
     */
    @PostMapping("/batchDelete")
    public R<String> batchDelete(@RequestBody List<Long> ids) {
        factoryVacationService.batchDeleteVacation(ids);
        return R.success("操作成功");
    }

    /**
     * 保存 (供应方和采购通用)
     *
     * @return
     */
    @PostMapping("/save")
    public R<String> vacationSave(@Valid @RequestBody FactoryVacationSetParam param) {
        factoryVacationService.vacationSave(param);
        return R.success("操作成功");
    }

    /**
     * 提示是否确认覆盖接口 (供应方和采购通用)
     *
     * @return
     */
    @PostMapping("/confirmCover")
    public R<Boolean> confirmCover(@RequestBody FactoryVacationSetParam param) {
        Boolean flag=factoryVacationService.confirmCover(param);
        return R.data(flag);
    }

    /**
     * 编辑  (供应方和采购通用)
     *
     * @return
     */
    @PutMapping("/update")
    public R<String> vacationUpdate(@RequestBody FactoryVacationSetParam param) {
        factoryVacationService.vacationUpdate(param);
        return R.success("操作成功");
    }

    /**
     * 假期按钮操作
     *
     * 传该假期的id  和开关的状态
     *
     * @return
     */
    @PostMapping("/vacationButton")
    public R<String> vacationButton(@RequestBody FactoryVacationParam param) {
        factoryVacationService.vacationButton(param);
        return R.success("操作成功");
    }

    /**
     * 采购配置修改 修改假日接口
     *
     * 开启订阅,会同步法定节假日的数据
     *
     * 如果是关闭,不需要删除这些节假日数据,这里可能会有疑惑,保留数据,那么用户怎么知道今天是不是放假
     *
     * 根据产品意思  订阅只是订阅功能(取数据)  假期表不是表示是否放假,只是展示数据作用
     *
     * 所以,只是展示数据,当然可以不删除
     *
     * @return
     */
    @PostMapping("/autoSubscribe")
    public R<String> configVacation(@RequestBody AutoSubscribeParam param) {
        factoryVacationService.autoVacation(param);
        return R.success("操作成功");
    }


    /**
     * 获取附件模板id
     *
     * @return
     */
    @GetMapping("/getModelId")
    public R<Long> getModelId() {
        return R.data(downloadFactoryVacationModel.getModelId());
    }

    /**
     * 导入节假日
     *
     * @return
     */
    @PostMapping("/importVacation")
    public R<String> importVacation( @RequestBody FactoryVacationImportVo vo) {
        factoryVacationService.importVacation(vo);
        return R.success("操作成功");
    }

    /**
     * 导入节假日发生冲突提示接口
     *
     * @param file
     * @return
     */
    @PostMapping("/importVacationConflictMessage")
    public R<FactoryVacationImportVo> importVacationConflictMessage(@RequestParam("file") MultipartFile file,@RequestParam("workBench")String workBench) {
        FactoryVacationImportVo factoryVacationImportVo = factoryVacationService.importVacationConflictMessage(file,workBench);
        return R.data(factoryVacationImportVo);
    }


}
