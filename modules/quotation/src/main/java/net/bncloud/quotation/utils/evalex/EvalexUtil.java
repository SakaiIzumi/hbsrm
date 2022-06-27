package net.bncloud.quotation.utils.evalex;

import cn.hutool.core.util.StrUtil;
import com.udojava.evalex.Expression;
import net.bncloud.common.util.StringUtil;
import net.bncloud.quotation.entity.MaterialTemplateExt;
import net.bncloud.quotation.entity.QuotationLineExt;
import net.bncloud.quotation.enums.CalculateValidateEnum;
import net.bncloud.quotation.enums.QuotationDataType;
import net.bncloud.quotation.vo.BiddingLineExtVo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class EvalexUtil {

    /**
     * 生成计算语句
     *
     * @param formula           计算公式
     * @param biddingLineExtVos 物料名称和物料值的列表
     */
    public BigDecimal calculate(String formula, List<BiddingLineExtVo> biddingLineExtVos) {
        BigDecimal result = BigDecimal.ZERO;
        if (StringUtil.isEmpty(formula)) {
            return result;
        }
        for (BiddingLineExtVo biddingLineExtVo : biddingLineExtVos) {
            if (biddingLineExtVo.getDataType().equals(QuotationDataType.normal.name()) && formula.contains(biddingLineExtVo.getField())) {
//                StrUtil.isBlank(biddingLineExtVo.getBiddingLineExtValue())
                formula = formula.replace(biddingLineExtVo.getField(), biddingLineExtVo.getBiddingLineExtValue());
            }
        }

        Expression expression = new Expression(formula);
        result = expression.eval();
        return result;
    }


    /**
     * 销售协同的询价单详情界面 计算需要计算的行扩展
     *
     * @param expressionQuotationLineExt           计算的扩展字段
     * @param quotationLineExts 物料名称和物料值的列表
     */
    public void calculateInfo(QuotationLineExt expressionQuotationLineExt, List<QuotationLineExt> quotationLineExts) {
        if (StringUtil.isEmpty(expressionQuotationLineExt) || StringUtil.isEmpty(expressionQuotationLineExt.getFormula())) {
            return;
        }
        /**
         * 根据field长度倒序排序 防止替换表达式的时候替换错误
         */
        quotationLineExts.sort((o1, o2) -> Integer.compare(o2.getField().length(), o1.getField().length()));
        String formula = expressionQuotationLineExt.getFormula();
        for (QuotationLineExt quotationLineExt : quotationLineExts) {
            if ("normal".equals(quotationLineExt.getDataType()) && formula.contains(quotationLineExt.getField())) {
                formula = formula.replace(quotationLineExt.getField(), quotationLineExt.getValue());
            }
        }
        Expression expression = new Expression(formula);
        expressionQuotationLineExt.setExpression(formula);
        try {
            //出现异常就不给这个计算行给值
            BigDecimal result = expression.eval();
            expressionQuotationLineExt.setExpressionValue(result);
        } catch (Exception ignored) {

        }
    }

    /**
     * 保存或者更新模板时比价公式的字段为空时公式的补全
     *
     * @param materialTemplateExtList
     */
    public static void calculateValidate( List<MaterialTemplateExt> materialTemplateExtList) {
        for (MaterialTemplateExt materialTemplateExt : materialTemplateExtList) {
            if (   materialTemplateExt.getDataType().equals(QuotationDataType.expression.name())   ){

                try{
                    new Expression(materialTemplateExt.getExpression()).eval();
                }catch (Exception e){
                    //说明公式中需要填写的字段有空值,补充
                    String expression = materialTemplateExt.getFormula();

                    for (MaterialTemplateExt templateExt : materialTemplateExtList) {
                        if (templateExt.getDataType().equals(QuotationDataType.normal.name()) && expression.contains(templateExt.getField())) {
                            //为空需要补充字段
                            if(StrUtil.isBlank(templateExt.getValue()) ){
                                templateExt.setValue(CalculateValidateEnum.CALCULATE_VALIDATE_DEFAULT_NUM.getCode());
                            }
                            expression = expression.replace(templateExt.getField(), templateExt.getValue());
                        }
                    }
                    materialTemplateExt.setExpression(expression);
                }
            }
        }
    }


}
