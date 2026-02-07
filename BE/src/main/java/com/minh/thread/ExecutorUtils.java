package com.minh.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Supplier;

public class ExecutorUtils {
    private ExecutorUtils() {
    }

    public static <T> CompletableFuture<T> runOnSharedExecutor(Supplier<T> task) {
        try {
            return CompletableFuture.supplyAsync(task, SharedExecutor.executor0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

// ExecutorUtils.runOnSharedExecutor(() ->
