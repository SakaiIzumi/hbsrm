package net.bncloud.saas.user.strategy.selector;

import cn.hutool.core.lang.tree.Tree;
import net.bncloud.common.exception.ApiException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 请在子类赋予名字,没有名字的子类,无法添加到策略工厂
 */
public abstract class MemberSelectorStrategy {

    public abstract String getName();

    public List<Tree<Object>> getTreeData(SideTreeQuery query) {
        throw new ApiException(500, "需子类实现此方法");
    }

    public Page<?> getTableData(TableQuery query, Pageable pageable) {
        throw new ApiException(500, "需子类实现此方法");
    }

    public List<?> getDataEcho(DataEchoQuery query) {
        throw new ApiException(500, "需子类实现此方法");
    }
}
