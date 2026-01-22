package com.minh.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class ExecutorUtils {
    private ExecutorUtils() {
    }

    public static <T> T runOnSharedExecutor(Callable<T> task) {
        try {
            Future<T> future = SharedExecutor.executor0.submit(task);
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void runOnSharedExecutor(Runnable task) {
        try {
            SharedExecutor.executor0.submit(task).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

// ExecutorUtils.runOnSharedExecutor(() ->
