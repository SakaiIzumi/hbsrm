package net.bncloud.saas.ding.service;

import net.bncloud.saas.ding.domain.DingUser;
import net.bncloud.saas.ding.repository.DingUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DingTalkUserService {

    @Autowired
    private DingUserRepository dingUserRepository;

    public List<String> findAllUserIds() {
        List<DingUser> all = dingUserRepository.findAll();
        return all.stream().map(user-> user.getId().getUserId()).collect(Collectors.toList());
    }

}
