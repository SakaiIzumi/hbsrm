package net.bncloud.logging.util;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.stream.Collectors;

public class AopUtils {

	private AopUtils() {}

	public static JoinPoint unWrapJoinPoint(final JoinPoint point) {
		JoinPoint naked = point;
		while (naked.getArgs().length > 0 && naked.getArgs()[0] instanceof JoinPoint) {
			naked = (JoinPoint) naked.getArgs()[0];
		}
		return naked;
	}

    public static String extractMethodWithParamToString(final JoinPoint logTarget) {
        Signature signature = logTarget.getSignature();
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException(""); //TODO
        }

        Object[] args = logTarget.getArgs();
        String argString = Arrays.stream(args).filter(arg -> {
            if (arg instanceof HttpServletResponse) {
                return false;
            }
            if (arg instanceof HttpServletRequest) {
                return false;
            }
            return !(arg instanceof HttpSession);
        }).map(JSON::toJSONString).collect(Collectors.joining(","));
        return extractMethodToString(logTarget) + "(" + argString + ")";
    }

    public static String extractMethodToString(final JoinPoint joinTarget) {
        Signature signature = joinTarget.getSignature();
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("");
        }
        MethodSignature methodSignature = (MethodSignature) signature;

        Object target = joinTarget.getTarget();

        return target.getClass().getSimpleName() + "#" + methodSignature.getName();
    }

    public static String extractAction(final JoinPoint joinTarget) {
        Signature signature = joinTarget.getSignature();
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("");
        }
        MethodSignature methodSignature = (MethodSignature) signature;
        ApiOperation apiOperation = methodSignature.getMethod().getAnnotation(ApiOperation.class);
        if (apiOperation != null) {
            String value = apiOperation.value();
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }
        return extractMethodToString(joinTarget);
    }
}
