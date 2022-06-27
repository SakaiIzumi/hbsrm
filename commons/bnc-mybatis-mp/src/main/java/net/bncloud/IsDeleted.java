package net.bncloud;

/**
 * @ClassName IsDeleted
 * @Description: 删除标志枚举类
 * @Author Administrator
 * @Date 2021/3/15
 * @Version V1.0
 **/
public enum IsDeleted {
    Y(1,"已删除")
    ,N(0,"未删除");

    private Integer code;
    private String name;

    IsDeleted(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
