package net.bncloud.service.api.platform.sys.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author ddh
 * @version 1.0.0
 * @description
 * @since 2022/3/14
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
@NoArgsConstructor
@Builder
public class CfgParamDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long orgId;
    private String code;
    private String value;
    private String remark;
}
