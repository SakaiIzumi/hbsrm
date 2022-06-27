package net.bncloud.quotation.mapper;

import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.TRfqQuotationRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.bncloud.quotation.vo.TRfqQuotationRecordVo;
import net.bncloud.quotation.param.TRfqQuotationRecordParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报价记录信息 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-25
 */
public interface TRfqQuotationRecordMapper extends BaseMapper<TRfqQuotationRecord> {
    /**
     * <p>
     * 自定义分页
     * </p>
     *
     * @author Auto-generator
     * @since 2022-02-25
     */
    List<TRfqQuotationRecord> selectListPage(IPage page, QueryParam<TRfqQuotationRecordParam> pageParam);


    List<TRfqQuotationRecord> querylistLately(@Param("quotationBaseId") Long quotationBaseId);
}
