package net.bncloud.oem.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.base.BaseUserEntity;
import net.bncloud.common.api.ResultCode;
import net.bncloud.common.exception.ApiException;
import net.bncloud.common.util.CollectionUtil;
import net.bncloud.common.util.StringUtil;
import net.bncloud.oem.domain.entity.AddressModule;
import net.bncloud.oem.domain.entity.OperationLog;
import net.bncloud.oem.domain.entity.ReceivingAddress;
import net.bncloud.oem.enums.AddressOperationLogEnum;
import net.bncloud.oem.service.OperationLogService;
import net.bncloud.oem.service.ReceivingAddressService;
import net.bncloud.utils.AuthUtil;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ddh
 * @description
 * @since 2022/4/26
 */
@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class ImportReceivingAddressListener extends AnalysisEventListener<AddressModule> {

    private List<ReceivingAddress> successList = Lists.newArrayList();


    private final ReceivingAddressService receivingAddressService;


    private final OperationLogService operationLogService;

    public ImportReceivingAddressListener(ReceivingAddressService receivingAddressService,OperationLogService operationLogService) {
        this.receivingAddressService = receivingAddressService;
        this.operationLogService=operationLogService;
    }

    /**
     * 校验表头
     *
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        super.invokeHeadMap(headMap, context);
        if (!(headMap.containsKey(0) && headMap.containsKey(1) && headMap.containsKey(2) && headMap.containsKey(3) && "收货地址编码".equals(headMap.get(0))
                && "收货地址".equals(headMap.get(1)) && "供应商编码".equals(headMap.get(2)) && "供应商名称".equals(headMap.get(3)))) {
            log.info("您上传的文件格式与模板格式不一致，请检查后重新上传");
            throw new ApiException(ResultCode.FAILURE.getCode(), "您上传的文件格式与模板格式不一致，请检查后重新上传");
        }
    }

    /**
     * 解析一条数据，调用一次这个方法
     *
     * @param addressModule   地址对象
     * @param analysisContext 上下文
     */
    @Override
    public void invoke(AddressModule addressModule, AnalysisContext analysisContext) {
        if (StringUtil.isNotBlank(addressModule.getAddressCode()) && StringUtil.isNotBlank(addressModule.getAddressName())
                && StringUtil.isNotBlank(addressModule.getSupplierCode()) && StringUtil.isNotBlank(addressModule.getSupplierName())) {

            ReceivingAddress receivingAddress = new ReceivingAddress()
                    .setCode(addressModule.getAddressCode())
                    .setAddress(addressModule.getAddressName())
                    .setSupplierCode(addressModule.getSupplierCode())
                    .setSupplierName(addressModule.getSupplierName())
                    //数据来源：1用户维护，2用户导入，3ERP同步
                    .setSourceType("2")
                    //状态：0待维护（未和任何供应商关联是待维护），1已维护（已经和供应商关联是已维护）
                    .setStatus("1");
            successList.add(receivingAddress);

        }

    }


    /**
     * 解析完数据之后，都会来调用这个方法
     *
     * @param analysisContext 上下文
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (CollectionUtils.isEmpty(successList)) {
            log.info("你上传了一个无数据的excel文档");
            throw new RuntimeException("你上传了一个无数据的excel文档");
        }
        ArrayList<ReceivingAddress> result = Lists.newArrayList();

        //处理Excel导入的数据重复的问题
        List<ReceivingAddress> distinctAddressList = successList.stream().filter(CollectionUtil.distinctByKey(distinctByKeyFunction()))
                .collect(Collectors.toList());

        //处理Excel导入的数据和数据库的数据重复的问题
        Set<String> addressCodeAndSupplierCodeSet = receivingAddressService.list().stream()
                .map(receivingAddress -> receivingAddress.getCode() + "-" + receivingAddress.getSupplierCode())
                .collect(Collectors.toSet());

        distinctAddressList.forEach(distinctAddress->{
            String addressCodeAndSupplierCode = distinctAddress.getCode() + "-" + distinctAddress.getSupplierCode();
            if (!addressCodeAndSupplierCodeSet.contains(addressCodeAndSupplierCode)) {
                result.add(distinctAddress);
            }
        });
        log.info("解析非空的Excel数据{}条，去重之后入库的数据{}条", JSON.toJSONString(successList.size()), JSON.toJSONString(result.size()));
        receivingAddressService.saveBatch(result);

        //导入之后记录操作日志
        List<OperationLog> operationLogList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(result)) {
            for (ReceivingAddress receivingAddress : result) {
                String logContent = null;
                String remark = null;
                //为空是新建
                logContent = AddressOperationLogEnum.IMPORT.getName();
                remark = receivingAddress.getSupplierCode() + " " + receivingAddress.getSupplierName();

                //操作记录
                OperationLog log = saveLog(receivingAddress, logContent, remark);
                operationLogList.add(log);
            }
            operationLogService.saveBatch(operationLogList);
        }

    }

    public static Function<ReceivingAddress, String> distinctByKeyFunction() {
        return (ReceivingAddress receivingAddress) -> receivingAddress.getCode() + "-" + receivingAddress.getSupplierCode();
    }


    /**
     *
     * 记录地址的维护操作日志
     *
     * */
    public OperationLog saveLog(ReceivingAddress receivingAddress,String content,String remark){
        BaseUserEntity user = AuthUtil.getUser();

        OperationLog log = OperationLog.builder()
                .billId(receivingAddress.getId())
                .operatorContent(content)
                .operatorName(user.getUserName())
                .operatorNo(user.getUserId() + "")
                .address(receivingAddress.getAddress())
                .addressCode(receivingAddress.getCode())
                .remark(remark)
                .build();

        //operationLogService.save( log);
        return log;
    }


}
