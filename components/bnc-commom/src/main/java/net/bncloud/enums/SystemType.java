package net.bncloud.enums;



import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @ClassName SystemType
 * @Description: 系统类型
 * @Author liulu
 * @Date 2021/3/23
 * @Version V1.0
 **/
@Getter
@AllArgsConstructor
public enum SystemType {


    ZC("0","智采"),
    ZY("1","智易");
    ;



    private String code;

    private String name;
}
