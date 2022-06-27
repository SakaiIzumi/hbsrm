package net.bncloud.delivery.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.util.DateUtil;
import net.bncloud.common.util.StringUtil;
import net.bncloud.delivery.entity.DeliveryPlanDetailItem;

import java.time.LocalDate;

/**
 * @author ddh
 * @description
 * @since 2022/6/9
 */
@Slf4j
@Data
@AllArgsConstructor
public class DeliveryPlanDetailItemConverter  implements Converter<DeliveryPlanDetailItem> {

    /**
     * 工作台类型：
     * zc:采购工作台
     * zy:销售工作台
     */
    private String workBench;

    @Override
    public Class<?> supportJavaTypeKey() {
        return DeliveryPlanDetailItem.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public WriteCellData<?> convertToExcelData(DeliveryPlanDetailItem item, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration){
        String deliveryQuantity = StringUtil.isNotBlank(item.getDeliveryQuantity()) ? item.getDeliveryQuantity() : "";
        String confirmQuantity = StringUtil.isNotBlank(item.getConfirmQuantity()) ? item.getConfirmQuantity() : "";
        String differenceReason = StringUtil.isNotBlank(item.getDifferenceReason()) ? item.getDifferenceReason() : "";
        String date = "";
        if ("zc".equals(this.getWorkBench())) {
            LocalDate suggestedDeliveryDate = item.getSuggestedDeliveryDate();
            date = DateUtil.formatDate(suggestedDeliveryDate);
        }else{
            LocalDate deliveryDate = item.getDeliveryDate().toLocalDate();
            date = DateUtil.formatDate(deliveryDate);
        }
        String value = deliveryQuantity + "\n" + confirmQuantity + "\n" + date + "\n" + differenceReason;
        return new WriteCellData<>(value);
    }
}
