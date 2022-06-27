package net.bncloud.common.security.data;

/**
 * SQL中最后的子句类型
 * @author linsh2@meicloud.com
 */
public enum LastClauseType {
    ORDER_BY(0, "ORDER BY为最后的子句"),
    LIMIT(1, "LIMIT为最后的子句"),
    OTHERS(2, "其他子句");

    private int code;
    private String desc;

    LastClauseType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
