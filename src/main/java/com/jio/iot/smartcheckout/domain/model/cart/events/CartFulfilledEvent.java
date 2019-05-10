package com.jio.iot.smartcheckout.domain.model.cart.events;

import com.jio.iot.smartcheckout.domain.model.cart.values.Item;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class CartFulfilledEvent {
    private String storeId;
    private String cartId;
    private String userId;
    private List<Item> items;
    private Long fulfilledEpoch;
    private Double amount;
}
