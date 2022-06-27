package net.bncloud.logging;

import net.bncloud.logging.context.LogContext;

public interface LogManager {

    void record(LogContext logContext);
}
