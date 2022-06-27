package net.bncloud.quotation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.RestateBase;
import net.bncloud.quotation.param.RestateBaseParam;
import net.bncloud.quotation.vo.RestateBaseVo;

/**
 * <p>
 * 询价重报基础信息 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-08
 */
public interface IRestateBaseService extends BaseService<RestateBase> {

		/**
         * 自定义分页
         * @param page
         * @param pageParam
         * @return
         */
		IPage<RestateBaseVo> selectPage(IPage<RestateBaseVo> page, QueryParam<RestateBaseParam> pageParam);


}
