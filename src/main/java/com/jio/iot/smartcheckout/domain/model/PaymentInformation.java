package com.jio.iot.smartcheckout.domain.model;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PaymentInformation {
    private String cartId;
    private Double totalPrice;
}
