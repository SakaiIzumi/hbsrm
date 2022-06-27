package net.bncloud.saas.authorize.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import net.bncloud.api.feign.saas.user.RoleGroupType;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.base.service.BaseService;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.security.*;
import net.bncloud.common.util.ObjectUtil;
import net.bncloud.common.util.StringUtil;
import net.bncloud.enums.DimensionType;
import net.bncloud.saas.authorize.domain.*;
import net.bncloud.saas.authorize.domain.Role;
import net.bncloud.saas.authorize.domain.vo.DimensionGrantVO;
import net.bncloud.saas.authorize.repository.*;
import net.bncloud.saas.authorize.service.command.GrantMenuToRoleCommand;
import net.bncloud.saas.authorize.service.command.GrantRoleToUsers;
import net.bncloud.saas.authorize.service.dto.*;
import net.bncloud.saas.authorize.service.mapstruct.*;
import net.bncloud.saas.authorize.service.query.RoleQuery;
import net.bncloud.saas.authorize.domain.DimensionPerm;
import net.bncloud.saas.authorize.repository.DimensionPermRepository;
import net.bncloud.saas.event.RoleBatch;
import net.bncloud.saas.event.RoleBatchGrantEvent;
import net.bncloud.saas.event.RoleBatchRemoveEvent;
import net.bncloud.saas.supplier.domain.QSupplierStaff;
import net.bncloud.saas.supplier.service.dto.SupplierStaffDTO;
import net.bncloud.saas.sys.domain.vo.RolePermVO;
import net.bncloud.saas.tenant.domain.OrgEmployeeMangeScope;
import net.bncloud.saas.tenant.domain.QOrgEmployee;
import net.bncloud.saas.tenant.domain.vo.BatchRoleMenuVO;
import net.bncloud.saas.tenant.domain.vo.BatchUserRoleVO;
import net.bncloud.saas.tenant.domain.vo.RoleCopyVO;
import net.bncloud.saas.tenant.repository.OrgEmployeeManageScopeRepository;
import net.bncloud.saas.tenant.service.dto.OrgEmployeeDTO;
import net.bncloud.saas.tenant.service.dto.OrgEmployeeMangeScopeDTO;
import net.bncloud.saas.tenant.service.query.UserRoleQuery;
import net.bncloud.saas.tenant.web.param.OrgEmployeeMangeScopeListParam;
import net.bncloud.saas.tenant.web.param.OrgEmployeeMangeScopeParam;
import net.bncloud.saas.tenant.web.param.OrgEmployeeMangeUserIdsListAndScopeParam;
import net.bncloud.saas.utils.pageable.PageUtils;
import net.bncloud.service.api.platform.tenant.enums.ScopeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class RoleService extends BaseService {

    private final RoleRepository roleRepository;
    private final RoleGroupRepository roleGroupRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final RoleMapper roleMapper;
    private final RoleBigDTOMapper roleBigDTOMapper;
    private final RoleSmallMapper roleSmallMapper;
    private final RoleGroupMapper roleGroupMapper;
    private final RoleGroupTreeMapper roleGroupTreeMapper;
    private final MenuMapper menuMapper;
    private final RoleDetailMapper roleDetailMapper;
    private final DataDimensionRepository dataDimensionRepository;
    private final DimensionPermRepository dimensionPermRepository;
    private final DataGrantRepository dataGrantRepository;
    private final DataSubjectDimensionRelRepository dataSubjectDimensionRelRepository;
    private final OrgEmployeeManageScopeRepository orgEmployeeManageScopeRepository;
    private final DataGrantService dataGrantService;
    private final JPAQueryFactory queryFactory;




    @Transactional(readOnly = true)
    public RoleBigDTO findById(Long id) {
        Role role = roleRepository.findById(id).orElse(null);
        return roleBigDTOMapper.toDto(role);
    }

    @Transactional(readOnly = true)
    public List<Role> findByIds(List<Long> ids) {
        return roleRepository.findAllById(ids);
    }


    @Transactional(rollbackFor = Exception.class)
    public RoleBigDTO createRole(RoleDTO resources) {
        Optional<RoleGroup> grp = roleGroupRepository.findById(resources.getGroup().getId());
        if (!grp.isPresent()) {
            throw new IllegalArgumentException("角色组不存在");// TODO 规范异常
        }
        Role role = roleMapper.toEntity(resources);
        if (roleRepository.findAllByNameAndGroup(role.getName(), role.getGroup()).size() > 0) {
            throw new IllegalArgumentException("同一个角色组不能出现相同角色"); // TODO 规范异常
        }
        if (role.getMenus() != null && role.getMenus().size() > 0) {
            role.setMenus(menuRepository.findAllById(role.getMenus().stream().map(Menu::getId).collect(Collectors.toList())));
        } else {
            role.setMenus(null);
        }
        Role saved = roleRepository.save(role);
        return findById(saved.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(RoleDTO roleDTO) {
        Optional<Role> existRole = roleRepository.findById(roleDTO.getId());
        if (!existRole.isPresent()) {
            throw new IllegalArgumentException(); // TODO 规范异常
        }
        Role role = existRole.get();

        role.setName(roleDTO.getName());
        role.setDescription(roleDTO.getDescription());
        Optional<RoleGroup> grp = roleGroupRepository.findById(roleDTO.getGroup().getId());
        if (!grp.isPresent()) {
            throw new IllegalArgumentException("角色组不存在");// TODO 规范异常
        }
        role.setGroup(grp.get());
        role.setEnabled(roleDTO.isEnabled());

        if (roleDTO.getMenus() != null) {
            List<Long> menuIds = roleDTO.getMenus().stream().map(MenuDTO::getId).collect(Collectors.toList());
            List<Menu> menuAll = menuRepository.findAllById(menuIds);
            role.getMenus().removeAll(role.getMenus());
            role.setMenus(menuAll);
        }
        roleRepository.saveAndFlush(role);
    }

    @Transactional(readOnly = true)
    public List<RoleDTO> query(RoleQuery query) {

        List<Role> roles = roleRepository.findAll(buildSpecification(query));
        return roleMapper.toDto(roles);
    }

    @Transactional(readOnly = true)
    public List<RoleDTO> queryEventRole(RoleQuery query) {
        if (StringUtils.isEmpty(query.getSubjectType())) {
            throw new ApiException(400, "请选择主体subject");
        }
        List<Role> roles = roleRepository.findAll(buildSpecification(query));
        return roleMapper.toDto(roles);
    }

    @Transactional(readOnly = true)
    public Page<RoleDTO> pageQuery(RoleQuery query, Pageable pageable) {
        Page<Role> roles = roleRepository.findAll(buildSpecification(query), pageable);
        return roles.map(roleMapper::toDto);
    }

    private Specification<Role> buildSpecification(RoleQuery roleQuery) {
        return new Specification<Role>() {
            private static final long serialVersionUID = -7797421010838361430L;

            @Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (roleQuery == null) {
                    return predicate;
                }

                if (roleQuery.getId() != null) {
                    predicate.getExpressions().add(cb.equal(root.get("id"), roleQuery.getId()));
                }

                if (StringUtils.isNotBlank(roleQuery.getName())) {
                    predicate.getExpressions().add(cb.like(root.get("name"), "%" + roleQuery.getName() + "%"));
                }
                if (StringUtils.isNotBlank(roleQuery.getDescription())) {
                    predicate.getExpressions().add(cb.like(root.get("description"), "%" + roleQuery.getDescription() + "%"));
                }

                if (roleQuery.getGroupId() != null) {
                    predicate.getExpressions().add(cb.equal(root.get("group.id"), roleQuery.getGroupId()));
                }

                if (roleQuery.getGroupId() == null && StringUtils.isNotBlank(roleQuery.getGroupName())) {
                    predicate.getExpressions().add(cb.like(root.get("group.name"), "%" + roleQuery.getGroupName() + "%"));
                }
                if (StringUtils.isNotBlank(roleQuery.getSubjectType())) {
                    predicate.getExpressions().add(cb.equal(root.get("subjectType"), SubjectType.valueOf(roleQuery.getSubjectType())));
                }


                return predicate;
            }
        };
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        final Optional<Role> roleOptional = roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            final Role role = roleOptional.get();
            final List<User> users = role.getUsers();
            if (users != null && users.size() > 0) {
                throw new ApiException(400, "该角色已授权给用户，请先取消授权");
            }
            roleRepository.deleteById(id);
        }
    }

    public void bulkDeleteByIds(Set<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            ids.forEach(this::deleteById);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void grantPrivileges(GrantMenuToRoleCommand command) {
        Long roleId = command.getRoleId();
        roleRepository.findById(roleId).ifPresent(role -> {
            List<Menu> list = menuRepository.findAllById(command.getMenuIds());
            role.setMenus(list);
            roleRepository.save(role);
        });
    }

    public List<RoleSmallDTO> findByUsersId(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        List<Role> roles = userOptional.map(User::getRoles).orElse(Collections.emptyList());
        return roleSmallMapper.toDto(roles);
    }

    @Transactional(readOnly = true)
    public List<RoleGroupTreeDTO> tree() {
        List<RoleGroup> groups = roleGroupRepository.findAll();
        return roleGroupTreeMapper.toDto(groups);
    }

    public RoleGroupDTO createRoleGroup(String name, RoleGroupType type) {
        RoleGroup exist = roleGroupRepository.findOneByName(name);
        if (exist != null) {
            throw new IllegalArgumentException("角色组 [" + name + "] 已存在");// TODO 异常处理
        }
        RoleGroup group = new RoleGroup();
        group.setName(name);
//        group.setType(type);
        RoleGroup saved = roleGroupRepository.save(group);

        return roleGroupMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<RoleGroupDTO> groupList(String name) {
        List<RoleGroup> groups = roleGroupRepository.findAll();
        return roleGroupMapper.toDto(groups);
    }


    @Transactional(rollbackFor = Exception.class)
    public void deleteGroup(Long id) {
        Optional<RoleGroup> groupOptional = roleGroupRepository.findById(id);
        if (!groupOptional.isPresent()) {
            return;
        }
        RoleGroup group = groupOptional.get();
        if (group.hasRole()) {
            throw new IllegalArgumentException(); // 存在角色时不允许删除
        }

        if (group.isSysDefault()) {
            throw new IllegalArgumentException(); // TODO 系统内置默认角色组不能删除
        }
        roleGroupRepository.delete(group);
    }

    public void grantRoleToUsers(GrantRoleToUsers command) {
        roleRepository.findById(command.getRoleId()).ifPresent(role -> {
            List<User> users = userRepository.findAllById(command.getUserIds());
            for (User user : users) {
                user.addRole(role);
                userRepository.save(user);
            }
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateGroup(RoleGroupDTO resources) {
        roleGroupRepository.findById(resources.getId()).ifPresent(roleGroup -> {
            roleGroup.setName(resources.getName());
//            roleGroup.setType(resources.getType());
            roleGroupRepository.save(roleGroup);
        });
    }


//    public UserAuthorize getUserRoles(Long userId, LoginTarget loginTarget) {
//        RoleGroupType groupType = loginTarget == LoginTarget.ZC ? RoleGroupType.ZC : (loginTarget == LoginTarget.ZY ? RoleGroupType.ZY : RoleGroupType.USER);
//
//        UserAuthorize authorize = new UserAuthorize();
//        Optional<User> userOptional = userRepository.findById(userId);
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//            List<Role> roles = user.getRoles();
//            if (roles != null) {
//                roles.stream().filter(role -> {
//                    final RoleGroup group = role.getGroup();
//                    return group.getType() == groupType;
//                }).forEach(role -> {
//                    authorize.addRole(role.getId(), role.getName());
//                    role.getMenus().forEach(m -> authorize.addMenu(buildMenu(m)));
//                    RoleGroupPO roleGroupPO = new RoleGroupPO();
//                    roleGroupPO.setId(role.getGroup().getId());
//                    roleGroupPO.setName(role.getGroup().getName());
//                    authorize.addRoleGroup(roleGroupPO);
//                });
//            }
//        }
//        return authorize;
//    }

    private GrantedMenu buildMenu(Menu menu) {
        GrantedMenu m = new GrantedMenu();
        m.setId(menu.getId());
        m.setName(menu.getName());
        m.setTitle(menu.getTitle());
        m.setRoute(menu.getRoute());
        m.setComponent(m.getComponent());
        m.setPerm(menu.getPerm());
        m.setHidden(menu.isHidden());
        return m;
    }

    public Set<Long> getUserIdListByRoleIdList(Set<Long> roleIdList) {
        Set<Long> users = new HashSet<>();
        roleIdList.forEach(roleId ->
                roleRepository.findById(roleId)
                        .ifPresent(role -> role.getUsers().forEach(user -> users.add(user.getUserId()))));
        return users;
    }

    public Page<RoleGroupDTO> groupPageQuery(Pageable pageable) {
        return roleGroupRepository.findAll(pageable).map(roleGroupMapper::toDto);
    }

    public RoleGroupDTO getGroupById(Long id) {
        return roleGroupRepository.findById(id).map(roleGroupMapper::toDto).orElse(null);
    }

    public void switchRoleStatus(Long id) {
        roleRepository.findById(id).ifPresent(role -> {
            role.setEnabled(!role.isEnabled());
            roleRepository.save(role);
        });
    }

    //-----------------2021-12-09----------------

    @PostConstruct
    public void init() {
        List<Role> all = roleRepository.findAll();
        all.forEach(role -> {
            dataGrantService.cacheDataGrant(role.getId());
        });
    }


    public Object findRoleTreeBySubjectIdAndSubjectType(Long subjectId, SubjectType subjectType) {
        //查询org默认和指定组织的角色
        if (subjectId == null) {
            if (SubjectType.org.equals(subjectType)) {
                Optional<Org> currentOrg = SecurityUtils.getCurrentOrg();
                Org org = currentOrg.get();
                subjectId = org.getId();
            } else if (SubjectType.supplier.equals(subjectType)) {
                Optional<Supplier> currentSupplier = SecurityUtils.getCurrentSupplier();
                Supplier supplier = currentSupplier.get();
                subjectId = supplier.getSupplierId();
            }
        }

        List<RoleGroup> roleGroups = roleGroupRepository.findAllBySubjectTypeAndVisible(subjectType, true);
        Long finalSubjectId = subjectId;

        List<Map<String, Object>> mapList = roleGroups.stream().map(roleGroup -> {
            List<Role> roles = roleGroup.getRoles();
            List<RoleDTO> roleDTOS = null;
            if (SubjectType.org.equals(subjectType)) {
                roles = roles.stream().filter(role -> finalSubjectId.equals(role.getOrgId()) || !role.getCoped()).collect(Collectors.toList());
                roleDTOS = roleMapper.toDto(roles);
                roleDTOS.forEach(roleDTO -> roleDTO.setGroupId(roleGroup.getId()));
            } else if (SubjectType.supplier.equals(subjectType)) {
                List<Role> collect = roles.stream().filter(role -> finalSubjectId.equals(role.getSupId()) || !role.getCoped()).collect(Collectors.toList());
                roles = collect;
                roleDTOS = roleMapper.toDto(roles);
                roleDTOS.forEach(roleDTO -> roleDTO.setGroupId(roleGroup.getId()));
            }
            Map<String, Object> map = BeanUtil.beanToMap(roleGroup);
            if (CollectionUtil.isEmpty(roleDTOS)) {
                map.put("children", Lists.newArrayList());
            } else {
                map.put("children", roleDTOS);
            }
            return map;
        }).collect(Collectors.toList());

        return mapList;
    }

    public void copy(RoleCopyVO vo, Long subjectId, boolean isOrg) {
        Role role = roleRepository.findById(vo.getRoleId()).orElseThrow(() -> new IllegalArgumentException("角色组不存在"));
        Role roleNew = new Role();
        List<Menu> menus = role.getMenus();//不能直接赋值给复制角色,jpa存在内联关系
        List<Long> menuIds = menus.stream().map(Menu::getId).collect(Collectors.toList());
        List<Menu> menusRef = menuRepository.findAllById(menuIds);
        if (isOrg) {
            roleNew.setOrgId(subjectId);
            roleNew.setSubjectType(SubjectType.org);
        } else {
            roleNew.setSupId(subjectId);
            roleNew.setSubjectType(SubjectType.supplier);
        }
        roleNew.setCoped(Boolean.TRUE);
        Long sourceRoleId = role.getCoped() ? role.getSourceRoleId() : role.getId();
        roleNew.setSourceRoleId(sourceRoleId);
        roleNew.setName(vo.getName());
        roleNew.setMenus(menusRef);
        roleNew.setGroup(role.getGroup());
        roleRepository.save(roleNew);
    }

    public Page orgMemberInRoleTree(QueryParam<UserRoleQuery> queryParam, Pageable pageable) {
        Long roleId = queryParam.getParam().getRoleId();
        String searchValue = queryParam.getParam().getSearchValue();
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new IllegalArgumentException("角色不存在"));

        //构建询价单权限map供使用
        HashMap<Long, String> scopeMap = new HashMap<>();
        List<OrgEmployeeMangeScope> orgEmployeeMangeScopeList = orgEmployeeManageScopeRepository.findAll();
        orgEmployeeMangeScopeList.forEach(item->{
            scopeMap.put(item.getEmployeeId(),item.getScope());
        });


        if (SecurityUtils.getCurrentOrg().isPresent()) {
            Org org = SecurityUtils.getCurrentOrg().get();
            QOrgEmployee qOrgEmployee = QOrgEmployee.orgEmployee;
            com.querydsl.core.types.Predicate predicate = ExpressionUtils.anyOf();
            if (StrUtil.isNotBlank(searchValue)) {
                predicate = ExpressionUtils.or(predicate, qOrgEmployee.code.like("%" + searchValue + "%"));
                predicate = ExpressionUtils.or(predicate, qOrgEmployee.name.like("%" + searchValue + "%"));
                predicate = ExpressionUtils.or(predicate, qOrgEmployee.mobile.like("%" + searchValue + "%"));
            }
            predicate = ExpressionUtils.and(predicate, qOrgEmployee.org.id.eq(org.getId()));
            predicate = ExpressionUtils.and(predicate, qOrgEmployee.roles.contains(role));
            QueryResults<OrgEmployeeDTO> userQueryResults = queryFactory.select(
                    Projections.bean(OrgEmployeeDTO.class,
                            qOrgEmployee.id,
                            qOrgEmployee.name,
                            qOrgEmployee.code,
                            qOrgEmployee.mobile
                    )
            ).from(qOrgEmployee).where(predicate)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();

            List<OrgEmployeeDTO> results = userQueryResults.getResults();
            //回显询价单查询范围字段
            results.forEach(item->{
                Long id = item.getId();
                if(StrUtil.isNotEmpty( scopeMap.get(id)) ){
                    item.setOrgEmployeeMangeScope(scopeMap.get(id));
                }else {
                    //为空默认本人
                    item.setOrgEmployeeMangeScope(ScopeEnum.SELF.getCode());
                }
            });

            return new PageImpl<>(results, pageable, userQueryResults.getTotal());
        }
        return PageUtils.empty();
    }

    public Page supplierMemberInRoleTree(QueryParam<UserRoleQuery> queryParam, Pageable pageable) {
        Long roleId = queryParam.getParam().getRoleId();
        String searchValue = queryParam.getParam().getSearchValue();
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new IllegalArgumentException("角色不存在"));
        QSupplierStaff qSupplierStaff = QSupplierStaff.supplierStaff;
        if (SecurityUtils.getCurrentSupplier().isPresent()) {
            com.querydsl.core.types.Predicate predicate = ExpressionUtils.anyOf();
            if (StrUtil.isNotBlank(searchValue)) {
                predicate = ExpressionUtils.or(predicate, qSupplierStaff.code.like("%" + searchValue + "%"));
                predicate = ExpressionUtils.or(predicate, qSupplierStaff.name.like("%" + searchValue + "%"));
                predicate = ExpressionUtils.or(predicate, qSupplierStaff.mobile.like("%" + searchValue + "%"));
            }
            Supplier supplier = SecurityUtils.getCurrentSupplier().get();
            predicate = ExpressionUtils.and(predicate, qSupplierStaff.supplier.id.eq(supplier.getSupplierId()));
            predicate = ExpressionUtils.and(predicate, qSupplierStaff.roles.contains(role));
            QueryResults<SupplierStaffDTO> queryResults = queryFactory.select(
                    Projections.bean(SupplierStaffDTO.class,
                            qSupplierStaff.id,
                            qSupplierStaff.name,
                            qSupplierStaff.code,
                            qSupplierStaff.mobile
                    )
            ).from(qSupplierStaff).where(predicate)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
            return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
        }
        return PageUtils.empty();
    }

    public void batchGrantRole(BatchUserRoleVO vo, SubjectType subjectType) {
        RoleBatch roleBatch = RoleBatch.of(vo.getUserIds(), vo.getRoleIds(), subjectType.name());
        applicationEventPublisher.publishEvent(new RoleBatchGrantEvent(this, roleBatch));
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchGrant(BatchRoleMenuVO vo) {
        Role role = roleRepository.findById(vo.getId()).orElseThrow(() -> new IllegalArgumentException("角色不存在"));
        //TODO 判断当前用户是否是系统管理员
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        String currentSubjectType = loginInfo.getCurrentSubjectType();
        if (!SubjectType.isPlatform(currentSubjectType)) {
            if (!role.getCoped()) {
                throw new ApiException(403, "禁止修改默认角色权限");
            }
        }
        role.setEnabled(vo.getEnabled());
        role.setName(vo.getName());
        List<Long> menuIds = vo.getMenuIds();
        List<Menu> menus = menuRepository.findAllById(menuIds);
        role.setMenus(menus);

        List<DimensionGrantVO> purGrants = vo.getPurGrants();
        List<DimensionGrantVO> supGrants = vo.getSupGrants();
        List<DimensionGrantVO> amountGrants = vo.getAmountGrants();

        List<DimensionPerm> dimensionPerms = Lists.newArrayList();
        List<Role> roles = Lists.newArrayList(role);
        List<DataGrant> dataGrants = Lists.newArrayList();
        // 关于供应商的所有维度
        addPermsAndGrants(purGrants, dimensionPerms, roles, dataGrants, DimensionType.purchaser_code.getCode(), false); //TODO 根据角色权限判断是否具备特权
        // 关于供应商的所有维度
        addPermsAndGrants(supGrants, dimensionPerms, roles, dataGrants, DimensionType.supplier_code.getCode(), false);
        // 关于金额的所有维度
        addPermsAndGrants(amountGrants, dimensionPerms, roles, dataGrants, DimensionType.amount_field.getCode(), false);
        if (!roles.isEmpty()) {
            List<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
            roleIds.forEach(dimensionPermRepository::deleteByRoleIdNative);
        }
        if (CollectionUtil.isNotEmpty(dimensionPerms)) {
            dimensionPermRepository.saveAll(dimensionPerms);
        }
        if (!roles.isEmpty()) {
            roles.stream().map(Role::getId).collect(Collectors.toList()).forEach(dataGrantRepository::deleteByRoleIdNative);
        }
        if (CollectionUtil.isNotEmpty(dataGrants)) {
            dataGrantRepository.saveAll(dataGrants);
        }
        roleRepository.save(role);
        dataGrantService.cacheDataGrant(role.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchRemoveRole(BatchUserRoleVO vo, SubjectType subjectType) {
        RoleBatch roleBatch = RoleBatch.of(vo.getUserIds(), vo.getRoleIds(), subjectType.name());
        applicationEventPublisher.publishEvent(new RoleBatchRemoveEvent(this, roleBatch));
    }

    public Object findDefTreeGroupBySubjectType() {
        ArrayList<SubjectType> subjectTypes = Lists.newArrayList(SubjectType.org, SubjectType.supplier);
        List<RoleGroup> roleGroups = roleGroupRepository.findAllBySubjectTypeIn(subjectTypes);
        return roleGroups.stream().map(roleGroup -> {
            Map<String, Object> map = BeanUtil.beanToMap(roleGroup);
            List<Role> filterRoles = roleGroup.getRoles().stream().filter(role -> role.getGroup().getId().equals(roleGroup.getId())).collect(Collectors.toList());
            List<RoleDTO> roleDTOS = roleMapper.toDto(filterRoles);
            roleDTOS.forEach(roleDTO -> roleDTO.setGroupId(roleGroup.getId()));
            if (CollectionUtil.isEmpty(filterRoles)) {
                map.put("children", Lists.newArrayList());
            } else {
                map.put("children", roleDTOS);
            }
            return map;
        }).collect(Collectors.groupingBy(r -> r.get("subjectType")));
    }

    public Object queryGroupBySubject(SubjectType subjectType) {
        return null;
    }

    public RoleDetailDTO info(Long roleId) {
        Role role = roleRepository.findById(roleId).orElse(null);
        RoleDetailDTO roleDetailDTO = roleDetailMapper.toDto(role);
        roleDetailDTO.setRoleGroupName(role.getGroup().getName());
        List<Long> menuIds = role.getMenus().stream().map(Menu::getId).collect(Collectors.toList());
        roleDetailDTO.setEnabled(role.isEnabled());
        roleDetailDTO.setMenuIds(menuIds);
        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        String currentSubjectType = loginInfo.getCurrentSubjectType();
        if (StrUtil.equals(SubjectType.org.name(), currentSubjectType)) {
            Org currentOrg = loginInfo.getCurrentOrg();
            roleDetailDTO.setOrgName(currentOrg.getName());
        } else if (StrUtil.equals(SubjectType.supplier.name(), currentSubjectType)) {
            Supplier currentSupplier = loginInfo.getCurrentSupplier();
            String supplierName = currentSupplier.getSupplierName();
            roleDetailDTO.setSupplierName(supplierName);
        }
        List<DimensionPerm> dimensionPerms = dimensionPermRepository.findAllByRolesIn(Lists.newArrayList(role));
        if (CollectionUtil.isNotEmpty(dimensionPerms)) {
            roleDetailDTO.setAmountGrants(buildDimensionPermDTO(dimensionPerms, DimensionType.amount_field));
            roleDetailDTO.setPurGrants(buildDimensionPermDTO(dimensionPerms, DimensionType.purchaser_code));
            roleDetailDTO.setSupGrants(buildDimensionPermDTO(dimensionPerms, DimensionType.supplier_code));
        }
        return roleDetailDTO;
    }

    private List<DimensionPermDTO> buildDimensionPermDTO(List<DimensionPerm> dimensionPerms, DimensionType dimensionType) {
        return dimensionPerms.stream().filter(dimensionPerm -> dimensionPerm.getDimensionType().equals(dimensionType.getCode())).map(p -> {
            DimensionPermDTO dimensionPermDTO = new DimensionPermDTO();
            dimensionPermDTO.setDimensionType(p.getDimensionType());
            dimensionPermDTO.setPermName(p.getPermName());
            dimensionPermDTO.setPermValue(p.getPermValue());
            dimensionPermDTO.setName(p.getPermName());
            dimensionPermDTO.setCode(p.getPermValue());
            return dimensionPermDTO;
        }).collect(Collectors.toList());
    }

    public void assignMenuPerms(RolePermVO vo) {
        Role role = roleRepository.findById(vo.getId()).orElseThrow(() -> new ApiException(404, "Entity Not Found"));
        List<Menu> menus = menuRepository.findAllById(vo.getMenuIds());
        role.setName(vo.getName());
        role.setMenus(menus);
        role.setEnabled(vo.getEnabled());
        roleRepository.save(role);
    }


    private void addPermsAndGrants(List<DimensionGrantVO> grantVOList, List<DimensionPerm> dimensionPerms, List<Role> roles, List<DataGrant> dataGrants, String dimensionType, boolean isSpecial) {
        if (CollectionUtil.isNotEmpty(grantVOList)) {
            List<DimensionPerm> perms = grantVOList.stream().map(p -> this.buildDimensionPerm(p, roles, dimensionType)).collect(Collectors.toList());
            dimensionPerms.addAll(perms);
            addDataGrants(roles, dataGrants, dimensionType, perms, isSpecial);
        }
    }

    private void addDataGrants(List<Role> roles, List<DataGrant> dataGrants, String dimensionType, List<DimensionPerm> perms, boolean isSpecial) {
        //根据分类获取维度
        List<DataDimension> dataDimensions = dataDimensionRepository.findAllByDimensionType(dimensionType);
        // 获取相关维度code
        List<String> dimensionCodes = dataDimensions.stream().map(DataDimension::getDimensionCode).collect(Collectors.toList());
        // 获取主体维度适用数据
        List<DataSubjectDimensionRel> dataSubjectDimensionRels = dataSubjectDimensionRelRepository.findAllByDimensionCodeIn(dimensionCodes);
        perms.forEach(p -> {
            List<DataGrant> grants = dataSubjectDimensionRels.stream().map(d ->
                    this.buildDataGrant(d.getDataSubject().getId(), d.getDimensionCode(), p.getPermValue(), roles, dimensionType, isSpecial))
                    .collect(Collectors.toList());
            dataGrants.addAll(grants);
        });
    }

    //创建用户数据授权
    private DataGrant buildDataGrant(Long subjectId, String dimensionCode, String dimensionValue, List<Role> roles, String dimensionType, boolean isSpecial) {
        return DataGrant.builder()
                .subjectId(subjectId)
                .dimensionType(dimensionType)
                .roles(roles)
                .dimensionCode(dimensionCode)
                .dimensionValue(dimensionValue)
                .isSpecial(isSpecial)
                .build();
    }


    private DimensionPerm buildDimensionPerm(DimensionGrantVO vo, List<Role> roles, String dimensionType) {
        return DimensionPerm.builder()
                .dimensionType(dimensionType)
                .permName(vo.getName())
                .permValue(vo.getCode())
                .roles(roles)
                .build();
    }

    @Transactional
    public void saveQuotationRole(OrgEmployeeMangeScope scope) {

        OrgEmployeeMangeScope byEmployeeId = orgEmployeeManageScopeRepository.findByEmployeeId(scope.getEmployeeId());
        if(ObjectUtil.isEmpty(byEmployeeId)){
            orgEmployeeManageScopeRepository.save(scope);
        }else{
            orgEmployeeManageScopeRepository.updateByEmployeeIdAndScope(scope.getEmployeeId(),scope.getScope());
        }

    }

    public OrgEmployeeMangeScope findByEmployeeId(Long id) {
        return orgEmployeeManageScopeRepository.findByEmployeeId(id);
    }

    @Transactional
    public void saveQuotationRoleBatch(OrgEmployeeMangeUserIdsListAndScopeParam param) {
        List<Long> userIds = param.getUserIds();
        String scope = param.getBoundary();
        //不为空
        if(CollectionUtil.isNotEmpty(userIds)&&StrUtil.isNotEmpty(scope)){
            List<OrgEmployeeMangeScope> collect = userIds.stream().map(item -> OrgEmployeeMangeScope.builder()
                    .employeeId(item)
                    .scope(scope)
                    .build())
                    .collect(Collectors.toList());
            //先删除后保存
//            orgEmployeeManageScopeRepository.deleteInBatch(collect);
            collect.forEach(orgEmployeeMangeScope->this.saveQuotationRole(orgEmployeeMangeScope));
        }
    }

    public List<OrgEmployeeMangeScope> findAllRoleQuotation() {
        return orgEmployeeManageScopeRepository.findAll();
    }
}
