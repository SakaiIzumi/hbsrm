package net.bncloud.delivery.vo.apihub;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * desc: 假日参数
 *
 * @author Rao
 * @Date 2022/05/19
 **/
@Data
@SuperBuilder
public class HolidayParam implements Serializable {
    private static final long serialVersionUID = 1424194307852945142L;

    /**
     * 年份
     */
    @Builder.Default
    private Integer year = LocalDate.now().getYear();

    /**
     * 年月  202205  不传则查年
     */
    private Integer month;

    /**
     * 是否返回中文字段  只能等于 1 目前没加中文字段预留
     * {@link ApiHubResult.DateInfo}
     */
    private Integer cn;

    /**
     * 数 查月则传大于当月天数 eg:32，查年传大于当年天数 eg: 400
     */
    @Builder.Default
    private Integer size = 400;

    /**
     * 页
     */
    @Builder.Default
    private Integer page = 1;

}
