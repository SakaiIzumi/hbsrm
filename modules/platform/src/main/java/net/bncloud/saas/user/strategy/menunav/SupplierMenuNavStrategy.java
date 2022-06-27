package net.bncloud.saas.user.strategy.menunav;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import net.bncloud.common.constants.MenuNavType;
import net.bncloud.saas.authorize.domain.MenuNav;
import net.bncloud.saas.authorize.service.MenuNavService;
import net.bncloud.saas.supplier.domain.SupplierManager;
import net.bncloud.saas.supplier.repository.SupplierManagerRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("supplierMenuNavStrategy")
@AllArgsConstructor
public class SupplierMenuNavStrategy implements IMenuNavStrategy {
    private final SupplierManagerRepository supplierManagerRepository;
    private final MenuNavService menuNavService;

    @Override
    public List<MenuNav> getMeuNavs(Long userId) {
        List<MenuNav> menuNavs = new ArrayList<>();
        menuNavs.addAll(menuNavService.findAllByMenuNavTypeIn(Lists.newArrayList(MenuNavType.supplier)));
        // 是否有供应商配置导航
        List<SupplierManager> supplierManagers = supplierManagerRepository.findAllBySupplierStaff_User_UserId(userId);
        if (CollectionUtil.isNotEmpty(supplierManagers)) {
            ArrayList<MenuNavType> menuNavTypes = Lists.newArrayList(MenuNavType.supplierSetting);
            menuNavs.addAll(menuNavService.findAllByMenuNavTypeIn(menuNavTypes));
        }
        return menuNavs;
    }

}
