package ru.dachertanov;

import ru.dachertanov.model.Ant;
import ru.dachertanov.model.Graph;
import ru.dachertanov.model.Route;

import java.util.*;
import java.util.stream.IntStream;

public class Solver {
    private static final Random random = new Random();

    private final double q0 = 0.9;
    private final double beta = 2;
    private final double rho = 0.1;
    private final double alpha = 0.1;
    private int numberOfAnts = 10;
    private final double tau0 = 1e-3;
    private int numberOfIterations = 1;
    private Graph graph;
    private List<Ant> ants = new ArrayList<>();

    public Solver(int numberOfAnts, int numberOfIterations, int[][] distances) {
        this.numberOfAnts = numberOfAnts;
        this.numberOfIterations = numberOfIterations;

        graph = new Graph(distances, new double[distances.length][distances.length]);
        for (double[] row : graph.pheromones) {
            Arrays.fill(row, tau0);
        }
        for (int i = 0; i < numberOfAnts; ++i) {
            ants.add(new Ant(distances.length, random.nextInt(distances.length)));
        }
    }

    /**
     * @return Лучший найденный Гамильтонов цикл (по графу)
     */
    public Route solve() {
        Route bestRoute = new Route(null, Long.MAX_VALUE);

        for (int iteration = 0; iteration < numberOfIterations; ++iteration) {
            clearAnts();
            for (int step = 0; step < graph.distances.length - 1; ++step) {
                for (var ant : ants) {
                    if (ant.terminated) { // Пропускаем муравьев, которые уткнулись в тупик
                        continue;
                    }

                    int nextNode = getNextNode(ant);
                    if (nextNode == -1) {
                        ant.terminated = true;
                        continue;
                    }

                    ant.addNextNode(nextNode, graph);
                    updateEdgePheromone(ant.route.get(ant.route.size() - 2), nextNode); // Обновляем феромон на ребре

                    if (step == graph.distances.length - 2 && bestRoute.cost > ant.routeCost) { // Обновляем лучший маршрут
                        bestRoute.route = ant.route;
                        bestRoute.cost = ant.routeCost;
                    }
                }
            }

            updateRoutePheromone(bestRoute.route, bestRoute.cost); // Обновляем феромоны на ребрах лучшего маршрута (среди всех муравьев)
        }

        return bestRoute;
    }

    private int getNextNode(Ant ant) {
        if (random.nextDouble() <= q0) {
            return getNextNodeGreedy(ant);
        } else {
            return getNextNodeRandomly(ant);
        }
    }

    /**
     * Случайным образом выбирает следующую вершину муровья (в соответствие с распределение вероятностей перехода в доступные вершины)
     */
    private int getNextNodeRandomly(Ant ant) {
        double[] probabilities = getEdgesProbabilities(ant);
        double probabilitiesSum = Arrays.stream(probabilities)
                .filter(x -> x != -1)
                .sum();

        // Выбираем следующее ребро в соответствие с распределением вероятностей
        double randomValue = random.nextDouble();
        double currentProbability = 1;
        int resultNode = -1;
        for (int nextNode = 0; nextNode < graph.distances.length; ++nextNode) {
            if (probabilities[nextNode] == -1) {
                continue;
            }

            resultNode = nextNode;
            currentProbability -= probabilities[nextNode] / probabilitiesSum;
            if (currentProbability < randomValue) {
                return nextNode;
            }
        }
        return resultNode;
    }

    /**
     * Выбирает лучшую вершину для перехода муравья (жадник)
     */
    private int getNextNodeGreedy(Ant ant) {
        double[] probabilities = getEdgesProbabilities(ant);
        int nextNode = IntStream.range(0, graph.distances.length)
                .boxed()
                .max(Comparator.comparingDouble(x -> probabilities[x]))
                .orElse(-1);

        if (nextNode == -1 || probabilities[nextNode] == -1) {
            return -1;
        }
        return nextNode;
    }

    private double[] getEdgesProbabilities(Ant ant) {
        double[] probabilities = new double[graph.distances.length];
        Arrays.fill(probabilities, -1);
        int currentNode = ant.route.get(ant.route.size() - 1);

        for (int nextNode = 0; nextNode < graph.distances.length; ++nextNode) {
            if (graph.distances[currentNode][nextNode] == Integer.MAX_VALUE ||
                    ant.used[nextNode]) {
                continue;
            }

            probabilities[nextNode] = edgeProbability(currentNode, nextNode);
        }

        return probabilities;
    }

    /**
     * Вычисляет выгодность перехода от одной вершины к другой
     */
    private double edgeProbability(int startNode, int lastNode) {
        return Math.pow(graph.distances[startNode][lastNode], -beta) * graph.pheromones[startNode][lastNode];
    }

    private void updateEdgePheromone(int startNode, int endNode) {
        graph.pheromones[startNode][endNode] = (1 - rho) * graph.pheromones[startNode][endNode] + rho * tau0;
    }

    private void updateRoutePheromone(List<Integer> route, long routeCost) {
        for (int i = 1; i < route.size(); ++i) {
            graph.pheromones[route.get(i - 1)][route.get(i)] =
                    (1d - alpha) * graph.pheromones[route.get(i - 1)][route.get(i)] + alpha / routeCost;
        }
    }

    private void clearAnts() {
        for (var ant : ants) {
            ant.clear();
        }
    }
}
