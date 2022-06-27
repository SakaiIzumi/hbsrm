package net.bncloud.saas.user.strategy.selector;

import cn.hutool.core.lang.tree.Tree;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class SideTreeSelectorStrategyContext {

    private final SideTreeSelectorStrategyFactory sideTreeSelectorStrategyFactory;

    public List<Tree<Object>> getTreeData(SideTreeQuery condition) {
        String name = condition.getType() + "SelectorStrategy";
        return sideTreeSelectorStrategyFactory.get(name).getTreeData(condition);
    }

    public Page<?> getTableData(TableQuery query, Pageable pageable) {
        String name = query.getType() + "SelectorStrategy";
        return sideTreeSelectorStrategyFactory.get(name).getTableData(query, pageable);
    }

    public List<?> getDataEcho(DataEchoQuery query) {
        String name = query.getType() + "SelectorStrategy";
        return sideTreeSelectorStrategyFactory.get(name).getDataEcho(query);
    }
}
