package com.kiluet.sieve;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.logging.Logger;

public class SieveOfEratosthenesSerial implements Runnable {

    private static final Logger logger = Logger.getLogger(SieveOfEratosthenesSerial.class.getName());

    private final boolean[] primeArray;

    private final Integer ceiling;

    public SieveOfEratosthenesSerial(final Integer ceiling) {
        super();
        this.ceiling = ceiling;
        primeArray = new boolean[ceiling + 1];
    }

    @Override
    public void run() {
        Arrays.fill(primeArray, true);
        for (int i = 2; i * i <= ceiling; i++) {
            if (primeArray[i]) {
                for (int j = i; i * j <= ceiling; j++) {
                    primeArray[i * j] = false;
                }
            }
        }

        TreeSet<Integer> primes = new TreeSet<>();
        for (int i = 0; i <= ceiling; i++) {
            if (primeArray[i]) {
                primes.add(i);
            }
        }

//        logger.info(String.format("ceiling: %s, primes: %s", ceiling, primes.stream().map(String::valueOf).collect(Collectors.joining(","))));

    }

    public static void main(String[] args) {
        Instant start = Instant.now();
        SieveOfEratosthenesSerial runnable = new SieveOfEratosthenesSerial(1_000_000_000);
        runnable.run();
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        long seconds = duration.getSeconds();
        logger.info(String.format("duration: %d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60));
    }

}
