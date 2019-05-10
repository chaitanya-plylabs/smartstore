package com.jio.iot.smartcheckout.application.resources;

import com.jio.iot.smartcheckout.domain.model.PaymentInformation;
import com.jio.iot.smartcheckout.domain.model.cart.commands.*;
import io.grpc.BinaryLog;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@RequestMapping("store/{storeId}/cart")
public class CartResource {
    @Autowired
    private CommandGateway commandGateway;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity> post(@NotNull @PathVariable("storeId") final String storeId
            , @RequestBody @NotNull @Valid final CreateCartCommand cmd) {
        cmd.setCartId(UUID.randomUUID().toString());
        cmd.setStoreId(storeId);
        this.commandGateway.sendAndWait(cmd);
        return Mono.just(ResponseEntity.noContent().header("X-Resource-Id", cmd.getCartId()).build());
    }

    @PostMapping("/{cartId}/item/{skuId}/add")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addItem(@NotNull @PathVariable("storeId") final String storeId
            , @NotNull @PathVariable("cartId") final String cartId
            , @NotNull @PathVariable("skuId") final String skuId) {
        final var cmd = new AddItemToCartCommand();
        cmd.setStoreId(storeId);
        cmd.setCartId(cartId);
        cmd.setSkuId(skuId);
        this.commandGateway.sendAndWait(cmd);
    }

    @PostMapping("/{cartId}/item/{skuId}/remove")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItem(@NotNull @PathVariable("storeId") final String storeId
            , @NotNull @PathVariable("cartId") final String cartId
            , @NotNull @PathVariable("skuId") final String skuId) {
        final var cmd = new RemoveItemFromCartCommand();
        cmd.setStoreId(storeId);
        cmd.setCartId(cartId);
        cmd.setSkuId(skuId);
        this.commandGateway.sendAndWait(cmd);
    }

    @PostMapping("/{cartId}/checkout")
    @ResponseStatus(HttpStatus.OK)
    public Mono<PaymentInformation> checkout(@NotNull @PathVariable("storeId") final String storeId
            , @NotNull @PathVariable("cartId") final String cartId
            , @NotNull @RequestBody @Valid final CheckoutCartCommand cmd) {
        cmd.setStoreId(storeId);
        cmd.setCartId(cartId);
        final var paymentInformation = (PaymentInformation) this.commandGateway.sendAndWait(cmd);
        return Mono.just(paymentInformation);
    }

    @PostMapping("/{cartId}/processed")
    @ResponseStatus(HttpStatus.OK)
    public void processPayment(@NotNull @PathVariable("storeId") final String storeId
            ,@NotNull @PathVariable("cartId") final String cartId
            ,@NotNull @RequestBody @Valid ProcessPaymentCommand cmd) {
        cmd.setStoreId(storeId);
        cmd.setCartId(cartId);
        this.commandGateway.sendAndWait(cmd);
    }

}
