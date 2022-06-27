package net.bncloud.financial.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.financial.entity.FinancialAttachment;
import net.bncloud.financial.param.FinancialAttachmentParam;

import java.util.List;

/**
 * <p>
 * 对账附件信息表 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2021-12-15
 */
public interface FinancialAttachmentMapper extends BaseMapper<FinancialAttachment> {
    /**
     * <p>
     * 自定义分页
     * </p>
     *
     * @author Auto-generator
     * @since 2021-12-15
     */
    List<FinancialAttachment> selectListPage(IPage page, QueryParam<FinancialAttachmentParam> pageParam);
}
