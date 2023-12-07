package org.example.lab1.algo.externalsort;

import org.example.lab1.algo.AlgorithmExecutor;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class ExternalFileSortExecutor implements AlgorithmExecutor {
    private void mergeChunks(String outputFileName, Path inputFilePath, int chunksAmount) {
        try (var outputFile = new RandomAccessFile(outputFileName, "rw")) {
            IntBuffer outputBuffer;
            try (var channel = outputFile.getChannel()) {
                outputBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0,
                        inputFilePath.toFile().length()).asIntBuffer();
            }
            var chunkBuffers = new IntBuffer[chunksAmount];
            for (var i = 0; i < chunksAmount; i++) {
                try (var channel = FileChannel.open(Path.of("chunk" + i + ".tmp.bin"))) {
                    chunkBuffers[i] = channel.map(FileChannel.MapMode.READ_ONLY, 0,
                            channel.size()).asIntBuffer();
                }
            }
            mergeBuffers(chunksAmount, chunkBuffers, outputBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void mergeBuffers(int chunksAmount, IntBuffer[] chunkBuffers, IntBuffer outputBuffer) {
        var peeks = new PriorityQueue<AbstractMap.SimpleImmutableEntry<Integer, Integer>>(chunksAmount,
                Comparator.comparingInt(AbstractMap.SimpleImmutableEntry::getValue));
        for (int j = 0; j < chunksAmount; j++) {
            peeks.add(new AbstractMap.SimpleImmutableEntry<>(j, chunkBuffers[j].get()));
        }
        int last;
        while (!peeks.isEmpty()) {
            var min = peeks.poll();
            last = min.getKey();
            outputBuffer.put(min.getValue());
            if (chunkBuffers[last].hasRemaining()) {
                peeks.add(new AbstractMap.SimpleImmutableEntry<>(last, chunkBuffers[last].get()));
            }
        }
    }

    private void splitInChunks(Path inputFilePath,
                                      int chunksAmount,
                                      ExecutorService executor,
                                      Phaser sortCompletionTracker) {
        IntBuffer mappedBuffer;
        try (var channel = FileChannel.open(inputFilePath)) {
            mappedBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0,
                    inputFilePath.toFile().length()).asIntBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (var i = 0; i < chunksAmount; i++) {
            var chunkSize = (mappedBuffer.capacity() / chunksAmount +
                    (i == chunksAmount - 1 ? mappedBuffer.capacity() % chunksAmount : 0));
            var chunk = mappedBuffer.slice();
            chunk.limit(chunkSize);
            mappedBuffer.position(mappedBuffer.position() + chunkSize);
            executor.submit(new ChunkSortThread(sortCompletionTracker, chunk, chunkSize, i));
        }
        sortCompletionTracker.arriveAndAwaitAdvance();
    }

    @Override
    public void execute(Map<String, Object> params) {
        var start = System.currentTimeMillis();
        var inputFileName = (String) params.get("inputFileName");
        var outputFileName = (String) params.get("outputFileName");
        var inputFilePath = Path.of(inputFileName);
        var chunksAmount = 3;
        var sortCompletionTracker = new Phaser(chunksAmount + 1);
        System.out.println("Sorting...");

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            splitInChunks(inputFilePath, chunksAmount, executor, sortCompletionTracker);
            mergeChunks(outputFileName, inputFilePath, chunksAmount);
            sortCompletionTracker.arriveAndAwaitAdvance();
            var end = System.currentTimeMillis();
            System.out.println("Completed");
            System.out.println("Time: " + (end - start) + " ms");
        }
    }
}
