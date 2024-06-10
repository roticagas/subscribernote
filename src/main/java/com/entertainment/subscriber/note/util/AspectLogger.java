package com.entertainment.subscriber.note.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    @Around("@annotation(com.entertainment.subscriber.note.util.TrackTime)")
    public Object trackTime(ProceedingJoinPoint joinPoint) throws Throwable {
        final long start = System.currentTimeMillis();

        final Object proceed = joinPoint.proceed();

        if (proceed instanceof Mono) {
            return ((Mono<?>) proceed).doOnSuccess(o -> {
                logger.info("{} executed Mono in {} ms", joinPoint.getSignature().toShortString(), System.currentTimeMillis() - start);
            });
        } else if (proceed instanceof Flux) {
            return ((Flux<?>) proceed).doOnComplete(() -> {
                logger.info("{} executed Flux in {} ms", joinPoint.getSignature().toShortString(), System.currentTimeMillis() - start);
            });
        } else if (proceed instanceof Throwable) {
            logger.info("{} executed Throwable in {} ms", joinPoint.getSignature().toShortString(), System.currentTimeMillis() - start);
        } else {
            logger.info("{} executed in {} ms", joinPoint.getSignature().toShortString(), System.currentTimeMillis() - start);
        }
        return proceed;
    }
}
