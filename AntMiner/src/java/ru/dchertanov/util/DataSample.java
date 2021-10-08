package ru.dchertanov.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSample {
    private final Map<String, String> value = new HashMap<>();

    public void addFeatureValue(String key, String value) {
        this.value.put(key, value);
    }
}
