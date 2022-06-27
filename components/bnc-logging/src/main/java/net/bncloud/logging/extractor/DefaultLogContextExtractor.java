package net.bncloud.logging.extractor;

import org.aspectj.lang.JoinPoint;

public class DefaultLogContextExtractor extends AbstractLogContextExtractor {

    @Override
    public boolean supports(JoinPoint joinPoint) {
        return true;
    }
}
