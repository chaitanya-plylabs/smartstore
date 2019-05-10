package com.jio.iot.smartreplenishment.application.resources;

import com.jio.iot.smartreplenishment.application.representation.ConstraintRepresentation;
import com.jio.iot.smartreplenishment.application.representation.ReplenishRepresentation;
import com.jio.iot.smartreplenishment.application.service.ReplenishmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("store/{storeId}/replenish")
public class ReplenishmentResource {
    @Autowired
    private ReplenishmentService replenishmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<ReplenishRepresentation> replenish(@NotNull @PathVariable("storeId") final String storeId
            , @RequestBody @NotNull @Valid final ConstraintRepresentation representation) {
        return Mono.just(this.replenishmentService.replenish(storeId, representation));
    }
}
