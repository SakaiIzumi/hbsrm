package net.bncloud.delivery.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName FBillType
 * @Description: 收料通知单枚举
 * @Author liyh
 * @Date 2022/1/25
 * @Version V1.0
 **/
@Getter
@AllArgsConstructor
public enum FBillTypeEnum {
    //单据类型
    FBILLTYPE_STANDAR("SLD01_SYS","标准收料单"),
    FBILLTYPE_OUTSOURCING("SLD03_SYS","委外收料单"),
    //是否同步
    SYNC_FALSE("0","否"),
    SYNC_TRUE("1","是"),
    //验收方式
    DOCKING_FALSE("0","否"),
    DOCKING_TRUE("1","是"),
    //验收方式
    CHECK_NUM("Q","数量验收"),
    CHECK_ACCOUNT("A","金额验收"),
    CHECK_PROPORTION("R","比例验收");

    private String code;
    private String name;
}