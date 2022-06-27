package net.bncloud.saas.authorize.service;

import cn.hutool.core.util.StrUtil;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.saas.authorize.domain.DataDimension;
import net.bncloud.saas.authorize.domain.QDataDimension;
import net.bncloud.saas.authorize.repository.DataDimensionRepository;
import net.bncloud.saas.authorize.service.query.DataDimensionQuery;
import net.bncloud.saas.utils.pageable.PageUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@AllArgsConstructor
@Slf4j
public class DataDimensionService {

    private final DataDimensionRepository dataDimensionRepository;

    private final QDataDimension qDataDimension = QDataDimension.dataDimension;

    private final JPAQueryFactory queryFactory;

    public List<DataDimension> queryAll(DataDimensionQuery query) {
        Predicate predicate = ExpressionUtils.anyOf();
        predicate = StrUtil.isBlank(query.getDimensionCode()) ? predicate : qDataDimension.dimensionCode.eq(query.getDimensionCode());
        predicate = StrUtil.isBlank(query.getDimensionName()) ? predicate : qDataDimension.dimensionType.like(query.getDimensionName());
        return queryFactory.select(qDataDimension).from(qDataDimension)
                .where(predicate)
                .fetch();
    }

    public Object queryPage(DataDimensionQuery query, Pageable pageable) {
        Predicate predicate = ExpressionUtils.anyOf();
        predicate = StrUtil.isBlank(query.getDimensionCode()) ? predicate : qDataDimension.dimensionCode.eq(query.getDimensionCode());
        predicate = StrUtil.isBlank(query.getDimensionName()) ? predicate : qDataDimension.dimensionType.like(query.getDimensionName());
        QueryResults<DataDimension> dataDimensionQueryResults = queryFactory.select(qDataDimension).from(qDataDimension)
                .where(predicate)
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return PageUtils.of(dataDimensionQueryResults, pageable);
    }
}
