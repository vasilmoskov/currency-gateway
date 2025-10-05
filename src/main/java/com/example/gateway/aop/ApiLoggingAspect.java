package com.example.gateway.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class ApiLoggingAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiLoggingAspect.class);

    /**
     * Logs all incoming API calls and their execution time.
     * Applies to all methods in classes annotated with @RestController.
     */
    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object logApiCall(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        LOGGER.info("API request: {} called with args: {}", methodName, args);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object result = joinPoint.proceed();

        stopWatch.stop();

        LOGGER.info("API request: {} completed in {} ms", methodName, stopWatch.getTotalTimeMillis());

        return result;
    }

    /**
     * Logs exceptions thrown by any method within the package 'com.example.gateway' and its subpackages.
     */
    @AfterThrowing(pointcut = "execution(* com.example.gateway..*(..))", throwing = "ex")
    public void logExceptions(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().toShortString();
        LOGGER.warn("Exception in {}: {}", methodName, ex.getLocalizedMessage());
    }
}
