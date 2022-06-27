package net.bncloud.saas.authorize.service;

import lombok.AllArgsConstructor;
import net.bncloud.saas.authorize.domain.Role;
import net.bncloud.saas.authorize.domain.User;
import net.bncloud.saas.authorize.repository.RoleRepository;
import net.bncloud.saas.authorize.repository.UserRepository;
import net.bncloud.saas.event.CreatedUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    @Transactional(rollbackFor = Exception.class)
    public User createUser(CreatedUser user) {
        Optional<User> exist = userRepository.findById(user.getId());
        User u;
        if (exist.isPresent()) {
            u = exist.get();
        } else {
            u = new User();
            u.setUserId(user.getId());
        }
        u.setName(user.getName());
        return userRepository.save(u);
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(User user) {
        userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getUserByRoles(List<Role> roles) {
        return userRepository.findByRolesIn(roles);

    }

}
