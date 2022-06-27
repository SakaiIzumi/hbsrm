package net.bncloud.delivery.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import net.bncloud.common.util.DateUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author ddh
 * @version 1.0.0
 * @description 导出Excel时，转换LocalDate成string
 * @since 2022/3/7
 */
public class LocalDateStringConverter implements Converter<LocalDate> {
    @Override
    public Class supportJavaTypeKey() {
        return LocalDate.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDate convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return LocalDate.parse(cellData.getStringValue(), DateTimeFormatter.ofPattern(DateUtil.PATTERN_DATE));
    }

    @Override
    public WriteCellData<?> convertToExcelData(LocalDate localDate, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DateUtil.PATTERN_DATE);
        String localDateStr = dtf.format(localDate);
        return new WriteCellData<>(localDateStr);
    }
}
