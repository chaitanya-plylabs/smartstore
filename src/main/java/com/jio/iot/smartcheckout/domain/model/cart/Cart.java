package com.jio.iot.smartcheckout.domain.model.cart;

import com.jio.iot.smartcheckout.domain.model.PaymentInformation;
import com.jio.iot.smartcheckout.domain.model.cart.commands.*;
import com.jio.iot.smartcheckout.domain.model.cart.events.*;
import com.jio.iot.smartcheckout.domain.model.cart.values.CartStatus;
import com.jio.iot.smartcheckout.domain.model.cart.values.Item;
import com.jio.iot.smartcheckout.domain.model.product.ProductId;
import com.jio.iot.smartcheckout.domain.model.product.ProductRepository;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@SuppressWarnings("all")
@EqualsAndHashCode(of = "cartId")
@ToString
@Aggregate
public class Cart {
    @AggregateIdentifier
    private String cartId;
    private String userId;
    private List<Item> items;
    private CartStatus status;

    @SuppressWarnings("unused")
    Cart() {

    }

    @CommandHandler
    public Cart(final CreateCartCommand cmd) {
        apply(CartCreatedEvent.builder()
                .storeId(cmd.getStoreId())
                .cartId(cmd.getCartId())
                .userId(cmd.getUserId())
                .build());
    }

    @CommandHandler
    public void handle(final AddItemToCartCommand cmd, @Autowired final ProductRepository productRepository) {
        checkState(this.status.equals(CartStatus.ACTIVE));
        final var product = productRepository.findById(new ProductId(cmd.getStoreId(), cmd.getSkuId())).get();
        apply(ItemAddedToCartEvent.builder()
                .storeId(cmd.getStoreId())
                .cartId(cmd.getCartId())
                .skuId(cmd.getSkuId())
                .name(product.getName())
                .price(product.getPrice().doubleValue())
                .weight(product.getWeight().doubleValue())
                .build());
    }

    @CommandHandler
    private void handle(final RemoveItemFromCartCommand cmd) {
        checkState(this.status.equals(CartStatus.ACTIVE));
        if(this.items.stream().noneMatch(item -> item.getSkuId().equals(cmd.getSkuId())))
            throw new IllegalStateException();
        apply(ItemRemovedFromCartEvent.builder()
                .storeId(cmd.getStoreId())
                .skuId(cmd.getSkuId())
                .build());
    }

    @CommandHandler
    private PaymentInformation handle(final CheckoutCartCommand cmd) {
        checkState(this.status.equals(CartStatus.ACTIVE));
        final var totalWeight = this.items.stream()
                .map(item -> item.getWeight().doubleValue() * item.getQuantity())
                .mapToDouble(d -> d)
                .sum();
        checkState(new BigDecimal(cmd.getCartWeight()).equals(new BigDecimal(totalWeight)));
        final var total = this.items.stream()
                .map(item -> item.getPrice().doubleValue() * item.getQuantity())
                .mapToDouble(d -> d)
                .sum();
        apply(CartCheckedoutEvent.builder().cartId(cmd.getCartId()).build());
        return new PaymentInformation(cmd.getCartId(), total);
    }

    @CommandHandler
    private void handle(final ProcessPaymentCommand cmd) {
        checkState(this.status.equals(CartStatus.PAYMENT_PENDING));
        apply(CartFulfilledEvent.builder()
                .storeId(cmd.getStoreId())
                .cartId(cmd.getCartId())
                .userId(this.userId)
                .amount(cmd.getAmount())
                .fulfilledEpoch(Instant.now().toEpochMilli())
                .items(this.items)
                .build());
    }

    @EventHandler
    public void on(final CartCreatedEvent event) {
        this.cartId = event.getCartId();
        this.userId = event.getUserId();
        this.items = new ArrayList<>();
        this.status = CartStatus.ACTIVE;

    }

    @EventHandler
    public void on(final ItemAddedToCartEvent event) {
        final var itemOptional = this.items.stream().filter(item -> item.getSkuId().equals(event.getSkuId())).findFirst();
        itemOptional.ifPresentOrElse(Item::incQuantity, () -> {
            final var item = Item.builder()
                    .skuId(event.getSkuId())
                    .name(event.getName())
                    .price(new BigDecimal(event.getPrice()))
                    .weight(new BigDecimal(event.getWeight()))
                    .quantity(1)
                    .build();
            this.items.add(item);
        });
    }

    @EventHandler
    public void on(final ItemRemovedFromCartEvent event) {
        final var itemOptional = this.items.stream().filter(item -> item.getSkuId().equals(event.getSkuId())).findFirst();
        itemOptional.ifPresent(item -> {
            item.decQuantity();
            if(item.getQuantity() == 0)
                this.items.remove(item);
        });
    }

    @EventHandler
    public void on(final CartCheckedoutEvent event) {
        this.status = CartStatus.PAYMENT_PENDING;
    }

    @EventHandler
    public void on(final CartFulfilledEvent event) {
        this.status = CartStatus.PROCESSED;
    }
}
