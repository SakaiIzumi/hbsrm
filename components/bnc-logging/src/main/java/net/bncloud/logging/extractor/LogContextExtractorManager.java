package net.bncloud.logging.extractor;

import java.util.Collections;
import java.util.List;

public class LogContextExtractorManager {

    private List<LogContextExtractor> extractors = Collections.emptyList();

    public LogContextExtractorManager(List<LogContextExtractor> extractors) {
        this.extractors = extractors;
    }

    private void checkState() {
        if (extractors.isEmpty()) {
            throw new IllegalArgumentException("没有可用的ActionContextExtractor");
        }
    }

    public List<LogContextExtractor> getExtractors() {
        return extractors;
    }

//    public RequestContext extractFrom(JoinPoint joinPoint) {
//        RequestContext requestContext = new RequestContext();
//
//
//        return requestContext;
//    }
//
//    public ResponseContext extractFrom(JoinPoint joinPoint, Object retVal, SysLog sysLog) {
//        return null;
//    }
//
//    public ResponseContext extractFrom(JoinPoint joinPoint, Exception exception, SysLog sysLog) {
//        return null;
//    }
}
