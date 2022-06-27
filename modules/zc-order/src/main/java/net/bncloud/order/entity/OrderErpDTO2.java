package net.bncloud.order.entity;/**
 * 创建人:    lv
 */

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.base.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * 类名称:    SyncRep
 * 类描述:    TODO
 * 创建人:    lvxiangyi
 * 创建时间:  2021/3/24 3:29 下午
 */
@Data

public class OrderErpDTO2 extends OrderErp {
	private static final long serialVersionUID = 1L;
	

	private List<OrderProductDetailsErp> orderProductDetailsErpList;
	

}
