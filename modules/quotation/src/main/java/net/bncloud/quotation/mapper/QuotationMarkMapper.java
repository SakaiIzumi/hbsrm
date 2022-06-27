package net.bncloud.quotation.mapper;

import net.bncloud.quotation.entity.QuotationMark;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.bncloud.quotation.vo.QuotationMarkVo;
import net.bncloud.quotation.param.QuotationMarkParam;
import net.bncloud.common.base.domain.QueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 询价单应标关联表 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-01
 */
public interface QuotationMarkMapper extends BaseMapper<QuotationMark> {
    /**
     * <p>
     * 自定义分页
     * </p>
     *
     * @author Auto-generator
     * @since 2022-03-01
     */
    List<QuotationMark> selectListPage(IPage page, QueryParam<QuotationMarkParam> pageParam);

}
