package postman.bottler.global.aop;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import postman.bottler.user.auth.CustomUserDetails;

@Aspect
@Slf4j
@Component
public class LogAspect {

    @Pointcut("execution(public * postman.bottler.*.presentation..*Controller.*(..))")
    public void controllerPointcut() {
    }

    @Pointcut("execution(public * postman.bottler.*.exception.*Handler.*(..))")
    public void exceptionPointcut() {
    }

    @Before("controllerPointcut()")
    public void logAround(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal instanceof CustomUserDetails ? ((CustomUserDetails) principal).getUserId().toString()
                : "none";

        log.info("Request : {} (UserId={}) {}\n{}", request.getMethod(), userId, request.getRequestURI(),
                params(joinPoint));
    }

    @AfterReturning(value = "controllerPointcut() || exceptionPointcut()", returning = "returnObj")
    public void logAfterReturning(JoinPoint joinPoint, Object returnObj) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal instanceof CustomUserDetails ? ((CustomUserDetails) principal).getUserId().toString()
                : "none";

        log.info("Response : {} (UserId={})\n{}", request.getRequestURI(), userId, returnObj);
    }

    private Map<String, Object> params(JoinPoint joinPoint) {
        String[] parameterNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
        Object[] args = joinPoint.getArgs();
        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            params.put(parameterNames[i], args[i]);
        }
        return params;
    }
}
