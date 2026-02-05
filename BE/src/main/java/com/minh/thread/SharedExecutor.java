package com.minh.thread;

import com.minh.config.SpringConfig;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.minh.config.Config.CORE;

public final class SharedExecutor {

    public static final ExecutorService executor0 =
            new ThreadPoolExecutor(
                    CORE,
                    CORE,
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
