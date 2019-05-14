package com.jio.iot;

import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Order(-1)
public class GlobalExceptionHandler implements WebExceptionHandler {
    public Mono<Void> handle(final ServerWebExchange exchange, final Throwable ex) {
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        final DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(getErrorResponse());
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }

    private byte[] getErrorResponse() {
        return "{}".getBytes();
    }
}
