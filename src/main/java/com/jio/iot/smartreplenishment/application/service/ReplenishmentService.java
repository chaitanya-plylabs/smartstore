package com.jio.iot.smartreplenishment.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jio.iot.smartreplenishment.application.representation.ConstraintRepresentation;
import com.jio.iot.smartreplenishment.application.representation.ReplenishRepresentation;
import com.jio.iot.smartreplenishment.domain.model.Forecasting;
import com.jio.iot.smartreplenishment.domain.model.sale.SkuSaleViewRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.stream.IntStream;

@SuppressWarnings("unchecked")
@Service
public class ReplenishmentService {
    @Autowired
    private SkuSaleViewRepository skuSaleViewRepository;

    @Autowired
    private Forecasting forecasting;

    @Autowired
    @Qualifier("solverUrl")
    private String solverUrl;

    private static final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public ReplenishRepresentation replenish(final String storeId, final ConstraintRepresentation representation) {
        var forecast = this.forecasting.forecast(storeId, representation.getNumberOfTimePeriods());
        final var httpClient = HttpClient.newHttpClient();
        final var httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(this.solverUrl))
                .timeout(Duration.ofMinutes(5))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writer().writeValueAsString(buildRequest(representation, forecast))))
                .build();
        final var httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        final var responseBody = (Map) mapper.readValue(httpResponse.body(), Map.class);
        final var replenishQuantity = Optional.of((List<List<Integer>>)responseBody.get("replenishmentQuantities"))
                .map(x -> {
                    var result = new HashMap<String, List<Integer>>();
                    var skus = forecast.keySet().toArray(new String[0]);
                    IntStream.iterate(0, i -> i + 1)
                            .limit(skus.length)
                            .forEach(i -> result.put(skus[0], x.get(0)));
                    return result;
                    })
                .get();
        return ReplenishRepresentation.builder()
                .forecast(forecast)
                .replenishQuantity(replenishQuantity)
                .objectiveValue((Double) responseBody.get("objectiveValue"))
                .replenishFlags((List<Integer>) responseBody.get("replenishmentFlags"))
                .build();
    }

    private SolverRequest buildRequest(final ConstraintRepresentation representation, final Map<String, List<Integer>> forecast) {
        final var skus = forecast.keySet();
        final var N = skus.size();
        final var T = Optional.ofNullable(representation.getNumberOfTimePeriods()).orElse(30);
        final var orderingCost = Collections.nCopies(T, representation.getOrderingCost());
        final var itemOrderingCost = Optional.ofNullable(representation.getSkuOrderingCostLookup())
                .map(x -> {
                    List<List<Double>> result = new ArrayList<>();
                    skus.forEach(sku -> result.add(Collections.nCopies(T, x.getOrDefault(sku, 0.0))));
                    return result;
                }).orElse(Collections.nCopies(N, Collections.nCopies(T,0.0)));
        final var itemHoldingCost = Optional.ofNullable(representation.getSkuHoldingCostLookup())
                .map(x -> {
                    List<List<Double>> result = new ArrayList<>();
                    skus.forEach(sku -> result.add(Collections.nCopies(T, x.getOrDefault(sku, 0.0))));
                    return result;
                }).orElse(Collections.nCopies(N, Collections.nCopies(T,0.0)));
        final var itemDemand = Optional.of(forecast)
                .map(x -> {
                    List<List<Integer>> result = new ArrayList<>();
                    skus.forEach(sku -> result.add(x.get(sku)));
                    return result;
                }).get();
        final var itemReplenishmentCapacity = Optional.ofNullable(representation.getSkuReplenishmentCapacity())
                .map(x -> {
                    List<Integer> result = new ArrayList<>();
                    skus.forEach(sku -> result.add(x.getOrDefault(sku, Integer.MAX_VALUE)));
                    return result;
                }).orElse(Collections.nCopies(N, Integer.MAX_VALUE));
        return SolverRequest.builder()
                .numberOfItems(N)
                .numberOfTimePeriods(T)
                .orderingCost(orderingCost)
                .itemOrderingCost(itemOrderingCost)
                .itemHoldingCost(itemHoldingCost)
                .itemDemand(itemDemand)
                .itemReplenishmentCapacity(itemReplenishmentCapacity)
                .totalReplenishmentCapacity(Optional.ofNullable(representation.getTotalReplenishmentCapacity()).orElse(Integer.MAX_VALUE))
                .maxReplenishmentCycles(Optional.ofNullable(representation.getMaxReplenishmentCycles()).orElse(T))
                .build();
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode
    @ToString
    private static final class SolverRequest {
        private Integer numberOfItems;
        private Integer numberOfTimePeriods;
        private List<Double> orderingCost;
        private List<List<Double>> itemOrderingCost;
        private List<List<Double>> itemHoldingCost;
        private List<List<Integer>> itemDemand;
        private List<Integer> itemReplenishmentCapacity;
        private Integer totalReplenishmentCapacity;
        private Integer maxReplenishmentCycles;
    }
}
