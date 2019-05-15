package com.jio.iot.smartad.domain.model;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Offer {
    private String userId;
    private String storeId;
    private String skuId;
    private Double percentOffer;
}
