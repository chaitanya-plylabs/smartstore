package com.jio.iot.smartcheckout.domain.model.cart.events;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class ItemAddedToCartEvent {
    private String storeId;
    private String cartId;
    private String skuId;
    private String name;
    private Double price;
    private Double weight;
}
