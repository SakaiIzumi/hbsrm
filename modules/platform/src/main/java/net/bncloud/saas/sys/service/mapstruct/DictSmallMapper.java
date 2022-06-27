package net.bncloud.saas.sys.service.mapstruct;

import net.bncloud.convert.base.BaseMapper;
import net.bncloud.saas.sys.domain.Dict;
import net.bncloud.saas.sys.service.dto.DictSmallDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictSmallMapper extends BaseMapper<DictSmallDto, Dict> {

}
