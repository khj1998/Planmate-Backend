package com.planmate.server.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class LoggerAspect {
    private final Logger logger = LoggerFactory.getLogger(LoggerAspect.class);

    @Around("execution(* com.planmate.server.controller..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        logger.info("[Method Start] -> classname : {}, methodname : {}",className,methodName);

        final StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        logger.info("[Method End] -> classname : {}, methodname : {}, execution time : {}ms",
                className,methodName,stopWatch.getTotalTimeMillis());

        return result;
    }

    @AfterThrowing(pointcut = "execution(* com.planmate.server.exception..*(..))",throwing = "exception")
    public void logException(JoinPoint joinPoint, Exception exception) {
        logger.error("[Exception] -> exception message : {}",exception.getMessage());
    }
}