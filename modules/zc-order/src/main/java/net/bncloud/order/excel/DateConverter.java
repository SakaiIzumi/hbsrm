package net.bncloud.order.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import net.bncloud.common.util.DateUtil;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author ddh
 * @version 1.0.0
 * @description
 * @since 2022/3/15
 */
@Component
public class DateConverter implements Converter<Date> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return Date.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Date convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.PATTERN_DATETIME, Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sdf.parse(cellData.getStringValue());
    }

    @Override
    public WriteCellData<?> convertToExcelData(Date date, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.PATTERN_DATETIME,Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String format = sdf.format(date);
        return new WriteCellData<>(format);
    }
}
