package net.bncloud.quotation.service;

import net.bncloud.quotation.entity.MaterialForm;
import net.bncloud.quotation.vo.MaterialFormVo;
import net.bncloud.quotation.param.MaterialFormParam;
import net.bncloud.base.BaseService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.common.base.domain.QueryParam;

/**
 * <p>
 * 物料表单信息表 服务类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
public interface MaterialFormService extends BaseService<MaterialForm> {

    /**
     * 自定义分页
     * @param page 分页参数
	 * @param pageParam  查询参数
     * @return 物料信息
     */
    IPage<MaterialForm> selectPage(IPage<MaterialForm> page, QueryParam<MaterialFormParam> pageParam);


	/**
	 * 保存物料表单信息
	 * @param materialForm 物料表单信息
	 */
	void saveInfo(MaterialFormVo materialForm);


	/**
	 * 查询物料表单详情信息
	 * @param id 物料表单主键
	 * @return 物料表单信息
	 */
	MaterialFormVo getInfoById(Long id);

	void updateMaterialForm(MaterialFormVo materialForm);
}
