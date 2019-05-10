package com.jio.iot.smartreplenishment.application.representation;

import lombok.*;

import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ConstraintRepresentation {
    private Integer numberOfTimePeriods;
    private Double orderingCost;
    private Integer totalReplenishmentCapacity;
    private Integer maxReplenishmentCycles;
    private Map<String, Double> skuOrderingCostLookup;
    private Map<String, Double> skuHoldingCostLookup;
    private Map<String, Integer> skuReplenishmentCapacity;
}
