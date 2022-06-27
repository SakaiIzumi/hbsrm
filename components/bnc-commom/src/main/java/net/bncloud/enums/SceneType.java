package net.bncloud.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum SceneType {

    All("all", "全部"),
    Purchaser("purchaser", "采购方"),
    Supplier("supplier", "供应商"),
    Org("org", "组织"),
    Default("default", "默认"),
    ;

    private final String code;
    private final String name;

    public static SceneType code(String code) {
        SceneType[] values = SceneType.values();
        List<SceneType> sceneTypes = Arrays.asList(values);
        return sceneTypes.stream().filter(sceneType -> sceneType.getCode().equals(code)).findFirst().orElseGet(() -> sceneTypes.get(sceneTypes.size() - 1));
    }
}
