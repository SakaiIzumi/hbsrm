package net.bncloud.order.service.mapstruct;

import net.bncloud.convert.base.BaseMapper;
import net.bncloud.order.entity.Order;
import net.bncloud.order.vo.OrderVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderVoConverter extends BaseMapper<OrderVo, Order> {
}
