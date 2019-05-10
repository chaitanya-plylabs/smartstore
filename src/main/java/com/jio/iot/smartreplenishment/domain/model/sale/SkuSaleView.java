package com.jio.iot.smartreplenishment.domain.model.sale;


import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sku_sale_view")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class SkuSaleView {
    @EmbeddedId
    private SkuSaleViewId id;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "quantity")
    private Integer quantity;
}
