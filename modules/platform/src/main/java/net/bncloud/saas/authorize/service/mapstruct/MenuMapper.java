package net.bncloud.saas.authorize.service.mapstruct;

import net.bncloud.convert.base.BaseMapper;
import net.bncloud.saas.authorize.domain.Menu;
import net.bncloud.saas.authorize.service.dto.MenuDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuMapper extends BaseMapper<MenuDTO, Menu> {
}
