package org.example.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.Random;

public class FileUtils {
    public static void generateBinaryFile(String fileName, int size) {
        var file = new File(fileName);
        try (var outputStream = new FileOutputStream(file)) {
            var random = new Random();
            for (var i = 0; i < size; i++) {
                var value = random.nextInt(-100, 101);
                var bytes = new byte[]{
                        (byte) (value >>> 24),
                        (byte) (value >>> 16),
                        (byte) (value >>> 8),
                        (byte) value};
                outputStream.write(bytes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("File " + fileName + " generated");
    }

    public static void printFile(String name) {
        Path file = Path.of(name);
        try (var channel = FileChannel.open(file)) {
            var buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.toFile().length()).asIntBuffer();
            while (buffer.hasRemaining()) {
                System.out.print(buffer.get() + " ");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
