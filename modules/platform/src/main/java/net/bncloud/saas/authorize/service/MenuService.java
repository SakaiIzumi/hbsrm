package net.bncloud.saas.authorize.service;

import com.google.common.collect.Lists;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.saas.authorize.domain.Menu;
import net.bncloud.saas.authorize.domain.QMenu;
import net.bncloud.saas.authorize.domain.User;
import net.bncloud.saas.authorize.repository.MenuRepository;
import net.bncloud.saas.authorize.repository.UserRepository;
import net.bncloud.saas.authorize.service.dto.MenuDTO;
import net.bncloud.saas.authorize.service.dto.MenuNodeDTO;
import net.bncloud.saas.authorize.service.mapstruct.MenuMapper;
import net.bncloud.saas.authorize.service.query.MenuQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    private final MenuMapper menuMapper;
    private final UserRepository userRepository;

    private final JPAQueryFactory queryFactory;
    private final QMenu qMenu = QMenu.menu;

    public Page<MenuDTO> pageQuery(MenuQuery query, Pageable pageable) {


        return null;
    }

    /*public List<MenuVo> buildMenus(List<MenuDTO> menuDtos) {
        List<MenuVo> list = new LinkedList<>();
        menuDtos.forEach(menuDTO -> {
                    if (menuDTO!=null){
                        List<MenuDTO> menuDtoList = menuDTO.getChildren();
                        MenuVo menuVo = new MenuVo();
                        menuVo.setName(ObjectUtil.isNotEmpty(menuDTO.getComponent())  ? menuDTO.getComponent() : menuDTO.getTitle());
                        // 一级目录需要加斜杠，不然会报警告
                        menuVo.setPath(menuDTO.getParentId() == null ? "/" + menuDTO.getRoute() :menuDTO.getRoute());
                        menuVo.setHidden(menuDTO.isHidden());
                        // 如果不是外链
                        if(menuDTO.getParentId() == null){
                            menuVo.setComponent(StringUtils.isEmpty(menuDTO.getComponent())?"Layout":menuDTO.getComponent());
                        }else if(!StringUtils.isEmpty(menuDTO.getComponent())){
                            menuVo.setComponent(menuDTO.getComponent());
                        }
                        menuVo.setMeta(new MenuMetaVo(menuDTO.getTitle(),menuDTO.getIcon()));
                        if(menuDtoList !=null && menuDtoList.size()!=0){
                            menuVo.setAlwaysShow(true);
                            menuVo.setRedirect("noredirect");
                            menuVo.setChildren(buildMenus(menuDtoList));
                            // 处理是一级菜单并且没有子菜单的情况
                        } else if(menuDTO.getParentId() == null){
                            MenuVo menuVo1 = new MenuVo();
                            menuVo1.setMeta(menuVo.getMeta());
                            menuVo1.setPath("index");
                            menuVo1.setName(menuVo.getName());
                            menuVo1.setComponent(menuVo.getComponent());
                            menuVo.setName(null);
                            menuVo.setMeta(null);
                            menuVo.setComponent("Layout");
                            List<MenuVo> list1 = new ArrayList<>();
                            list1.add(menuVo1);
                            menuVo.setChildren(list1);
                        }
                        list.add(menuVo);
                    }
                }
        );
        return list;
    }*/

    public Set<Menu> findByUser(Long currentUserId) {
        Optional<User> user = userRepository.findById(currentUserId);
        final Set<Menu> menus = new HashSet<>();
        user.ifPresent(u -> u.getRoles().forEach(role -> menus.addAll(role.getMenus())));
        return menus;
    }

    public List<MenuDTO> getUserMenus(Long userId) {
        Set<Menu> menus = findByUser(userId);
        return menuMapper.toDto(new ArrayList<>(menus));
    }

    public List<MenuDTO> getUserMenuTree() {
        return null;
    }

    public List<MenuDTO> queryAll(MenuQuery query) {
        com.querydsl.core.types.Predicate predicate = ExpressionUtils.anyOf();
        predicate = ExpressionUtils.and(predicate, qMenu.subjectType.eq(query.getSubjectType()));
        predicate = ExpressionUtils.and(predicate, qMenu.hidden.eq(false));
        List<Menu> menus = queryFactory.selectFrom(qMenu).where(predicate).fetch();
        List<MenuDTO> resList = new ArrayList<>();
        for (Menu menu : menus) {
            MenuDTO menuDTO = BeanUtil.copy(menu, MenuDTO.class);
            menuDTO.setParentId(menu.getParentId());
            resList.add(menuDTO);
        }
        return resList;
    }

    /**
     * 构建查询条件
     *
     * @param queryMenu
     * @return
     */
    private Specification<Menu> buildDictQuerySpecification(MenuQuery queryMenu) {
        return new Specification<Menu>() {
            private static final long serialVersionUID = 6100258546575874666L;

            @Override
            public Predicate toPredicate(Root<Menu> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (queryMenu == null) {
                    return predicate;
                }
                if (StringUtils.isNotBlank(queryMenu.getSysType().name())) {
                    predicate.getExpressions().add(cb.equal(root.get("sysType"), queryMenu.getSysType()));
                }
                return predicate;
            }
        };
    }


    public List<MenuDTO> buildTree(List<MenuDTO> menus) {
        List<MenuDTO> trees = new ArrayList<>();
        Set<Long> ids = new HashSet<>();
        for (MenuDTO menuDTO : menus) {
            ids.add(menuDTO.getId());
        }
        for (Iterator<MenuDTO> iterator = menus.iterator(); iterator.hasNext(); ) {
            MenuDTO menu = iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!ids.contains(menu.getParentId())) {
                recursionFn(menus, menu);
                trees.add(menu);
            }
        }

        if (trees.size() == 0) {
            trees = menus.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
        }

        return trees;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<MenuDTO> list, MenuDTO t) {
        // 得到子节点列表
        List<MenuDTO> childList = getChildList(list, t);
        t.setChildren(childList);
        for (MenuDTO tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<MenuDTO> getChildList(List<MenuDTO> list, MenuDTO t) {
        List<MenuDTO> tlist = new ArrayList<MenuDTO>();
        Iterator<MenuDTO> it = list.iterator();
        while (it.hasNext()) {
            MenuDTO n = it.next();
            if (n.getParentId() != null) {
                if (n.getParentId().equals(t.getId())) {
                    tlist.add(n);
                }
            }

        }
        return tlist;
    }


    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<MenuDTO> list, MenuDTO t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }


    public void createMenu(Menu resources) {
        if (menuRepository.findByTitle(resources.getTitle()) != null) {
            throw new IllegalArgumentException("菜单标题重复");
        }
        if (StringUtils.isNotBlank(resources.getComponent())) {
            if (menuRepository.findByComponent(resources.getComponent()) != null) {
                throw new IllegalArgumentException("组件重复");
            }
        }

        menuRepository.save(resources);
    }

    public void updateMenu(Menu resources) {
        if (resources.getParent() != null && resources.getId().equals(resources.getParent().getId())) {
            throw new IllegalArgumentException("上级不能为自己");
        }
        Menu menu = menuRepository.findById(resources.getId()).orElseGet(Menu::new);

        Menu menu1 = menuRepository.findByTitle(resources.getTitle());

        if (menu1 != null && !menu1.getId().equals(menu.getId())) {
            throw new IllegalArgumentException("菜单标题重复");
        }

        if (StringUtils.isNotBlank(resources.getComponent())) {
            menu1 = menuRepository.findByComponent(resources.getComponent());
            if (menu1 != null && !menu1.getId().equals(menu.getId())) {
                throw new IllegalArgumentException("组件重复");
            }
        }
        menu.setTitle(resources.getTitle());
        menu.setComponent(resources.getComponent());
        menu.setRoute(resources.getRoute());
        menu.setIcon(resources.getIcon());
        menu.setParent(resources.getParent());
        menu.setOrder(resources.getOrder());
        menu.setHidden(resources.isHidden());
        menu.setComponent(resources.getComponent());
        menu.setMenuType(resources.getMenuType());
        menu.setPerm(resources.getPerm());
        menuRepository.save(menu);
    }

    public void deleteById(Long id) {

        // TODO not support
    }

    // ********************
    public List<MenuNodeDTO> menuTreeBySubjectType(SubjectType subjectType) {
        List<Menu> menus = menuRepository.findAllBySubjectType(subjectType);
        List<MenuNodeDTO> source = menus.stream().filter(menu -> !menu.isHidden()).map(menu -> MenuNodeDTO.builder()
                .id(menu.getId())
                .name(menu.getTitle())
                .parentId(menu.getParentId())
                .children(Lists.newArrayList()).build()
        ).collect(Collectors.toList());
        ArrayList<MenuNodeDTO> returnList = Lists.newArrayList();
        List<Long> tempList = source.stream().map(MenuNodeDTO::getId).collect(Collectors.toList());

        for (MenuNodeDTO menuNodeDTO : source) {
            if (!tempList.contains(menuNodeDTO.getParentId())) {
                //菜单递归
                recursion(source, menuNodeDTO);
                returnList.add(menuNodeDTO);
            }
        }
        return returnList;
    }

    private void recursion(List<MenuNodeDTO> source, MenuNodeDTO parent) {
        // 得到子节点列表
        List<MenuNodeDTO> childList = getChildList(source, parent);
        parent.setChildren(childList);
        for (MenuNodeDTO tChild : childList) {
            if (hasChild(source, tChild)) {
                recursion(source, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<MenuNodeDTO> getChildList(List<MenuNodeDTO> list, MenuNodeDTO t) {
        List<MenuNodeDTO> tlist = new ArrayList<MenuNodeDTO>();
        Iterator<MenuNodeDTO> it = list.iterator();
        while (it.hasNext()) {
            MenuNodeDTO n = (MenuNodeDTO) it.next();
            if (n.getParentId() != null) {
                if (n.getParentId().longValue() == t.getId().longValue()) {
                    tlist.add(n);
                }
            }
        }
        return tlist;
    }

    private boolean hasChild(List<MenuNodeDTO> list, MenuNodeDTO t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }
}
