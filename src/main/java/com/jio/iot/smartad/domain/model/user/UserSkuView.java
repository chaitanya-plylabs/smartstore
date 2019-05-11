package com.jio.iot.smartad.domain.model.user;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "user_sku_view")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class UserSkuView {
    @Id
    @Column(name = "row_num")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "store_id")
    private String storeId;

    @Column(name = "sku_id")
    private String skuId;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "amount")
    private BigDecimal amount;
}
