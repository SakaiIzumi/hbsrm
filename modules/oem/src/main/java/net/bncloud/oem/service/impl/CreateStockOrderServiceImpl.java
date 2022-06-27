package net.bncloud.oem.service.impl;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import net.bncloud.oem.domain.entity.PurchaseOrder;
import net.bncloud.oem.domain.entity.PurchaseOrderReceiving;
import org.apache.ibatis.session.SqlSession;

public class CreateStockOrderServiceImpl {
    /**
     * 批量操作 SqlSessionPurchaseOrder
     */
    protected SqlSession sqlSessionBatchOrder() {
        return SqlHelper.sqlSessionBatch(currentModelClassOrder());
    }
    protected Class<PurchaseOrder> currentModelClassOrder() {
        return (Class<PurchaseOrder>) ReflectionKit.getSuperClassGenericType(getClass(), 1);
    }
    protected String sqlStatement(SqlMethod sqlMethod) {
        return SqlHelper.table(currentModelClassOrder()).getSqlStatement(sqlMethod.getMethod());
    }

    /**
     * 批量操作 SqlSessionReceiving
     */
    protected SqlSession sqlSessionBatchReceiving() {
        return SqlHelper.sqlSessionBatch(currentModelClassReceiving());
    }
    protected Class<PurchaseOrderReceiving> currentModelClassReceiving() {
        return (Class<PurchaseOrderReceiving>) ReflectionKit.getSuperClassGenericType(getClass(), 1);
    }
    protected String sqlStatementReceiving(SqlMethod sqlMethod) {
        return SqlHelper.table(currentModelClassReceiving()).getSqlStatement(sqlMethod.getMethod());
    }

}
