package net.bncloud.saas.supplier.service.mapper;

import net.bncloud.convert.base.BaseMapper;
import net.bncloud.saas.supplier.domain.TagConfigItem;
import net.bncloud.saas.supplier.service.dto.TagConfigItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {TagMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagConfigItemMapper extends BaseMapper<TagConfigItemDTO, TagConfigItem> {
}
