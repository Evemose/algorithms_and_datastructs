package org.example.lab1.algo.externalsort;

import org.example.lab1.util.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.Objects;

public class ExternalSortExecutorTest {
    @ParameterizedTest
    @ValueSource(ints = {100, 1000, 10})
    public void test1(int size) {
        var inputFileName = String.format("testinput%d.tmp.bin", size);
        var outputFileName = String.format("testoutput%d.tmp.bin", size);
        FileUtils.generateBinaryFile(String.format(inputFileName, size), size);
        var params = Map.of(
                "inputFileName", (Object) inputFileName,
                "outputFileName", outputFileName
        );
        var executor = new ExternalFileSortExecutor();
        executor.execute(params);
        var result = verifySorted(outputFileName);
        Assertions.assertTrue(result);
    }

    @AfterAll
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void cleanUp() {
        var file = new File(".");
        for (var f : Objects.requireNonNull(file.listFiles())) {
            if (f.getName().endsWith(".tmp.bin")) {
                f.delete();
            }
        }
    }

    private boolean verifySorted(String fileName) {
        var result = true;
        try (var outputFile = new RandomAccessFile(fileName, "rw")) {
            IntBuffer outputBuffer;
            try (var channel = outputFile.getChannel()) {
                outputBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0,
                        new File(fileName).length()).asIntBuffer();
            }
            for (int i = 0; i < outputBuffer.capacity() - 1; i++) {
                if (outputBuffer.get(i) > outputBuffer.get(i + 1)) {
                    result = false;
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
