package net.bncloud.saas.authorize.service.mapstruct;

import net.bncloud.common.security.MenuNavDto;
import net.bncloud.convert.base.BaseMapper;
import net.bncloud.saas.authorize.domain.MenuNav;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuNavMapper extends BaseMapper<MenuNavDto, MenuNav> {
}
