package com.jio.iot.smartreplenishment.domain.model;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.IntStream;

@Service("RandomForecastingForPrototype")
public class RandomForecastingForPrototype implements Forecasting {
    @Override
    public Map<String, List<Integer>> forecast(final String storeId, final Integer noOfDays) {
        final Map<String, List<Integer>> result = new HashMap<>();
        final var random = new Random();
        IntStream.iterate(1, i -> i + 1)
                .limit(10)
                .forEach(i -> {
                    final var pred = new ArrayList<Integer>();
                    random.ints(1, 100)
                            .limit(noOfDays)
                            .forEach(pred::add);
                    result.put( i != 10 ? "SKU000000" + i : "SKU0000010", pred);
                });
        return result;
    }
}
