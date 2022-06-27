package net.bncloud.quotation.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class GetLastGroupTreeParam implements Serializable {
    /**
     * 组id
     * */
    private Long id;
    /**
     * 组的erp的id
     * */
    private Long erpId;
    /**
     * 组的erp的parent的id
     * */
    private Long erpParentId;
    /**
     * 组的erp的number
     * */
    private String erpNumber;
    /**
     * erpName
     * */
    private String erpName;
    /**
     * 组关联的父的id
     * */
    private Long parentId;
    /**
     * 组关联的父的parentErpId
     * */
    private Long parentErpId;
    /**
     * 组关联的父的parentErpNumber
     * */
    private String parentErpNumber;
    /**
     * 组关联的父的parentName
     * */
    private String parentName;
    /**
     * 高级查询用到的字段  查询  组的erpName 和父的erpName
     * */
    private String searchValue;


}
