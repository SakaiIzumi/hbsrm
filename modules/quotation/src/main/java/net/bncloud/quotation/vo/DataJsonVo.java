package net.bncloud.quotation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataJsonVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * dataJson字段
     */
    private String quotationId;
    private String quotationNo;
}
