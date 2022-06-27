package net.bncloud.saas.sys.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class DependencyFunc {
    private Long targetId;
    private String targetName;

    public static DependencyFunc of(Long targetId, String targetName) {
        return new DependencyFunc(targetId, targetName);
    }
}
