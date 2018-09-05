package de.tum.ipraktikum.customized.jenetics;

import io.jenetics.Gene;
import io.jenetics.Phenotype;
import io.jenetics.util.ISeq;

import static java.util.Objects.requireNonNull;

/**
 * Copied from jenetics
 * @param <G>
 * @param <C>
 */
public final class FilterResult<
        G extends Gene<?, G>,
        C extends Comparable<? super C>
        > {

    private final ISeq<Phenotype<G, C>> population;
    private final int killCount;
    private final int invalidCount;

    public FilterResult(
            final ISeq<Phenotype<G, C>> population,
            final int killCount,
            final int invalidCount
    ) {
        this.population = requireNonNull(population);
        this.killCount = killCount;
        this.invalidCount = invalidCount;
    }

    public ISeq<Phenotype<G, C>> getPopulation() {
        return population;
    }

    public int getKillCount() {
        return killCount;
    }

    public int getInvalidCount() {
        return invalidCount;
    }
}
