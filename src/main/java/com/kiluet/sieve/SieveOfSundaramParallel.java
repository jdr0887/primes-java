package com.kiluet.sieve;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class SieveOfSundaramParallel implements Runnable {

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
        System.out.println(String.format("ceiling = %d", ceiling));
        Arrays.fill(primeArray, true);
        long startTime = System.currentTimeMillis();
        forkJoinPool.invoke(new OuterTask());
        long endTime = System.currentTimeMillis();
        System.out.println(String.format("duration to calculate = %d milliseconds", endTime - startTime));
        startTime = System.currentTimeMillis();
        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter(new File("/tmp/sos.parallel.txt")));
            bw.write("2 3 ");
            for (int i = 2; i < primeArray.length / 2; i++) {
                if (primeArray[i]) {
                    bw.write(String.format("%d%n", (2 * i + 1)));
                    bw.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        endTime = System.currentTimeMillis();
        System.out.println(String.format("duration to write to disk = %d milliseconds", endTime - startTime));

    }

    class OuterTask extends RecursiveTask<Void> {

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
        SieveOfSundaramParallel runnable = new SieveOfSundaramParallel(100000000);
        runnable.run();
    }

}
