package com.jio.iot.smartcheckout.domain.model.cart.values;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Item {
    @NonNull
    private String skuId;

    @NonNull
    private String name;

    @NonNull
    private BigDecimal price;

    @NonNull
    private BigDecimal weight;

    @NonNull
    private Integer quantity;

    public void incQuantity() {
        this.quantity += 1;
    }

    public void decQuantity() {
        this.quantity -= 1;
    }
}
