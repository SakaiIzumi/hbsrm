package net.bncloud.saas.authorize.repository;

import net.bncloud.common.constants.MenuNavType;
import net.bncloud.saas.authorize.domain.Menu;
import net.bncloud.saas.authorize.domain.MenuNav;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuNavRepository extends JpaRepository<MenuNav, Long>, JpaSpecificationExecutor<MenuNav> {

    List<MenuNav> findAllByNeedAuthOrderByOrderNum(Boolean aTrue);

    List<MenuNav> findAllByMenuNavTypeIn(List<MenuNavType> types);
}
