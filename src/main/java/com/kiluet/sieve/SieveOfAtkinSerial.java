package com.kiluet.sieve;

import org.apache.commons.lang.time.DurationFormatUtils;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SieveOfAtkinSerial implements Runnable {

    private static final Logger logger = Logger.getLogger(SieveOfAtkinSerial.class.getName());

    private final boolean[] primeArray;

    private final Integer ceiling;

    public SieveOfAtkinSerial(final Integer ceiling) {
        super();
        this.ceiling = ceiling;
        primeArray = new boolean[ceiling + 1];
        Logger.getLogger("").setLevel(Level.INFO);
    }

    @Override
    public void run() {
        Arrays.fill(primeArray, false);

        primeArray[0] = false;
        primeArray[1] = false;
        primeArray[2] = true;
        primeArray[3] = true;

        for (int x = 1; x * x < ceiling; x++) {
            for (int y = 1; y * y < ceiling; y++) {

                int n = (4 * x * x) + (y * y);
                if (n <= ceiling && (n % 12 == 1 || n % 12 == 5)) {
                    primeArray[n] ^= true;
                }

                n = (3 * x * x) + (y * y);
                if (n <= ceiling && (n % 12 == 7)) {
                    primeArray[n] ^= true;
                }
                n = (3 * x * x) - (y * y);
                if (x > y && n <= ceiling && (n % 12 == 11)) {
                    primeArray[n] ^= true;
                }
            }
        }

        for (int n = 5; n * n < ceiling; n++) {
            if (primeArray[n]) {
                int x = n * n;
                for (int i = x; i <= ceiling; i += x) {
                    primeArray[i] = false;
                }
            }
        }

        TreeSet<Integer> primes = new TreeSet<>();
        for (int i = 0; i <= ceiling; i++) {
            if (primeArray[i]) {
                primes.add(i);
            }
        }

        logger.fine(String.format("ceiling: %s, primes: %s", ceiling, primes.stream().map(String::valueOf).collect(Collectors.joining(","))));

    }

    public static void main(String[] args) {
        Instant start = Instant.now();
        SieveOfAtkinSerial runnable = new SieveOfAtkinSerial(100_000_000);
        runnable.run();
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        long millis = duration.toMillis();
        logger.info(DurationFormatUtils.formatDurationHMS(millis));
    }

}
