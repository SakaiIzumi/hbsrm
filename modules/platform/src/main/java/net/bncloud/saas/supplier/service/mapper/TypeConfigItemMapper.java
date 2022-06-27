package net.bncloud.saas.supplier.service.mapper;

import net.bncloud.convert.base.BaseMapper;
import net.bncloud.saas.supplier.domain.TypeConfigItem;
import net.bncloud.saas.supplier.service.dto.TypeConfigItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {TypeMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TypeConfigItemMapper extends BaseMapper<TypeConfigItemDTO, TypeConfigItem> {
}
