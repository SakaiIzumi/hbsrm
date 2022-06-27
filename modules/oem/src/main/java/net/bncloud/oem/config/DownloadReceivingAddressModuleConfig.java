package net.bncloud.oem.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author ddh
 * @description
 * @since 2022/4/26
 */
@Getter
@Setter
@Configuration
public class DownloadReceivingAddressModuleConfig {

    /**
     * 附件ID
     */
    @Value("${application.downloadAttachment.fileId}")
    private Long fileId;
}
