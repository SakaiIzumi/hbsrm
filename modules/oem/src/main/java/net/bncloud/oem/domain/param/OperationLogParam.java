package net.bncloud.oem.domain.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bncloud.common.util.DateUtil;
import net.bncloud.oem.domain.entity.OperationLog;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author liyh
 * @description 操作日志查询参数
 * @since 2022/5/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OperationLogParam extends OperationLog implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     * 日期区间开始时间
     *
     * */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    private String startDate;

    /**
     *
     * 日期区间结束时间
     *
     * */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    private String endDate;
}
