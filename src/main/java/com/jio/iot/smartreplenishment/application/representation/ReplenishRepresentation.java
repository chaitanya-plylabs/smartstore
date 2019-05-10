package com.jio.iot.smartreplenishment.application.representation;

import lombok.*;

import java.util.List;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ReplenishRepresentation {
    private Map<String, List<Integer>> forecast;
    private Double objectiveValue;
    private List<Integer> replenishFlags;
    private Map<String, List<Integer>> replenishQuantity;
}
