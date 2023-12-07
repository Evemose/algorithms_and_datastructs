package org.example.lab1;

import org.example.lab1.algo.externalsort.ExternalFileSortExecutor;
import org.example.lab1.util.FileUtils;

import java.util.Map;

public class Lab1 {
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
        FileUtils.printFile("output.bin");
    }
}
