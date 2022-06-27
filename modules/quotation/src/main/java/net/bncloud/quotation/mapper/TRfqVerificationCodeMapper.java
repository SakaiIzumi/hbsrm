package net.bncloud.quotation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.TRfqVerificationCode;
import net.bncloud.quotation.param.TRfqVerificationCodeParam;
import net.bncloud.quotation.vo.TRfqVerificationCodeVo;

import java.util.List;
/**
 * <p>
 * 短信验证码信息表 Mapper 接口
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-07
 */
public interface TRfqVerificationCodeMapper extends BaseMapper<TRfqVerificationCode> {
		/**
		 * <p>
		 * 自定义分页
		 * </p>
		 *
		 * @author Auto-generator
		 * @since 2022-03-07
		 */
		List<TRfqVerificationCodeVo> selectListPage(IPage page, QueryParam<TRfqVerificationCodeParam> pageParam);
}
