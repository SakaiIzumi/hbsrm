package net.bncloud.delivery.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScopedProxyMode;

/**
 * desc: 送货明细导入配置
 *
 * @author Rao
 * @Date 2022/03/15
 **/
@RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
@Getter
@Setter
@Configuration
public class ImportDetailExcelConfig {

    /**
     * 附件ID
     */
    @Value("${application.importDetailExcelConfig.fileId}")
    private Long fileId;

}
