package net.bncloud.common.helper;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * desc: 环境助手
 *
 * @author Rao
 * @Date 2022/03/26
 **/
@Component
public class EnvHelper implements ApplicationContextAware {

    /**
     * 正式环境配置
     */
    public static final String PRO = "pro";
    public static final String UAT = "uat";
    public static final String DEV = "dev";

    /**
     * 当前环境变量
     */
    private List<String> envs = new ArrayList<>();

    /**
     * 开发环境
     * @return
     */
    public boolean isDev(){
        return envs.contains( DEV );
    }

    /**
     * UAT环境
     * @return
     */
    public boolean isUat(){
        return envs.contains( UAT );
    }

    /**
     * 生产
     * @return
     */
    public boolean isPro(){
        return envs.contains( PRO );
    }

    /**
     * 非生产
     */
    public boolean nonPro(){
        return ! isPro();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        envs = Arrays.asList(  applicationContext.getEnvironment().getActiveProfiles() );
    }
}
