package net.bncloud.quotation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.RestateSupplier;
import net.bncloud.quotation.param.RestateSupplierParam;
import net.bncloud.quotation.vo.RestateSupplierVo;

import java.util.List;

/**
 * <p>
 * 询价重报供应商邀请信息 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-08
 */
public interface RestateSupplierMapper extends BaseMapper<RestateSupplier> {
    /**
     * <p>
     * 自定义分页
     * </p>
     *
     * @author Auto-generator
     * @since 2022-03-08
     */
    List<RestateSupplierVo> selectListPage(IPage page, QueryParam<RestateSupplierParam> pageParam);
}
