package org.example;

import org.example.algo.externalsort.ExternalFileSortExecutor;
import org.example.util.FileUtils;

import java.util.Map;

public class App {
    public static void main(String[] args) {
        System.out.println("Generating file...");
        FileUtils.generateBinaryFile("input.bin", 100000000);
        System.out.println("File generated");
        var params = Map.of(
                "inputFileName", (Object) "input.bin",
                "outputFileName", "output.bin"
        );
        var executor = new ExternalFileSortExecutor();
        executor.execute(params);
        // FileUtils.printFile("output.bin");
    }
}
