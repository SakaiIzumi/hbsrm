package net.bncloud.delivery.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.delivery.entity.SupplyDemandBalance;
import net.bncloud.delivery.entity.SupplyDemandDetailView;
import net.bncloud.delivery.param.SupplyDemandBalanceParam;
import net.bncloud.delivery.param.SupplyDemandDetailViewParam;
import org.springframework.data.domain.PageImpl;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ddh
 * @description
 * @since 2022/4/8
 */
public interface SupplyDemandBalanceService{

    PageImpl<SupplyDemandDetailView> selectReportDetailPage(IPage<SupplyDemandDetailView> page, QueryParam<SupplyDemandDetailViewParam> queryParam);

    void exportReportDetail(HttpServletResponse response, QueryParam<SupplyDemandDetailViewParam> queryParam) throws IOException;

    PageImpl<SupplyDemandBalance> selectReportPage(IPage<SupplyDemandBalance> page,QueryParam<SupplyDemandBalanceParam> queryParam);

    void exportReport(HttpServletResponse response, QueryParam<SupplyDemandBalanceParam> queryParam) throws IOException;

    void batchCalculateReport(String supplier);
}
