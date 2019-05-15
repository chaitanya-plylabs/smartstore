package com.jio.iot.smartad.application;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "ad")
public class AdRulesConfig {
    private List<String> rules;

    AdRulesConfig() {
        this.rules = new ArrayList<>();
    }

    public List<String> getRules() {
        return this.rules;
    }
}
