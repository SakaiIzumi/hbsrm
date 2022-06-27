package net.bncloud.integrated.xxljob.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * desc: xxljob 配置
 *
 * @author Rao
 * @Date 2022/01/26
 **/
@Data
@Configuration
@ConditionalOnBean(value = XxlJobProperties.class)
public class XllJobConfiguration {

    /**
     * 配置执行器
     * @param xxlJobProperties
     * @return
     */
    @Bean
    public XxlJobSpringExecutor xxlJobExecutor(XxlJobProperties xxlJobProperties) {
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses( xxlJobProperties.getAdminAddresses() );
        xxlJobSpringExecutor.setAppname( xxlJobProperties.getAppname() );
        xxlJobSpringExecutor.setAddress( xxlJobProperties.getAddress() );
        xxlJobSpringExecutor.setIp( xxlJobProperties.getIp() );
        xxlJobSpringExecutor.setPort( xxlJobProperties.getPort() );
        xxlJobSpringExecutor.setAccessToken( xxlJobProperties.getAccessToken());
        xxlJobSpringExecutor.setLogPath( xxlJobProperties.getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays( xxlJobProperties.getLogRetentionDays() );
        return xxlJobSpringExecutor;
    }

}
