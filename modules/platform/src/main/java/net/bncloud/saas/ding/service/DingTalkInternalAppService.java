package net.bncloud.saas.ding.service;

import net.bncloud.saas.ding.domain.DingTalkApp;
import net.bncloud.saas.ding.repository.DingTalkAppRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DingTalkInternalAppService {

    private final DingTalkAppRepository dingTalkAppRepository;

    public DingTalkInternalAppService(DingTalkAppRepository dingTalkAppRepository) {
        this.dingTalkAppRepository = dingTalkAppRepository;
    }

    public DingTalkApp createInternalApp(DingTalkApp app) {
        DingTalkApp saved = dingTalkAppRepository.save(app);
        return saved;
    }

    public Page<DingTalkApp> pageQuery(Pageable pageable) {
        return dingTalkAppRepository.findAll(pageable);
    }

    public void updateInternalApp(DingTalkApp app) {
        dingTalkAppRepository.save(app);
    }
}
