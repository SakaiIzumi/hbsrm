package net.bncloud.baidu.configuration;

import net.bncloud.baidu.fallbackfactory.BaiduMapFeignClientFallbackFactory;
import net.bncloud.baidu.properties.BaiduMapProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/05/17
 **/
@Configuration
@ComponentScan("net.bncloud.baidu")
@EnableFeignClients(basePackages = {"net.bncloud.baidu.api"})
@Import({BaiduMapFeignClientFallbackFactory.class,BaiduMapProperties.class})
public class BaiduMapAutoConfiguration extends FeignClientsConfiguration {

}
