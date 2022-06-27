package net.bncloud.baidu.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import net.bncloud.baidu.constant.BaiduMapConstant;
import net.bncloud.baidu.properties.BaiduMapProperties;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/05/17
 **/
@Slf4j
public class BaiduMapRequestInterceptor implements RequestInterceptor {

    @Resource
    private BaiduMapProperties baiduMapProperties;

    @Override
    public void apply(RequestTemplate template) {
        template.query(BaiduMapConstant.AK_KEY, baiduMapProperties.getAk());
    }
}
