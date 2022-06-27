package net.bncloud.saas.supplier.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bncloud.common.web.jackson.CustomInstantDateSerializer;

import java.io.Serializable;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SupplierOpsLogDTO implements Serializable {
    private static final long serialVersionUID = 429260431179813108L;

    private Long id;
    private Long supplierId;
    private String content;
    private String remark;
    private Long createdBy;

    @JsonSerialize(using = CustomInstantDateSerializer.class)
    @JsonFormat(pattern = "YYYY-MM-DD hh:mm:ss")
    private Instant createdDate;

    private String opsUserName;
}
