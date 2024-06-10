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
        final String signature = joinPoint.getSignature().toShortString();
        final Object proceed = joinPoint.proceed();
        switch (proceed) {
            case Mono<?> mono -> {
                return mono.doOnSuccess(o -> logger.info("{} executed Mono in {} ms", signature, System.currentTimeMillis() - start));
            }
            case Flux<?> flux -> {
                return flux.doOnComplete(() -> logger.info("{} executed Flux in {} ms", signature, System.currentTimeMillis() - start));
            }
            case Throwable throwable -> {
                logger.info("{} executed Throwable in {} ms", signature, System.currentTimeMillis() - start);
            }
            case null, default -> {
                logger.info("{} executed in {} ms", signature, System.currentTimeMillis() - start);
            }
        }
        return proceed;
    }
}
