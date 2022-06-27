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
import net.bncloud.saas.supplier.domain.Supplier;
import net.bncloud.saas.supplier.repository.SupplierStaffRepository;
import net.bncloud.saas.supplier.service.SupplierService;
import net.bncloud.saas.supplier.service.SupplierStaffService;
import net.bncloud.saas.supplier.service.query.SupplierStaffQuery;
import net.bncloud.saas.user.strategy.selector.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("supplierRelOrgSelectorStrategy")
@Slf4j
@AllArgsConstructor
public class SupplierRelOrgSelectorStrategy implements ISelectorStrategy {

    private final SupplierStaffService supplierStaffService;

    private final SupplierService supplierService;

    private final SupplierStaffRepository supplierStaffRepository;



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
            List<Supplier> suppliers = supplierService.queryRelateSuppliers();
            List<TreeNode<Object>> nodeList = CollUtil.newArrayList();
            suppliers.forEach(org -> {
                nodeList.add(new TreeNode<>(org.getId().toString(), "0", org.getName(), null));
            });
            List<Tree<Object>> build = TreeUtil.build(nodeList, "0");
            return build;
        }
        return Lists.newArrayList();
    }

    @Override
    public Page<?> getTableData(TableQuery query, Pageable pageable) {
        SupplierStaffQuery supplierStaffQuery = new SupplierStaffQuery();
        QueryParam<SupplierStaffQuery> queryParam = new QueryParam<>();
        String id = query.getId();
        if (StrUtil.isNotEmpty(id)) {
            Long supplierId = NumberUtil.toLong(query.getId());
            supplierStaffQuery.setSupplierId(supplierId);
        }
        supplierStaffQuery.setName(query.getName());
        queryParam.setParam(supplierStaffQuery);
        return (Page<?>) supplierStaffService.supplierStaffTableByOrgId(queryParam, pageable);
    }

    @Override
    public List<?> getDataEcho(DataEchoQuery query) {
        List<Long> ids = query.getIds().stream().map(NumberUtil::toLong).collect(Collectors.toList());
        return supplierStaffRepository.findAllById(ids);
    }
}
