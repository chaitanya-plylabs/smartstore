package com.jio.iot.smartcheckout.domain.model.rack.commands;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class AddItemToRackCommand {
    @NotNull
    private String skuId;

    @NotNull
    private String rackId;
}
