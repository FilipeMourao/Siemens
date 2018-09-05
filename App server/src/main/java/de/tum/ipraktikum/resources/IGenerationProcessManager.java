package de.tum.ipraktikum.resources;

import de.tum.ipraktikum.model.simulation.EvolutionConfiguration;
import de.tum.ipraktikum.model.simulation.Room;
import de.tum.ipraktikum.utils.Tuple;

/**
 * Interface describing functionality to manage generation processes
 */
public interface IGenerationProcessManager {

    /**
     * Gets the currently best results of the given process
     *
     * @param processId application process id
     * @return the currently best results
     */
    Tuple<Long, Room[]> getBestResultsOfProcess(String processId);

    /**
     * schedules a process with a application internal processId to reference the generation process
     *
     * @param processId application internal process id
     */
    void scheduleProcess(String processId, EvolutionConfiguration requiredSwitchingCabinet);

    /**
     * stops a process with a given application process id
     *
     * @param processId the application process id of the process which should be stopped
     */
    void stopProcess(String processId);

    GenerationProcess getProcess(String processId);

    boolean doesProcessExist(String processId);
}
