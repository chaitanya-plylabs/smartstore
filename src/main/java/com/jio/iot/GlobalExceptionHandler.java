package com.jio.iot;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Component
@Order(-1)
public class GlobalExceptionHandler implements WebExceptionHandler {
    public Mono<Void> handle(final ServerWebExchange exchange, final Throwable ex) {
        ex.printStackTrace();
        return Mono.empty();
    }
}
