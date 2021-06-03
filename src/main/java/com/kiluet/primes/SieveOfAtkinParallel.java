package com.kiluet.primes;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.io.Serial;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.logging.Logger;

public class SieveOfAtkinParallel implements Callable<Void> {

    private static final Logger logger = Logger.getLogger(SieveOfAtkinParallel.class.getName());

    private static final ForkJoinPool forkJoinPool = new ForkJoinPool();

    private final boolean[] primeArray;

    private final Integer ceiling;

    public SieveOfAtkinParallel(final Integer ceiling) {
        super();
        this.ceiling = ceiling;
        primeArray = new boolean[ceiling + 1];
    }

    @Override
    public Void call() {
        Arrays.fill(primeArray, false);
        primeArray[0] = false;
        primeArray[1] = false;
        primeArray[2] = true;
        primeArray[3] = true;
        forkJoinPool.invoke(new OuterTask());

        Set<Integer> primes = new TreeSet<Integer>();
        for (int i = 0; i <= ceiling; i++) {
            if (primeArray[i]) {
                primes.add(i);
            }
        }

        //logger.info(String.format("ceiling: %s, primes: %s", ceiling, primes.stream().map(String::valueOf).collect(java.util.stream.Collectors.joining(","))));

        return null;
    }


    class OuterTask extends RecursiveTask<Void> {

        @Serial
        private static final long serialVersionUID = -5582589149573253752L;

        @Override
        protected Void compute() {

            List<RecursiveTask<Void>> forks = new LinkedList<RecursiveTask<Void>>();

            for (int x = 1; x * x < ceiling; x++) {
                RecursiveTask<Void> task = new InnerTask(x);
                forks.add(task);
                task.fork();
            }

            for (RecursiveTask<Void> task : forks) {
                task.join();
            }

            for (int n = 5; n * n < ceiling; n++) {
                if (primeArray[n]) {
                    int x = n * n;
                    for (int i = x; i <= ceiling; i += x) {
                        primeArray[i] = false;
                    }
                }
            }

            return null;
        }

    }

    class InnerTask extends RecursiveTask<Void> {

        @Serial
        private static final long serialVersionUID = -7928670821687785005L;

        private final Integer divisor;

        public InnerTask(Integer divisor) {
            super();
            this.divisor = divisor;
        }

        @Override
        protected Void compute() {

            for (int y = 1; y * y < ceiling; y++) {
                int n = (4 * divisor * divisor) + (y * y);
                if (n <= ceiling && (n % 12 == 1 || n % 12 == 5)) {
                    primeArray[n] ^= true;
                }
                n = (3 * divisor * divisor) + (y * y);
                if (n <= ceiling && (n % 12 == 7)) {
                    primeArray[n] ^= true;
                }
                n = (3 * divisor * divisor) - (y * y);
                if (divisor > y && n <= ceiling && (n % 12 == 11)) {
                    primeArray[n] ^= true;
                }
            }

            return null;
        }

    }

    public static void main(String[] args) {
        Instant start = Instant.now();
        SieveOfAtkinParallel runnable = new SieveOfAtkinParallel(100_000_000);
        runnable.call();
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        long millis = duration.toMillis();
        logger.info(DurationFormatUtils.formatDurationHMS(millis));
    }

}
