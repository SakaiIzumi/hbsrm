package net.bncloud.saas.sys.domain;

import lombok.Getter;
import lombok.Setter;
import net.bncloud.common.domain.AbstractAuditingEntity;
import net.bncloud.common.security.Platform;

import javax.persistence.*;

/**
 * @ClassName SearchHistory
 * @Description: 搜索历史
 * @Author Administrator
 * @Date 2021/5/10
 * @Version V1.0
 **/
@Entity
@Table(name = "ss_sys_search_history")
@Getter
@Setter
public class SearchHistory extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long companyId;
    private Long userId;
    @Enumerated(EnumType.STRING)
    private Platform platform;
    @Enumerated(EnumType.STRING)
    private BncModuleType module;
    private String value;
}
