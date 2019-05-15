package com.jio.iot.smartad.application.resources;

import com.jio.iot.smartad.application.service.AdService;
import com.jio.iot.smartad.domain.model.Offer;
import com.jio.iot.smartad.domain.model.events.UserEnteredBeaconProximityEvent;
import com.jio.iot.smartad.domain.model.events.UserHomeSkuQuantityEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/offer")
public class AdResource {
    @Autowired
    private AdService adService;

    @PostMapping("/proximity")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Offer> proximityOffers(@RequestBody @NotNull @Valid final UserEnteredBeaconProximityEvent event) {
        return Flux.fromIterable(this.adService.getOffers(event));
    }

    @PostMapping("/home")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Offer> homeOffers(@RequestBody @NotNull @Valid final UserHomeSkuQuantityEvent event) {
        return Flux.fromIterable(this.adService.getOffers(event));
    }
}
