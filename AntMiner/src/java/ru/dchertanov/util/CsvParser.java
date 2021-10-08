package ru.dchertanov.util;

import java.io.*;
import java.util.*;

public class CsvParser {
    private CsvParser() {
    }

    public static List<String> getDatasetFeatures(File file) throws IOException {
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return List.of(reader.readLine().split(","));
        }
    }

    public static List<DataSample> getDataSamples(File file, List<String> features) throws IOException {
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
