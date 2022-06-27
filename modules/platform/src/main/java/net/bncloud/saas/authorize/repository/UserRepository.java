package net.bncloud.saas.authorize.repository;

import net.bncloud.saas.authorize.domain.Role;
import net.bncloud.saas.authorize.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   List<User> findByRolesIn(List<Role> roles);
}
