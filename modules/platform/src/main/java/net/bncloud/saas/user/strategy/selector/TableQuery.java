package net.bncloud.saas.user.strategy.selector;

import lombok.Data;

@Data
public class TableQuery {

    private String id;

    private String parentId;

    private String mobile;

    private String type;

    private String name;

    /**
     * 搜索框
     */
    private String qs;


}
