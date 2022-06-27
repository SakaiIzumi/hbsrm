package net.bncloud.delivery.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bncloud.common.util.DateUtil;
import net.bncloud.delivery.entity.DeliveryDetail;
import net.bncloud.delivery.entity.FileInfo;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 送货明细表
 * </p>
 *
 * @author huangtao
 * @since 2021-03-17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeliveryDetailVo extends DeliveryDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * COA附件列表
     */
    private List<FileInfo> attachmentList;

    /**
     * 是否有COA附件：Y有，N没有
     */
    private String attachment;

    /**
     * 送货日期
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
    @JsonFormat(pattern = DateUtil.PATTERN_DATE)
    private LocalDate deliveryDate;

}
