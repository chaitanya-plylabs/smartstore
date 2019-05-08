package com.jio.iot.smartcheckout.domain.model.sale;

import com.jio.iot.smartcheckout.domain.model.cart.events.CartFulfilledEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.util.stream.Collectors;

@Service
@ProcessingGroup("smartstore")
public class SaleEventHandler {
    @Autowired
    private SaleRepository saleRepository;

    @EventHandler
    public void on(final CartFulfilledEvent event) {
        final var sale =  Sale.builder()
                .id(new SaleId(event.getStoreId(), event.getCartId()))
                .amount(new BigDecimal(event.getAmount()))
                .date(Date.from(Instant.ofEpochMilli(event.getFulfilledEpoch())))
                .userId(event.getUserId())
                .createdEpoch(event.getFulfilledEpoch())
                .lineItems(event.getItems().stream().map(x -> LineItem.builder()
                        .skuId(x.getSkuId())
                        .quantity(x.getQuantity())
                        .weight(x.getWeight())
                        .price(x.getPrice())
                        .build()).collect(Collectors.toSet()))
                .build();
        this.saleRepository.save(sale);
    }
}
