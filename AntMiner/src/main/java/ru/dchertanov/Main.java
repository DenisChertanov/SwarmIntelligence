package ru.dchertanov;

import ru.dchertanov.miner.AntMiner;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        AntMiner antMiner = new AntMiner(new File("adult+stretch.csv"));
        antMiner.run();
        antMiner.printAnts();
    }
}
