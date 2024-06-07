package com.entertainment.subscriber.note.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectLogger {
    private static final Logger logger = LoggerFactory.getLogger(AspectLogger.class);

    /**
     * Tracks the execution time of a method annotated with @TrackTime.
     *
     * @param joinPoint The ProceedingJoinPoint representing the pointcut where the annotation is applied.
     * @return The result returned by the method being intercepted.
     * @throws Throwable if an error occurs during method execution.
     */
    @Around("@annotation(TrackTime)")
    public Object trackTime(ProceedingJoinPoint joinPoint) throws Throwable {
        final long start = System.currentTimeMillis();

        final Object proceed = joinPoint.proceed();

        final long executionTime = System.currentTimeMillis() - start;

        logger.info("{} executed in {} ms", joinPoint.getSignature().toShortString(), executionTime);

        return proceed;
    }
}
