package de.tum.ipraktikum.customized.jenetics;
import de.tum.ipraktikum.model.Configuration;
import de.tum.ipraktikum.model.simulation.Table;
import de.tum.ipraktikum.model.simulation.Room;
import io.jenetics.Chromosome;
import io.jenetics.Crossover;
import io.jenetics.Gene;
import io.jenetics.Genotype;
import io.jenetics.MultiPointCrossover;
import io.jenetics.Phenotype;
import io.jenetics.Recombinator;
import io.jenetics.internal.util.Equality;
import io.jenetics.internal.util.Hash;
import io.jenetics.util.MSeq;
import io.jenetics.util.RandomRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.lang.Math.min;
import static java.lang.String.format;
import static java.lang.Math.min;

import java.util.Random;

import io.jenetics.util.MSeq;
import io.jenetics.util.RandomRegistry;

public class CustomCrossover extends MultiPointCrossover<Table, Double> {
    public CustomCrossover(final double probability, final int n) {
        super(probability, n);

    }


    /**
     * Constructs an alterer with a given recombination probability.
     *
     * @param probability the crossover probability.
     * @throws IllegalArgumentException if the {@code probability} is not in the
     *                                  valid range of {@code [0, 1]}.
     */


    @Override
    public int hashCode() {
        return Hash.of(getClass()).and(super.hashCode()).value();
    }

    @Override
    public boolean equals(final Object obj) {
        return Equality.of(this, obj).test(super::equals);
    }

    @Override
    public String toString() {
        return format("%s[p=%f]", getClass().getSimpleName(), _probability);
    }

    @Override
    protected int crossover(MSeq<Table> that, MSeq<Table> other) {
        Random rand = new Random();
        Table currentTable1,curentTable2;
        
        int numberOfGenesChanged = 2;//rand.nextInt(that.size()/2) + 1;
        List<Table> tableList1 = that.asList();
        List<Table> tableList2 = (List<Table>) other.asList();
        tableList1 = new ArrayList<Table>(tableList1);
        tableList2 = new ArrayList<Table>(tableList2);
        if (tableList1.isEmpty() || tableList2.isEmpty()) return 2;

        for (int i = 0; i < numberOfGenesChanged; i++) {
        	currentTable1 = tableList1.get(rand.nextInt(tableList1.size()));
        	curentTable2 = tableList2.get(rand.nextInt(tableList2.size()));
        	while(currentTable1.getType() != curentTable2.getType())  curentTable2 = tableList2.get(rand.nextInt(tableList2.size()));
        	tableList1.remove(currentTable1);
        	tableList2.remove(curentTable2);
        	tableList1.add(curentTable2);
        	tableList2.add(currentTable1);
        }
        for (int i = 0; i < tableList1.size();i++) {
        	that.set(i, tableList1.get(i));
        	other.set(i, tableList2.get(i));
        }
        return 2;
    }

}
