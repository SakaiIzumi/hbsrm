package net.bncloud.saas.sys.web;

import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.util.DateUtil;
import net.bncloud.saas.sys.domain.SettlementPoolRule;
import net.bncloud.saas.sys.domain.vo.SettlementPoolRuleVO;
import net.bncloud.saas.sys.service.SettlementPoolRuleService;
import org.apache.commons.lang.time.DateUtils;
import org.bouncycastle.jcajce.provider.symmetric.Noekeon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * 结算池规则设置
 *
 * @author ddh
 * @since 2021/12/30
 */
@RestController
@RequestMapping("/sys/settlement-pool-rule")
public class SettlementPoolRuleController {
    private final SettlementPoolRuleService poolRuleService;

    public SettlementPoolRuleController(SettlementPoolRuleService poolRuleService) {
        this.poolRuleService = poolRuleService;
    }


    /**
     * 查询结算池规则列表
     *
     * @param param
     * @return
     */
    @PostMapping("/getList")
    @ResponseBody
    public R<Page<SettlementPoolRuleVO>> getSettlementPoolRuleList(Pageable pageable, @RequestBody QueryParam<SettlementPoolRule> param) {
        return R.data(poolRuleService.getList(pageable,param));
    }

    /**
     * 查询结算池规则详情
     *
     * @return
     */
    @GetMapping("/getLine/{ruleId}")
    public R<SettlementPoolRuleVO> getSettlementPoolRuleLine(@PathVariable("ruleId") Long ruleId) {
        return R.data(poolRuleService.getLine(ruleId));
    }

    /**
     * 编辑结算池规则
     *
     * @param vo
     * @return
     */
    @PostMapping("/editSettlementPoolRule")
    public R editSettlementPoolRule(@RequestBody SettlementPoolRuleVO vo) {
        poolRuleService.saveOrUpdate(vo);
        return R.success();
    }

    /**
     * 批量修改
     *
     * @param ids    结算池规则的id集合
     * @param status true 或 false
     * @return 通用放回对象
     */
    @PostMapping("/updateBatch")
    public R updateBatch(@RequestParam("ids") List<Long> ids, @RequestParam("status") String status) {
        poolRuleService.updateBatch(ids, status);
        return R.success();
    }


}
