package net.bncloud.saas.sys.web;

import net.bncloud.common.api.R;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.saas.sys.domain.CfgParam;
import net.bncloud.saas.sys.service.CfgParamService;
import net.bncloud.service.api.platform.config.enums.CfgParamKeyEnum;
import net.bncloud.service.api.platform.sys.dto.MrpCfgDTO;
import net.bncloud.service.api.platform.sys.dto.SetSupplierConfigDTO;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/sys/cfg/param")
public class CfgParamResource {

    private final CfgParamService cfgParamService;

    public CfgParamResource(CfgParamService cfgParamService) {
        this.cfgParamService = cfgParamService;
    }

    @GetMapping("/companyParam")
    public R<List<CfgParam>> getCurrentCompanyParams() {
        final List<CfgParam> cfgParams = SecurityUtils.getCurrentCompany()
                .map(company -> cfgParamService.gatCompanyAllParam(company.getId()))
                .orElse(Collections.emptyList());
        return R.data(cfgParams);
    }

    /**
     * 协同配置
     * @return
     */
    @GetMapping("/list")
    public R<List<CfgParam>> getCurrentParams() {
        final List<CfgParam> cfgParams = SecurityUtils.getCurrentOrg()
                .map(org -> cfgParamService.gatAllParamByOrgId(org.getId()))
                .orElse(Collections.emptyList());
        return R.data(cfgParams);
    }

    @PostMapping("/save")
    public R<Void> save(@RequestBody CfgParam param) {
        cfgParamService.save(param);
        return R.success();
    }

    @GetMapping("/getByCode")
    public R<CfgParam> getParamByCode(@RequestParam(value = "code") String code) {
        return R.data(cfgParamService.getParamByCode(code));
    }

    @GetMapping("/findListByCode")
    public R<List<CfgParam>> findListByCode(@RequestParam(value = "code") String code) {
        return R.data(cfgParamService.findListByCode(code));
    }

    /**
     * 依据code和组织Id查询配置
     * @param cfgParamKeyEnum
     * @param orgId
     * @return
     */
    @GetMapping("/findListByCodeAndOrgId")
    public R<CfgParam> findListByCodeAndOrgId(@RequestParam(value = "cfgParamKeyEnum") CfgParamKeyEnum cfgParamKeyEnum, @RequestParam(value = "orgId")Long orgId) {
        CfgParam cfgParam = cfgParamService.findFirstByCodeAndOrgId(cfgParamKeyEnum.getCode(), orgId);
        return cfgParam != null ? R.data( cfgParam ) : R.fail("获取配置信息失败，请检查key！");
    }


    /**
     * 初始化新的供应商设置订阅节假日配置
     *
     * 这里首先前端调用平时的配置接口,检查现在登录的供应/采购编码是否在配置中
     *
     * 在 直接操作 也是调原来的配置接口  不在  需要调用这个接口进行初始化
     * @return
     */
    @PostMapping("/set")
    public R setConfig(@RequestBody SetSupplierConfigDTO dto){
        cfgParamService.saveConfig(dto);
        return R.success();
    }

    /**
     * 初始化新的供应商设置订阅节假日配置
     * @return
     */
    @PostMapping("/MrpAllConfig")
    public R<MrpCfgDTO> getAllSubscribeConfig(){
        MrpCfgDTO allSubscribeConfig = cfgParamService.getAllSubscribeConfig();
        return R.data(allSubscribeConfig);
    }

}
