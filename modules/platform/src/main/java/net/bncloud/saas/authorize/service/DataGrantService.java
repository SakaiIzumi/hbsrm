package net.bncloud.saas.authorize.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import com.google.common.collect.Lists;
import com.querydsl.core.types.EntityPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.api.feign.saas.user.SubjectType;
import net.bncloud.common.security.LoginInfo;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.common.security.data.DataDimensionGrant;
import net.bncloud.common.security.data.GrantDataHolder;
import net.bncloud.saas.authorize.domain.*;
import net.bncloud.saas.authorize.repository.DataGrantRepository;
import net.bncloud.saas.authorize.repository.DataSubjectDimensionRelRepository;
import net.bncloud.saas.authorize.repository.DataSubjectRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class DataGrantService {

    private final JPAQueryFactory queryFactory;

    private final GrantDataHolder grantDataHolder;

    private final DataSubjectRepository dataSubjectRepository;

    private final DataGrantRepository dataGrantRepository;

    private final DataSubjectDimensionRelRepository dataSubjectDimensionRelRepository;

    private final QDataGrant qDataGrant = QDataGrant.dataGrant;




    public void cacheDataGrant(Long roleId) {
        List<DataSubject> subjects = dataSubjectRepository.findAll();
        //清理角色维度权限数据
        if (CollectionUtil.isNotEmpty(subjects)) {
            subjects.forEach(dataSubject -> {
                grantDataHolder.delete(roleId.toString(), dataSubject.getId().toString());
            });
        }

        //TODO 目前只有针对角色的数据权限
        Role role = new Role();
        role.setId(roleId);
        ArrayList<Role> roles = Lists.newArrayList(role);
        List<DataGrant> dataGrantList = dataGrantRepository.findAllByRolesIn(roles);

        Map<String, List<DataGrant>> collect = dataGrantList.stream().collect(Collectors.groupingBy(dataGrant -> dataGrant.getSubjectId() + "#" + dataGrant.getDimensionCode()));
        if (CollectionUtil.isNotEmpty(collect)) {
            for (Map.Entry<String, List<DataGrant>> entry : collect.entrySet()) {
                String key = entry.getKey();
                String[] split = key.split("#");
                String subjectIdStr = split[0];
                Long subjectId = NumberUtil.parseLong(subjectIdStr);
                String dimensionCode = split[1];
                List<DataGrant> dataGrants = entry.getValue();
                boolean special = getSpecial(dataGrants);
                Set<String> dataIds = getDataIds(dataGrants);
                Optional<DataSubjectDimensionRel> dataSubjectDimensionRelOptional = dataSubjectDimensionRelRepository.findByDimensionCodeAndDataSubject_Id(dimensionCode, subjectId);
                if (dataSubjectDimensionRelOptional.isPresent()) {
                    DataSubjectDimensionRel dataSubjectDimensionRel = dataSubjectDimensionRelOptional.get();
                    DataDimensionGrant dataDimensionGrant = new DataDimensionGrant();
                    dataDimensionGrant.setSubjectId(subjectIdStr);
                    dataDimensionGrant.setDimensionCode(dimensionCode);
                    dataDimensionGrant.setSpecial(special);
                    if (dataIds != null) {
                        dataDimensionGrant.setDataIds(new ArrayList<>(dataIds));
                    }
                    dataDimensionGrant.setAlias(dataSubjectDimensionRel.getAlias());
                    grantDataHolder.cache(roleId.toString(), dataDimensionGrant);
                }
            }
        }
    }
//    public void cacheCurrent(String roleId) {
//        LoginInfo loginInfo = SecurityUtils.getLoginInfoOrThrow();
//        String username = loginInfo.getName();
//        Long id = loginInfo.getId();
//        List<DataSubject> subjects = dataSubjectRepository.findAll();
//
//        //清理当前用户维度权限数据
//        if (CollectionUtil.isNotEmpty(subjects)) {
//            subjects.forEach(dataSubject -> {
//                grantDataHolder.delete(username, dataSubject.getId().toString());
////                grantDataHolder.delete(id.toString(), dataSubject.getId().toString());
//            });
//        }
//
//        //TODO 目前只有针对角色的数据权限
//        Set<net.bncloud.common.security.Role> rolesSet = loginInfo.getRoles();
//        List<Role> roles = rolesSet.stream().map(r -> {
//            Role role = new Role();
//            role.setId(r.getId());
//            return role;
//        }).collect(Collectors.toList());
//
//        List<DataGrant> dataGrantList = dataGrantRepository.findAllByRolesIn(roles);
//
//        Map<String, List<DataGrant>> collect = dataGrantList.stream().collect(Collectors.groupingBy(dataGrant -> dataGrant.getSubjectId() + "#" + dataGrant.getDimensionCode()));
//        if (CollectionUtil.isNotEmpty(collect)) {
//            for (Map.Entry<String, List<DataGrant>> entry : collect.entrySet()) {
//                String key = entry.getKey();
//                String[] split = key.split("#");
//                String subjectIdStr = split[0];
//                Long subjectId = NumberUtil.parseLong(subjectIdStr);
//                String dimensionCode = split[1];
//                List<DataGrant> dataGrants = entry.getValue();
//                boolean special = getSpecial(dataGrants);
//                Set<String> dataIds = getDataIds(dataGrants);
//                Optional<DataSubjectDimensionRel> dataSubjectDimensionRelOptional = dataSubjectDimensionRelRepository.findByDimensionCodeAndDataSubject_Id(dimensionCode, subjectId);
//                if (dataSubjectDimensionRelOptional.isPresent()) {
//                    DataSubjectDimensionRel dataSubjectDimensionRel = dataSubjectDimensionRelOptional.get();
//                    DataDimensionGrant dataDimensionGrant = new DataDimensionGrant();
//                    dataDimensionGrant.setSubjectId(subjectIdStr);
//                    dataDimensionGrant.setDimensionCode(dimensionCode);
//                    dataDimensionGrant.setSpecial(special);
//                    if (dataIds != null) {
//                        dataDimensionGrant.setDataIds(new ArrayList<>(dataIds));
//                    }
//                    dataDimensionGrant.setAlias(dataSubjectDimensionRel.getAlias());
//                    grantDataHolder.cache(username, dataDimensionGrant);
//                }
//            }
//        }
//    }

    /**
     * 获取同一主题，同一维度下，数据授权-维度值集合
     *
     * @param dataGrants
     * @return
     */
    Set<String> getDataIds(List<DataGrant> dataGrants) {
        if (dataGrants == null || dataGrants.isEmpty()) {
            return null;
        }
        Set<String> dataIds = new HashSet<>();
        for (DataGrant dataGrant : dataGrants) {
            dataIds.add(dataGrant.getDimensionValue());
        }
        return dataIds;
    }

    private boolean getSpecial(List<DataGrant> dataGrants) {
        boolean special = false;
        if (dataGrants == null || dataGrants.isEmpty()) {
            return false;
        }
        for (DataGrant dataGrant : dataGrants) {
            Boolean isSpecial = dataGrant.getIsSpecial();
            if (isSpecial) {
                special = isSpecial;
                break;
            }
        }
        return special;
    }

}
