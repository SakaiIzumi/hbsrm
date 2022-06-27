package net.bncloud.saas.sys.service;

import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.common.util.StringUtil;
import net.bncloud.saas.sys.domain.SettlementPoolRule;
import net.bncloud.saas.sys.domain.SettlementPoolRuleRel;
import net.bncloud.saas.sys.domain.vo.SettlementPoolRuleVO;
import net.bncloud.saas.sys.repository.SettlementPoolRuleRelRepository;
import net.bncloud.saas.sys.repository.SettlementPoolRuleRepository;
import net.bncloud.saas.utils.pageable.PageUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author ddh
 * @since 2021/12/30
 */
@Service
public class SettlementPoolRuleService {

    private final SettlementPoolRuleRepository poolRuleRepository;

    private final SettlementPoolRuleRelRepository poolRuleRelRepository;

    public SettlementPoolRuleService(SettlementPoolRuleRepository poolRuleRepository, SettlementPoolRuleRelRepository poolRuleRelRepository) {
        this.poolRuleRepository = poolRuleRepository;
        this.poolRuleRelRepository = poolRuleRelRepository;
    }


    /**
     * 查询结算池规则列表
     *
     * @param queryParam
     * @return
     */
    public Page<SettlementPoolRuleVO> getList(Pageable pageable,QueryParam<SettlementPoolRule> queryParam) {
        ArrayList<SettlementPoolRuleVO> voList = new ArrayList<>();
        SettlementPoolRule poolRule = queryParam.getParam();
        String searchValue = queryParam.getSearchValue();
        List<SettlementPoolRule> poolRuleList= poolRuleRepository.findAll(new Specification<SettlementPoolRule>() {
            @Override
            public Predicate toPredicate(Root<SettlementPoolRule> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                ArrayList<Predicate> list = new ArrayList<>();
                if (StringUtil.isNotBlank(poolRule.getBillType())) {
                    list.add(builder.equal(root.get("billType").as(String.class), poolRule.getBillType()));
                }
                if (StringUtil.isNotBlank(poolRule.getStatus())) {
                    list.add(builder.equal(root.get("status").as(String.class), poolRule.getStatus()));
                }

                if (StringUtil.isNotBlank(searchValue)) {
                    Predicate p1 = builder.like(root.get("ruleCode").as(String.class), "%" + searchValue + "%");
                    Predicate p2 = builder.like(root.get("ruleName").as(String.class), "%" + searchValue + "%");
                    list.add(builder.or(p1, p2));
                }
                query.where(list.toArray(new Predicate[list.size()]));
                return query.getRestriction();
            }
        });

        poolRuleList.forEach(s->{
            SettlementPoolRuleVO poolRuleVO = getLine(s.getId());
            voList.add(poolRuleVO);
        });


        return PageUtils.of(voList, pageable, 0L);
    }


    /**
     * 查询结算池规则明细
     *
     * @param ruleId
     * @return
     */

    public SettlementPoolRuleVO getLine(Long ruleId) {
        SettlementPoolRuleVO vo = new SettlementPoolRuleVO();
        Optional<SettlementPoolRule> poolRule = poolRuleRepository.findById(ruleId);
        poolRule.ifPresent(exist -> {
            BeanUtil.copy(exist, vo);
        });
        List<SettlementPoolRuleRel> poolRuleRels = poolRuleRelRepository.findBySettlementPoolRuleId(ruleId);
        vo.setList(poolRuleRels);
        return vo;
    }


    /**
     * 已有记录，更新；没有记录，保存。
     *
     * @param vo
     */
    public void saveOrUpdate(SettlementPoolRuleVO vo) {
        List<SettlementPoolRuleRel> voList = vo.getList();
        voList.forEach(s -> {
            s.setSettlementPoolRuleId(vo.getId());
            poolRuleRelRepository.save(s);
        });
    }

    /**
     * 批量修改
     *
     * @param ids
     * @param status
     */
    public void updateBatch(List<Long> ids, String status) {
        List<SettlementPoolRule> poolRules = poolRuleRepository.findAllById(ids);
        poolRules.forEach(s -> {
            s.setStatus(status);
            poolRuleRepository.save(s);
        });
    }
}
