package com.kiluet.sieve;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SieveOfSundaramSerial implements Runnable {

    private static Logger logger = null;

    static {
        try {
            InputStream stream = SieveOfSundaramSerial.class.getClassLoader().
                    getResourceAsStream("logging.properties");
            java.util.logging.LogManager.getLogManager().readConfiguration(stream);
            logger = Logger.getLogger(SieveOfSundaramSerial.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final Integer ceiling;

    public SieveOfSundaramSerial(final Integer ceiling) {
        super();
        this.ceiling = ceiling;
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
        int ceiling = 1_000_000_000;
        if (args.length > 0) {
            ceiling = Integer.parseInt(args[0]);
        }
        logger.info(String.format("ceiling: %s", ceiling));
        Instant start = Instant.now();
        SieveOfSundaramSerial runnable = new SieveOfSundaramSerial(ceiling);
        runnable.run();
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        long millis = duration.toMillis();
        logger.info(String.format("Duration: %s", DurationFormatUtils.formatDurationHMS(millis)));
    }

}
