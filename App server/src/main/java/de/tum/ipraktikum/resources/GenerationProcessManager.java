package de.tum.ipraktikum.resources;

import de.tum.ipraktikum.model.simulation.Furniture;
import de.tum.ipraktikum.model.simulation.EvolutionConfiguration;
import de.tum.ipraktikum.model.simulation.Room;
import de.tum.ipraktikum.utils.ResettableTimer;
import de.tum.ipraktikum.utils.Triple;
import de.tum.ipraktikum.utils.Tuple;
import io.jenetics.Phenotype;

import javax.ejb.Singleton;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

/**
 * Singleton which manages the generation processes to support multiple clients
 */
@Singleton
public class GenerationProcessManager implements IGenerationProcessManager {

    private Map<String, Triple<GenerationProcess, Future<Phenotype<Furniture, Double>>, ResettableTimer>> processMap = new HashMap<>();
    //Millisec
    private static final long timerDelay = 180000;

    public void scheduleProcess(String processId, EvolutionConfiguration evolutionConfiguration) {
        final ResettableTimer resettableTimer = new ResettableTimer(timerDelay, processId) {
            @Override
            public void runPeriodic() {
                System.out.println("Timer Thread interrupted");
                stopProcess(this.getProcessId());
            }
        };
        final GenerationProcess newProcess = new GenerationProcess(processId, evolutionConfiguration);

        //Single Thread Executor for each process
        ExecutorService threadPool = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r) {
                    @Override
                    public void interrupt() {
                        super.interrupt();
                        newProcess.cancel();
                    }
                };
            }
        });
        Future<Phenotype<Furniture, Double>> future = threadPool.submit(newProcess);
        processMap.put(processId, new Triple<>(newProcess, future, resettableTimer));
    }

    @Override
    public void stopProcess(String processId) {
        System.out.println("Attempt to stop process" +
                "");
        Triple<GenerationProcess, Future<Phenotype<Furniture, Double>>, ResettableTimer> processPair = processMap.remove(processId);
        if (processPair != null) {
            processPair.getFirst().cancel();
            processPair.getSecond().cancel(false);
            processPair.getThird().cancel();
        }
    }

    @Override
    public GenerationProcess getProcess(String processId) {
        return processMap.get(processId).getFirst();
    }

    @Override
    public boolean doesProcessExist(String processId) {
        return processMap.containsKey(processId) && processMap.get(processId) != null;
    }

    public Tuple<Long, Room[]> getBestResultsOfProcess(String processId) {
        Triple<GenerationProcess, Future<Phenotype<Furniture, Double>>, ResettableTimer> processPair = processMap.get(processId);
        if (processPair != null) {
            Long generation = processPair.getFirst().getGeneration();
            Room[] switchingCabinet = null;
            Phenotype[] bestResults = processPair.getFirst().getBestResults();
            if (bestResults != null) {
                switchingCabinet = Arrays.stream(bestResults).map(phenotype ->
                        (Room) phenotype.getGenotype().getChromosome()).toArray(Room[]::new);
            }
            processPair.getThird().reset();
            return new Tuple<>(generation, switchingCabinet);
        }
        return null;
    }
}
