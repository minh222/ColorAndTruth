package com.minh.thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class SharedExecutor {

    private static final int CORES = Runtime.getRuntime().availableProcessors();

    public static final ExecutorService executor0 =
            new ThreadPoolExecutor(
                    CORES,
                    CORES,
                    0L,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(0),
                    new ThreadFactory() {
                        private final AtomicInteger idx = new AtomicInteger(1);

                        @Override
                        public Thread newThread(Runnable r) {
                            Thread t = new Thread(r);
                            t.setName("executor0-" + idx.getAndIncrement());
                            t.setDaemon(false);
                            return t;
                        }
                    },
                    new ThreadPoolExecutor.AbortPolicy()
            );
    private SharedExecutor() {
    }


}
