package com.jio.iot.smartad.domain.model.events;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class UserHomeSkuQuantityEvent {
    private String userId;
    private String deviceId;
    private String skuId;
    private Integer quantity;
}
