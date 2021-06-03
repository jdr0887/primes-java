package com.kiluet.sieve;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SieveOfSundaramSerial implements Runnable {

    private static final Logger logger = Logger.getLogger(SieveOfSundaramSerial.class.getName());

    private final boolean[] primeArray;

    private final Integer ceiling;

    public SieveOfSundaramSerial(final Integer ceiling) {
        super();
        this.ceiling = ceiling;
        this.primeArray = new boolean[ceiling];
    }

    @Override
    public void run() {
        Arrays.fill(primeArray, true);
        int n = ceiling / 2;
        for (int x = 1; x < n; x++) {
            for (int y = x; y <= (n - x) / (2 * x + 1); y++) {
                primeArray[x + y + 2 * x * y] = false;
            }
        }

        TreeSet<Integer> primes = new TreeSet<>();
        for (int i = 0; i < ceiling; i++) {
            if (primeArray[i]) {
                primes.add(i);
            }
        }

       logger.info(String.format("ceiling: %s, primes: %s", ceiling, primes.stream().map(String::valueOf).collect(Collectors.joining(","))));

    }

    public static void main(String[] args) {
        Instant start = Instant.now();
        SieveOfSundaramSerial runnable = new SieveOfSundaramSerial(10);
        runnable.run();
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        long seconds = duration.getSeconds();
        logger.info(String.format("duration: %d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60));
    }

}
