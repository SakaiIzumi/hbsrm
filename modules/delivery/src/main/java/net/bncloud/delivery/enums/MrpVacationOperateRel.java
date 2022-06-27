package net.bncloud.delivery.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author liyh
 **/
@Getter
@AllArgsConstructor
@Slf4j
public enum MrpVacationOperateRel {

    BUTTON("1","按钮控制",new MrpOperateType[]{
            MrpOperateType.EDIT,
            MrpOperateType.DELETE,
            MrpOperateType.ON_OFF

    }),

    ;

    private String code;

    private String name;

    private MrpOperateType[] operations;

    /**
     */
    public static Map<String,Boolean> operations(Boolean flag){
        Map<String,Boolean> permissions = new HashMap<>();
        //初始化
        for(MrpOperateType mrpOperateType:MrpOperateType.values()){
            permissions.put(mrpOperateType.getCode(),flag);
        }
        return permissions;
    }
}