package com.jio.iot.smartcheckout.domain.model.cart.events;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class CartCheckedoutEvent {
    private String storeId;
    private String cartId;
}
