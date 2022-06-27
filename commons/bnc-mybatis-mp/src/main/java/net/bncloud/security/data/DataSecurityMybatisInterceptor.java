package net.bncloud.security.data;

import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.security.Role;
import net.bncloud.common.security.data.*;
import net.bncloud.utils.AuthUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author 武书静 wusj4 shujing.wu@meicloud.com
 */
@Intercepts(@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))
@Slf4j
public class DataSecurityMybatisInterceptor implements Interceptor, PriorityOrdered {


    private final String appCode;

    private final DataSubjectHolder dataSubjectHolder;

    private final GrantDataHolder grantDataHolder;

    public DataSecurityMybatisInterceptor(String appCode, DataSubjectHolder dataSubjectHolder, GrantDataHolder grantDataHolder) {
        this.appCode = appCode;
        this.dataSubjectHolder = dataSubjectHolder;
        this.grantDataHolder = grantDataHolder;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];

        if (mappedStatement.getSqlCommandType() != SqlCommandType.SELECT) {
            return invocation.proceed();
        }

        String id = mappedStatement.getId();
        // 数据主题
        List<DataSubject> dataSubjects = dataSubjectHolder.get(appCode, id);
        if (dataSubjects == null || dataSubjects.isEmpty()) {
            return invocation.proceed();
        }

        // 获取sql
        String sql = getSqlByInvocation(invocation);
        if (StringUtils.isBlank(sql)) {
            return invocation.proceed();
        }
        // sql交由处理类处理  对sql语句进行处理  此处是范例  不做任何处理
        String sql2Reset = sql;

        StringBuilder lastOrderByOrLimitClause = new StringBuilder("");
        // 判断原SQL语句的最后子句的类型
        LastClauseType lastClauseType = getLastSqlClauseType(sql2Reset);
        // 若最后子句的类型为ORDER BY或LIMIT，则先去掉，待拼上数据权限的查询条件后再加上
        sql2Reset = getSqlExcludeLastOrderByOrLimitClause(sql2Reset, lastClauseType, lastOrderByOrLimitClause);

        String whereSql = dataSubjects.stream()
                .map(this::buildSubjectDimensionSql)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" \n OR "));
        if (StringUtils.isNotBlank(whereSql)) {
            sql2Reset = sql2Reset + " \n AND (" + whereSql + ")";
        }

        // 若原来的最后子句为ORDER BY/LIMIT,则现在重新加回来
        sql2Reset = addOrderByOrLimitClauseToSql(sql2Reset, lastClauseType, lastOrderByOrLimitClause);

        // 包装sql后，重置到invocation中
        resetSql2Invocation(invocation, sql2Reset);

        // 返回，继续执行
        return invocation.proceed();
    }

    /**
     * 将之前去掉的最后的ORDER BY子句/LIMIT子句加回SQL上
     *
     * @param sql
     * @param lastClauseType
     * @param lastOrderByOrLimitClause
     * @return
     */
    private String addOrderByOrLimitClauseToSql(String sql, LastClauseType lastClauseType, StringBuilder lastOrderByOrLimitClause) {
        if (LastClauseType.ORDER_BY == lastClauseType) {
            return sql + " ORDER BY " + lastOrderByOrLimitClause.toString();
        } else if (LastClauseType.LIMIT == lastClauseType) {
            return sql + " LIMIT " + lastOrderByOrLimitClause.toString();
        }
        return sql;
    }

    /**
     * 获取去掉了最后的ORDER BY子句/LIMIT子句的SQL
     *
     * @param sql
     * @param lastClauseType
     * @param lastOrderByOrLimitClause
     * @return
     */
    private String getSqlExcludeLastOrderByOrLimitClause(String sql, LastClauseType lastClauseType, StringBuilder lastOrderByOrLimitClause) {
        if (LastClauseType.ORDER_BY == lastClauseType) {
            Pattern pattern = Pattern.compile("[oO][rR][dD][eE][rR]\\s+[bB][yY]\\s+");
            String[] temp = pattern.split(sql.toUpperCase());
            if (temp.length < 2) {
                return sql;
            }
            StringBuilder sqlBuilder = new StringBuilder("");
            for (int ix = 0; ix < temp.length - 2; ix++) {
                sqlBuilder.append(temp[ix] + " ORDER BY ");
            }
            sqlBuilder.append(temp[temp.length - 2]);
            lastOrderByOrLimitClause.append(temp[temp.length - 1]);
            return sqlBuilder.toString();
        } else if (LastClauseType.LIMIT == lastClauseType) {
            StringBuilder sqlBuilder = new StringBuilder("");
            String[] temp = sql.toUpperCase().split("LIMIT");
            if (temp.length < 2) {
                return sql;
            }
            for (int ix = 0; ix < temp.length - 2; ix++) {
                sqlBuilder.append(temp[ix] + " LIMIT ");
            }
            sqlBuilder.append(temp[temp.length - 2]);
            lastOrderByOrLimitClause.append(temp[temp.length - 1]);
            return sqlBuilder.toString();
        } else {
            return sql;
        }
    }

    /**
     * 获取SQL的最后子句的类型
     *
     * @param sql
     * @return
     */
    private LastClauseType getLastSqlClauseType(String sql) {
        // 检测ORDER BY子句
        Pattern orderByClausePattern = Pattern.compile("[oO][rR][dD][eE][rR]\\s+[bB][yY]\\s+");
        String[] temp = orderByClausePattern.split(sql);
        if (temp.length < 2) {
            return LastClauseType.OTHERS;
        }
        String lastStr = temp[temp.length - 1].trim().toUpperCase();
        if (lastStr.toUpperCase().endsWith("DESC") || lastStr.toUpperCase().endsWith("ASC")) {
            return LastClauseType.ORDER_BY;
        }

        // 最后的字符串非DESC或ASC，则lastStr中可能含有LIMIT子句或ORDER BY以默认顺序排序（没显示写ASC）
        Pattern limitClausePattern = Pattern.compile("[lL][iI][mM][iI][tT]\\s+\\d+\\s*,\\s*\\d+$");
        Matcher limitMatcher = limitClausePattern.matcher(lastStr);
        int limitClauseCount = 0;
        while (limitMatcher.find()) {
            limitClauseCount++;
        }
        if (limitClauseCount > 1) {
            // 检测到多个LIMIT子句，不符合要求
            return LastClauseType.OTHERS;
        } else if (limitClauseCount == 1) {
            if (!(lastStr.contains("AND") || lastStr.contains("OR") || lastStr.contains("WHERE") || lastStr.contains("FROM"))) {
                // LIMIT子句前仅有ORDER BY子句，无其他子句
                return LastClauseType.ORDER_BY;
            }
            // LIMIT子句结尾
            return LastClauseType.LIMIT;
        } else {
            // 非LIMIT子句结束,需进一步判断后面是否还有其他SQL关键词
            if (!(lastStr.contains("AND") || lastStr.contains("OR") || lastStr.contains("WHERE") || lastStr.contains("FROM"))) {
                return LastClauseType.ORDER_BY;
            }
            return LastClauseType.OTHERS;
        }
    }

    private String buildSubjectDimensionSql(DataSubject dataSubject) {
        if (dataSubject == null) {
            return null;
        }
        List<DataDimension> dimensions = dataSubject.getDimensions();
        if (dimensions == null || dimensions.isEmpty()) {
            return null;
        }
//        String userName = AuthUtil.getUser().getUserName();
//        Long userId = AuthUtil.getUser().getUserId();

        Set<Role> roles = AuthUtil.getUser().getRoles();
        if (roles == null || roles.isEmpty()) {
            return null;
        }
        /*String sql = dimensions.stream()
                .map(dimension -> grantDataHolder.get(username, dataSubject.getId(), dimension.getCode()))
                .map(this::buildDimensionSql)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" AND "));*/

        final List<String> dimensionSqlList = new ArrayList<>();
        for (DataDimension dimension : dimensions) {
            boolean useDefault = dimension.isUseDefault();
            if (useDefault) {
                String code = dimension.getCode();
                // TODO 通过维度code从缓存获取默认值（base应用中用户登录时加载到缓存）
                // TODO build sql and add to dimensionSqlList
            } else {
                //TODO username 非帐号名 可能重复,
                Set<DataDimensionGrant> dataDimensionGrants = new HashSet<>();
                for (Role role : roles) {
                    DataDimensionGrant dataDimensionGrant = grantDataHolder.get(role.getId().toString(), dataSubject.getId(), dimension.getCode());
                    if (dataDimensionGrants.contains(dataDimensionGrant)) {
                        continue;
                    } else {
                        dataDimensionGrants.add(dataDimensionGrant);
                    }
                    String dimensionSql = buildDimensionSql(dataDimensionGrant, dimension);
                    dimensionSqlList.add(dimensionSql);
                }

            }
        }
        String sql = dimensionSqlList.stream().filter(Objects::nonNull).collect(Collectors.joining(" AND "));
        if (StringUtils.isBlank(sql)) {
            return " (1=1) ";
        } else {
            return " (" + sql + ") ";
        }
    }


    /**
     * 同一主题同一维度 IN
     */
    private String buildDimensionSql(DataDimensionGrant dataDimensionGrant, DataDimension dimension) {
        if (dataDimensionGrant == null) {
            return null;
        }
        String alias = dataDimensionGrant.getAlias();

        if (StringUtils.isBlank(alias)) {
            return null;
        }

        // 特权不控
        if (dataDimensionGrant.isSpecial()) {
            return null;
        }
        List<String> dataIds = dataDimensionGrant.getDataIds();
//        if ("String".equals(dimension.getType())) {
//            dataIds = dataIds.stream().map(d -> "'" + d + "'").collect(Collectors.toList());
//        }
        dataIds = dataIds.stream().map(d -> "'" + d + "'").collect(Collectors.toList());
        // 不是特权又没有被授权，则不满足
        if (dataIds == null || dataIds.isEmpty()) {
            return "(1=0)";
        }
        return alias + " IN (" + String.join(",", dataIds) + ")";
    }

    @Override
    public Object plugin(Object obj) {
        return Plugin.wrap(obj, this);
    }

    @Override
    public void setProperties(Properties arg0) {
        // doSomething
    }


    /**
     * 获取sql语句
     */
    private String getSqlByInvocation(Invocation invocation) {
        final Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameterObject = args[1];
        BoundSql boundSql = ms.getBoundSql(parameterObject);
        return boundSql.getSql();
    }

    /**
     * 包装sql后，重置到invocation中
     */
    private void resetSql2Invocation(Invocation invocation, String sql) throws SQLException {
        final Object[] args = invocation.getArgs();
        MappedStatement statement = (MappedStatement) args[0];
        Object parameterObject = args[1];
        BoundSql boundSql = statement.getBoundSql(parameterObject);
        MappedStatement newStatement = newMappedStatement(statement, new BoundSqlSqlSource(boundSql));
        MetaObject msObject = MetaObject.forObject(newStatement, new DefaultObjectFactory(), new DefaultObjectWrapperFactory(), new DefaultReflectorFactory());
        msObject.setValue("sqlSource.boundSql.sql", sql);
        args[0] = newStatement;
    }

    private MappedStatement newMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder =
                new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    private String getOperateType(Invocation invocation) {
        final Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        SqlCommandType commondType = ms.getSqlCommandType();
        if (commondType.compareTo(SqlCommandType.SELECT) == 0) {
            return "select";
        }
        if (commondType.compareTo(SqlCommandType.INSERT) == 0) {
            return "insert";
        }
        if (commondType.compareTo(SqlCommandType.UPDATE) == 0) {
            return "update";
        }
        if (commondType.compareTo(SqlCommandType.DELETE) == 0) {
            return "delete";
        }
        return null;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    // 定义一个内部辅助类，作用是包装sq
    class BoundSqlSqlSource implements SqlSource {
        private BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }
}
