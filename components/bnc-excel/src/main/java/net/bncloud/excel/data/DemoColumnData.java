package net.bncloud.excel.data;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class DemoColumnData {


        private String info;
        private String field;
        private String supplier_01;
        private String supplier_02;

        public List<List<String>> getColumeData() {
            List<List<String>> list = new ArrayList();
            List<String> data01 = new ArrayList();
            data01.add("模块信息");
            List<String> data02 = new ArrayList();
            data02.add("模板字段名");
            List<String> data03 = new ArrayList();
            data03.add("珠海祥兴隆材料科技有限公司");
            List<String> data04 = new ArrayList();
            data04.add("广州德尔玛电子科技有限公司");
            list.add(data01);
            list.add(data02);
            list.add(data03);
            list.add(data04);
            return list;
        }

    public List<DemoColumnData> getData() {
        List<DemoColumnData> list = new ArrayList();
        DemoColumnData demoColumnData = new DemoColumnData();
        demoColumnData.setInfo("物料比价");
//        demoColumnData.setField("xxx原料1单价");
//        demoColumnData.setSupplier_01("22");
        demoColumnData.setSupplier_02("22");
        DemoColumnData demoColumnData01 = new DemoColumnData();
        demoColumnData01.setInfo("物料比价");
//        demoColumnData01.setField("xxx原料1数量");
//        demoColumnData01.setSupplier_01("11");
        demoColumnData01.setSupplier_02("11");
        DemoColumnData demoColumnData02 = new DemoColumnData();
        demoColumnData02.setInfo("物料比价");
//        demoColumnData02.setField("xxx原料1期望单价");
//        demoColumnData02.setSupplier_01("11");
        demoColumnData02.setSupplier_02("11");
        DemoColumnData demoColumnData03 = new DemoColumnData();
        demoColumnData03.setInfo("物料比价");
//        demoColumnData03.setField("xxx原料1单位");
//        demoColumnData03.setSupplier_01("11");
        demoColumnData03.setSupplier_02("11");
        list.add(demoColumnData);
        list.add(demoColumnData01);
        list.add(demoColumnData02);
        list.add(demoColumnData03);
        return list;
    }
}
