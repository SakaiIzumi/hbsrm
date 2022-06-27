package net.bncloud.saas.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CreateOrganization {
    private final Long userId;
    private final Long orgId;
    private final String orgName;
    private final Long orgManagerRecordId;
    private final boolean isFirstTime; //首次需要清除原有ss_user权限

    public static CreateOrganization of(Long userId, Long orgId, String orgName, Long orgManagerRecordId, boolean isFirstTime) {
        return new CreateOrganization(userId, orgId, orgName, orgManagerRecordId, isFirstTime);
    }
}
