package com.jio.iot.smartcheckout.domain.model.sale;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Embeddable
public class LineItem implements Serializable {
    @Column(name = "sku_id")
    private String skuId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "weight")
    private BigDecimal weight;

    @Column(name = "price")
    private BigDecimal price;
}
