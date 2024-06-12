package com.entertainment.subscriber.note.config;

import com.entertainment.subscriber.note.util.MdcContextLifter;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Operators;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

/**
 * RequestIdWebFilter is a WebFilter implementation that sets the request ID in the ThreadContext (MDC)
 * for each incoming request. If the request does not contain a value for the "X-REQUEST-ID" header,
 * a new unique Base64-encoded UUID is created.
 */
@Component
public class RequestIdWebFilter implements WebFilter {

    private static String convertUUIDToBase64(UUID uuid) {
        // Write the UUID to a ByteBuffer
        ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());

        // Convert to base64 and return
        return (Base64.getUrlEncoder().withoutPadding().encodeToString(buffer.array()) + "XYZ").substring(0, 25);
    }

    private static String getRequestId(ServerWebExchange exchange) {
        if (exchange == null) {
            return convertUUIDToBase64(UUID.randomUUID());
        }
        String requestId = exchange.getRequest().getHeaders().getFirst("X-REQUEST-ID");
        if (requestId == null || requestId.isEmpty()) {
            return convertUUIDToBase64(UUID.randomUUID());
        }
        return requestId;
    }


    /**
     * This method is used to filter incoming requests and set the request ID in the ThreadContext (MDC).
     *
     * @param exchange The ServerWebExchange object representing the incoming request and response.
     * @param chain    The WebFilterChain object representing the chain of filters to be applied.
     * @return A Mono<Void> that represents the completion of the filter operation.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        final String requestId = getRequestId(exchange);
        MDC.put("requestId", requestId);
        return chain.filter(exchange)
                .contextWrite(context -> context.put("requestId", requestId))
                .doOnTerminate(MDC::clear);
    }

    private static final String KEY = RequestIdWebFilter.class.getName();

    /**
     * This method is used to register a context operator hook.
     * The hook sets the MDC (Mapped Diagnostic Context) context for each operator in the reactive stream pipeline.
     *
     * The MDC is a thread-local context store that allows the application to store diagnostic information
     * that is associated with the current execution thread. This information is typically used for logging
     * and diagnostic purposes.
     *
     * The method registers a lift operator that creates an instance of the MdcContextLifter class for each
     * operator in the stream. The MdcContextLifter class is responsible for setting the MDC context for each
     * operator and propagating the context downstream.
     *
     * This method is annotated with @PostConstruct, which means it will be called automatically after the
     * bean is constructed by the Spring container.
     */
    @PostConstruct
    void contextOperatorHook() {
        Hooks.onEachOperator(KEY, Operators.lift((ignored, subscriber) -> new MdcContextLifter(subscriber)));
    }

    /**
     * This method is annotated with @PreDestroy, which means it will be called automatically
     * before the containing bean is destroyed by the Spring container. It is used as a cleanup hook
     * to reset the MDC (Mapped Diagnostic Context) context for each operator in the reactive stream pipeline.
     */
    @PreDestroy
    void cleanupHook() {
        Hooks.resetOnEachOperator(KEY);
    }
}



