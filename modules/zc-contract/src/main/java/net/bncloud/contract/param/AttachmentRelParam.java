package net.bncloud.contract.param;


import net.bncloud.contract.entity.AttachmentRel;
import net.bncloud.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
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
public class AttachmentRelParam extends AttachmentRel implements Serializable {

    private static final long serialVersionUID = 1L;



}
