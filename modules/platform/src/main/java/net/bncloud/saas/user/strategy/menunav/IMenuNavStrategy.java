package net.bncloud.saas.user.strategy.menunav;

import net.bncloud.saas.authorize.domain.MenuNav;

import java.util.List;

public interface IMenuNavStrategy {


    List<MenuNav> getMeuNavs(Long userId);
}
