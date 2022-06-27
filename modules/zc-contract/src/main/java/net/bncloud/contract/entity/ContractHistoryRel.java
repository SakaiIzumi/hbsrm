package net.bncloud.contract.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import net.bncloud.base.BaseEntity;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import net.bncloud.common.util.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

/**
 * <p>
 * 合同与历史合同关联关系表
 * </p>
 *
 * @author huangtao
 * @since 2021-03-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_purchase_contract_history_rel")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContractHistoryRel extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 合同ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "合同ID")
    private Long contractId;

    /**
     * 历史合同ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "历史合同ID")
    private Long historyContractId;

}
