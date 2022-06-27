package net.bncloud.contract.mapper;

import net.bncloud.contract.entity.ContractHistoryRel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import net.bncloud.contract.param.ContractHistoryRelParam;
import net.bncloud.common.base.domain.QueryParam;
import java.util.List;
/**
 * <p>
 * 合同与历史合同关联关系表 Mapper 接口
 * </p>
 *
 * @author huangtao
 * @since 2021-03-22
 */
public interface ContractHistoryRelMapper extends BaseMapper<ContractHistoryRel> {
		/**
		 * <p>
		 * 自定义分页
		 * </p>
		 *
		 * @author huangtao
		 * @since 2021-03-22
		 */
		List<ContractHistoryRel> selectListPage(IPage page, QueryParam<ContractHistoryRelParam> queryParam);
}
