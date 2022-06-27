package net.bncloud.quotation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.RestateBase;
import net.bncloud.quotation.param.RestateBaseParam;
import net.bncloud.quotation.vo.RestateBaseVo;

import java.util.List;

/**
 * <p>
 * 询价重报基础信息 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-08
 */
public interface RestateBaseMapper extends BaseMapper<RestateBase> {
    /**
     * <p>
     * 自定义分页
     * </p>
     *
     * @author Auto-generator
     * @since 2022-03-08
     */
    List<RestateBaseVo> selectListPage(IPage page, QueryParam<RestateBaseParam> pageParam);
}
