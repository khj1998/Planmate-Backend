package com.planmate.server.aspect;

import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
@Generated
public class LoggerAspect {

    @Before("execution(* com.planmate.server..*(..))")
    public void before(JoinPoint joinPoint) {
        log.info("[START] | where: " + joinPoint.toString());
    }

    @AfterReturning("execution(* com.planmate.server..*(..))")
    public void afterReturning(JoinPoint joinPoint) {
        log.info("[END] | where: " + joinPoint.toString());
    }

    @AfterThrowing(value = "execution(* com.planmate.server..*(..))", throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, Exception exception) {
        log.error("[END_WITH_FAIL] | where: " + joinPoint.toString());
        log.error("[END_WITH_FAIL] | exception: [" + exception + "]", exception);
    }
}
