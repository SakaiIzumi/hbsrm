package net.bncloud.delivery.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScopedProxyMode;

/**
 * @author ddh
 * @description
 * @since 2022/5/19
 */
@RefreshScope(proxyMode = ScopedProxyMode.DEFAULT)
@Getter
@Setter
@Configuration
public class DownloadFactoryVacationModel {

    /**
     * 采购方附件ID
     */
    @Value("${application.downloadFactoryVacationModel.modelId}")
    private Long modelId;

    /**
     * 供应方附件ID
     */
    @Value("${application.downloadSupplierFactoryVacationModel.modelId}")
    private Long supModelId;
}
