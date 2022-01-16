package ru.dachertanov;

import ru.dachertanov.model.Route;
import ru.dachertanov.parser.Parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        for(var dsName : List.of("br17", "ry48p", "ft53", "ft70")) {
            int[][] distances = new Parser().parse(new FileInputStream(dsName));
            Solver model = new Solver(100, 100, distances);

            Route route = model.solve();
            System.out.println("Dataset name: " + dsName);
            System.out.println("Number of nodes: " + distances.length);
            System.out.println("Result cost: " + route.cost);
            System.out.println();
        }
    }
}
