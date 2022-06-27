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
 * 合同类型信息表
 * </p>
 *
 * @author huangtao
 * @since 2021-03-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_purchase_contract_type")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractType extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 合同类型名称
     */
    @ApiModelProperty(value = "合同类型名称")
    private String contractTypeName;

    /**
     * 是否必要 Y是，N否
     */
    @ApiModelProperty(value = "是否必要 Y是，N否")
    private String isNecessary;



}
