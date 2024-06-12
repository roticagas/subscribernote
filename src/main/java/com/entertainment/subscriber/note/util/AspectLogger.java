package com.entertainment.subscriber.note.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
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
        Object proceed = joinPoint.proceed();
        final String requestId = MDC.get("requestId");
        switch (proceed) {
            case Mono<?> mono -> {
                return mono.doOnSuccess(o -> trackTimeLog(joinPoint, requestId, start));
            }
            case Flux<?> flux -> {
                return flux.doOnComplete(() -> trackTimeLog(joinPoint, requestId, start));
            }
            case null, default -> trackTimeLog(joinPoint, requestId, start);
        }
        return proceed;
    }

    private void trackTimeLog(ProceedingJoinPoint joinPoint, String requestId, long start) {
        MDC.put("requestId", requestId);
        logger.info("{} executed in {} ms", joinPoint.getSignature().toShortString(), System.currentTimeMillis() - start);
        MDC.clear();
    }
}
