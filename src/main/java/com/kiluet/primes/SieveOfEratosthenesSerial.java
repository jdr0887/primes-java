package com.kiluet.primes;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.logging.Logger;

public class SieveOfEratosthenesSerial implements Runnable {

    private static Logger logger = null;

    static {
        try {
            InputStream stream = SieveOfEratosthenesSerial.class.getClassLoader().
                    getResourceAsStream("logging.properties");
            java.util.logging.LogManager.getLogManager().readConfiguration(stream);
            logger = Logger.getLogger(SieveOfEratosthenesSerial.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final Integer ceiling;

    public SieveOfEratosthenesSerial(final Integer ceiling) {
        super();
        this.ceiling = ceiling;
    }

    @Override
    public void run() {
        boolean[] primeArray = new boolean[ceiling + 1];
        Arrays.fill(primeArray, true);

        for (int i = 2; i * i <= ceiling; i++) {
            if (primeArray[i]) {
                for (int j = i; i * j <= ceiling; j++) {
                    primeArray[i * j] = false;
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
        SieveOfEratosthenesSerial runnable = new SieveOfEratosthenesSerial(ceiling);
        runnable.run();
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        long millis = duration.toMillis();
        logger.info(String.format("Duration: %s", DurationFormatUtils.formatDurationHMS(millis)));
    }

}
