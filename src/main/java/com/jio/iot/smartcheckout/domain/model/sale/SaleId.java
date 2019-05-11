package com.jio.iot.smartcheckout.domain.model.sale;

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
public class SaleId implements Serializable {
    @Column(name = "store_id")
    private String storeId;

    @Column(name = "cart_id")
    private String cartId;
}
