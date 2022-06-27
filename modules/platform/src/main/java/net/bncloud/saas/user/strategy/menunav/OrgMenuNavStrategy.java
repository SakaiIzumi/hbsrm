package net.bncloud.saas.user.strategy.menunav;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import net.bncloud.common.constants.MenuNavType;
import net.bncloud.saas.authorize.domain.MenuNav;
import net.bncloud.saas.authorize.service.MenuNavService;
import net.bncloud.saas.tenant.domain.OrgManager;
import net.bncloud.saas.tenant.domain.OrgManagerRecord;
import net.bncloud.saas.tenant.repository.OrgManagerRecordRepository;
import net.bncloud.saas.tenant.repository.OrgManagerRepository;
import net.bncloud.saas.tenant.service.OrgManagerService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("orgMenuNavStrategy")
@AllArgsConstructor
public class OrgMenuNavStrategy implements IMenuNavStrategy {

    private final OrgManagerRepository orgManagerRepository;

    private final OrgManagerRecordRepository orgManagerRecordRepository;

    private final OrgManagerService orgManagerService;

    private final MenuNavService menuNavService;

//    @Override
//    public List<MenuNav> getMeuNavs(Long userId) {
//        List<OrgManager> orgManagers = orgManagerService.findAllByUserId(userId);
//        if (CollectionUtil.isNotEmpty(orgManagers)) {
//            ArrayList<MenuNavType> menuNavTypes = Lists.newArrayList(MenuNavType.purchaserCooperate, MenuNavType.purchaserSetting);
//            return menuNavService.findAllByMenuNavTypeIn(menuNavTypes);
//        }
//        return new ArrayList<>();
//    }

    @Override
    public List<MenuNav> getMeuNavs(Long userId) {
        List<OrgManager> orgManagers = orgManagerRepository.findAllByUser_Id(userId);
        List<MenuNav> menuNavs = new ArrayList<>();
        menuNavs.addAll(menuNavService.findAllByMenuNavTypeIn(Lists.newArrayList(MenuNavType.purchaser)));
        boolean existOrgManager = existOrgManager(userId);
        if (CollectionUtil.isNotEmpty(orgManagers) || existOrgManager) {
            ArrayList<MenuNavType> menuNavTypes = Lists.newArrayList(MenuNavType.purchaserCooperate, MenuNavType.purchaserSetting);
            menuNavs.addAll(menuNavService.findAllByMenuNavTypeIn(menuNavTypes));
        }
        return menuNavs;
    }

    private boolean existOrgManager(Long userId) {
        //TODO 未创建组织的组织管理员
        Optional<OrgManagerRecord> orgManagerRecord = orgManagerRecordRepository.findByUserId(userId);
        return orgManagerRecord.isPresent();
    }
}
