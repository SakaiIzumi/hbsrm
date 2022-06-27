package net.bncloud.baidu.util;

import lombok.extern.slf4j.Slf4j;
import net.bncloud.common.exception.Asserts;

/**
 * desc: 结果校验工具
 *
 * @author Rao
 * @Date 2022/05/17
 **/
@Slf4j
public class BaiduMapResultUtils {

    /**
     * 成功
     */
    public static void successfulResult(BaiduMapResultCheck baiduMapResultCheck){
        Asserts.isTrue( baiduMapResultCheck.normalState(),baiduMapResultCheck.errorMsg() );
    }

}
