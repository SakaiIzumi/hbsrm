package net.bncloud.contract.param;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import net.bncloud.contract.entity.Contract;
import net.bncloud.base.BaseEntity;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * <p>
 * 合同信息表
 * </p>
 *
 * @author huangtao
 * @since 2021-03-12
 */
@Data
@NoArgsConstructor
@ApiModel
public class ContractQueryParam extends Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 含税金额最小值
     */
    @ApiModelProperty(value = "含税金额最小值")
    private BigDecimal taxIncludedAmountMin;

    /**
     * 含税金额最大值
     */
    @ApiModelProperty(value = "含税金额最大值")
    private BigDecimal taxIncludedAmountMax;

    /**
     * 不含税金额最小值
     */
    @ApiModelProperty(value = "不含税金额最小值")
    private BigDecimal excludingTaxAmountMin;

    /**
     * 不含税金额最大值
     */
    @ApiModelProperty(value = "不含税金额最大值")
    private BigDecimal excludingTaxAmountMax;






    /**
     * 签订时间,查询开始时间
     */
    private String signedTimeBegin;

    /**
     * 签订时间,查询结束时间
     */
    private String signedTimeEnd;


    /**
     * 搜索内容
     */
    @ApiModelProperty(value = "搜索内容")
    private String searchContent;

    /**
     * tab栏分类
     */
    @ApiModelProperty(value = "tab栏分类")
    private String tabCategory;

    /**
     * 草稿过滤字段
     */
    @ApiModelProperty(value = "草稿过滤字段")
    private Boolean draft;


}
