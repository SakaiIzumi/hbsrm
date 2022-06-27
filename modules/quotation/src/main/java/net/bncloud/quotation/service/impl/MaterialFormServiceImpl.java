package net.bncloud.quotation.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.bncloud.common.api.ResultCode;
import net.bncloud.common.base.globallock.DistributedLock;
import net.bncloud.common.base.globallock.lock.LockWrapper;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.exception.BizException;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.common.util.ObjectUtil;
import net.bncloud.quotation.entity.MaterialForm;
import net.bncloud.quotation.entity.MaterialFormExt;
import net.bncloud.quotation.entity.MaterialQuotationTemplate;
import net.bncloud.quotation.enums.QuotationResultCode;
import net.bncloud.quotation.mapper.MaterialFormMapper;
import net.bncloud.quotation.service.MaterialFormExtService;
import net.bncloud.quotation.service.MaterialFormService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import net.bncloud.quotation.vo.MaterialFormVo;
import net.bncloud.quotation.param.MaterialFormParam;
import net.bncloud.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.common.base.domain.QueryParam;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <p>
 * 物料表单信息表 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Service
public class MaterialFormServiceImpl extends BaseServiceImpl<MaterialFormMapper, MaterialForm> implements MaterialFormService {

	private final MaterialFormExtService materialFormExtService;

	private static final String MATERIAL_SAVE_LOCK_PREFIX_KEY = "MATERIAL_SAVE_LOCK:materialSave";

	public MaterialFormServiceImpl(MaterialFormExtService materialFormExtService) {
		this.materialFormExtService = materialFormExtService;
	}

	@Autowired
	private  DistributedLock distributedLock;

	@Override
    public IPage<MaterialForm> selectPage(IPage<MaterialForm> page, QueryParam<MaterialFormParam> pageParam) {
        // 若不使用mybatis-plus自带的分页方法，则不会自动带入tenantId，所以我们需要自行注入
		MaterialFormParam param = pageParam.getParam();
		if (param == null) {
			pageParam.setParam(new MaterialFormParam());
		}
		return page.setRecords(baseMapper.selectListPage(page, pageParam));
    }

    @Override
	@Transactional(rollbackFor = Exception.class)
    public void saveInfo(MaterialFormVo materialForm) {
		if( StrUtil.isBlank(materialForm.getMaterialName()) ){
			throw new ApiException(500,"请填写正确的表单名");
		}

		String lockKey  = MATERIAL_SAVE_LOCK_PREFIX_KEY + materialForm.getMaterialName();
		LockWrapper lockWrapper = new LockWrapper().setKey(lockKey).setWaitTime(0).setLeaseTime(10).setUnit(TimeUnit.MINUTES);;
		distributedLock.tryLock(lockWrapper,()->{
			//校验，新建表单设计器名称不能和已存在的表单的名称一样
			MaterialForm materialFormForexist = getOne(new LambdaQueryWrapper<MaterialForm>().eq(MaterialForm::getFormName, materialForm.getFormName()));
			if (materialFormForexist!=null){
				throw new ApiException(ResultCode.INTERNAL_SERVER_ERROR.getCode(), "表单名称已经被使用，请重新命名！");
			}
			save(materialForm);
			List<MaterialFormExt> materialFormExtList = materialForm.getMaterialFormExtList();
			//保存物料表单字段信息
			cascade(materialFormExtList,materialForm);
			return null;
		},()->{
			return null;
		});

	}

	/**
	 * 查询物料表单信息
	 * @param id 物料表单主键
	 * @return 物料表单信息
	 */
	@Override
	public MaterialFormVo getInfoById(Long id) {
		MaterialForm materialForm = getById(id);
		if (materialForm == null) {
		    throw new BizException(QuotationResultCode.SOURCE_NOT_FOUND);
		}
		MaterialFormVo materialFormVo = BeanUtil.copyProperties(materialForm, MaterialFormVo.class);
		LambdaQueryWrapper<MaterialFormExt> wrapper = Condition.getQueryWrapper(new MaterialFormExt()).lambda()
				.eq(MaterialFormExt::getMaterialFormId,id);
		wrapper.orderByAsc(MaterialFormExt::getOrderValue);
		List<MaterialFormExt> materialFormExtList = materialFormExtService.list(wrapper);
		assert materialFormVo != null;
		materialFormVo.setMaterialFormExtList(materialFormExtList);

		return materialFormVo;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMaterialForm(MaterialFormVo materialFormVo) {
		//校验，更新表单设计器后名称不能和已存在的表单的名称一样
		MaterialForm materialFormForexist = getOne(new LambdaQueryWrapper<MaterialForm>()
				                                        .eq(MaterialForm::getFormName, materialFormVo.getFormName())
				                                        .and(item->item.ne(MaterialForm::getId,materialFormVo.getId())) );

		if (materialFormForexist!=null){
			throw new ApiException(ResultCode.INTERNAL_SERVER_ERROR.getCode(), "表单名称已经被使用，请重新命名！");
		}
		this.updateById(materialFormVo);

		List<MaterialFormExt> materialFormExtList = materialFormVo.getMaterialFormExtList();
		materialFormExtService.deleteByFromId(materialFormVo.getId());
		cascade(materialFormExtList,materialFormVo);
	}

	public void cascade(List<MaterialFormExt> materialFormExtList,MaterialFormVo materialForm){
		if(CollectionUtil.isNotEmpty(materialFormExtList)){
			AtomicInteger orderValue = new AtomicInteger();
			List<MaterialFormExt> materialFormExtListToSave = materialFormExtList.stream().map(formExt -> {
				MaterialFormExt materialFormExt = new MaterialFormExt();
				BeanUtil.copyProperties(formExt, materialFormExt,"id");
				/*if(ObjectUtil.isNotEmpty(materialFormExt.getProps())){
					materialFormExt.setProps(JSON.toJSONString(materialFormExt.getProps()));
				}*/
				materialFormExt.setMaterialFormId(materialForm.getId());
				materialFormExt.setOrderValue(orderValue.incrementAndGet());
				return materialFormExt;
			}).collect(Collectors.toList());
			materialFormExtService.saveBatch(materialFormExtListToSave);
			materialForm.setExtContent(JSON.toJSONString(materialFormExtListToSave));
			updateById(materialForm);
		}

	}

}
