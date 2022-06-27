package net.bncloud.delivery.wrapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;

public class Mytest {


    public static void main(String[] args) {
        String result = "{\"data\":[{\"type\":\"BOOL\",\"value\":true},{\"type\":\"INPUT\",\"value\":\"12\"}]}";
        // 字符串转换为JSON
        JSONObject jsonObject = JSON.parseObject(result);  // result数据源：JSON格式字符串
        // 获取值
        JSONArray data = jsonObject.getJSONArray("data");
        JSONObject jsonObject1 = JSON.parseObject(data.getString(0));
        Boolean flag = jsonObject1.getBoolean("value");


        JSONObject jsonObject2 = JSON.parseObject(data.getString(1));
        BigDecimal value2 = jsonObject2.getBigDecimal("value");

//        JSONObject jsonObject1 = JSON.parseObject(s);
//        String subOrderId = jsonObject1.getString("subOrderId");
        System.out.println(flag);
        System.out.println(value2);
    }

}
