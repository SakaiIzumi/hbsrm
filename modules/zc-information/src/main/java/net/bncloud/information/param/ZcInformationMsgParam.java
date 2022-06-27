package net.bncloud.information.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.bncloud.common.util.DateUtil;
import net.bncloud.information.entity.ZcInformationMsg;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

/**
 * 智采消息表
 * @author dr
 */
@Data
public class ZcInformationMsgParam extends ZcInformationMsg implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 接收日期开始
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    private String getTimeStart;

    /**
     * 接收日期结束
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    private String getTimeEnd;

    private List<Integer> moduleTypeList;



}