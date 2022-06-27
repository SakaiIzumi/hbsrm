package net.bncloud.bis.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * desc: 试点供应商
 *
 * @author Rao
 * @Date 2022/02/22
 **/
@Data
@TableName("experiment_supplier")
public class ExperimentSupplier implements Serializable {
    private static final long serialVersionUID = -326028828840725677L;


    /**
     * 主键ID
     */
    private Long id;

    /**
     * 供应商名称
     */
    private String	supplierName;

    /**
     * 供应商编码
     */
    private String	supplierCode;
    /**
     * 备注
     */
    private String	remark;

    /**
     * 禁用状态 0-非禁用 1是禁用
     */
    private Integer status;

}
