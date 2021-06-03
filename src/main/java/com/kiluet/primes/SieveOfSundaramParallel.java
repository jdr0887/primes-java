package com.kiluet.sieve;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.logging.Logger;

public class SieveOfSundaramParallel implements Runnable {

    private static final Logger logger = Logger.getLogger(SieveOfEratosthenesSerial.class.getName());

    private static final ForkJoinPool forkJoinPool = new ForkJoinPool();

    private final boolean[] primeArray;

    private final Integer ceiling;

    public SieveOfSundaramParallel(final Integer ceiling) {
        super();
        this.ceiling = ceiling;
        primeArray = new boolean[ceiling];
    }

    @Override
    public void run() {
        Arrays.fill(primeArray, true);
        forkJoinPool.invoke(new OuterTask());

        Set<Integer> primes = new TreeSet<Integer>();
        for (int i = 0; i <= ceiling; i++) {
            if (primeArray[i]) {
                primes.add(i);
            }
        }

        //logger.info(String.format("ceiling: %s, primes: %s", ceiling, primes.stream().map(String::valueOf).collect(Collectors.joining(","))));
    }

    class OuterTask extends RecursiveTask<Void> {

        @Serial
        private static final long serialVersionUID = -5582589149573253752L;

        @Override
        protected Void compute() {

            List<RecursiveTask<Void>> forks = new LinkedList<RecursiveTask<Void>>();
            int n = ceiling / 2;
            for (int i = 1; i < n; i++) {
                RecursiveTask<Void> task = new InnerTask(i);
                forks.add(task);
                task.fork();
            }

            for (RecursiveTask<Void> task : forks) {
                task.join();
            }

            return null;
        }

    }

    class InnerTask extends RecursiveTask<Void> {

        @Serial
        private static final long serialVersionUID = -7928670821687785005L;

        private Integer divisor;

        public InnerTask(Integer divisor) {
            super();
            this.divisor = divisor;
        }

        @Override
        protected Void compute() {
            int n = ceiling / 2;

            for (int j = divisor; j <= (n - divisor) / (2 * divisor + 1); j++) {
                primeArray[divisor + j + 2 * divisor * j] = false;
            }

            return null;
        }

    }

    public static void main(String[] args) {
        Instant start = Instant.now();
        SieveOfSundaramParallel runnable = new SieveOfSundaramParallel(100_000_000);
        runnable.run();
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        long millis = duration.toMillis();
        logger.info(DurationFormatUtils.formatDurationHMS(millis));
    }

}
