package net.bncloud.quotation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseService;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.TRfqVerificationCode;
import net.bncloud.quotation.param.TRfqVerificationCodeParam;
import net.bncloud.quotation.vo.TRfqVerificationCodeVo;

/**
 * <p>
 * 短信验证码信息表 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-07
 */
public interface ITRfqVerificationCodeService extends BaseService<TRfqVerificationCode> {

		/**
         * 自定义分页
         * @param page
         * @param pageParam
         * @return
         */
		IPage<TRfqVerificationCodeVo> selectPage(IPage<TRfqVerificationCodeVo> page, QueryParam<TRfqVerificationCodeParam> pageParam);


}
