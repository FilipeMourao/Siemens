package de.tum.ipraktikum.customized.jenetics;
import de.tum.ipraktikum.model.Configuration;
import de.tum.ipraktikum.model.simulation.Furniture;
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

public class CustomCrossover extends MultiPointCrossover<Furniture, Double> {
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
    protected int crossover(MSeq<Furniture> that, MSeq<Furniture> other) {
        Random rand = new Random();
        Furniture currentTable1,currentTable2,currentChair1,currentChair2;
        int randomValue;
        
        int numberOfGenesChanged = rand.nextInt(that.size()/2) + 1;
        List<Furniture> furnitureList1 =  (List<Furniture>)that.asList();
        List<Furniture> furnitureList2 = (List<Furniture>)other.asList();
        List<Furniture> tableList1 = that.asList().stream().filter(c -> c.getType() == 0 ).collect(Collectors.toList());
        List<Furniture> chairList1 =  that.asList().stream().filter(c -> c.getType() == 1 ).collect(Collectors.toList());
        List<Furniture> tableList2 = other.asList().stream().filter(c -> c.getType() == 0 ).collect(Collectors.toList());
        List<Furniture> chairList2 =  other.asList().stream().filter(c -> c.getType() == 1 ).collect(Collectors.toList());
        if (furnitureList1.isEmpty() || furnitureList2.isEmpty()) return 2;

        for (int i = 0; i < numberOfGenesChanged; i++) {
        	randomValue = rand.nextInt(tableList1.size());
        	
        	currentTable1 = tableList1.get(randomValue);
        	currentChair1 = chairList1.get(randomValue);
        	currentTable2 = tableList2.get(randomValue);
        	currentChair2 = chairList2.get(randomValue);
        	
        	//randomValue = furnitureList1.indexOf(currentTable1);
        	that.set(randomValue*2, currentTable2);
        	that.set(randomValue*2 + 1, currentChair2);
        	other.set(randomValue*2, currentTable1);
        	other.set(randomValue*2 + 1, currentChair1);        	
//        	furnitureList1.remove(currentChair1);
//        	furnitureList1.remove(currentTable1);
//        	furnitureList1.add(currentTable2);
//        	furnitureList1.add(currentChair2);
//        	furnitureList2.remove(currentTable2);
//        	furnitureList2.remove(currentChair2);
//        	furnitureList2.add(currentTable1);
//        	furnitureList2.add(currentChair1);
        	
        }
//        for (int i = 0; i < furnitureList1.size();i++) {
//        	that.set(i, furnitureList1.get(i));
//        	other.set(i, furnitureList2.get(i));
//        }
        return 2;
    }

}
