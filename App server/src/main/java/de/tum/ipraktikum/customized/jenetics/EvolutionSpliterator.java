package de.tum.ipraktikum.customized.jenetics;

import static java.util.Objects.requireNonNull;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import io.jenetics.Gene;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStart;

/**
 * Copied from jenetics, adjustments to clear stream resources after process stop
 *
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @version 4.1
 * @since 3.0
 */
public final class EvolutionSpliterator<
        G extends Gene<?, G>,
        C extends Comparable<? super C>
        >
        implements Spliterator<EvolutionResult<G, C>> {

    private final Supplier<EvolutionStart<G, C>> _start;
    private final Function<? super EvolutionStart<G, C>, EvolutionResult<G, C>> _evolution;

    private long _estimate;
    private EvolutionStart<G, C> _next = null;

    private EvolutionSpliterator(
            final Supplier<EvolutionStart<G, C>> start,
            final Function<? super EvolutionStart<G, C>, EvolutionResult<G, C>> evolution,
            final long estimate
    ) {
        _evolution = requireNonNull(evolution);
        _start = requireNonNull(start);
        _estimate = estimate;
    }

    public EvolutionSpliterator(
            final Supplier<EvolutionStart<G, C>> start,
            final Function<? super EvolutionStart<G, C>, EvolutionResult<G, C>> evolution
    ) {
        this(start, evolution, Long.MAX_VALUE);
    }

    @Override
    public boolean
    tryAdvance(final Consumer<? super EvolutionResult<G, C>> action) {
        if (_next == null) {
            _next = _start.get();
        }

        final EvolutionResult<G, C> result = _evolution.apply(_next);
        action.accept(result);
        if (result != null && runEvolution)
            _next = result.next();
        return runEvolution;
    }

    @Override
    public Spliterator<EvolutionResult<G, C>> trySplit() {
        return _estimate > 0
                ? new EvolutionSpliterator<>(_start, _evolution, _estimate >>>= 1)
                : null;
    }

    @Override
    public long estimateSize() {
        return _estimate;
    }

    @Override
    public int characteristics() {
        return NONNULL | IMMUTABLE | ORDERED;
    }

    private boolean runEvolution = true;

    public void stopEvolution() {
        runEvolution = false;
    }
}