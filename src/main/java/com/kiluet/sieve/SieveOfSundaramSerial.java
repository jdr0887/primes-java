package com.kiluet.sieve;

import org.apache.commons.lang.time.DurationFormatUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SieveOfSundaramSerial implements Runnable {

    private static final Logger logger = Logger.getLogger(SieveOfSundaramSerial.class.getName());

    private final Integer ceiling;

    public SieveOfSundaramSerial(final Integer ceiling) {
        super();
        this.ceiling = ceiling;
        Logger.getLogger("").setLevel(Level.INFO);
    }

    @Override
    public void run() {

        boolean[] primeArray = new boolean[ceiling + 1];
        Arrays.fill(primeArray, false);

        int n = (ceiling - 1) / 2;

        for (int x = 1; x <= n; x++) {
            for (int y = x; (x + y + 2 * x * y) <= n; y++) {
                int idx = x + y + 2 * x * y;
                if (Integer.signum(idx) > 0) {
                    primeArray[idx] = true;
                }
            }
        }

//        TreeSet<Integer> primes = new TreeSet<>();
//        for (int i = 1; i <= n; i++) {
//            if (!primeArray[i]) {
//                primes.add(2 * i + 1);
//            }
//        }

//        logger.info(String.format("ceiling: %s, primes: %s", ceiling, primes.stream().map(String::valueOf).collect(java.util.stream.Collectors.joining(","))));

    }

    public static void main(String[] args) {
        Instant start = Instant.now();
        SieveOfSundaramSerial runnable = new SieveOfSundaramSerial(100_000_000);
        runnable.run();
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        long millis = duration.toMillis();
        logger.info(DurationFormatUtils.formatDurationHMS(millis));
    }

}
