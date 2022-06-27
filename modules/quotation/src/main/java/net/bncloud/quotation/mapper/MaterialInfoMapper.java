package net.bncloud.quotation.mapper;

import net.bncloud.quotation.entity.MaterialInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.bncloud.quotation.param.MaterialInfoParam;
import net.bncloud.common.base.domain.QueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 物料信息表 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface MaterialInfoMapper extends BaseMapper<MaterialInfo> {
    /**
	 * 分页查询
     * @param page 分页
     * @param pageParam 分页参数
     * @return 列表
     */
    List<MaterialInfo> selectListPage(IPage page, QueryParam<MaterialInfoParam> pageParam);

    MaterialInfo selectOneByMaterialCode(@Param("materialCode") String materialCode);
}
