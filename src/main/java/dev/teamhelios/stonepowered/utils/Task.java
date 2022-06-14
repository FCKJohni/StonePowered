package dev.teamhelios.stonepowered.utils;

import java.util.concurrent.*;

public class Task<V> extends CompletableFuture<V> {
    private static final ExecutorService SERVICE = Executors.newCachedThreadPool();


    public static <V> Task<V> supply(Runnable runnable) {
        return supply(() -> {
            runnable.run();
            return null;
        });
    }


    public static <V> Task<V> supply(ThrowableSupplier<V, Throwable> supplier) {
        var task = new Task<V>();
        SERVICE.execute(() -> {
            try {
                task.complete(supplier.get());
            } catch (Throwable throwable) {
                task.completeExceptionally(throwable);
            }
        });
        return task;
    }


    public static <V> Task<V> wrapFuture(CompletableFuture<V> future) {
        var task = new Task<V>();
        future.whenComplete((result, exception) -> {
            // uni push either the exception or the result, the exception is unwrapped already
            if (exception == null) {
                task.complete(result);
            } else {
                task.completeExceptionally(exception);
            }
        });
        return task;
    }


    @SuppressWarnings("unchecked") // it's fine
    public static <V> Task<V> completedTask(Object result) {
        var future = new Task<V>();
        // complete exceptionally if an exception was given
        if (result instanceof Throwable throwable) {
            future.completeExceptionally(throwable);
        } else {
            future.complete((V) result);
        }
        // instantly completed when returning
        return future;
    }

    public V getOrNull() {
        return this.getDef(null);
    }

    public V getDef(V def) {
        try {
            return this.join();
        } catch (CancellationException | CompletionException exception) {
            return def;
        }
    }

    public V get(long time, TimeUnit timeUnit, V def) {
        try {
            return this.get(time, timeUnit);
        } catch (CancellationException | ExecutionException | InterruptedException | TimeoutException exception) {
            return def;
        }
    }
}
