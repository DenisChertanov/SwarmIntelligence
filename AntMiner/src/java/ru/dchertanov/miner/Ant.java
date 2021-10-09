package ru.dchertanov.miner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Ant implements Comparable<Ant> {
    private final List<String> rules = new ArrayList<>();
    private final Set<String> usedFeatures = new HashSet<>();
    private double probability;
    private boolean worked;

    public Ant() {
        rules.add("START");
    }

    public void addRule(String rule, String feature, double probability) {
        rules.add(rule);
        usedFeatures.add(feature);
        this.probability = probability;
    }

    @Override
    public String toString() {
        return "rules: " + rules + "\n" +
                "probability: " + probability + "\n";
    }

    public boolean isFeatureUsed(String feature) {
        return usedFeatures.contains(feature);
    }

    public List<String> getRules() {
        return rules;
    }

    public boolean isWorked() {
        return worked;
    }

    public void finish() {
        worked = true;
    }

    public String getLastRule() {
        return rules.get(rules.size() - 1);
    }

    @Override
    public int compareTo(Ant ant) {
        if (probability == ant.probability) {
            return Integer.compare(rules.size(), ant.rules.size()) * -1;
        }
        return Double.compare(probability, ant.probability);
    }
}
