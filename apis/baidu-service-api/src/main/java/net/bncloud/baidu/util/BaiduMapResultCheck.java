package net.bncloud.baidu.util;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/05/17
 **/
public interface BaiduMapResultCheck {

    /**
     * 正常状态
     */
    boolean normalState();

    /**
     * 错误消息
     * @return
     */
    default String errorMsg(){
        return "百度地图接口请求结果非正常，请手动debug接口结果！";
    }

}
