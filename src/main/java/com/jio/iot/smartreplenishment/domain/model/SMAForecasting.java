package com.jio.iot.smartreplenishment.domain.model;

import com.jio.iot.smartreplenishment.domain.model.sale.SkuSaleView;
import com.jio.iot.smartreplenishment.domain.model.sale.SkuSaleViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service("SMAForecasting")
public class SMAForecasting implements Forecasting {
    @Autowired
    private SkuSaleViewRepository skuSaleViewRepository;

    private static final int SMA_PERIOD = 5;

    @Override
    public Map<String, List<Integer>> forecast(final String storeId, final Integer noOfDays) {
        final var skuSales = this.skuSaleViewRepository.findByStoreId(storeId);
        skuSales.sort(Comparator.comparing(SkuSaleView::getDate));
        final var skuIds = skuSales.stream().map(SkuSaleView::getSkuId).collect(Collectors.toSet());
        final var result = new HashMap<String, List<Integer>>();
        skuIds.forEach(skuId -> {
            final var dataset = skuSales.stream()
                    .filter(x -> x.getSkuId().equals(skuId))
                    .map(SkuSaleView::getQuantity)
                    .collect(Collectors.toList());
            final var predicted = this.sma(dataset, noOfDays);
            result.put(skuId, predicted);
        });
        return result;
    }


    private List<Integer> sma(final List<Integer> dataset, final Integer predictionPeriod) {
        final var result = new ArrayList<Integer>();
        final var window = new LinkedList<Integer>();
        dataset.stream().sorted(Collections.reverseOrder()).limit(SMAForecasting.SMA_PERIOD).forEach(window::addFirst);
        IntStream.iterate(0, i -> i + 1)
                .limit(predictionPeriod)
                .forEach(i -> {
                    final var sum = window.stream().mapToInt(Integer::intValue).sum();
                    final var nextVal =  sum/ SMAForecasting.SMA_PERIOD;
                    result.add(nextVal);
                    window.removeFirst();
                    window.addLast(nextVal);

                });
        return result;
    }
}
