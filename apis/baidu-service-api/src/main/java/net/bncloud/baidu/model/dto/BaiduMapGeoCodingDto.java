package net.bncloud.baidu.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import net.bncloud.baidu.model.enums.Output;

/**
 * desc: https://lbsyun.baidu.com/index.php?title=webapi/guide/webservice-geocoding
 *
 * @author Rao
 * @Date 2022/05/17
 **/
@SuperBuilder
@Data
public class BaiduMapGeoCodingDto {
    /**
     * 待解析的地址。最多支持84个字节。
     * 可以输入两种样式的值，分别是：
     * 1、标准的结构化地址信息，如北京市海淀区上地十街十号 【推荐，地址结构越完整，解析精度越高】
     * 2、支持“*路与*路交叉口”描述方式，如北一环路和阜阳路的交叉路口
     * 第二种方式并不总是有返回结果，只有当地址库中存在该地址描述时才有返回。
     */
    private String address;

    /**
     * 地址所在的城市名。用于指定上述地址所在的城市，当多个城市都有上述地址时，该参数起到过滤作用，但不限制坐标召回城市。
     * eg: 广东省广州市番禺区
     */
    private String city;

    /**
     * 输出格式
     */
    @Builder.Default
    private String output = Output.json.name();

}
