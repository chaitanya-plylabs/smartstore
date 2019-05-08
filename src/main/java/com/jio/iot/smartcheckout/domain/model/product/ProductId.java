package com.jio.iot.smartcheckout.domain.model.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@Embeddable
public class ProductId implements Serializable {
    @Column(name = "store_id")
    private String storeId;

    @Column(name = "sku_id")
    private String skuId;
}
