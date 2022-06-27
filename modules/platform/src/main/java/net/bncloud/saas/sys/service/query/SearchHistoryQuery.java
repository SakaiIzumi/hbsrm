package net.bncloud.saas.sys.service.query;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.security.Platform;
import net.bncloud.saas.sys.domain.BncModuleType;

/**
 * @ClassName SearchHistoryQuery
 * @Author Administrator
 * @Date 2021/5/10
 * @Version V1.0
 **/
@Getter
@Setter
public class SearchHistoryQuery {

    private Platform platform;

    private BncModuleType module;


}
