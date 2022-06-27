package net.bncloud.saas.authorize.service;

import cn.hutool.core.util.StrUtil;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.saas.authorize.domain.DesensitizeType;
import net.bncloud.saas.authorize.domain.QDesensitizeType;
import net.bncloud.saas.authorize.repository.DesensitizeTypeRepository;
import net.bncloud.saas.authorize.service.query.DesensitizeTypeQuery;
import net.bncloud.saas.utils.pageable.PageUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class DesensitizeTypeService {

    private final JPAQueryFactory queryFactory;

    private final DesensitizeTypeRepository desensitizeTypeRepository;

    private final QDesensitizeType qDesensitizeType = QDesensitizeType.desensitizeType;

    public Page<DesensitizeType> queryPage(DesensitizeTypeQuery query, Pageable pageable) {
        Predicate predicate = ExpressionUtils.anyOf();
        String qs = query.getQs();
        if (StrUtil.isNotBlank(qs)) {
            predicate = ExpressionUtils.or(predicate, qDesensitizeType.dimensionType.like("%" + qs + "%"));
            predicate = ExpressionUtils.or(predicate, qDesensitizeType.name.like("%" + qs + "%"));
            predicate = ExpressionUtils.or(predicate, qDesensitizeType.code.like("%" + qs + "%"));
        }
        predicate = StrUtil.isBlank(query.getDimensionType()) ? predicate : ExpressionUtils.and(predicate, qDesensitizeType.dimensionType.eq(query.getDimensionType()));
        predicate = StrUtil.isBlank(query.getName()) ? predicate : ExpressionUtils.and(predicate, qDesensitizeType.name.eq(query.getDimensionType()));
        predicate = StrUtil.isBlank(query.getValue()) ? predicate : ExpressionUtils.and(predicate, qDesensitizeType.code.eq(query.getDimensionType()));

        QueryResults<DesensitizeType> desensitizeTypeQueryResults =
                queryFactory.select(qDesensitizeType).from(qDesensitizeType)
                        .where(predicate)
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetchResults();
        return PageUtils.of(desensitizeTypeQueryResults, pageable);
    }
}
