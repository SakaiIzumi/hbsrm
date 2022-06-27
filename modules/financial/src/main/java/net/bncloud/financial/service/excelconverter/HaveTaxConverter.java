package net.bncloud.financial.service.excelconverter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.util.Optional;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/04/08
 **/
public class HaveTaxConverter implements Converter<Boolean> {
    @Override
    public Class supportJavaTypeKey() {
        return Boolean.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.BOOLEAN;
    }

    /**
     * 这里读取的时候会调用  因此当此转换器 作用于 excel 读取时，需要留意此次
     * @param cellData
     * @param excelContentProperty
     * @param globalConfiguration
     * @return
     * @throws Exception
     */
    @Override
    public Boolean convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return false;
    }

    /**
     * 写的时候会调用
     * @param value
     * @param excelContentProperty
     * @param globalConfiguration
     * @return
     * @throws Exception
     */
    @Override
    public CellData convertToExcelData(Boolean value, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String valueStr = Optional.ofNullable(value).orElse(false) ? "是" : "否";
        return new CellData(valueStr);
    }
}
