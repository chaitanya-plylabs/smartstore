package com.jio.iot.smartcheckout.domain.model.sale;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "sale")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Sale {
    @EmbeddedId
    private SaleId id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "created_epoch")
    private Long createdEpoch;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "sale_date")
    @Temporal(TemporalType.DATE)
    private Date date;

    @ElementCollection
    @CollectionTable(name = "line_item", joinColumns = { @JoinColumn(name = "cart_id"), @JoinColumn(name = "store_id")})
    private Set<LineItem> lineItems;

}
