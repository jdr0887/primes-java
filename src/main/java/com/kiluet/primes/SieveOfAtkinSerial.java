package com.kiluet.primes;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.logging.Logger;

public class SieveOfAtkinSerial implements Runnable {

    private static Logger logger = null;

    static {
        try {
            InputStream stream = SieveOfAtkinSerial.class.getClassLoader().
                    getResourceAsStream("logging.properties");
            java.util.logging.LogManager.getLogManager().readConfiguration(stream);
            logger = Logger.getLogger(SieveOfAtkinSerial.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final Integer ceiling;

    public SieveOfAtkinSerial(final Integer ceiling) {
        super();
        this.ceiling = ceiling;
    }

    @Override
    public void run() {
        boolean[] primeArray = new boolean[ceiling + 1];
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

//        TreeSet<Integer> primes = new TreeSet<>();
//        for (int i = 0; i <= ceiling; i++) {
//            if (primeArray[i]) {
//                primes.add(i);
//            }
//        }

        //logger.info(String.format("ceiling: %s, primes: %s", ceiling, primes.stream().map(String::valueOf).collect(java.util.stream.Collectors.joining(","))));
    }

    public static void main(String[] args) {
        int ceiling = 1_000_000_000;
        if (args.length > 0) {
            ceiling = Integer.parseInt(args[0]);
        }
        logger.info(String.format("ceiling: %s", ceiling));
        Instant start = Instant.now();
        SieveOfAtkinSerial runnable = new SieveOfAtkinSerial(ceiling);
        runnable.run();
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        long millis = duration.toMillis();
        logger.info(String.format("Duration: %s", DurationFormatUtils.formatDurationHMS(millis)));
    }

}
