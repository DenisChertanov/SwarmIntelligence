package ru.dachertanov.model;

import java.util.List;

public class Route implements Comparable<Route> {
    public List<Integer> route;
    public long cost;

    public Route(List<Integer> route, long cost) {
        this.route = route;
        this.cost = cost;
    }

    @Override
    public int compareTo(Route o) {
        return Long.compare(cost, o.cost);
    }
}
