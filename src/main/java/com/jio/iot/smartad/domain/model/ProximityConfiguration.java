package com.jio.iot.smartad.domain.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "proximity_configuration")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class ProximityConfiguration {
    @Id
    private Integer configId;

    @Column(name = "store_id")
    private String storeId;

    @Column(name = "beacon_id")
    private String beaconId;

    @Column(name = "sku_id")
    private String skuId;
}
