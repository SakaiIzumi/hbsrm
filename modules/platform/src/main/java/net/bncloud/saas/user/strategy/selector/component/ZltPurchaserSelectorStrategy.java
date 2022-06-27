package net.bncloud.saas.user.strategy.selector.component;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.Org;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.util.NumberUtil;
import net.bncloud.saas.purchaser.domain.Purchaser;
import net.bncloud.saas.purchaser.repository.PurchaserStaffRepository;
import net.bncloud.saas.purchaser.service.PurchaserService;
import net.bncloud.saas.purchaser.service.PurchaserStaffService;
import net.bncloud.saas.purchaser.service.query.PurchaserStaffQuery;
import net.bncloud.saas.user.strategy.selector.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("zltPurchaserSelectorStrategy")
@Slf4j
@AllArgsConstructor
public class ZltPurchaserSelectorStrategy implements ISelectorStrategy {


    private final PurchaserService purchaserService;
    private final PurchaserStaffService purchaserStaffService;
    private final PurchaserStaffRepository purchaserStaffRepository;


    @Override
    public List<Tree<Object>> getTreeData(SideTreeQuery condition) {
        Long id = null;
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        Org currentOrg = loginInfo.getCurrentOrg();
        if (StrUtil.isEmpty(condition.getParentId())) {
            if (currentOrg != null) {
                id = currentOrg.getId();
            }
        } else {
            id = NumberUtil.toLong(condition.getParentId());
            if (!id.equals(currentOrg.getId())) {
                return Lists.newArrayList();
            }
        }
        if (id != null) {
            List<Purchaser> purchasers = purchaserService.queryRelatePurchasers();
            List<TreeNode<Object>> nodeList = CollUtil.newArrayList();
            purchasers.forEach(org -> {
                nodeList.add(new TreeNode<>(org.getId().toString(), "0", org.getName(), null));
            });
            List<Tree<Object>> build = TreeUtil.build(nodeList, "0");
            return build;
        }
        return Lists.newArrayList();
    }

    @Override
    public Page<?> getTableData(TableQuery query, Pageable pageable) {
        PurchaserStaffQuery purchaserStaffQuery = new PurchaserStaffQuery();
        QueryParam<PurchaserStaffQuery> queryParam = new QueryParam<>();
        String id = query.getId();
        if (StrUtil.isNotEmpty(id)) {
            Long purchaserId = NumberUtil.toLong(query.getId());
            purchaserStaffQuery.setPurchaserId(purchaserId);
        }
        purchaserStaffQuery.setName(query.getName());
        queryParam.setParam(purchaserStaffQuery);
        return (Page<?>) purchaserStaffService.purchaserStaffTableByOrgId(queryParam, pageable);
    }

    @Override
    public List<?> getDataEcho(DataEchoQuery query) {
        List<Long> ids = query.getIds().stream().map(NumberUtil::toLong).collect(Collectors.toList());
        return purchaserStaffRepository.findAllById(ids);
    }
}
