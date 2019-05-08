package com.jio.iot.smartcheckout.domain.model.cart.events;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class CartCreatedEvent {
    private String storeId;
    private String cartId;
    private String userId;
}
