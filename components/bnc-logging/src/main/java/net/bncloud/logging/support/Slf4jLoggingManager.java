package net.bncloud.logging.support;

import net.bncloud.logging.context.LogContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jLoggingManager extends AbstractStringLogManager {

    @Override
    public void record(LogContext actionContext) {
        String className = actionContext.getClassName();
        Logger logger = LoggerFactory.getLogger(className);
        logger.info(toString(actionContext));
    }
}
