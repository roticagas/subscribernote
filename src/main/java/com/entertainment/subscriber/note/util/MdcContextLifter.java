package com.entertainment.subscriber.note.util;

import org.reactivestreams.Subscription;
import org.slf4j.MDC;
import reactor.core.CoreSubscriber;
import reactor.util.context.Context;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * The MdcContextLifter class is responsible for lifting the MDC (Mapped Diagnostic Context) context
 * for each operator in the reactive stream pipeline. It implements the CoreSubscriber interface.
 *
 * The MDC is a thread-local context store that allows the application to store diagnostic information
 * that is associated with the current execution thread. This information is typically used for logging
 * and diagnostic purposes.
 */
public class MdcContextLifter<T> implements CoreSubscriber<T> {
    final CoreSubscriber<T> coreSubscriber;

    public MdcContextLifter(CoreSubscriber<T> coreSubscriber) {
        this.coreSubscriber = coreSubscriber;
    }

    @Override
    public Context currentContext() {
        return coreSubscriber.currentContext();
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        coreSubscriber.onSubscribe(subscription);
    }

    @Override
    public void onNext(T t) {
        if (!coreSubscriber.currentContext().isEmpty()) {
            Map<String, String> map = coreSubscriber.currentContext().stream()
                    .collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue().toString()));
            MDC.setContextMap(map);
        } else {
            MDC.clear();
        }
        coreSubscriber.onNext(t);
    }

    @Override
    public void onError(Throwable throwable) {
        coreSubscriber.onError(throwable);
    }

    @Override
    public void onComplete() {
        coreSubscriber.onComplete();
    }
}
