package com.jio.iot.smartcheckout.domain.model.cart.commands;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class RemoveItemFromCartCommand extends CartCommand{
    @NotNull
    private String skuId;
}
