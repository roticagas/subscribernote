package com.entertainment.subscriber.note.config;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

/**
 * A WebFilter implementation that sets the request ID in the ThreadContext.
 * This class implements the WebFilter interface and is responsible for setting the request ID in the ThreadContext
 * for each incoming request. If the request does not contain a value for the "X-REQUEST-ID" header, a new unique
 * request ID will be generated and set in the ThreadContext. The request ID is then available to other components
 * in the application for logging or any other purpose.
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

    /**
     * Sets the request ID in the ThreadContext for each incoming request.
     * If the request does not contain a value for the "X-REQUEST-ID" header,
     * create a new unique Base64 from UUID
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String requestId = exchange.getRequest().getHeaders().getFirst("X-REQUEST-ID");
        if (requestId == null || requestId.isEmpty()) {
            requestId = convertUUIDToBase64(UUID.randomUUID());
        }
        MDC.put("requestId", requestId);
        return chain.filter(exchange)
                .doOnTerminate(MDC::clear);
    }
}
