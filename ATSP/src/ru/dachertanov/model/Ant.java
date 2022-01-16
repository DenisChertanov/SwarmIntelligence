package ru.dachertanov.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ant {
    public boolean[] used;
    public int startNode;
    public List<Integer> route;
    public long routeCost;
    public boolean terminated;

    public Ant(int nNodes, int startNode) {
        this.route = new ArrayList<>();
        this.used = new boolean[nNodes];
        this.startNode = startNode;
        route.add(startNode);
    }

    public void addNextNode(int nextNode, Graph graph) {
        route.add(nextNode);
        used[nextNode] = true;
        routeCost += graph.distances[route.get(route.size() - 2)][nextNode];
    }

    public void clear() {
        Arrays.fill(used, false);
        route.clear();
        route.add(startNode);
        routeCost = 0;
        terminated = false;
    }
}
