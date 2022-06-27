package net.bncloud.saas.purchaser.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.security.Org;
import net.bncloud.common.security.SecurityUtils;
import net.bncloud.saas.purchaser.domain.QPurchaser;
import net.bncloud.saas.purchaser.domain.QPurchaserStaff;
import net.bncloud.saas.purchaser.service.dto.PurchaserStaffDTO;
import net.bncloud.saas.purchaser.service.query.PurchaserStaffQuery;
import net.bncloud.saas.tenant.domain.Organization;
import net.bncloud.saas.user.domain.QUserInfo;
import net.bncloud.saas.utils.pageable.PageUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class PurchaserStaffService {
    final QPurchaser qPurchaser = QPurchaser.purchaser;
    private final QPurchaserStaff qPurchaserStaff = QPurchaserStaff.purchaserStaff;
    private final JPAQueryFactory queryFactory;


    public Object purchaserStaffTableByOrgId(QueryParam<PurchaserStaffQuery> queryParam, Pageable pageable) {

        Optional<Org> currentOrg = SecurityUtils.getCurrentOrg();
        if (currentOrg.isPresent()) {
            Org org = currentOrg.get();
            QUserInfo qUserInfo = QUserInfo.userInfo;
            String searchValue = queryParam.getSearchValue();
            PurchaserStaffQuery param = queryParam.getParam();

            Predicate predicate = ExpressionUtils.anyOf();
            if (StrUtil.isNotBlank(searchValue)) {
                searchValue = "%" + searchValue + "%";
                predicate = ExpressionUtils.or(predicate, qUserInfo.code.like(searchValue));
                predicate = ExpressionUtils.or(predicate, qUserInfo.name.like(searchValue));
                predicate = ExpressionUtils.or(predicate, qUserInfo.mobile.like(searchValue));
            } else {
                predicate = StrUtil.isBlank(param.getCode())  ? predicate : ExpressionUtils.or(predicate, qUserInfo.code.eq(param.getCode()));
                predicate = StrUtil.isBlank(param.getName()) ? predicate : ExpressionUtils.or(predicate, qUserInfo.name.eq(param.getName()));
                predicate = StrUtil.isBlank(param.getMobile()) ? predicate : ExpressionUtils.or(predicate, qUserInfo.mobile.eq(param.getMobile()));
                predicate = ObjectUtil.isEmpty(param.getPurchaserId()) ? predicate : ExpressionUtils.or(predicate, qPurchaser.id.eq(param.getPurchaserId()
                ));
            }
            predicate = ExpressionUtils.and(predicate, qPurchaserStaff.purchaser.organization.id.eq(org.getId()));

            QueryResults<PurchaserStaffDTO> queryResults = queryFactory.select(
                    Projections.bean(
                            PurchaserStaffDTO.class,
                            qPurchaserStaff.id,
                            qPurchaserStaff.name,
                            qPurchaserStaff.mobile,
                            qPurchaserStaff.purchaser.id.as("purchaserId"),
                            qPurchaserStaff.purchaser.name.as("purchaserName")
                    )
            ).distinct().from(qPurchaserStaff, qPurchaser)
                    .where(predicate)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchResults();

            return PageUtils.of(queryResults, pageable);

        }
        return PageUtils.empty();
    }
}
