package net.bncloud.delivery.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ddh
 * @description
 * @since 2022/5/24
 */
@Data
public class SupplierItem implements Serializable {

    private Long id;

    private String item;
}
