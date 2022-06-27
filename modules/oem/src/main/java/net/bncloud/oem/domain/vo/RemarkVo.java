package net.bncloud.oem.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author liyh
 * @description
 * @since 2022/4/26
 */
@Data
public class RemarkVo implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 收货数量
     * */
    private Long receiveQuantity;

    /**
     * 收货日期
     * */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    private LocalDate receiveDate;

    /**
     * 品牌方备注（采购方备注）
     * */
    private String brandRemark;

    /**
     * oem供应商备注
     * */
    private String oemSupplierRemark;

    /**
     * 采购单号(美尚同步)
     * */
    private String purchaseOrderCode;


}
