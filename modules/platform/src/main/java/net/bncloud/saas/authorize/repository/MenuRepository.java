package net.bncloud.saas.authorize.repository;

import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.saas.authorize.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long>, JpaSpecificationExecutor<Menu> {

    /**
     * 根据菜单标题查询
     * @param title 菜单标题
     * @return /
     */
    Menu findByTitle(String title);

    /**
     * 根据组件名称查询
     * @param component 组件名称
     * @return /
     */
    Menu findByComponent(String component);



    /**
     * 根据菜单的 PID 查询
     * @return /
     */
//    List<Menu> findByParent(Menu p);

    /**
     * 查询顶级菜单
     * @return /
     */
//    List<Menu> findByParentIsNull();

    /**
     * 获取节点数量
     * @return /
     */
//    int countByParent(Menu p);



    List<Menu> findAllBySubjectType(SubjectType subjectType);

}
