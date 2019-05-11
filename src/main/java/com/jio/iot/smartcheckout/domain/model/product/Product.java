package com.jio.iot.smartcheckout.domain.model.product;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Product implements Serializable {

    @EmbeddedId
    private ProductId id;

    @Column(name = "name")
    private String name;

    @Column(name= "category")
    private String category;

    @Column(name= "sub_category")
    private String subCategory;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "weight")
    private BigDecimal weight;

}
