package net.bncloud.saas.sys.service.mapstruct;

import net.bncloud.convert.base.BaseMapper;
import net.bncloud.saas.sys.domain.DictItem;
import net.bncloud.saas.sys.service.dto.DictItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {DictSmallMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictItemMapper extends BaseMapper<DictItemDTO, DictItem> {

}
