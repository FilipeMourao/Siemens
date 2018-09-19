package de.tum.ipraktikum.resources;

import de.tum.ipraktikum.customized.jenetics.CustomCrossover;
import de.tum.ipraktikum.customized.jenetics.CustomizedEngine;
import de.tum.ipraktikum.customized.jenetics.EvolutionStream;
import de.tum.ipraktikum.model.Configuration;
import de.tum.ipraktikum.model.simulation.*;
import io.jenetics.*;
import io.jenetics.engine.EvolutionInit;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.internal.collection.Array;
import io.jenetics.internal.collection.ArrayISeq;
import io.jenetics.internal.collection.ObjectStore;
import io.jenetics.util.Factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GenerationProcess implements Callable<Phenotype<Furniture, Double>> {

    private final String processId;
    private Genotype[] initialPopulation;

    private EvolutionConfiguration evolutionConfiguration;
    private Room optimalSwitchingCabinet;

    private long currentGerneration = 1;
    //best results of current generation
    private Phenotype[] bestResults;

    //jenetics objects
    private CustomizedEngine<Furniture, Double> customizedEngine;
    private EvolutionStream<Furniture, Double> evoStream;

    Factory<Room> roomFactory = new Factory<Room>() {
        @Override
        public Room newInstance() {
            Room randomRoom;
            randomRoom = Room.createRandomRoomFromInitialRoom(evolutionConfiguration.getInitialRoom());
            return randomRoom;
        }
    };

    Factory<Genotype<Furniture>> gtf = new Factory<Genotype<Furniture>>() {
        @Override
        public Genotype<Furniture> newInstance() {
            return Genotype.of(roomFactory, 1);
        }
    };

    public GenerationProcess(String processId, EvolutionConfiguration evolutionConfig) {
        this.evolutionConfiguration = evolutionConfig;
        this.processId = processId;
        List<Room> initialPopulation = evolutionConfig.getInitialPopulation();
        if (initialPopulation != null && initialPopulation.size() > 0) {
            this.initialPopulation = new Genotype[Configuration.totalPopulation];
            for (int i = 0; i < initialPopulation.size(); i++) {
                Room room = initialPopulation.get(i);
                this.initialPopulation[i] = Genotype.of(room);
            }
            //Fill with copies
            for (int i = initialPopulation.size(); i < this.initialPopulation.length; i++) {
            	this.initialPopulation[i] = Genotype.of(Room.copyRoom(initialPopulation.get(i % initialPopulation.size())));
            }
        }
        this.customizedEngine = CustomizedEngine
                .builder(genotype -> customizedEngine.fitness(genotype), gtf)
                .populationSize(Configuration.totalPopulation)
                .optimize(Optimize.MINIMUM)
                .offspringSelector(new RouletteWheelSelector<>())
                .alterers(new CustomCrossover(0.7, 2), new Mutator<>(0.0))
                .process(this)
                .build();
    }

    private void findBestCandidatesOfGeneration(EvolutionResult<Furniture, Double> unsortedResult) {
        if (unsortedResult != null) {
            //int maxAmountPhenotypes = 5;
        	int maxAmountPhenotypes = 1;
        	//phenotype array
            Phenotype[] sortedPhenotypes = unsortedResult
                    .getPopulation()
                    .stream()
                    .sorted((o1, o2) -> {
                        if (o1.getFitness() > o2.getFitness()) {
                            return 1;
                        } else if (o1.getFitness() < o2.getFitness()) {
                            return -1;
                        }
                        return 0;
                    })
                    .toArray(Phenotype[]::new);
            // MAX OR MINIMUM CHANGE HERE
            List<Phenotype> bestPhenotypes = new ArrayList<>();
//            for (int i = sortedPhenotypes.length - 1 ; i < sortedPhenotypes.length && bestPhenotypes.size() < maxAmountPhenotypes; i--) {
            for (int i = 0 ; i < sortedPhenotypes.length && bestPhenotypes.size() < maxAmountPhenotypes; i++) {
            	 
                Phenotype bestCandiate = sortedPhenotypes[i];
                Room room = (Room) bestCandiate.getGenotype().getChromosome();
                List<Room> bestRooms = bestPhenotypes.stream()
                        .map(phenotype -> (Room) phenotype.getGenotype().getChromosome())
                        .collect(Collectors.toList());
                if (!bestRooms.contains(room)) {
                    //if the switching cabinet is not yet included in bestPhenotypes
                    bestPhenotypes.add(bestCandiate);
                }
            }
//            List<Phenotype> bestPhenotypes = new ArrayList<>();
//            for (int i = 0; i < sortedPhenotypes.length && bestPhenotypes.size() < maxAmountPhenotypes; i++) {
//                Phenotype bestCandiate = sortedPhenotypes[i];
//                Room room = (Room) bestCandiate.getGenotype().getChromosome();
//                List<Room> bestRooms = bestPhenotypes.stream()
//                        .map(phenotype -> (Room) phenotype.getGenotype().getChromosome())
//                        .collect(Collectors.toList());
//                if (!bestRooms.contains(room)) {
//                    //if the switching cabinet is not yet included in bestPhenotypes
//                    bestPhenotypes.add(bestCandiate);
//                }
//            }
            //best results
            setBestResults(bestPhenotypes.toArray(new Phenotype[0]));
            currentGerneration = unsortedResult.getGeneration();
           // System.out.println(currentGerneration);
	        GsonBuilder builder = new GsonBuilder();
	        Gson gson = builder.create();
	        



          Room room = (Room) getBestResults()[0].getGenotype().getChromosome();
//	      double totalDistance = room.getSumOfTablesDistances();
          System.out.println(gson.toJson(room));
       //   System.out.printf("%.2f", room.getDistancePerLampMeanAndVariance().a);
          System.out.println(currentGerneration);
          System.out.printf("%.2f", room.getDistancePerLampMeanAndVariance().a);
          System.out.println();

        }
    }

    /**
     * implementation of the @{@link Callable} interface of java. Core functionality for generation process
     *
     * @return the most fitting phenotype
     * @throws Exception
     */
    @Override
    public Phenotype<Furniture, Double> call() throws Exception {
        Phenotype<Furniture, Double> bestPhenotype = null;
        try {
            if (initialPopulation != null) {
                ObjectStore<Genotype<Furniture>> store = ObjectStore.of(initialPopulation);
                Array<Genotype<Furniture>> array = Array.of(store);
                ArrayISeq<Genotype<Furniture>> population = new ArrayISeq<Genotype<Furniture>>(array);
                EvolutionInit<Furniture> evolutionInit = EvolutionInit.of(population, 1);
                evoStream = customizedEngine
                        .stream(evolutionInit);
            } else {
                evoStream = customizedEngine
                        .stream();
            }
            bestPhenotype = evoStream
                    .limit(Configuration.totalGeneration)
                    .peek(r -> findBestCandidatesOfGeneration(r))
                    .collect(EvolutionResult.toBestPhenotype());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bestPhenotype;
    }

    /**
     * get bests results of current generation process
     *
     * @return
     */
    public Phenotype[] getBestResults() {
        return bestResults == null ? null : Arrays.copyOf(bestResults, bestResults.length);
    }

    public void setBestResults(Phenotype[] bestResults) {
        this.bestResults = bestResults;
    }

    public String getProcessId() {
        return processId;
    }

    public void cancel() {
        System.out.println("Current Thread Resources closed");
        evoStream.stopEvolution();
        evoStream.close();
        customizedEngine = null;
        System.out.println("Current Thread Resources closed");
    }

    public Room[] getSwitchingCabinetForSimulation() {
        return customizedEngine.currentPopulation != null ? customizedEngine.currentPopulation
                .stream()
                .map(phenotype -> (Room) phenotype.getGenotype().getChromosome())
                .toArray(Room[]::new) : new Room[0];
    }

    public Long getGeneration() {
        return currentGerneration;
    }

    public void setEvolutionConfiguration(EvolutionConfiguration evolutionConfiguration) {
        this.evolutionConfiguration = evolutionConfiguration;
    }

    public EvolutionConfiguration getEvolutionConfiguration() {
        return this.evolutionConfiguration;
    }

    public Room getOptimalSwitchingCabinet() {
        return optimalSwitchingCabinet;
    }

    public void setOptimalSwitchingCabinet(Room optimalSwitchingCabinet) {
        this.optimalSwitchingCabinet = optimalSwitchingCabinet;
    }

}
