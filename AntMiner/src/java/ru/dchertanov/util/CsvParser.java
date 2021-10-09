package ru.dchertanov.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvParser {
    private CsvParser() {
    }

    private static List<String> getDatasetFeatures(File file) throws IOException {
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return List.of(reader.readLine().split(","));
        }
    }

    public static List<DataSample> getDataSamples(File file) throws IOException {
        List<String> features = getDatasetFeatures(file);
        List<DataSample> dataSamples = new ArrayList<>();

        if (!file.exists()) {
            return dataSamples;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String currentSample = "";
            reader.readLine();
            while ((currentSample = reader.readLine()) != null) {
                DataSample dataSample = new DataSample();
                String[] splitRow = currentSample.split(",");
                for (int i = 0; i < features.size(); ++i) {
                    dataSample.addFeatureValue(features.get(i), splitRow[i]);
                }
                dataSamples.add(dataSample);
            }
        }

        return dataSamples;
    }
}
