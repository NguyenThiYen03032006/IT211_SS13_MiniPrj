package rikkei.edu.miniprojectss13.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Before("execution(* rikkei.edu.miniprojectss13.controller.*.*(..))")
    public void logBeforeController(JoinPoint joinPoint) {
        log.info("Before - Method: {}", joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "execution(* rikkei.edu.miniprojectss13.service.*.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("AfterReturning - Method {} result: {}", joinPoint.getSignature().getName(), result);
    }

    @Around("execution(* rikkei.edu.miniprojectss13.controller.*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;
        log.info("Around - {} took {} ms", joinPoint.getSignature().getName(), duration);
        return result;
    }
}

