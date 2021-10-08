package ru.dchertanov;

import ru.dchertanov.util.Dataset;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Dataset dataset = new Dataset(new File("adult+stretch.csv"));



        System.out.println("good");
    }
}
