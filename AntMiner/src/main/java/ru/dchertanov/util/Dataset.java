package ru.dchertanov.util;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Dataset {
    private final List<DataSample> dataSamples;
    private final Set<String> featuresValues = new HashSet<>();
    private final Map<String, String> valueToFeature = new HashMap<>();

    public Dataset(File file) throws IOException {
        dataSamples = CsvParser.getDataSamples(file);
        initializeFeaturesValues();
    }

    private void initializeFeaturesValues() {
        dataSamples
                .forEach(dataSample -> dataSample.getValue()
                        .forEach((feature, value) -> {
                            featuresValues.add(value);
                            valueToFeature.put(value, feature);
                        }));
    }

    public String getFeatureFromValue(String value) {
        return valueToFeature.get(value);
    }

    public double getDivisionValue(Collection<String> rules) {
        List<DataSample> satisfiesSamples = dataSamples
                .stream()
                .filter(dataSample -> dataSample.satisfiesRules(rules))
                .collect(Collectors.toList());

        return satisfiesSamples
                .stream()
                .filter(DataSample::isTrueClass)
                .count() / (double) satisfiesSamples.size();
    }

    public Set<String> getFeaturesValues() {
        return featuresValues;
    }
}
