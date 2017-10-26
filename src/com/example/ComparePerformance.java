package com.example;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

import static java.util.stream.Collectors.toList;

/**
 * Class to compare the performance of the implementations
 */
public class ComparePerformance {

    public static void main(String[] args) {
        Random random = new Random();

        List<Long> data = random
                .longs(10_000_000, 1, 100)
                .boxed()
                .collect(toList());

        //testForkJoin(data);
        testSequentially(data);
        //testSequentiallyStream(data);
        //testParallelStream(data);
    }

    private static void testSequentially(List<Long> data) {
        final long start = System.currentTimeMillis();

        long sum = 0;
        for (Long l: data) {
            sum += l;
        }

        System.out.println("Executed sequentially in (ms): " + (System.currentTimeMillis() - start));
    }

    private static void testForkJoin(List<Long> data) {
        final long start = System.currentTimeMillis();

        ForkJoinPool pool = new ForkJoinPool();
        SumTask task = new SumTask(data);
        pool.invoke(task);

        System.out.println("Executed with fork/join in (ms): " + (System.currentTimeMillis() - start));
    }

    private static void testSequentiallyStream(List<Long> data) {
        final long start = System.currentTimeMillis();

        data.stream().reduce(0L, Long::sum);

        System.out.println("Executed with a sequential stream in (ms): " + (System.currentTimeMillis() - start));
    }

    private static void testParallelStream(List<Long> data) {
        final long start = System.currentTimeMillis();

        data.parallelStream().reduce(0L, Long::sum);

        System.out.println("Executed with parallel streams in (ms): " + (System.currentTimeMillis() - start));
    }
}
