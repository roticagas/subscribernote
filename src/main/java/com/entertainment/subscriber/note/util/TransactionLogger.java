package com.entertainment.subscriber.note.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
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
        try {
            Object proceed = joinPoint.proceed();
            return ((Mono<?>) proceed).doOnSuccess(o -> logger.info("0||{}", System.currentTimeMillis() - start));
        } catch (Throwable throwable) {
            logger.info("-1|{}|{}", throwable.getMessage(), System.currentTimeMillis() - start);
            throw throwable;
        }
    }
}
