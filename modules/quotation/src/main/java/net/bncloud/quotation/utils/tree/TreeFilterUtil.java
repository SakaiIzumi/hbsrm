package net.bncloud.quotation.utils.tree;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lijiaju
 * @date 2022/3/14
 *
 * 过滤hutool TreeUtil生成的树
 * I id的类型
 */
public class TreeFilterUtil<T extends Tree<I>,I> {


    /**
     * 获取树状结构
     *
     * @param treeList 搜索的树列表
     * @param hitIds         命中的ids
     * @return
     */
    public List<T> searchNode(List<T> treeList, List<I> hitIds) {
        List<T> filterTree = Lists.newArrayList();
        for (T t : treeList) {
            T node = filterTree(t, hitIds);
            filterTree.add(node);
        }
        return filterTree.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 过滤树
     *
     * @param tree
     * @param hitIds
     * @return
     */
    private T filterTree(T tree, List<I> hitIds) {
        if (isRemoveNode(tree, hitIds)) {
            return null;
        }
        if(CollectionUtil.isEmpty(tree.getChildren())){
            return tree;
        }
        Iterator<T> iterator = (Iterator<T>) tree.getChildren().iterator();
        while (iterator.hasNext()) {
            T child = iterator.next();
            deleteNode(child, iterator, hitIds);
        }
        return tree;
    }

    /**
     * 删除节点
     *
     * @param child
     * @param iterator
     * @param hitIds
     */
    private void deleteNode(T child, Iterator<T> iterator, List<I> hitIds) {
        if (isRemoveNode(child, hitIds)) {
            iterator.remove();
            return;
        }
        List<T> childrenList = (List<T>) child.getChildren();
        if (CollectionUtil.isEmpty(childrenList)) {
            return;
        }
        Iterator<T> children = childrenList.iterator();
        while (children.hasNext()) {
            T childChild = children.next();
            deleteNode(childChild, children, hitIds);
        }
    }

    /**
     * 判断该节点是否该删除
     *
     * @param root
     * @param hitIds 命中的节点
     * @return ture 需要删除  false 不能被删除
     */
    private boolean isRemoveNode(T root, List<I> hitIds) {
        List<T> children = (List<T>) root.getChildren();
        // 叶子节点
        if (CollectionUtil.isEmpty(children)) {
            return !hitIds.contains(root.getId());
        }
        // 子节点
        if (hitIds.contains(root.getId())) {
            return false;
        }
        boolean bool = true;
        for (T child : children) {
            if (!isRemoveNode(child, hitIds)) {
                bool = false;
                break;
            }
        }
        return bool;
    }
}