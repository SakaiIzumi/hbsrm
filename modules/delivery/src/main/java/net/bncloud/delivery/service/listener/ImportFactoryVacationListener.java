package net.bncloud.delivery.service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.api.ResultCode;
import net.bncloud.common.exception.ApiException;
import net.bncloud.delivery.vo.ImportFactoryVacationVo;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author ddh
 * @description
 * @since 2022/5/19
 */
@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class ImportFactoryVacationListener extends AnalysisEventListener<ImportFactoryVacationVo> {


    private final List<ImportFactoryVacationVo> returnList = Lists.newArrayList();

    /**
     * 表头校验
     *
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        super.invokeHeadMap(headMap, context);
        if (!(headMap.containsKey(0) && headMap.containsKey(1) && headMap.containsKey(2) && headMap.containsKey(3) &&headMap.containsKey(4) && "采购方编码".equals(headMap.get(0))
                && "采购方名称".equals(headMap.get(1)) && "日期".equals(headMap.get(2)) && "假期类型".equals(headMap.get(3)) && "备注".equals(headMap.get(4)))) {
            log.info("您上传的文件格式与模板格式不一致，请检查后重新上传");
            throw new ApiException(ResultCode.FAILURE.getCode(), "您上传的文件格式与模板格式不一致，请检查后重新上传");
        }
    }

    @Override
    public void invoke(ImportFactoryVacationVo importFactoryVacationVo, AnalysisContext analysisContext) {
        Optional.ofNullable(importFactoryVacationVo).ifPresent(vo->{
            if ("节假日".equals(vo.getVacationType())){
                vo.setVacationType("0");
            }else if ("调休休息".equals(vo.getVacationType())){
                vo.setVacationType("1");
            }
            returnList.add(vo);
        });
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("解析完成，导入了{}条记录", returnList.size());
    }
}
