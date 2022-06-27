package net.bncloud.api.feign.saas.user;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum SubjectType {
    platform,
    org,
    supplier;



    public static boolean isPlatform(String subjectType) {
        return SubjectType.platform.name().equals(subjectType);
    }
}
