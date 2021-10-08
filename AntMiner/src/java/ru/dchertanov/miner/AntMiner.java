package ru.dchertanov.miner;

import ru.dchertanov.util.Dataset;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class AntMiner {
    private final Dataset dataset;
    private final Map<String, Map<String, Double>> pheromones = new HashMap<>();
    private final List<Ant> ants = new ArrayList<>();
    private final Random random = new Random();

    public AntMiner(File file) throws IOException {
        dataset = new Dataset(file);
        initializePheromones();
    }

    private void initializePheromones() {
        Set<String> allFeaturesValues = new HashSet<>(dataset.getFeaturesValues());
        allFeaturesValues.add("START");
        allFeaturesValues
                .forEach(processedFeatureValue -> {
                    Map<String, Double> pheromonesForFeatures = new HashMap<>();
                    dataset.getFeaturesValues()
                            .forEach(featureValue -> {
                                if (!dataset.getFeatureFromValue(featureValue).equals("CLASS") &&
                                        (processedFeatureValue.equals("START") ||
                                                !dataset.getFeatureFromValue(processedFeatureValue)
                                                        .equals(dataset.getFeatureFromValue(featureValue)))) {
                                    pheromonesForFeatures.put(featureValue, 1.0);
                                }
                            });

                    pheromones.put(processedFeatureValue, pheromonesForFeatures);
                });
    }

    public void run() {
        int NUMBER_OF_ANTS = 50;
        for (int i = 0; i < NUMBER_OF_ANTS; ++i) {
            Ant ant = new Ant();
            while (!ant.isWorked()) {
                String rule = getNextRule(ant);
                List<String> newRules = new ArrayList<>(ant.getRules());
                newRules.add(rule);

                if (newRules.size() == 2 || dataset.getDivisionValue(newRules) > dataset.getDivisionValue(ant.getRules())) {
                    ant.addRule(rule, dataset.getFeatureFromValue(rule), dataset.getDivisionValue(newRules));
                } else {
                    ant.finish();
                }
            }
            ants.add(ant);
        }
    }

    private String getNextRule(Ant ant) {
        Map<String, Double> edgesMetricValue = new HashMap<>();
        Set<String> newRules = new HashSet<>(ant.getRules());
        dataset.getFeaturesValues()
                .stream()
                .filter(featureValue -> !dataset.getFeatureFromValue(featureValue).equals("CLASS") &&
                        !ant.isFeatureUsed(dataset.getFeatureFromValue(featureValue)))
                .forEach(featureValue -> {
                    newRules.add(featureValue);
                    double metricValue = pheromones.get(ant.getLastRule()).get(featureValue) *
                            (dataset.getDivisionValue(newRules) / dataset.getDivisionValue(ant.getRules()));
                    edgesMetricValue.put(featureValue, metricValue);
                    newRules.remove(featureValue);
                });
        double metricValueSum = edgesMetricValue.values().stream().mapToDouble(x -> x).sum();

        double randomValue = random.nextDouble();
        double currentProbability = 1;
        String result = null;
        for (var metricValue : edgesMetricValue.entrySet()) {
            currentProbability -= metricValue.getValue() / metricValueSum;
            if (currentProbability < randomValue) {
                result = metricValue.getKey();
                break;
            }
        }
        return result;
    }

    public void printAnts() {
        Collections.sort(ants);
        ants.forEach(System.out::println);
    }
}
