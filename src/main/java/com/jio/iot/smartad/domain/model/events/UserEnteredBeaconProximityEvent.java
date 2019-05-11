package com.jio.iot.smartad.domain.model.events;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class UserEnteredBeaconProximityEvent {
    private String storeId;
    private String userId;
    private String beaconId;
}
