package net.bncloud.security.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.bncloud.security.annotation.web.AnonymousDeleteMapping;
import net.bncloud.security.annotation.web.AnonymousGetMapping;
import net.bncloud.security.annotation.web.AnonymousPatchMapping;
import net.bncloud.security.annotation.web.AnonymousPostMapping;
import net.bncloud.security.annotation.web.AnonymousPutMapping;

@Getter
@AllArgsConstructor
public enum RequestMethodEnum {

    /**
     * @see AnonymousGetMapping
     */
    GET("GET"),

    /**
     * @see AnonymousPostMapping
     */
    POST("POST"),

    /**
     * @see AnonymousPutMapping
     */
    PUT("PUT"),

    /**
     * @see AnonymousPatchMapping
     */
    PATCH("PATCH"),

    /**
     * @see AnonymousDeleteMapping
     */
    DELETE("DELETE"),

    /**
     * 否则就是所有 Request 接口都放行
     */
    ALL("All");

    /**
     * Request 类型
     */
    private final String type;

    public static RequestMethodEnum find(String type) {
        for (RequestMethodEnum value : RequestMethodEnum.values()) {
            if (type.equals(value.getType())) {
                return value;
            }
        }
        return ALL;
    }
}
