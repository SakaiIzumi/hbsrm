package net.bncloud.contract.param;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ContractExistValidateParam
 * @Author Administrator
 * @Date 2021/5/19
 * @Version V1.0
 **/
@Data
public class ContractExistValidateParam implements Serializable {

    /**
     * 合同ID
     */
    private Long id;

    /**
     * 合同编号
     */
    private String contractCode;

}
