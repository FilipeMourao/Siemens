package de.tum.ipraktikum.customized.jenetics;

import java.time.Clock;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;
import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Copied from jenetics
 */
public class TimedExecutor {
    private final Executor _executor;

    public TimedExecutor(final Executor executor) {
        _executor = requireNonNull(executor);
    }

    public <T> CompletableFuture<TimedResult<T>> async(
            final Supplier<T> supplier,
            final Clock clock
    ) {
        return supplyAsync(TimedResult.of(supplier, clock), _executor);
    }

    public <U, T> CompletableFuture<TimedResult<T>> thenApply(
            final CompletableFuture<U> result,
            final Function<U, T> function,
            final Clock clock
    ) {
        return result.thenApplyAsync(TimedResult.of(function, clock), _executor);
    }


    public Executor get() {
        return _executor;
    }
}
