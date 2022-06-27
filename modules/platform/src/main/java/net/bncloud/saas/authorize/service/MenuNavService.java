package net.bncloud.saas.authorize.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import net.bncloud.common.constants.MenuNavType;
import net.bncloud.common.security.MenuNavDto;
import net.bncloud.saas.authorize.domain.MenuNav;
import net.bncloud.saas.authorize.repository.MenuNavRepository;
import net.bncloud.saas.authorize.service.mapstruct.MenuNavMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MenuNavService {


    private final MenuNavRepository menuNavRepository;

    private final MenuNavMapper menuNavMapper;


    public List<MenuNav> normals() {
        return menuNavRepository.findAllByNeedAuthOrderByOrderNum(false);
    }

    public List<MenuNav> findAllByMenuNavTypeIn(List<MenuNavType> types) {
        return menuNavRepository.findAllByMenuNavTypeIn(types);
    }

    public List<MenuNavDto> toDto(List<MenuNav> navs) {
        return menuNavMapper.toDto(navs);
    }

    public String getSubjectType(String currentMenuNav) {
        List<MenuNav> menuNavs = menuNavRepository.findAll();
        ImmutableMap<MenuNavType, MenuNav> immutableMap = Maps.uniqueIndex(menuNavs, MenuNav::getMenuNavType);
        MenuNav menuNav = immutableMap.get(MenuNavType.valueOf(currentMenuNav));
        if (menuNav != null) {
            return menuNav.getSubjectType();
        }
        return null;
    }

}
