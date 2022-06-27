package net.bncloud.serivce.api.order.dto;/**
 * 创建人:    lv
 */


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.bncloud.serivce.api.order.entity.OrderErpInfo;
import java.util.List;

/**
 *
 * 类名称:    SyncRep
 * 类描述:    TODO
 * 创建人:    lvxiangyi
 * 创建时间:  2021/3/24 3:29 下午
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderErpDTO extends OrderErpInfo {
	private static final long serialVersionUID = 1L;


	private List<OrderProductDetailsErpDTO> orderProductDetailsErpList;

	private Long orgId;

	private Boolean isSend = false;

	/**
	 * 采购订单来源ID
	 */
	private String sourceId;

}
