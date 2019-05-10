package com.jio.iot.smartreplenishment.domain.model;

import java.util.List;
import java.util.Map;

public interface Forecasting {
    Map<String, List<Integer>> forecast(String storeId, Integer noOfDays);
}
