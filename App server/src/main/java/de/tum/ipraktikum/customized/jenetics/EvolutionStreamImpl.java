package de.tum.ipraktikum.customized.jenetics;

import java.util.Spliterator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

import io.jenetics.Gene;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStart;
import io.jenetics.internal.util.LimitSpliterator;
import io.jenetics.internal.util.StreamProxy;

/**
 * Copied from jenetics
 * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
 * @since 3.0
 * @version 4.1
 */
public final class EvolutionStreamImpl<
        G extends Gene<?, G>,
        C extends Comparable<? super C>
        >
        extends StreamProxy<EvolutionResult<G, C>>
        implements EvolutionStream<G, C>
{

    private final Spliterator<EvolutionResult<G, C>> _spliterator;

    private EvolutionStreamImpl(
            final Spliterator<EvolutionResult<G, C>> spliterator,
            final boolean parallel
    ) {
        super(StreamSupport.stream(spliterator, parallel));
        if(spliterator instanceof EvolutionSpliterator){
            evolutionSpliterator = (EvolutionSpliterator) spliterator;
        }
        _spliterator = spliterator;
    }

    public EvolutionStreamImpl(
            final Supplier<EvolutionStart<G, C>> start,
            final Function<? super EvolutionStart<G, C>, EvolutionResult<G, C>> evolution
    ) {
        this(new EvolutionSpliterator<>(start, evolution), false);
    }

    private EvolutionSpliterator evolutionSpliterator;

    @Override
    public void stopEvolution() {
        evolutionSpliterator.stopEvolution();
    }

    @Override
    public EvolutionStream<G, C>
    limit(final Predicate<? super EvolutionResult<G, C>> proceed) {
        return new EvolutionStreamImpl<>(
                LimitSpliterator.of(_spliterator, proceed),
                isParallel()
        );
    }

}
