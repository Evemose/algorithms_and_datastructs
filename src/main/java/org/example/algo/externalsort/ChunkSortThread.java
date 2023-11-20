package org.example.algo.externalsort;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.Phaser;

public class ChunkSortThread implements Runnable {
    private final Phaser sortCompletionTracker;

    private final IntBuffer inputBuffer;

    private final int chunkSize;

    private final int chunkNumber;

    public ChunkSortThread(Phaser sortCompletionTracker,
                           IntBuffer inputBuffer,
                           int chunkSize,
                           int chunkNumber) {
        this.sortCompletionTracker = sortCompletionTracker;
        this.inputBuffer = inputBuffer;
        this.chunkSize = chunkSize;
        this.chunkNumber = chunkNumber;
    }

    private static void convertToBytes(int[] chunk, byte[] bytes) {
        for (int i = 0; i < chunk.length; i++) {
            bytes[i * 4] = (byte) (chunk[i] >> 24);
            bytes[i * 4 + 1] = (byte) (chunk[i] >> 16);
            bytes[i * 4 + 2] = (byte) (chunk[i] >> 8);
            bytes[i * 4 + 3] = (byte) chunk[i];
        }
    }

    @Override
    public void run() {
        String fileName = "chunk" + chunkNumber + ".tmp.bin";
        try {
            var chunk = readChunk();

            Arrays.sort(chunk);

            var bytes = new byte[chunk.length * 4];
            convertToBytes(chunk, bytes);

            var file = new File(fileName);
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
            try (var outputStream = new FileOutputStream(file)) {
                outputStream.write(bytes, 0, bytes.length);
            }
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
        sortCompletionTracker.arriveAndAwaitAdvance();
        sortCompletionTracker.arriveAndAwaitAdvance();
        try {
            Files.delete(Path.of(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int[] readChunk() throws InterruptedException {
        var chunk = new int[chunkSize];
        inputBuffer.get(chunk);
        return chunk;
    }
}
