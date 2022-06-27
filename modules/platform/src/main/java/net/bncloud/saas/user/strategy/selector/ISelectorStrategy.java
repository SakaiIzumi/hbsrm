package net.bncloud.saas.user.strategy.selector;

import cn.hutool.core.lang.tree.Tree;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ISelectorStrategy {

    List<Tree<Object>> getTreeData(SideTreeQuery query);

    Page<?> getTableData(TableQuery query, Pageable pageable);

    List<?> getDataEcho(DataEchoQuery query);
}
