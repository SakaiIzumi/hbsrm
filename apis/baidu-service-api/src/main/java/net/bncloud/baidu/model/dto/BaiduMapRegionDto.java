package net.bncloud.baidu.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * desc: https://lbsyun.baidu.com/index.php?title=webapi/district-search
 *
 * @author Rao
 * @Date 2022/05/17
 **/
@SuperBuilder
@Data
public class BaiduMapRegionDto {

    /**
     * 检索行政区划关键字。
     * 行政区划区域检索不支持多关键字检索 关键字可填写：行政区名称（"中华人民共和国"/"中国"/"全国"，省、市、区和镇名称）以及 adcode；
     */
    @Builder.Default
    private String keyword = "中国";

    /**
     * 级数最高为4，当且仅当 keyword表达的地理存在4级，若keyword的为番禺区，那么即使传 4，返回依旧只有 一级。
     * 0： 当前
     * 1： 省
     * 2： 市
     * 3： 区
     * 4: 街道
     */
    @Builder.Default
    private int sub_admin = 0;

    /**
     * 是否召回国标行政区划编码，1(召回)；0(不召回)
     */
    @Builder.Default
    private int extensions_code = 1;
}
