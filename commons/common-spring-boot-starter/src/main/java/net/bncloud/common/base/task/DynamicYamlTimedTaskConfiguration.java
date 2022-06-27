package net.bncloud.common.base.task;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * desc: 基于Yaml的动态定时任务配置
 *
 * @author Rao
 * @Date 2022/01/17
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "application.task")
public class DynamicYamlTimedTaskConfiguration {

    /**
     * 类名:corn
     */
    private Map<String,String> taskNameCronMap;

}
