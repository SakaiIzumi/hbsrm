package net.bncloud.quotation.utils.tree;

import cn.hutool.core.lang.tree.Tree;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lijiaju
 * @date 2022/3/14 11:11
 */
@Configuration
public class TreeFilterUtilConfig {
    @Bean
    public TreeFilterUtil<Tree<Long>,Long> treeFilterUtil(){
        return new TreeFilterUtil<>();
    }
}
