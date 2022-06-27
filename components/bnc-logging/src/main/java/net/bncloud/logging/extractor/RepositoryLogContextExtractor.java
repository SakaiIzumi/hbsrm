package net.bncloud.logging.extractor;

import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

public class RepositoryLogContextExtractor extends AbstractLogContextExtractor {

    private final Set<String> supportClass = new HashSet<>();

    @Override
    public boolean supports(JoinPoint joinPoint) {

        String name = joinPoint.getTarget().getClass().getName();
        if (supportClass.contains(name)) {
            return true;
        }
        Repository annotation = joinPoint.getTarget().getClass().getAnnotation(Repository.class);
        if (annotation != null) {
            supportClass.add(name);
            return true;
        }
        return false;
    }
}
