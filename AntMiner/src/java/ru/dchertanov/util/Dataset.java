package ru.dchertanov.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Dataset {
    private final List<String> features;
    private final List<DataSample> dataSamples;

    public Dataset(File file) throws IOException {
        features = CsvParser.getDatasetFeatures(file);
        dataSamples = CsvParser.getDataSamples(file, features);
    }
}
