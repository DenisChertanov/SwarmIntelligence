package ru.dachertanov.model;

public class Graph {
    public int[][] distances;
    public double[][] pheromones;

    public Graph(int[][] distances, double[][] pheromones) {
        this.distances = distances;
        this.pheromones = pheromones;
    }
}
