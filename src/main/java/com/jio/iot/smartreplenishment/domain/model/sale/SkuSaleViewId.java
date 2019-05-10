package com.jio.iot.smartreplenishment.domain.model.sale;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
@Embeddable
public class SkuSaleViewId implements Serializable {
    @Column(name = "store_id")
    private String storeId;

    @Column(name = "sku_id")
    private String skuId;
}
