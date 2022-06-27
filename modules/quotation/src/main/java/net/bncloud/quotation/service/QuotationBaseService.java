package net.bncloud.quotation.service;

import net.bncloud.common.api.R;
import net.bncloud.quotation.entity.QuotationBase;
import net.bncloud.quotation.entity.QuotationLineExt;
import net.bncloud.quotation.param.QuotationBaseRestateParam;
import net.bncloud.quotation.vo.*;
import net.bncloud.quotation.param.QuotationBaseParam;
import net.bncloud.base.BaseService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 询价基础信息 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface QuotationBaseService extends BaseService<QuotationBase> {

    IPage<QuotationBaseVo> selectQuotationBaseSalePage(IPage<QuotationBase> page, QueryParam<QuotationBaseParam> pageParam) throws Exception;

    /**
     * 自定义分页
     *
     * @param page
     * @param pageParam
     * @return
     */
    IPage<QuotationBaseVo> selectPage(IPage<QuotationBase> page, QueryParam<QuotationBaseParam> pageParam);


    /**
     * 查询详细信息
     *
     * @param id 主键ID
     * @return 询价基础信息
     */
    QuotationBaseVo getInfoById4Copy(Long id);

    Object calculateQuotationInfo(Long quotationId, List<QuotationLineExt> quotationLineExtList);

    QuotationBaseVo getSaleInfoById(Long id) throws Exception;

    QuotationBaseVo getInfoById(Long id);

    /**
     * 保存询价基础信息
     *
     * @param quotationBase 询价基础信息
     * @return 询价基础信息主键
     */
    Long saveInfo(QuotationBaseVo quotationBase);

    void copyQuotation(Long id);

    /**
     * 转换字典编码等信息
     *
     * @param list 询价单列表
     * @return 询价单列表
     */
    List<QuotationBaseData> convertDictCode(List<QuotationBase> list);

    void updateDate(QuotationUpdateDateVo quotationUpdateDateVo);

    void exportData(QuotationBase quotationBase, HttpServletResponse response);

    /**
     * 更新询价基础信息
     *
     * @param quotationBase 询价基础信息
     */
    void updateInfo(QuotationBaseVo quotationBase);

    /**
     * 导出销售协同的询价基础订单信息
     */
    @Deprecated
    void exportSaleExcelData(String fileName, QueryParam<QuotationBaseParam> pageParam, HttpServletResponse servletResponse) throws Exception;

    void updateDisableStatus(Long quotationId);

    R<String> confirmMarked(Long quotationId) throws Exception;

    void updateInvalideStatus(Long id);

    void saveQuotePriceInfo(Long quotationId, List<QuotationLineExtVo> quotationLineExtList) throws Exception;

    void restateDo(Long quotationBaseId, QuotationBaseRestateParam quotationBaseRestateParam);


    /**
     *
     * @param quotationId
     * @param quotationLineExtList
     * @return
     */
	List<QuotationLineExtVo> computeLineExtPrice(Long quotationId,List<QuotationLineExt> quotationLineExtList);
    QuotationBase restateCheck(Long quotationBaseId);

    Object restateEnableSupplier(Long quotationBaseId);

    QuotationStaticCountVo getQuotationStaticCount();

    Integer getQuotationSingleStaticCount(String status);

    /**
     * 询价单数量统计
     * @return 数量
     */
    QuotationStaticCountVo statics();

    /**
     * 列表查询
     * @param queryParam
     * @return
     */
    List<QuotationBase> queryList(QueryParam<QuotationBaseParam> queryParam);

    /**
     * 列表查询-销售工作台
     * @param queryParam
     * @return
     */
    List<QuotationBase> querySaleList(QueryParam<QuotationBaseParam> queryParam);

    /**
     * 发布询价单
     * @param quotationBaseId 询价单ID
     * @return 询价单ID
     */
    Long publish(Long quotationBaseId);

    /**
     * 供应商应标数量+1
     * @param quotationBaseId 询价单基础信息ID
     * @return 修改记录数
     */
    Integer responseNumIncrease(Long quotationBaseId);

    /**
     * 定时任务检查询价单报价是否到达截止时间并修改状态
     */
    void modifyQuotationStatus();


    /**
     * 转换字典编码等信息
     *
     * @param list 询价单列表
     * @return 询价单列表
     */
    List<QuotationBaseSaleData> convertSaleDictCode(List<QuotationBase> list);

    /**
     * 修改询价单预警设置
     */
    Boolean earlyWranningSwitch(Long quotationBaseId);

    /**
     * 获取预警信息接口
     */
    EarlyWranningVo selectEarlyWranning(Long quotationBaseId);

    /**
     * 校验询价单是否存在
     * @param quotationBaseId 询价单基础信息ID
     */
    void checkQuotationExist(Long quotationBaseId);
}
