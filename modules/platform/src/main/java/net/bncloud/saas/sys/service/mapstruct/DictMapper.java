package net.bncloud.saas.sys.service.mapstruct;

import net.bncloud.convert.base.BaseMapper;
import net.bncloud.saas.sys.domain.Dict;
import net.bncloud.saas.sys.service.dto.DictDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {DictItemMapper.class, DictSmallMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictMapper extends BaseMapper<DictDTO, Dict> {

}
