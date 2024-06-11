package com.entertainment.subscriber.note.util;

import org.apache.logging.log4j.ThreadContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Aspect
@Component
public class TransactionLogger {
    private static final Logger logger = LoggerFactory.getLogger("TransactionLogger");

    @Pointcut("execution(* com.entertainment.subscriber.note.controller.SubscriberNoteController.update(..))")
    public void subscriberNoteControllerUpdate() {}

    @Around("subscriberNoteControllerUpdate()")
    public Object subscriberNoteControllerUpdateLog(ProceedingJoinPoint joinPoint) throws Throwable {
        final long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        final String requestId = ThreadContext.get("requestId");
        switch (proceed) {
            case Mono<?> mono -> {
                return mono.doOnSuccess(o -> handleLogging("0", "", requestId, start));
            }
            case Flux<?> flux -> {
                return flux.doOnComplete(() -> handleLogging("0", "", requestId, start));
            }
            case Throwable throwable -> handleLogging("-1", throwable.getMessage(), requestId, start);
            case null, default -> handleLogging("0", "", requestId, start);
        }
        return proceed;
    }

    private void handleLogging(String status, String status2, String requestId, long start) {
        ThreadContext.put("requestId", requestId);
        logger.info("{}|{}|{}", status, status2, System.currentTimeMillis() - start);
        ThreadContext.clearMap();
    }
}
