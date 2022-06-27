package net.bncloud.quotation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.quotation.entity.TRfqVerificationCode;
import net.bncloud.quotation.mapper.TRfqVerificationCodeMapper;
import net.bncloud.quotation.param.TRfqVerificationCodeParam;
import net.bncloud.quotation.service.ITRfqVerificationCodeService;
import net.bncloud.quotation.vo.TRfqVerificationCodeVo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 短信验证码信息表 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-03-07
 */
@Service
public class TRfqVerificationCodeServiceImpl extends BaseServiceImpl<TRfqVerificationCodeMapper, TRfqVerificationCode> implements ITRfqVerificationCodeService {

		@Override
		public IPage<TRfqVerificationCodeVo> selectPage(IPage<TRfqVerificationCodeVo> page, QueryParam<TRfqVerificationCodeParam> pageParam) {
		// 若不使用mybatis-plus自带的分页方法，则不会自动带入tenantId，所以我们需要自行注入
		//notice.setTenantId(SecureUtil.getTenantId());
		return page.setRecords(baseMapper.selectListPage(page, pageParam));
		}
}
