package ru.dchertanov.util;

import java.util.*;

public class DataSample {
    private final Map<String, String> value = new HashMap<>();

    public boolean satisfiesRules(Collection<String> rules) {
        Collection<String> newRules = new HashSet<>(rules);
        newRules.remove("START");
        return value.values().containsAll(newRules);
    }

    public void addFeatureValue(String key, String value) {
        this.value.put(key, value);
    }

    public Map<String, String> getValue() {
        return value;
    }

    public boolean isTrueClass() {
        return value.get("CLASS").equals("T");
    }
}
