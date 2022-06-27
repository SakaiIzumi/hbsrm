package net.bncloud.saas.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class DeleteOrgManagerRecord {
    private Long userId;

    public static DeleteOrgManagerRecord of(Long userId) {
        return new DeleteOrgManagerRecord(userId);
    }
}
