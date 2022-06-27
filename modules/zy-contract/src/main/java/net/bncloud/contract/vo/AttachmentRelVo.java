package net.bncloud.contract.vo;


import lombok.Data;
import net.bncloud.contract.entity.AttachmentRel;

import java.io.Serializable;

/**
 * <p>
 * 订单合同与附件关联关系表
 * </p>
 *
 * @author huangtao
 * @since 2021-03-13
 */
@Data
public class AttachmentRelVo extends AttachmentRel implements Serializable {

    private static final long serialVersionUID = 1L;



}
