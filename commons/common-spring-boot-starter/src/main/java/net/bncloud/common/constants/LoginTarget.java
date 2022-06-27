package net.bncloud.common.constants;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public enum LoginTarget {
    ADMIN,
    ZC,
    ZY,
    UNKNOWN,
    ;


    private static final Map<String, LoginTarget> targets = new HashMap<>(4);

    static {
        targets.put(ADMIN.name(), ADMIN);
        targets.put(ZC.name(), ZC);
        targets.put(ZY.name(), ZY);
        targets.put(UNKNOWN.name(), UNKNOWN);
    }

    /**
     * 获取对应的枚举类型
     */
    public static LoginTarget getValueOf(String target){
        if (StringUtils.isBlank(target)) {
            return LoginTarget.UNKNOWN;
        }
        LoginTarget loginTarget = targets.get(target.toUpperCase());
        if (loginTarget == null) {
            return LoginTarget.UNKNOWN;
        }
        return loginTarget;
    }
}
