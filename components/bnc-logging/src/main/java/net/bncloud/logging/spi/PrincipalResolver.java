package net.bncloud.logging.spi;

import org.aspectj.lang.JoinPoint;

public interface PrincipalResolver {

    /**
     * Default String that can be used when the user is anonymous.
     */
    String ANONYMOUS_USER = "log:anonymous";

    /**
     * Default String that can be used when the user cannot be determined.
     */
    String UNKNOWN_USER = "log:unknown";

    /**
     * Resolve the principal performing an log-able action.
     * <p>
     * Note, this method should NEVER throw an exception *unless* the expectation is that a failed resolution causes
     * the entire transaction to fail.  Otherwise use {@link PrincipalResolver#UNKNOWN_USER}.
     *
     * @param joinPoint the join point where we're logging.
     * @param retVal the returned value
     * @return	The principal as a String. CANNOT be NULL.
     */
    Principal resolveFrom(JoinPoint joinPoint, Object retVal);

    /**
     * Resolve the principal performing an log-able action that has incurred
     * an exception.
     * <p>
     * Note, this method should NEVER throw an exception *unless* the expectation is that a failed resolution causes
     * the entire transaction to log.  Otherwise use {@link PrincipalResolver#UNKNOWN_USER}.
     *
     * @param joinPoint the join point where we're logging.
     * @param exception	The exception incurred when the join point proceeds.
     * @return	The principal as a String. CANNOT be NULL.
     */
    Principal resolveFrom(JoinPoint joinPoint, Exception exception);

    /**
     * Called when there is no other way to resolve the principal (i.e. an error was captured, logging was not
     * called, etc.)
     * <p>
     * Note, this method should NEVER throw an exception *unless* the expectation is that a failed resolution causes
     * the entire transaction to fail.  Otherwise use {@link PrincipalResolver#UNKNOWN_USER}.
     *
     * @return the principal.  CANNOT be NULL.
     */
    Principal resolve();
}
