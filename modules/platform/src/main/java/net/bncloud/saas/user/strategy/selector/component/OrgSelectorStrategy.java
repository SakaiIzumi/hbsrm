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
import net.bncloud.saas.tenant.domain.Organization;
import net.bncloud.saas.tenant.repository.OrgEmployeeRepository;
import net.bncloud.saas.tenant.repository.OrganizationRepository;
import net.bncloud.saas.tenant.service.OrgEmployeeService;
import net.bncloud.saas.tenant.service.query.OrgMemberQuery;
import net.bncloud.saas.user.strategy.selector.DataEchoQuery;
import net.bncloud.saas.user.strategy.selector.ISelectorStrategy;
import net.bncloud.saas.user.strategy.selector.SideTreeQuery;
import net.bncloud.saas.user.strategy.selector.TableQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component("orgSelectorStrategy")
@Slf4j
@AllArgsConstructor
public class OrgSelectorStrategy implements ISelectorStrategy {


    private final OrgEmployeeRepository orgEmployeeRepository;
    private final OrganizationRepository organizationRepository;

    private final OrgEmployeeService orgEmployeeService;

    @Override
    public List<Tree<Object>> getTreeData(SideTreeQuery condition) {
        //获取当前用户所在的组织
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        Org currentOrg = loginInfo.getCurrentOrg();
        ArrayList<Long> ids = Lists.newArrayList();
        if (StrUtil.isEmpty(condition.getParentId())) {
            ids.add(currentOrg.getId());
        } else {
            Long id = NumberUtil.toLong(condition.getParentId());
            if (id.equals(currentOrg.getId())) {
                return TreeUtil.build(Lists.newArrayList(), "0");
            }
            ids.add(id);
            ids.add(NumberUtil.toLong(condition.getParentId()));
        }
        List<Organization> organizations = organizationRepository.findAllById(ids);
        List<TreeNode<Object>> nodeList = CollUtil.newArrayList();
        organizations.forEach(org -> {
            nodeList.add(new TreeNode<>(org.getId().toString(), "0", org.getName(), null));
        });
        List<Tree<Object>> build = TreeUtil.build(nodeList, "0");
        return build != null ? build : Lists.newArrayList();
    }

    @Override
    public Page<?> getTableData(TableQuery query, Pageable pageable) {
        Long orgId = NumberUtil.toLong(query.getParentId());
        OrgMemberQuery orgMemberQuery = new OrgMemberQuery();

        orgMemberQuery.setOrgId(orgId);
        orgMemberQuery.setName(query.getName());

        QueryParam<OrgMemberQuery> queryParam = new QueryParam<>();
        queryParam.setSearchValue( query.getQs() );
        queryParam.setParam(orgMemberQuery);
        return orgEmployeeService.orgMemberTable(queryParam, pageable);
    }

    @Override
    public List<?> getDataEcho(DataEchoQuery query) {
        List<Long> ids = query.getIds().stream().map(NumberUtil::toLong).collect(Collectors.toList());
        return orgEmployeeRepository.findAllById(ids);
    }
}
