package net.bncloud.quotation.service;

import net.bncloud.quotation.entity.MaterialFormExt;
import net.bncloud.quotation.param.MaterialFormExtParam;
import net.bncloud.base.BaseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;

/**
 * <p>
 * 物料表单扩展信息 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface MaterialFormExtService extends BaseService<MaterialFormExt> {

		/**
         * 自定义分页
         * @param page
         * @param pageParam
         * @return
         */
		IPage<MaterialFormExt> selectPage(IPage<MaterialFormExt> page, QueryParam<MaterialFormExtParam> pageParam);

		void deleteByFromId(Long id);


}
