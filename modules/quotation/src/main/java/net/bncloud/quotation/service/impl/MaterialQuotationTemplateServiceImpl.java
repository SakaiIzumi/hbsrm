package net.bncloud.quotation.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.udojava.evalex.Expression;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.base.BaseServiceImpl;
import net.bncloud.base.BaseUserEntity;
import net.bncloud.common.api.ResultCode;
import net.bncloud.common.base.domain.QueryParam;
import net.bncloud.common.bizutil.number.NumberFactory;
import net.bncloud.common.bizutil.number.NumberType;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.exception.BizException;
import net.bncloud.common.util.BeanUtil;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.common.util.ObjectUtil;
import net.bncloud.quotation.entity.MaterialQuotationTemplate;
import net.bncloud.quotation.entity.MaterialTemplateExt;
import net.bncloud.quotation.enums.QuotationDataType;
import net.bncloud.quotation.enums.QuotationResultCode;
import net.bncloud.quotation.mapper.MaterialQuotationTemplateMapper;
import net.bncloud.quotation.param.MaterialQuotationTemplateParam;
import net.bncloud.quotation.service.MaterialQuotationTemplateService;
import net.bncloud.quotation.service.MaterialTemplateExtService;
import net.bncloud.quotation.utils.evalex.EvalexUtil;
import net.bncloud.quotation.vo.MaterialQuotationTemplateVo;
import net.bncloud.support.Condition;
import net.bncloud.utils.AuthUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 物料报价模板 服务实现类
 * </p>
 *
 * @author Auto-generator
 * @since 2022-02-14
 */
@Service
@Slf4j
public class MaterialQuotationTemplateServiceImpl extends BaseServiceImpl<MaterialQuotationTemplateMapper, MaterialQuotationTemplate> implements MaterialQuotationTemplateService {

	private final MaterialTemplateExtService materialTemplateExtService;
	private final NumberFactory numberFactory;
	public MaterialQuotationTemplateServiceImpl(MaterialTemplateExtService materialTemplateExtService,NumberFactory numberFactory) {
		this.materialTemplateExtService = materialTemplateExtService;
		this.numberFactory=numberFactory;
	}


	@Override
    public IPage<MaterialQuotationTemplate> selectPage(IPage<MaterialQuotationTemplate> page, QueryParam<MaterialQuotationTemplateParam> pageParam) {
        return page.setRecords(baseMapper.selectListPage(page, pageParam));
    }

    /**
     * 新增物料模板信息
     *
     * @param materialQuotationTemplate 物料询价模板信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveInfo(MaterialQuotationTemplateVo materialQuotationTemplate) {
		if( StrUtil.isBlank(materialQuotationTemplate.getTemplateName()) ){
			throw new ApiException(500,"请填写正确的模板名");
		}

    	//判断是否调用结果测试按钮并且通过
		//校验模板公式
		validateSuccessType(materialQuotationTemplate);
//		calculateSuccessType(materialQuotationTemplate.getCalculateSuccessType());

		//校验，物料模板的名称不能和已存在的模板的名称一样
		MaterialQuotationTemplate template = getOne(new LambdaQueryWrapper<MaterialQuotationTemplate>().eq(MaterialQuotationTemplate::getTemplateName, materialQuotationTemplate.getTemplateName()));
		if (template!=null){
			throw new ApiException(ResultCode.INTERNAL_SERVER_ERROR.getCode(), "模板名称已经被使用，请重新命名！");
		}
		//获取当前登录信息
		BaseUserEntity user = AuthUtil.getUser();
		materialQuotationTemplate.setCreatedName(user.getUserName());
		//materialQuotationTemplate.setTemplateCode(new NumberFactory(NumberType.quotation_template).buildNumber());
		materialQuotationTemplate.setTemplateCode(numberFactory.buildNumber(NumberType.quotation_template));
        save(materialQuotationTemplate);


        cascadeSaveTemplateExt(materialQuotationTemplate);
    }

	/**
	 * 级联保存模板扩展信息
	 * @param materialQuotationTemplate 模板信息
	 */
	private void cascadeSaveTemplateExt(MaterialQuotationTemplateVo materialQuotationTemplate) {
		List<MaterialTemplateExt> materialTemplateExtList = materialQuotationTemplate.getMaterialTemplateExtList();

		Integer maxOrderValue = materialTemplateExtList.stream()
				.filter(item -> item.getOrderValue() != null)
				.map(item -> {
					return Integer.valueOf(item.getOrderValue());
				})
				.collect(Collectors.toList())
				.stream().max(Integer::compareTo)
				.get();

		if (CollectionUtil.isNotEmpty(materialTemplateExtList)) {
			List<MaterialTemplateExt> templateExtListToSave = materialTemplateExtList.stream().map(item -> {
						MaterialTemplateExt templateExt = new MaterialTemplateExt();
						BeanUtil.copyProperties(item, templateExt, "id");
						templateExt.setTemplateId(materialQuotationTemplate.getId());
						/*if (QuotationDataType.expression.name().equals(templateExt.getDataType())) {
							templateExt.setExpressionValue(new Expression(item.getExpression()).eval());
						}*/
						return templateExt;
					}
			).collect(Collectors.toList());

			for (MaterialTemplateExt materialTemplateExt : templateExtListToSave) {
				if (QuotationDataType.expression.name().equals(materialTemplateExt.getDataType())) {
					materialTemplateExt.setOrderValue(++maxOrderValue+"");
				}
			}

			materialTemplateExtService.saveBatch(templateExtListToSave);

			materialQuotationTemplate.setExtContent(JSON.toJSONString(templateExtListToSave));
			updateById(materialQuotationTemplate);
		}
	}

	/**
	 * 校验计算表达式
	 * @param materialQuotationTemplate 物料询价模板
	 */
	@Override
	public void validateExpression(MaterialQuotationTemplateVo materialQuotationTemplate) {
		List<MaterialTemplateExt> materialTemplateExtList = materialQuotationTemplate.getMaterialTemplateExtList();
		if(CollectionUtil.isNotEmpty(materialTemplateExtList)){
			List<MaterialTemplateExt> expressionList = materialTemplateExtList.stream().filter(item ->
					QuotationDataType.expression.name().equalsIgnoreCase(item.getDataType())).collect(Collectors.toList());
			for (MaterialTemplateExt materialTemplateExt : expressionList) {
				String expression = materialTemplateExt.getExpression();
				Expression expression1 = new Expression(expression);
				try {
					expression1.eval();
				} catch (Exception e) {
					log.error("表达式错误:" + materialTemplateExt.getTitle() , e);
					throw new BizException(QuotationResultCode.EXPRESSION_ERROR);
				}
			}
		}

	}


	/**
	 * 更新物料模板信息
	 * @param materialQuotationTemplate 物料询价模板
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updateInfo(MaterialQuotationTemplateVo materialQuotationTemplate) {
		//校验模板公式
		validateSuccessType(materialQuotationTemplate);

		//校验，物料模板的名称不能和已存在的模板的名称一样
		MaterialQuotationTemplate template = getById(materialQuotationTemplate.getId());
		//修改了模板名称
		if (!template.getTemplateName().equals(materialQuotationTemplate.getTemplateName())){
			MaterialQuotationTemplate quotationTemplate = getOne(new LambdaQueryWrapper<MaterialQuotationTemplate>().eq(MaterialQuotationTemplate::getTemplateName, materialQuotationTemplate.getTemplateName()));
			//预备修改的名称和已有的模板名称一样
			if (quotationTemplate!=null){
				throw new ApiException(ResultCode.FAILURE.getCode(), "该模板名称已经被使用，请重新命名！");
			}
		}
		materialQuotationTemplate.setExtContent(null);
		updateById(materialQuotationTemplate);
		materialTemplateExtService.clearTemplateExt(materialQuotationTemplate.getId());
		cascadeSaveTemplateExt(materialQuotationTemplate);
	}




	@Override
	public MaterialQuotationTemplateVo getInfoById(Long id) {
		MaterialQuotationTemplate quotationTemplate = getById(id);
		if(quotationTemplate == null){
			throw new BizException(QuotationResultCode.SOURCE_NOT_FOUND);
		}
		MaterialQuotationTemplateVo templateVo = BeanUtil.copy(quotationTemplate, MaterialQuotationTemplateVo.class);
		LambdaQueryWrapper<MaterialTemplateExt> queryWrapper = Condition.getQueryWrapper(new MaterialTemplateExt())
				.lambda().eq(MaterialTemplateExt::getTemplateId, id).orderByAsc(MaterialTemplateExt::getOrderValue);
		List<MaterialTemplateExt> templateExtList = materialTemplateExtService.list(queryWrapper);
		templateVo.setMaterialTemplateExtList(templateExtList);
		return templateVo;
	}

	/**
	 *表达式结果计算
	 * @param materialQuotationTemplate 询价模板
	 * @return 询价模板信息
	 */
	@Override
	public MaterialQuotationTemplateVo calculate(MaterialQuotationTemplateVo materialQuotationTemplate) {
		try {
			List<MaterialTemplateExt> materialTemplateExtList = materialQuotationTemplate.getMaterialTemplateExtList();
			if(CollectionUtil.isNotEmpty(materialTemplateExtList)){
				List<MaterialTemplateExt> extList = materialTemplateExtList.stream().filter(item->QuotationDataType.expression.name().equals(item.getDataType())).map(item -> {
					MaterialTemplateExt templateExt = BeanUtil.copyProperties(item, MaterialTemplateExt.class);
					assert templateExt != null;
					templateExt.setExpressionValue(new Expression(item.getExpression()).eval());
					return templateExt;
				}).collect(Collectors.toList());
				materialQuotationTemplate.setMaterialTemplateExtList(extList);
			}
		} catch (Exception e) {
			throw new BizException(QuotationResultCode.EXPRESSION_ERROR);
		}

		return materialQuotationTemplate;
	}


	/**
	 * 作废记录
	 * @param id 主键ID
	 */
    @Override
    public void disable(long id) {
		MaterialQuotationTemplate quotationTemplate = getById(id);
		if(quotationTemplate == null){
			throw new BizException(QuotationResultCode.SOURCE_NOT_FOUND);
		}
		deleteLogic(Collections.singletonList(id));
	}

	/**
	 * 保存和更新的时候判断是否调用过公式的结果测试并且状态为true
	 * @param calculateSuccessType 调用结果
	 */
	private void calculateSuccessType(Boolean calculateSuccessType) {
		if(ObjectUtil.isEmpty(calculateSuccessType)||calculateSuccessType.equals("false")){
			throw new ApiException(500,"请先点击公式的结果测试并且成功通过公式结果测试");
		}
	}

	/**
	 * 校验模板公式
	 * @param materialQuotationTemplate 模板参数
	 */
	private void validateSuccessType(MaterialQuotationTemplateVo materialQuotationTemplate) {
		List<MaterialTemplateExt> materialTemplateExtListForValidate=new ArrayList<>();
		MaterialQuotationTemplateVo materialQuotationTemplateVoForValidate = new MaterialQuotationTemplateVo();
		List<MaterialTemplateExt> materialTemplateExtList = materialQuotationTemplate.getMaterialTemplateExtList();

		for (MaterialTemplateExt materialTemplateExt : materialTemplateExtList) {
			MaterialTemplateExt copy = BeanUtil.copy(materialTemplateExt, MaterialTemplateExt.class);
			materialTemplateExtListForValidate.add(copy);
		}
		materialQuotationTemplateVoForValidate.setMaterialTemplateExtList(materialTemplateExtListForValidate);

		//校验公式并且进行补全
		EvalexUtil.calculateValidate(materialTemplateExtListForValidate);
		//校验表达式
		this.calculate(materialQuotationTemplateVoForValidate);
	}


}
