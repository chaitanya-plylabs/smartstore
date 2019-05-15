package com.jio.iot.smartad.application.service;

import com.google.common.collect.ImmutableMap;
import com.jio.iot.smartad.application.AdRulesConfig;
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

    @Autowired
    private AdRulesConfig adRulesConfig;

    public List<Offer> getOffers(final UserEnteredBeaconProximityEvent event) {
        final var skusAtProximity = this.proximityConfigurationRepository
                .findByBeaconId(event.getStoreId(), event.getBeaconId())
                .stream()
                .map(ProximityConfiguration::getSkuId)
                .collect(Collectors.toSet());
        final var offers = new ArrayList<Offer>();
        skusAtProximity
                .forEach(skuId -> {
                    final var ruleContext = new HashMap<>(this.getBaseRuleContext(event.getUserId(), event.getStoreId(), skuId));
                    ruleContext.put("event", "UserEnteredBeaconProximityEvent");
                    Optional.ofNullable(this.executeOfferRule(ruleContext)).ifPresent(offers::add);
                });

        return offers;
    }

    public List<Offer> getOffers(final UserHomeSkuQuantityEvent event) {
        final var offers = new ArrayList<Offer>();
        final var userStores = this.userSkuViewRepository.findByUserId(event.getUserId())
                .stream()
                .map(UserSkuView::getStoreId)
                .collect(Collectors.toList());
        userStores
                .forEach(storeId -> {
                    final var ruleContext = new HashMap<>(this.getBaseRuleContext(event.getUserId(), storeId, event.getSkuId()));
                    ruleContext.put("deviceId", event.getDeviceId());
                    ruleContext.put("homeSkuQuantity", event.getQuantity());
                    ruleContext.put("event", "UserHomeSkuQuantityEvent");
                    Optional.ofNullable(this.executeOfferRule(ruleContext)).ifPresent(offers::add);
                });
        return offers;
    }


    private Offer executeOfferRule(final Map<String, Object> ruleContext) {
        for(int i = 0; i < adRulesConfig.getRules().size(); i++) {
            final var expression = adRulesConfig.getRules().get(i);
            final var evaluationContext = new StandardEvaluationContext();
            evaluationContext.setVariables(ruleContext);
            final var parser = new SpelExpressionParser();
            final var percentage = parser.parseExpression(expression).getValue(evaluationContext, Double.class);
            if(percentage != null) {
                return Offer.builder()
                        .storeId((String) ruleContext.get("storeId"))
                        .userId((String) ruleContext.get("userId"))
                        .skuId((String) ruleContext.get("skuId"))
                        .percentOffer(percentage)
                        .build();
            }
        }
        return null;
    }


    private Map<String, Object> getBaseRuleContext(final String userId, final String storeId, final String skuId) {
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
