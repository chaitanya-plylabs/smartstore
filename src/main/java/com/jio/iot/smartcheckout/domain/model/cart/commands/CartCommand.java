package com.jio.iot.smartcheckout.domain.model.cart.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CartCommand {
    @TargetAggregateIdentifier
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String cartId;

    private String storeId;
}
