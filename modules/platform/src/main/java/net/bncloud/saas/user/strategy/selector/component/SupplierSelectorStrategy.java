package net.bncloud.saas.user.strategy.selector.component;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.security.Supplier;
import net.bncloud.common.util.NumberUtil;
import net.bncloud.saas.supplier.repository.SupplierRepository;
import net.bncloud.saas.supplier.repository.SupplierStaffRepository;
import net.bncloud.saas.supplier.service.SupplierService;
import net.bncloud.saas.supplier.service.SupplierStaffService;
import net.bncloud.saas.supplier.service.query.SupplierStaffQuery;
import net.bncloud.saas.user.strategy.selector.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component("supplierSelectorStrategy")
@Slf4j
@AllArgsConstructor
public class SupplierSelectorStrategy implements ISelectorStrategy {

    private final SupplierStaffService supplierStaffService;

    private final SupplierService supplierService;

    private final SupplierRepository supplierRepository;

    private final SupplierStaffRepository supplierStaffRepository;



    @Override
    public List<Tree<Object>> getTreeData(SideTreeQuery condition) {
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        Supplier currentSupplier = loginInfo.getCurrentSupplier();

        ArrayList<Long> ids = Lists.newArrayList();
        if (condition.getParentId() == null) {
            if (currentSupplier != null) {
                ids.add(currentSupplier.getSupplierId());
            }
        } else {
            Long id = NumberUtil.toLong(condition.getParentId());
            if (currentSupplier != null) {
                if (id.equals(currentSupplier.getSupplierId())) {
                    return Lists.newArrayList();
                }
                ids.add(id);
            }
        }
        List<net.bncloud.saas.supplier.domain.Supplier> suppliers = supplierRepository.findAllById(ids);
        List<TreeNode<Object>> nodeList = CollUtil.newArrayList();
        suppliers.forEach(org -> {
            nodeList.add(new TreeNode<>(org.getId().toString(), "0", org.getName(), null));
        });
        List<Tree<Object>> build = TreeUtil.build(nodeList, "0");
        return build != null ? build : Lists.newArrayList();
    }

    @Override
    public Page<?> getTableData(TableQuery query, Pageable pageable) {
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        Supplier currentSupplier = loginInfo.getCurrentSupplier();
        SupplierStaffQuery supplierStaffQuery = new SupplierStaffQuery();
        QueryParam<SupplierStaffQuery> queryParam = new QueryParam<>();

        supplierStaffQuery.setSupplierId(currentSupplier.getSupplierId());
        supplierStaffQuery.setName(query.getName());
        queryParam.setParam(supplierStaffQuery);

        return supplierStaffService.supplierMemberTable(queryParam, pageable);
    }

    @Override
    public List<?> getDataEcho(DataEchoQuery query) {
        List<Long> ids = query.getIds().stream().map(NumberUtil::toLong).collect(Collectors.toList());
        return supplierStaffRepository.findAllById(ids);
    }
}
