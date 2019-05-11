package com.jio.iot.smartad.application.service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.jio.iot.smartad.domain.model.Offer;
import com.jio.iot.smartad.domain.model.ProximityConfiguration;
import com.jio.iot.smartad.domain.model.ProximityConfigurationRepository;
import com.jio.iot.smartad.domain.model.events.UserEnteredBeaconProximityEvent;
import com.jio.iot.smartad.domain.model.events.UserHomeSkuQuantityEvent;
import com.jio.iot.smartad.domain.model.user.UserSkuView;
import com.jio.iot.smartad.domain.model.user.UserSkuViewRepository;
import org.axonframework.config.ProcessingGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@ProcessingGroup("smartad")
public class AdService {
    @Autowired
    private ProximityConfigurationRepository proximityConfigurationRepository;

    @Autowired
    private UserSkuViewRepository userSkuViewRepository;

    public List<Offer> getOffers(final UserEnteredBeaconProximityEvent event) {
        final var skusAtProximity = this.proximityConfigurationRepository
                .findByBeaconId(event.getStoreId(), event.getBeaconId())
                .stream()
                .map(ProximityConfiguration::getSkuId)
                .collect(Collectors.toSet());
        final var offers = new ArrayList<Offer>();
        skusAtProximity
                .forEach(skuId -> offers.addAll(this.getOffers(event.getUserId(), skuId, event.getStoreId())));
        return offers;
    }

    public List<Offer> getOffers(final UserHomeSkuQuantityEvent event) {
        return this.getOffers(event.getUserId(), event.getSkuId(), null);
    }

    private List<Offer> getOffers(final String userId, final String skuId, final String storeId) {
        return Optional.ofNullable(storeId)
                .map(x -> Optional.ofNullable(this.executeOfferRule(this.getRuleContext(userId, x, skuId)))
                    .map(ImmutableList::of)
                    .orElse(ImmutableList.of()))
                .orElseGet(() -> {
                    final var userStores = this.userSkuViewRepository.findByUserId(userId)
                            .stream()
                            .map(UserSkuView::getStoreId)
                            .collect(Collectors.toList());
                    return ImmutableList.copyOf(userStores.stream()
                            .map(x -> this.executeOfferRule(this.getRuleContext(userId, x, skuId)))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList()));
                });
    }

    private Offer executeOfferRule(final Map<String, Object> ruleContext) {
        final var expression = "#storeId matches 'STORE00001' && #skuId matches 'SKU0000001' && #amountSpent > 500 ? 10 : null ";
        final var evaluationContext = new StandardEvaluationContext();
        evaluationContext.setVariables(ruleContext);
        final var parser = new SpelExpressionParser();
        final var percentage = parser.parseExpression(expression).getValue(evaluationContext, Double.class);
        return Optional.ofNullable(percentage)
                .map(x -> Offer.builder()
                        .storeId((String) ruleContext.get("storeId"))
                        .userId((String) ruleContext.get("userId"))
                        .skuId((String) ruleContext.get("skuId"))
                        .percentOffer(percentage)
                        .build())
                .orElse(null);
    }


    private Map<String, Object> getRuleContext(final String userId, final String storeId, final String skuId) {
        final var userSkuSales =  this.userSkuViewRepository.findByUserId(userId);
        final var userStoreSkuSales = userSkuSales.stream()
                .filter(x -> x.getStoreId().equals(storeId))
                .filter(x -> x.getSkuId().equals(skuId))
                .collect(Collectors.toList());
        final var amountSpent = userStoreSkuSales.stream()
                .map(UserSkuView::getAmount)
                .mapToDouble(BigDecimal::doubleValue)
                .sum();
        final var quantityBought = userStoreSkuSales.stream()
                .map(UserSkuView::getQuantity)
                .mapToInt(x -> x)
                .sum();
        return ImmutableMap.<String, Object>builder()
                .put("userId", userId)
                .put("storeId", storeId)
                .put("skuId", skuId)
                .put("amountSpent", amountSpent)
                .put("quantityBought", quantityBought)
                .build();
    }

}
