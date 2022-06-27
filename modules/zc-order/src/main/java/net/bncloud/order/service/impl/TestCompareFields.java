package net.bncloud.order.service.impl;

import com.alibaba.fastjson.JSONObject;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 类说明：比较两个实体的有哪些属性值不同
 * @author zwq1105
 * @version 创建时间：2017-3-4 下午16:30:36
 */
public class TestCompareFields {

    public static void main(String[] args) {
        Student s1 = new Student("123", "张三", 23, "深圳");
        Student s2 = new Student("abc", "李四", 23, "深圳");
        // 比较s1和s2不同的属性值，其中id忽略比较
        Map<String, Boolean> compareResult = compareFields(s1, s2, new String[]{"id","age"});

        Set<String> keySet = compareResult.keySet();

        System.out.println(JSONObject.toJSONString(compareResult));


    }


    /**
     * 比较两个实体属性值，返回一个map以有差异的属性名为key，value为一个list分别存obj1,obj2此属性名的值
     * @param obj1 进行属性比较的对象1
     * @param obj2 进行属性比较的对象2
     * @param contrastArr 对比
     * @return 属性差异比较结果map
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, Boolean> compareFields(Object obj1, Object obj2, String[] contrastArr) {
        try{
            Map<String, Boolean> map = new HashMap<String, Boolean>();
            List<String> ignoreList = null;
            if(contrastArr != null && contrastArr.length > 0){
                // array转化为list
                ignoreList = Arrays.asList(contrastArr);
            }
            if (obj1.getClass() == obj2.getClass()) {// 只有两个对象都是同一类型的才有可比性
                Class clazz = obj1.getClass();
                // 获取object的属性描述
                PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz,
                        Object.class).getPropertyDescriptors();
                for (PropertyDescriptor pd : pds) {// 这里就是所有的属性了
                    String name = pd.getName();// 属性名
                    System.out.println(!ignoreList.contains(name));
                    if(ignoreList != null && !ignoreList.contains(name)){// 如果当前属性选择忽略比较，跳到下一次循环
                        continue;
                    }
                    Method readMethod = pd.getReadMethod();// get方法
                    // 在obj1上调用get方法等同于获得obj1的属性值
                    Object o1 = readMethod.invoke(obj1);
                    // 在obj2上调用get方法等同于获得obj2的属性值
                    Object o2 = readMethod.invoke(obj2);
                    if(o1 instanceof Timestamp){
                        o1 = new Date(((Timestamp) o1).getTime());
                    }
                    if(o2 instanceof Timestamp){
                        o2 = new Date(((Timestamp) o2).getTime());
                    }



                    if(o1 == null && o2 == null){
                        map.put(name, true);
                        continue;
                    }else if(o1 == null && o2 != null){
//                        List<Object> list = new ArrayList<Object>();
//                        list.add(o1);
//                        list.add(o2);
                        map.put(name, false);
                        continue;
                    }
                    if (!o1.equals(o2)) {// 比较这两个值是否相等,不等就可以放入map了
                        map.put(name, false);
                        continue;
                    }
                    map.put(name, true);
                }
            }
            return map;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

}

/**
 * 学生orm
 * @author zwq1105
 */
class Student{
    private String id;
    private String name;
    private int age;
    private String city;

    public Student(String id, String name, int age, String city) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}