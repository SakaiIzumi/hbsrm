package net.bncloud.saas.tenant.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import net.bncloud.common.api.R;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.constants.ManagerType;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.Org;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.saas.tenant.domain.*;
import net.bncloud.saas.tenant.repository.OrgEmployeeRepository;
import net.bncloud.saas.tenant.domain.vo.BatchOrgManagerVO;
import net.bncloud.saas.tenant.domain.vo.UserVO;
import net.bncloud.saas.tenant.repository.OrgManagerRepository;
import net.bncloud.saas.tenant.repository.OrganizationRepository;
import net.bncloud.saas.tenant.service.command.TransferOrgManagerCommand;
import net.bncloud.saas.tenant.service.dto.OrgAdministratorDTO;
import net.bncloud.saas.tenant.service.dto.OrgManagerDTO;
import net.bncloud.saas.tenant.service.query.OrgManagerQuery;
import net.bncloud.saas.tenant.web.param.OrgAdministratorParam;
import net.bncloud.saas.user.domain.QUserInfo;
import net.bncloud.saas.user.domain.UserInfo;
import net.bncloud.saas.user.service.UserInfoService;
import net.bncloud.saas.utils.pageable.PageUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class OrgAdministratorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrgAdministratorService.class);
    private final OrgEmployeeRepository orgEmployeeRepository;
    private final OrganizationRepository organizationRepository;
    private final OrgManagerRepository orgManagerRepository;
    private final UserInfoService userInfoService;
    private final JPAQueryFactory queryFactory;

    /**
     * 获取组织管理员列表
     */
    public Page<OrgAdministratorDTO> pageQuery(OrgManagerQuery queryParam, Pageable pageable) {
        final LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        final Org org = loginInfo.getCurrentOrg();

        final List<OrgManager> mainManagers = orgManagerRepository.findAllByOrgIdAndManagerType(org.getId(), ManagerType.MAIN);
        final OrgManager currentMainManager = getManagerByUserId(mainManagers, loginInfo.getId());

        final Page<OrgAdministratorDTO> page = orgManagerRepository.findAll((Specification<OrgManager>) (root, query, criteriaBuilder) -> {
            final Predicate predicate = criteriaBuilder.conjunction();
            predicate.getExpressions().add(criteriaBuilder.equal(root.get("orgId"), org.getId()));

            if (queryParam.getManagerType() != null) {
                predicate.getExpressions().add(criteriaBuilder.equal(root.get("managerType"), queryParam.getManagerType()));
            }

            if (StringUtils.isNotBlank(queryParam.getName())) {
                predicate.getExpressions().add(criteriaBuilder.like(root.get("name"), "%" + queryParam.getName() + "%"));
            }
            return predicate;
        }, pageable)
                .map(this::buildDto);
        if (currentMainManager == null) { // 如果不是主管理员操作，则默认返回不可删除、不可转让
            return page;
        }
        return page.map(dto -> buildDtoPermission(dto, mainManagers));
    }

    private OrgAdministratorDTO buildDtoPermission(OrgAdministratorDTO dto, List<OrgManager> mainManagers) {
        final LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();

        // 如果是当前管理员则返回可转让
        dto.getPermissionButton().setTransferable(Objects.equals(dto.getUserId(), loginInfo.getId()));
        dto.getPermissionButton().setDeletable(true);
        if (ManagerType.MAIN.name().equals(dto.getManagerType()) && mainManagers.size() <= 1) {
            dto.getPermissionButton().setDeletable(false);
        }
        return dto;
    }

    private OrgAdministratorDTO buildDto(OrgManager manager) {
        OrgAdministratorDTO orgAdministratorDTO = new OrgAdministratorDTO();
        orgAdministratorDTO.setId(manager.getId());
        orgAdministratorDTO.setOrgId(manager.getOrg().getId());
        orgAdministratorDTO.setUserId(manager.getUser().getId());
        orgAdministratorDTO.setName(manager.getUser().getName());
        orgAdministratorDTO.setManagerType(manager.getManagerType().name());
        orgAdministratorDTO.setManagerTypeName(ManagerType.SUB == manager.getManagerType() ? "子管理员" :
                (ManagerType.MAIN == manager.getManagerType()) ? "主管理员" : "");
        orgAdministratorDTO.setDeptName(""); // TODO
        orgAdministratorDTO.setJobNumber(""); // TODO

        final UserInfo userInfo = userInfoService.getUserById(orgAdministratorDTO.getUserId());
        if (userInfo != null) {
            orgAdministratorDTO.setMobile(userInfo.getMobile());
        }

        return orgAdministratorDTO;
    }

    /**
     * 删除组织管理员
     */
    public void deleteOrgAdmin(Long id) {
        final Long currentUserId = SecurityUtils.getLoginInfoOrThrow().getId();
        SecurityUtils.getCurrentOrg().ifPresent(org -> {
            final List<OrgManager> managers = orgManagerRepository.findAllByOrgId(org.getId());
            if (managers == null || managers.isEmpty()) {
                return;
            }
            final OrgManager currentManager = getManagerByUserId(managers, currentUserId);
            if (currentManager == null || currentManager.getManagerType() != ManagerType.MAIN) {
                throw new ApiException(20401, "只有主管理员才有权限删除管理员");
            }

            orgManagerRepository.findById(id).ifPresent(manager -> {
                if (Objects.equals(manager.getUser().getId(), currentUserId)) {
                    if (managers.stream().filter(m -> m.getManagerType() == ManagerType.MAIN).count() == 1) {
                        throw new ApiException(20401, "主管理员只有1个，不能删除, 只能转让");
                    }
                }
                orgManagerRepository.delete(manager);
            });
        });
    }

    private OrgManager getManagerByUserId(List<OrgManager> managers, Long userId) {
        if (managers == null) {
            return null;
        }
        for (OrgManager manager : managers) {
            if (Objects.equals(manager.getUser().getId(), userId)) {
                return manager;
            }
        }
        return null;
    }

    /**
     * 新增组织管理员
     */
    public void addOrgAdmin(OrgAdministratorParam orgAdministratorParam) {
        SecurityUtils.getCurrentOrg().ifPresent(organization -> orgAdministratorParam.setOrgId(organization.getId()));
        if (Objects.isNull(orgAdministratorParam.getOrgId())) {
            return;
        }
        organizationRepository.findById(orgAdministratorParam.getOrgId()).ifPresent(org -> {
            OrgManager manager = new OrgManager();
//            manager.setOrgId(org.getId());
            manager.setOrg(org);
            manager.setUser(UserVO.of(orgAdministratorParam.getUserId(), orgAdministratorParam.getName()));
            manager.setDepartment(null);
            manager.setManagerType(orgAdministratorParam.getManagerType());
            orgManagerRepository.save(manager);
        });
    }

    /**
     * 转让组织管理员
     */
    public void transferAdmin(TransferOrgManagerCommand command) {
        // TODO check sms code
        SecurityUtils.getLoginInfo().ifPresent(loginInfo -> {
            final Long currentUserId = loginInfo.getId();
            final Org currentOrg = loginInfo.getCurrentOrg();
            final List<OrgManager> managers = orgManagerRepository.findAllByOrgId(currentOrg.getId());
            final OrgManager manager = getManagerByUserId(managers, currentUserId);
            if (manager.isMain()) {
                manager.setUser(UserVO.of(command.getUserId(), command.getName()));
                orgManagerRepository.save(manager);
            } else {
                throw new ApiException(20401, "只有主管理员才可以转让");
            }
        });
    }

    /**
     * 发送短信验证码
     */
    public Integer sendShortMessage(String mobile) {
        Integer verificationCode = getRandom();
        LOGGER.info("手机验证码：{}", verificationCode);
        // TODO generate code and send
        String code = RandomStringUtils.randomNumeric(4);// TODO 验证码位数改为配置
        // TODO store code
        return verificationCode;
    }


    private Integer getRandom() {
        String randomString = null;
        Integer random = null;
        do {
            random = (int) (Math.random() * 1000000);
            randomString = random + "";
        }
        while (randomString.length() < 6);

        return random;
    }

    public R<OrgAdministratorDTO> detail(Long id) {
        return null;
    }

    public OrgAdministratorDTO getCurrentMainManagerForTransfer() {
        final LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        return SecurityUtils.getCurrentOrg()
                .map(org -> organizationRepository.findById(org.getId()).map(organization -> {
                    final List<OrgManager> managers = organization.getManagers();
                    final OrgManager manager = getManagerByUserId(managers, loginInfo.getId());
                    if (manager == null || !manager.isMain()) {
                        throw new ApiException(20401, "只有主管理员自己才可以转让");
                    }
                    return manager;
                }).orElseThrow(() -> new ApiException(20401, "只有主管理员自己才可以转让")))
                .map(this::buildDto)
                .orElse(null);
    }

    public Object orgManagerTable(QueryParam<OrgManagerQuery> queryParam, Pageable pageable) {
        Optional<Org> currentOrg = SecurityUtils.getCurrentOrg();
        if (currentOrg.isPresent()) {
            String searchValue = queryParam.getSearchValue();
            OrgManagerQuery param = queryParam.getParam();
            Org org = currentOrg.get();
            Long orgId = org.getId();
            if (orgId == null) {
                return new PageImpl<>(Lists.newArrayList(), pageable, 0);
            }
            QUserInfo userInfo = QUserInfo.userInfo;
            QOrgManager orgManager = QOrgManager.orgManager;
            com.querydsl.core.types.Predicate predicate = orgManager.org.id.eq(orgId);
            predicate = ExpressionUtils.and(predicate, userInfo.id.eq(orgManager.user.id));
            if (StrUtil.isNotBlank(searchValue)) {

                com.querydsl.core.types.Predicate or = userInfo.code.like("%" + searchValue + "%");
                or = ExpressionUtils.or(or, userInfo.name.like("%" + searchValue + "%"));
                or = ExpressionUtils.or(or, userInfo.mobile.like("%" + searchValue + "%"));
                predicate = ExpressionUtils.and(predicate,or);

            } else {
                predicate = StrUtil.isBlank(param.getName()) ? predicate : ExpressionUtils.or(predicate, userInfo.name.like("%" + param.getName() + "%"));
                predicate = StrUtil.isBlank(param.getMobile()) ? predicate : ExpressionUtils.or(predicate, userInfo.mobile.like("%" + param.getMobile() + "%"));
                predicate = StrUtil.isBlank(param.getUserCode()) ? predicate : ExpressionUtils.or(predicate, userInfo.code.like("%" + param.getUserCode() + "%"));
            }
            QueryResults<OrgManagerDTO> queryResults = queryFactory.select(
                    Projections.bean(OrgManagerDTO.class,
                            userInfo.id,
                            userInfo.name,
                            userInfo.code,
                            userInfo.mobile,
                            orgManager.enabled,
                            orgManager.id.as("managerId"),
                            orgManager.org.id.as("orgId"),
                            orgManager.org.name.as("orgName")
                    )
            ).from(userInfo, orgManager)
                    .where(predicate)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();
            List<OrgManagerDTO> results = queryResults.getResults();
            results.forEach(dto -> {
                buildManagerPermission(dto, null);
            });
            return PageUtils.of(queryResults, pageable);
        }
        return PageUtils.empty();
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchAddOrgManager(BatchOrgManagerVO vo) {
        LoginInfo loginInfoOrThrow = SecurityUtils.getLoginInfoOrThrow();
        Long currentUserId = loginInfoOrThrow.getId();
        List<Long> empIds = vo.getUserIds();
        List<OrgEmployee> orgEmployees = orgEmployeeRepository.findAllById(empIds);
        List<Long> userIds = orgEmployees.stream().map(e->e.getUser().getUserId()).collect(Collectors.toList());
        userIds = userIds.stream().filter(id -> !id.equals(currentUserId)).collect(Collectors.toList());
        List<Long> finalUserIds = userIds;
        SecurityUtils.getCurrentOrg().ifPresent(organization -> {
            Long orgId = organization.getId();
            if (CollectionUtil.isNotEmpty(finalUserIds)) {
                List<OrgEmployee> employees = orgEmployeeRepository.findAllByOrg_IdAndIdIn(orgId, empIds);
                List<OrgManager> orgManagers = orgManagerRepository.findAllByOrg_IdAndUser_IdIn(orgId, finalUserIds);
                List<Long> managerUserIds = orgManagers.stream().map(orgManager -> orgManager.getUser().getId()).collect(Collectors.toList());
                organizationRepository.findById(orgId).ifPresent(org -> {
                    List<OrgManager> batchInsertManagers = employees.stream().map(u -> OrgManager.builder()
                            .org(org)
                            .managerType(ManagerType.ORG)
                            .employeeId(u.getId())
                            .enabled(Boolean.TRUE)
                            .user(UserVO.of(u.getUser().getUserId(), u.getName()))
                            .build())
                            .filter(orgManager -> !managerUserIds.contains(orgManager.getUser().getId()))
                            .collect(Collectors.toList());
                    orgManagerRepository.saveAll(batchInsertManagers);
                });
            }
        });
    }


    public Object checkTransfer(Long orgManagerId) {
        QUserInfo userInfo = QUserInfo.userInfo;
        QOrgManager orgManager = QOrgManager.orgManager;
        QOrganization qOrganization = QOrganization.organization;
        Tuple tuple = queryFactory.select(
                userInfo.name,
                userInfo.mobile,
                orgManager.id.as("orgManagerId")
        ).from(userInfo, orgManager).where(userInfo.id.eq(orgManager.user.id)
                .and(orgManager.id.eq(orgManagerId))
        ).fetchFirst();
        return Dict.create()
                .set("name", tuple.get(0, String.class))
                .set("orgManagerId", tuple.get(2, String.class))
                .set("mobile", tuple.get(1, String.class));
    }

    private OrgManagerDTO buildManagerPermission(OrgManagerDTO dto, List<OrgManagerDTO> mainManagers) {
        final LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
        // 如果是当前管理员则返回可转让
        dto.getPermissionButton().setTransferable(Objects.equals(dto.getId(), loginInfo.getId()));
        dto.getPermissionButton().setDeletable(true);
//        if (ManagerType.MAIN.name().equals(dto.getManagerType()) && mainManagers.size() <= 1) {
//            dto.getPermissionButton().setDeletable(false);
//        }
        return dto;
    }
}
