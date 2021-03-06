package de.tum.ipraktikum.customized.jenetics;

import de.tum.ipraktikum.model.Configuration;
import de.tum.ipraktikum.model.simulation.FixedFurniture;
import de.tum.ipraktikum.model.simulation.Furniture;
import de.tum.ipraktikum.model.simulation.Room;
import de.tum.ipraktikum.resources.GenerationProcess;
import de.tum.ipraktikum.utils.Tuple;
import io.jenetics.*;
import io.jenetics.engine.*;
import io.jenetics.internal.util.Concurrency;
import io.jenetics.internal.util.require;
import io.jenetics.util.*;

import java.time.Clock;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.jenetics.internal.util.require.probability;
import static java.lang.Math.round;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * CustomizedEngine, customized evolution process of jenetics framework
 *
 * @param <G> some class implementing Gene interface
 * @param <C> some class implementing Comparable interace
 */
public final class CustomizedEngine<
        G extends Gene<?, G>,
        C extends Comparable<? super C>
        >
        implements
        Function<EvolutionStart<G, C>, EvolutionResult<G, C>>,
        EvolutionStreamable<G, C> {

    // Problem definition.
    private final Function<? super Genotype<G>, ? extends C> _fitnessFunction;
    private final Factory<Genotype<G>> _genotypeFactory;

    // Evolution parameters.
    private final Function<? super C, ? extends C> _fitnessScaler;
    private final Selector<G, C> _survivorsSelector;
    private final Selector<G, C> _offspringSelector;
    private final Alterer<G, C> _alterer;
    private final Predicate<? super Phenotype<G, C>> _validator;
    private final Optimize _optimize;
    private final int _offspringCount;
    private final int _survivorsCount;
    private final long _maximalPhenotypeAge;

    // Execution context for concurrent execution of evolving steps.
    private final TimedExecutor _executor;
    private final Clock _clock;

    // Additional parameters.
    private final int _individualCreationRetries;
    private final UnaryOperator<EvolutionResult<G, C>> _mapper;


    /**
     * Create a new GA engine with the given parameters.
     *
     * @param fitnessFunction           the fitness function this GA is using.
     * @param genotypeFactory           the genotype factory this GA is working with.
     * @param fitnessScaler             the fitness scaler this GA is using.
     * @param survivorsSelector         the selector used for selecting the survivors
     * @param offspringSelector         the selector used for selecting the offspring
     * @param alterer                   the alterer used for altering the offspring
     * @param validator                 phenotype validator which can override the default
     *                                  implementation the {@link Phenotype#isValid()} method.
     * @param optimize                  the kind of optimization (minimize or maximize)
     * @param offspringCount            the number of the offspring individuals
     * @param survivorsCount            the number of the survivor individuals
     * @param maximalPhenotypeAge       the maximal age of an individual
     * @param executor                  the executor used for executing the single evolve steps
     * @param clock                     the clock used for calculating the timing results
     * @param individualCreationRetries the maximal number of attempts for
     *                                  creating a valid individual.
     * @throws NullPointerException     if one of the arguments is {@code null}
     * @throws IllegalArgumentException if the given integer values are smaller
     *                                  than one.
     */
    CustomizedEngine(
            final Function<? super Genotype<G>, ? extends C> fitnessFunction,
            final Factory<Genotype<G>> genotypeFactory,
            final Function<? super C, ? extends C> fitnessScaler,
            final Selector<G, C> survivorsSelector,
            final Selector<G, C> offspringSelector,
            final Alterer<G, C> alterer,
            final Predicate<? super Phenotype<G, C>> validator,
            final Optimize optimize,
            final int offspringCount,
            final int survivorsCount,
            final long maximalPhenotypeAge,
            final Executor executor,
            final Clock clock,
            final int individualCreationRetries,
            final UnaryOperator<EvolutionResult<G, C>> mapper,
            final GenerationProcess process
    ) {
        _fitnessFunction = requireNonNull(fitnessFunction);
        _fitnessScaler = requireNonNull(fitnessScaler);
        _genotypeFactory = requireNonNull(genotypeFactory);
        _survivorsSelector = requireNonNull(survivorsSelector);
        _offspringSelector = requireNonNull(offspringSelector);
        _alterer = requireNonNull(alterer);
        _validator = requireNonNull(validator);
        _optimize = requireNonNull(optimize);

        _offspringCount = require.nonNegative(offspringCount);
        _survivorsCount = require.nonNegative(survivorsCount);
        _maximalPhenotypeAge = require.positive(maximalPhenotypeAge);

        _executor = new TimedExecutor(requireNonNull(executor));
        _clock = requireNonNull(clock);

        if (individualCreationRetries < 0) {
            throw new IllegalArgumentException(format(
                    "Retry count must not be negative: %d",
                    individualCreationRetries
            ));
        }
        _individualCreationRetries = individualCreationRetries;
        _mapper = requireNonNull(mapper);
        this.process = process;
    }

    /**
     * Perform one evolution step with the given {@code population} and
     * {@code generation}. New phenotypes are created with the fitness function
     * and fitness scaler defined by this <em>engine</em>
     *
     * <em>This method is thread-safe.</em>
     *
     * @param population the population to evolve
     * @param generation the current generation; used for calculating the
     *                   phenotype age.
     * @return the evolution result
     * @throws java.lang.NullPointerException if the given {@code population} is
     *                                        {@code null}
     * @throws IllegalArgumentException       if the given {@code generation} is
     *                                        smaller then one
     * @see #evolve(EvolutionStart)
     */
    public EvolutionResult<G, C> evolve(
            final ISeq<Phenotype<G, C>> population,
            final long generation
    ) {
        return evolve(EvolutionStart.of(population, generation));
    }


    //MY CODE

    public ISeq<Phenotype<G, C>> currentPopulation;
    private final GenerationProcess process;

    public double fitness(final Genotype<Furniture> genotype) {
        Room room = (Room) genotype.getChromosome();
        
        List<Double> listOfLampsDistanceMean = new ArrayList();
        List<Double> listOfWidnowsDistanceMean = new ArrayList();
        List<Double> listOfEqualLightDistanceMean = new ArrayList();
        Tuple<Double,Double> equalLightMeanAndVariance;// a = mean, b = variance 
        Tuple<Double,Double> lampsMeanAndVariance;// a = mean, b = variance 
        Tuple<Double,Double> windowsMeanAndVariance;// a = mean, b = variance
     
        for (int i = 0; i < currentPopulation.size(); i++) {// evaluate the design of every creature
            Room r = (Room) currentPopulation.get(i).getGenotype().getChromosome();
            lampsMeanAndVariance = r.getDistancePerLampMeanAndVariance();// a = mean, b = variance 
            listOfLampsDistanceMean.add(lampsMeanAndVariance.b);
            windowsMeanAndVariance = r.getDistancePerWindowsMeanAndVariance();// a = mean, b = variance
            listOfWidnowsDistanceMean.add(windowsMeanAndVariance.b); 
            equalLightMeanAndVariance = r.getEqualLightMeanAndVariance();
            listOfEqualLightDistanceMean.add(equalLightMeanAndVariance.b);
            
        }
        double normalizeLampMean = 0;
        double normalizeWindowMean = 0;
        double normalizeEqualLight = 0;
        
        double minLampMean = Collections.min(listOfLampsDistanceMean);
        double maxLampMean = Collections.max(listOfLampsDistanceMean);
        if(maxLampMean == minLampMean) normalizeLampMean = 0;
        else normalizeLampMean = (room.getDistancePerLampMeanAndVariance().b - minLampMean)/(maxLampMean - minLampMean);
        
        double minWindowMean = Collections.min(listOfWidnowsDistanceMean);
        double maxWindowMean = Collections.max(listOfWidnowsDistanceMean);
        if(maxWindowMean == minWindowMean) normalizeWindowMean = 0;
        else normalizeLampMean = (room.getDistancePerWindowsMeanAndVariance().b - minWindowMean)/(maxWindowMean - minWindowMean);
        
        double minEqualLightMean = Collections.min(listOfEqualLightDistanceMean);
        double maxEqualLightMean = Collections.max(listOfEqualLightDistanceMean);
        if(maxEqualLightMean == minEqualLightMean) normalizeWindowMean = 0;
        else normalizeEqualLight = (room.getEqualLightMeanAndVariance().b - minWindowMean)/(maxWindowMean - minWindowMean);
        
        
        return normalizeLampMean*Configuration.lampProximityWeight      +    normalizeWindowMean*Configuration.windowsProximityWeight  +	   normalizeEqualLight*Configuration.equalLightDistributionWeight;


    }

    //MY CODE

    /**
     * Perform one evolution step with the given evolution {@code start} object
     * New phenotypes are created with the fitness function and fitness scaler
     * defined by this <em>engine</em>
     *
     * <em>This method is thread-safe.</em>
     *
     * @param start the evolution start object
     * @return the evolution result
     * @throws java.lang.NullPointerException if the given evolution
     *                                        {@code start} is {@code null}
     * @see #evolve(ISeq, long)
     * @since 3.1
     */
    public EvolutionResult<G, C> evolve(final EvolutionStart<G, C> start) {
        //System.out.println("Evolution stopped for generation " + start.getGeneration());
        currentPopulation = start.getPopulation();
        if (start.getGeneration() == 1) {
            //setting first 5 creatures as best result for generation 0
            process.setBestResults(currentPopulation
                    .stream()
                    .limit(5)
                    .toArray(Phenotype[]::new));
        }
        final Timer timer = Timer.of(_clock).start();

        // Initial evaluation of the population.
        final Timer evaluateTimer = Timer.of(_clock).start();
        evaluate(start.getPopulation());
        evaluateTimer.stop();

        // Select the offspring population.
        final CompletableFuture<TimedResult<ISeq<Phenotype<G, C>>>> offspring =
                _executor.async(() ->
                                selectOffspring(start.getPopulation()),
                        _clock
                );

        // Select the survivor population.
        final CompletableFuture<TimedResult<ISeq<Phenotype<G, C>>>> survivors =
                _executor.async(() ->
                                selectSurvivors(start.getPopulation()),
                        _clock
                );

        // Altering the offspring population.
        final CompletableFuture<TimedResult<AltererResult<G, C>>> alteredOffspring =
                _executor.thenApply(offspring, p ->
                                _alterer.alter(p.getResult(), start.getGeneration()),
                        _clock
                );

        // Filter and replace invalid and old survivor individuals.
        final CompletableFuture<TimedResult<FilterResult<G, C>>> filteredSurvivors =
                _executor.thenApply(survivors, pop ->
                                filter(pop.getResult(), start.getGeneration()),
                        _clock
                );

        // Filter and replace invalid and old offspring individuals.
        final CompletableFuture<TimedResult<FilterResult<G, C>>> filteredOffspring =
                _executor.thenApply(alteredOffspring, pop ->
                                filter(pop.getResult().getPopulation(), start.getGeneration()),
                        _clock
                );

        // Combining survivors and offspring to the new population.
        final CompletableFuture<ISeq<Phenotype<G, C>>> population =
                filteredSurvivors.thenCombineAsync(filteredOffspring, (s, o) ->
                                ISeq.of(s.getResult().getPopulation().append(o.getResult().getPopulation())),
                        _executor.get()
                );

        // Evaluate the fitness-function and wait for result.
        final ISeq<Phenotype<G, C>> pop = population.join();
        final TimedResult<ISeq<Phenotype<G, C>>> result = TimedResult
                .of(() -> evaluate(pop), _clock)
                .get();


        final EvolutionDurations durations = EvolutionDurations.of(
                offspring.join().getDuration(),
                survivors.join().getDuration(),
                alteredOffspring.join().getDuration(),
                filteredOffspring.join().getDuration(),
                filteredSurvivors.join().getDuration(),
                result.getDuration().plus(evaluateTimer.getTime()),
                timer.stop().getTime()
        );

        final int killCount =
                filteredOffspring.join().getResult().getKillCount() +
                        filteredSurvivors.join().getResult().getKillCount();

        final int invalidCount =
                filteredOffspring.join().getResult().getInvalidCount() +
                        filteredSurvivors.join().getResult().getInvalidCount();

        return _mapper.apply(
                EvolutionResult.of(
                        _optimize,
                        result.getResult(),
                        start.getGeneration(),
                        durations,
                        killCount,
                        invalidCount,
                        alteredOffspring.join().getResult().getAlterations()
                )
        );
    }

    /**
     * This method is an <i>alias</i> for the {@link #evolve(EvolutionStart)}
     * method.
     *
     * @since 3.1
     */
    @Override
    public EvolutionResult<G, C> apply(final EvolutionStart<G, C> start) {
        return evolve(start);
    }

    // Selects the survivors population. A new population object is returned.
    private ISeq<Phenotype<G, C>>
    selectSurvivors(final ISeq<Phenotype<G, C>> population) {
        return _survivorsCount > 0
                ? _survivorsSelector.select(population, _survivorsCount, _optimize)
                : ISeq.empty();
    }

    // Selects the offspring population. A new population object is returned.
    private ISeq<Phenotype<G, C>>
    selectOffspring(final ISeq<Phenotype<G, C>> population) {
        return _offspringCount > 0
                ? _offspringSelector.select(population, _offspringCount, _optimize)
                : ISeq.empty();
    }

    // Filters out invalid and old individuals. Filtering is done in place.
    private FilterResult<G, C> filter(
            final Seq<Phenotype<G, C>> population,
            final long generation
    ) {
        int killCount = 0;
        int invalidCount = 0;

        final MSeq<Phenotype<G, C>> pop = MSeq.of(population);
        for (int i = 0, n = pop.size(); i < n; ++i) {
            final Phenotype<G, C> individual = pop.get(i);

            if (!_validator.test(individual)) {
                pop.set(i, newPhenotype(generation));
                ++invalidCount;
            } else if (individual.getAge(generation) > _maximalPhenotypeAge) {
                pop.set(i, newPhenotype(generation));
                ++killCount;
            }
        }

        return new FilterResult<>(pop.toISeq(), killCount, invalidCount);
    }

    // Create a new and valid phenotype
    private Phenotype<G, C> newPhenotype(final long generation) {
        int count = 0;
        Phenotype<G, C> phenotype;
        do {
            phenotype = Phenotype.of(
                    _genotypeFactory.newInstance(),
                    generation,
                    _fitnessFunction,
                    _fitnessScaler
            );
        } while (++count < _individualCreationRetries &&
                !_validator.test(phenotype));
//stop here
        return phenotype;
    }

    // Evaluates the fitness function of the give population concurrently.
    private ISeq<Phenotype<G, C>>
    evaluate(final ISeq<Phenotype<G, C>> population) {
        try (Concurrency c = Concurrency.with(_executor.get())) {
            c.execute(population);
        }
        return population;
    }

    @Override
    public de.tum.ipraktikum.customized.jenetics.EvolutionStream<G, C>
    stream(final Supplier<EvolutionStart<G, C>> start) {
        return de.tum.ipraktikum.customized.jenetics.EvolutionStream.of(evolutionStart(start), this::evolve);
    }

    @Override
    public de.tum.ipraktikum.customized.jenetics.EvolutionStream<G, C> stream(final EvolutionInit<G> init) {
        return stream(evolutionStart(init));
    }

    private Supplier<EvolutionStart<G, C>>
    evolutionStart(final Supplier<EvolutionStart<G, C>> start) {
        return () -> {
            final EvolutionStart<G, C> es = start.get();
            final ISeq<Phenotype<G, C>> population = es.getPopulation();
            final long generation = es.getGeneration();

            final Stream<Phenotype<G, C>> stream = Stream.concat(
                    population.stream().map(this::toFixedPhenotype),
                    Stream.generate(() -> newPhenotype(generation))
            );

            final ISeq<Phenotype<G, C>> pop = stream
                    .limit(getPopulationSize())
                    .collect(ISeq.toISeq());

            return EvolutionStart.of(pop, generation);
        };
    }

    private Phenotype<G, C> toFixedPhenotype(final Phenotype<G, C> pt) {
        return
                pt.getFitnessFunction() == _fitnessFunction &&
                        pt.getFitnessScaler() == _fitnessScaler
                        ? pt
                        : pt.newInstance(
                        pt.getGeneration(),
                        _fitnessFunction,
                        _fitnessScaler
                );
    }

    private Supplier<EvolutionStart<G, C>>
    evolutionStart(final EvolutionInit<G> init) {
        return evolutionStart(() -> EvolutionStart.of(
                init.getPopulation()
                        .map(gt -> Phenotype.of(
                                gt,
                                init.getGeneration(),
                                _fitnessFunction,
                                _fitnessScaler)
                        ),
                init.getGeneration())
        );
    }

    /* *************************************************************************
     * Property access methods.
     **************************************************************************/

    /**
     * Return the fitness function of the GA engine.
     *
     * @return the fitness function
     */
    public Function<? super Genotype<G>, ? extends C> getFitnessFunction() {
        return _fitnessFunction;
    }

    /**
     * Return the fitness scaler of the GA engine.
     *
     * @return the fitness scaler
     */
    public Function<? super C, ? extends C> getFitnessScaler() {
        return _fitnessScaler;
    }

    /**
     * Return the used genotype {@link Factory} of the GA. The genotype factory
     * is used for creating the initial population and new, random individuals
     * when needed (as replacement for invalid and/or died genotypes).
     *
     * @return the used genotype {@link Factory} of the GA.
     */
    public Factory<Genotype<G>> getGenotypeFactory() {
        return _genotypeFactory;
    }

    /**
     * Return the used survivor {@link Selector} of the GA.
     *
     * @return the used survivor {@link Selector} of the GA.
     */
    public Selector<G, C> getSurvivorsSelector() {
        return _survivorsSelector;
    }

    /**
     * Return the used offspring {@link Selector} of the GA.
     *
     * @return the used offspring {@link Selector} of the GA.
     */
    public Selector<G, C> getOffspringSelector() {
        return _offspringSelector;
    }

    /**
     * Return the used {@link Alterer} of the GA.
     *
     * @return the used {@link Alterer} of the GA.
     */
    public Alterer<G, C> getAlterer() {
        return _alterer;
    }

    /**
     * Return the number of selected offsprings.
     *
     * @return the number of selected offsprings
     */
    public int getOffspringCount() {
        return _offspringCount;
    }

    /**
     * The number of selected survivors.
     *
     * @return the number of selected survivors
     */
    public int getSurvivorsCount() {
        return _survivorsCount;
    }

    /**
     * Return the number of individuals of a population.
     *
     * @return the number of individuals of a population
     */
    public int getPopulationSize() {
        return _offspringCount + _survivorsCount;
    }

    /**
     * Return the maximal allowed phenotype age.
     *
     * @return the maximal allowed phenotype age
     */
    public long getMaximalPhenotypeAge() {
        return _maximalPhenotypeAge;
    }

    /**
     * Return the optimization strategy.
     *
     * @return the optimization strategy
     */
    public Optimize getOptimize() {
        return _optimize;
    }

    /**
     * Return the {@link Clock} the engine is using for measuring the execution
     * time.
     *
     * @return the clock used for measuring the execution time
     */
    public Clock getClock() {
        return _clock;
    }

    /**
     * Return the {@link Executor} the engine is using for executing the
     * evolution steps.
     *
     * @return the executor used for performing the evolution steps
     */
    public Executor getExecutor() {
        return _executor.get();
    }


    /**
     * Return the maximal number of attempt before the {@code Engine} gives
     * up creating a valid individual ({@code Phenotype}).
     *
     * @return the maximal number of {@code Phenotype} creation attempts
     * @since 4.0
     */
    public int getIndividualCreationRetries() {
        return _individualCreationRetries;
    }

    /**
     * Return the evolution result mapper.
     *
     * @return the evolution result mapper
     * @since 4.0
     */
    public UnaryOperator<EvolutionResult<G, C>> getMapper() {
        return _mapper;
    }

    /* *************************************************************************
     * Builder methods.
     **************************************************************************/

    /**
     * Create a new evolution {@code Engine.Builder} initialized with the values
     * of the current evolution {@code Engine}. With this method, the evolution
     * engine can serve as a template for a new one.
     *
     * @return a new engine builder
     */
    public CustomizedEngine.Builder<G, C> builder() {
        return new CustomizedEngine.Builder<G, C>(_genotypeFactory, _fitnessFunction)
                .alterers(_alterer)
                .clock(_clock)
                .executor(_executor.get())
                .fitnessScaler(_fitnessScaler)
                .maximalPhenotypeAge(_maximalPhenotypeAge)
                .offspringFraction((double) _offspringCount / (double) getPopulationSize())
                .offspringSelector(_offspringSelector)
                .optimize(_optimize)
                .phenotypeValidator(_validator)
                .populationSize(getPopulationSize())
                .survivorsSelector(_survivorsSelector)
                .individualCreationRetries(_individualCreationRetries)
                .mapping(_mapper);
    }

    /**
     * Create a new evolution {@code Engine.Builder} for the given
     * {@link Problem}.
     *
     * @param problem the problem to be solved by the evolution {@code Engine}
     * @param <T>     the (<i>native</i>) argument type of the problem fitness function
     * @param <G>     the gene type the evolution engine is working with
     * @param <C>     the result type of the fitness function
     * @return Create a new evolution {@code Engine.Builder}
     * @since 3.4
     */
    public static <T, G extends Gene<?, G>, C extends Comparable<? super C>>
    CustomizedEngine.Builder<G, C> builder(final Problem<T, G, C> problem) {
        return builder(problem.fitness(), problem.codec());
    }

    /**
     * Create a new evolution {@code Engine.Builder} with the given fitness
     * function and genotype factory.
     *
     * @param ff              the fitness function
     * @param genotypeFactory the genotype factory
     * @param <G>             the gene type
     * @param <C>             the fitness function result type
     * @return a new engine builder
     * @throws java.lang.NullPointerException if one of the arguments is
     *                                        {@code null}.
     */
    public static <G extends Gene<?, G>, C extends Comparable<? super C>>
    CustomizedEngine.Builder<G, C> builder(
            final Function<? super Genotype<G>, ? extends C> ff,
            final Factory<Genotype<G>> genotypeFactory
    ) {
        return new CustomizedEngine.Builder<>(genotypeFactory, ff);
    }

    /**
     * Create a new evolution {@code Engine.Builder} with the given fitness
     * function and chromosome templates.
     *
     * @param ff          the fitness function
     * @param chromosome  the first chromosome
     * @param chromosomes the chromosome templates
     * @param <G>         the gene type
     * @param <C>         the fitness function result type
     * @return a new engine builder
     * @throws java.lang.NullPointerException if one of the arguments is
     *                                        {@code null}.
     */
    @SafeVarargs
    public static <G extends Gene<?, G>, C extends Comparable<? super C>>
    CustomizedEngine.Builder<G, C> builder(
            final Function<? super Genotype<G>, ? extends C> ff,
            final Chromosome<G> chromosome,
            final Chromosome<G>... chromosomes
    ) {
        return new CustomizedEngine.Builder<>(Genotype.of(chromosome, chromosomes), ff);
    }

    /**
     * Create a new evolution {@code Engine.Builder} with the given fitness
     * function and problem {@code codec}.
     *
     * @param ff    the fitness function
     * @param codec the problem codec
     * @param <T>   the fitness function input type
     * @param <C>   the fitness function result type
     * @param <G>   the gene type
     * @return a new engine builder
     * @throws java.lang.NullPointerException if one of the arguments is
     *                                        {@code null}.
     * @since 3.2
     */
    public static <T, G extends Gene<?, G>, C extends Comparable<? super C>>
    CustomizedEngine.Builder<G, C> builder(
            final Function<? super T, ? extends C> ff,
            final Codec<T, G> codec
    ) {
        return builder(ff.compose(codec.decoder()), codec.encoding());
    }


    /* *************************************************************************
     * Inner classes
     **************************************************************************/

    /**
     * Builder class for building GA {@code Engine} instances.
     *
     * @author <a href="mailto:franz.wilhelmstoetter@gmail.com">Franz Wilhelmstötter</a>
     * @version 4.0
     * @see Engine
     * @since 3.0
     */
    public static final class Builder<
            G extends Gene<?, G>,
            C extends Comparable<? super C>
            >
            implements Copyable<CustomizedEngine.Builder<G, C>> {

        // No default values for this properties.
        private Function<? super Genotype<G>, ? extends C> _fitnessFunction;
        private Factory<Genotype<G>> _genotypeFactory;

        // This are the properties which default values.
        private Function<? super C, ? extends C> _fitnessScaler = a -> a;
        private Selector<G, C> _survivorsSelector = new TournamentSelector<>(3);
        private Selector<G, C> _offspringSelector = new TournamentSelector<>(3);
        private Alterer<G, C> _alterer = Alterer.of(
                new SinglePointCrossover<G, C>(0.2),
                new Mutator<>(0.15)
        );
        private Predicate<? super Phenotype<G, C>> _validator = Phenotype::isValid;
        private Optimize _optimize = Optimize.MAXIMUM;
        private double _offspringFraction = 0.6;
        private int _populationSize = 50;
        private long _maximalPhenotypeAge = 70;

        // Engine execution environment.
        private Executor _executor = ForkJoinPool.commonPool();
        private Clock _clock = NanoClock.systemUTC();

        private int _individualCreationRetries = 10;
        private UnaryOperator<EvolutionResult<G, C>> _mapper = r -> r;

        private Builder(
                final Factory<Genotype<G>> genotypeFactory,
                final Function<? super Genotype<G>, ? extends C> fitnessFunction
        ) {
            _genotypeFactory = requireNonNull(genotypeFactory);
            _fitnessFunction = requireNonNull(fitnessFunction);
        }

        /**
         * Set the fitness function of the evolution {@code Engine}.
         *
         * @param function the fitness function to use in the GA {@code Engine}
         * @return {@code this} builder, for command chaining
         */
        public CustomizedEngine.Builder<G, C> fitnessFunction(
                final Function<? super Genotype<G>, ? extends C> function
        ) {
            _fitnessFunction = requireNonNull(function);
            return this;
        }

        /**
         * Set the fitness scaler of the evolution {@code Engine}. <i>Default
         * value is set to the identity function.</i>
         *
         * @param scaler the fitness scale to use in the GA {@code Engine}
         * @return {@code this} builder, for command chaining
         */
        public CustomizedEngine.Builder<G, C> fitnessScaler(
                final Function<? super C, ? extends C> scaler
        ) {
            _fitnessScaler = requireNonNull(scaler);
            return this;
        }

        /**
         * The genotype factory used for creating new individuals.
         *
         * @param genotypeFactory the genotype factory for creating new
         *                        individuals.
         * @return {@code this} builder, for command chaining
         */
        public CustomizedEngine.Builder<G, C> genotypeFactory(
                final Factory<Genotype<G>> genotypeFactory
        ) {
            _genotypeFactory = requireNonNull(genotypeFactory);
            return this;
        }

        /**
         * The selector used for selecting the offspring population. <i>Default
         * values is set to {@code TournamentSelector<>(3)}.</i>
         *
         * @param selector used for selecting the offspring population
         * @return {@code this} builder, for command chaining
         */
        public CustomizedEngine.Builder<G, C> offspringSelector(
                final Selector<G, C> selector
        ) {
            _offspringSelector = requireNonNull(selector);
            return this;
        }

        /**
         * The selector used for selecting the survivors population. <i>Default
         * values is set to {@code TournamentSelector<>(3)}.</i>
         *
         * @param selector used for selecting survivors population
         * @return {@code this} builder, for command chaining
         */
        public CustomizedEngine.Builder<G, C> survivorsSelector(
                final Selector<G, C> selector
        ) {
            _survivorsSelector = requireNonNull(selector);
            return this;
        }

        /**
         * The selector used for selecting the survivors and offspring
         * population. <i>Default values is set to
         * {@code TournamentSelector<>(3)}.</i>
         *
         * @param selector used for selecting survivors and offspring population
         * @return {@code this} builder, for command chaining
         */
        public CustomizedEngine.Builder<G, C> selector(final Selector<G, C> selector) {
            _offspringSelector = requireNonNull(selector);
            _survivorsSelector = requireNonNull(selector);
            return this;
        }

        /**
         * The alterers used for alter the offspring population. <i>Default
         * values is set to {@code new SinglePointCrossover<>(0.2)} followed by
         * {@code new Mutator<>(0.15)}.</i>
         *
         * @param first the first alterer used for alter the offspring
         *              population
         * @param rest  the rest of the alterers used for alter the offspring
         *              population
         * @return {@code this} builder, for command chaining
         * @throws java.lang.NullPointerException if one of the alterers is
         *                                        {@code null}.
         */
        @SafeVarargs
        public final CustomizedEngine.Builder<G, C> alterers(
                final Alterer<G, C> first,
                final Alterer<G, C>... rest
        ) {
            requireNonNull(first);
            Stream.of(rest).forEach(Objects::requireNonNull);

            _alterer = rest.length == 0
                    ? first
                    : Alterer.of(rest).compose(first);

            return this;
        }

        // MY CODE

        private GenerationProcess process;

        public final CustomizedEngine.Builder<G, C> process(
                final GenerationProcess process
        ) {
            this.process = process;
            return this;
        }


        // MY CODE END


        /**
         * The phenotype validator used for detecting invalid individuals.
         * Alternatively it is also possible to set the genotype validator with
         * {@link #genotypeFactory(Factory)}, which will replace any
         * previously set phenotype validators.
         *
         * <p><i>Default value is set to {@code Phenotype::isValid}.</i></p>
         *
         * @param validator the {@code validator} used for validating the
         *                  individuals (phenotypes).
         * @return {@code this} builder, for command chaining
         * @throws java.lang.NullPointerException if the {@code validator} is
         *                                        {@code null}.
         * @see #genotypeValidator(Predicate)
         * @since 3.1
         */
        public CustomizedEngine.Builder<G, C> phenotypeValidator(
                final Predicate<? super Phenotype<G, C>> validator
        ) {
            _validator = requireNonNull(validator);
            return this;
        }

        /**
         * The genotype validator used for detecting invalid individuals.
         * Alternatively it is also possible to set the phenotype validator with
         * {@link #phenotypeValidator(Predicate)}, which will replace any
         * previously set genotype validators.
         *
         * <p><i>Default value is set to {@code Genotype::isValid}.</i></p>
         *
         * @param validator the {@code validator} used for validating the
         *                  individuals (genotypes).
         * @return {@code this} builder, for command chaining
         * @throws java.lang.NullPointerException if the {@code validator} is
         *                                        {@code null}.
         * @see #phenotypeValidator(Predicate)
         * @since 3.1
         */
        public CustomizedEngine.Builder<G, C> genotypeValidator(
                final Predicate<? super Genotype<G>> validator
        ) {
            requireNonNull(validator);

            _validator = pt -> validator.test(pt.getGenotype());
            return this;
        }

        /**
         * The optimization strategy used by the engine. <i>Default values is
         * set to {@code Optimize.MAXIMUM}.</i>
         *
         * @param optimize the optimization strategy used by the engine
         * @return {@code this} builder, for command chaining
         */
        public CustomizedEngine.Builder<G, C> optimize(final Optimize optimize) {
            _optimize = requireNonNull(optimize);
            return this;
        }

        /**
         * Set to a fitness maximizing strategy.
         *
         * @return {@code this} builder, for command chaining
         * @since 3.4
         */
        public CustomizedEngine.Builder<G, C> maximizing() {
            return optimize(Optimize.MAXIMUM);
        }

        /**
         * Set to a fitness minimizing strategy.
         *
         * @return {@code this} builder, for command chaining
         * @since 3.4
         */
        public CustomizedEngine.Builder<G, C> minimizing() {
            return optimize(Optimize.MINIMUM);
        }

        /**
         * The offspring fraction. <i>Default values is set to {@code 0.6}.</i>
         * This method call is equivalent to
         * {@code survivorsFraction(1 - offspringFraction)} and will override
         * any previously set survivors-fraction.
         *
         * @param fraction the offspring fraction
         * @return {@code this} builder, for command chaining
         * @throws java.lang.IllegalArgumentException if the fraction is not
         *                                            within the range [0, 1].
         * @see #survivorsFraction(double)
         */
        public CustomizedEngine.Builder<G, C> offspringFraction(final double fraction) {
            _offspringFraction = probability(fraction);
            return this;
        }

        /**
         * The survivors fraction. <i>Default values is set to {@code 0.4}.</i>
         * This method call is equivalent to
         * {@code offspringFraction(1 - survivorsFraction)} and will override
         * any previously set offspring-fraction.
         *
         * @param fraction the survivors fraction
         * @return {@code this} builder, for command chaining
         * @throws java.lang.IllegalArgumentException if the fraction is not
         *                                            within the range [0, 1].
         * @see #offspringFraction(double)
         * @since 3.8
         */
        public CustomizedEngine.Builder<G, C> survivorsFraction(final double fraction) {
            _offspringFraction = 1.0 - probability(fraction);
            return this;
        }

        /**
         * The number of offspring individuals.
         *
         * @param size the number of offspring individuals.
         * @return {@code this} builder, for command chaining
         * @throws java.lang.IllegalArgumentException if the size is not
         *                                            within the range [0, population-size].
         * @since 3.8
         */
        public CustomizedEngine.Builder<G, C> offspringSize(final int size) {
            if (size < 0) {
                throw new IllegalArgumentException(format(
                        "Offspring size must be greater or equal zero, but was %s.",
                        size
                ));
            }

            return offspringFraction((double) size / (double) _populationSize);
        }

        /**
         * The number of survivors.
         *
         * @param size the number of survivors.
         * @return {@code this} builder, for command chaining
         * @throws java.lang.IllegalArgumentException if the size is not
         *                                            within the range [0, population-size].
         * @since 3.8
         */
        public CustomizedEngine.Builder<G, C> survivorsSize(final int size) {
            if (size < 0) {
                throw new IllegalArgumentException(format(
                        "Survivors must be greater or equal zero, but was %s.",
                        size
                ));
            }

            return survivorsFraction((double) size / (double) _populationSize);
        }

        /**
         * The number of individuals which form the population. <i>Default
         * values is set to {@code 50}.</i>
         *
         * @param size the number of individuals of a population
         * @return {@code this} builder, for command chaining
         * @throws java.lang.IllegalArgumentException if {@code size < 1}
         */
        public CustomizedEngine.Builder<G, C> populationSize(final int size) {
            if (size < 1) {
                throw new IllegalArgumentException(format(
                        "Population size must be greater than zero, but was %s.",
                        size
                ));
            }
            _populationSize = size;
            return this;
        }

        /**
         * The maximal allowed age of a phenotype. <i>Default values is set to
         * {@code 70}.</i>
         *
         * @param age the maximal phenotype age
         * @return {@code this} builder, for command chaining
         * @throws java.lang.IllegalArgumentException if {@code age < 1}
         */
        public CustomizedEngine.Builder<G, C> maximalPhenotypeAge(final long age) {
            if (age < 1) {
                throw new IllegalArgumentException(format(
                        "Phenotype age must be greater than one, but was %s.", age
                ));
            }
            _maximalPhenotypeAge = age;
            return this;
        }

        /**
         * The executor used by the engine.
         *
         * @param executor the executor used by the engine
         * @return {@code this} builder, for command chaining
         */
        public CustomizedEngine.Builder<G, C> executor(final Executor executor) {
            _executor = requireNonNull(executor);
            return this;
        }

        /**
         * The clock used for calculating the execution durations.
         *
         * @param clock the clock used for calculating the execution durations
         * @return {@code this} builder, for command chaining
         */
        public CustomizedEngine.Builder<G, C> clock(final Clock clock) {
            _clock = requireNonNull(clock);
            return this;
        }

        /**
         * The maximal number of attempt before the {@code Engine} gives up
         * creating a valid individual ({@code Phenotype}). <i>Default values is
         * set to {@code 10}.</i>
         *
         * @param retries the maximal retry count
         * @return {@code this} builder, for command chaining
         * @throws IllegalArgumentException if the given retry {@code count} is
         *                                  smaller than zero.
         * @since 3.1
         */
        public CustomizedEngine.Builder<G, C> individualCreationRetries(final int retries) {
            if (retries < 0) {
                throw new IllegalArgumentException(format(
                        "Retry count must not be negative: %d",
                        retries
                ));
            }
            _individualCreationRetries = retries;
            return this;
        }

        /**
         * The result mapper, which allows to change the evolution result after
         * each generation.
         *
         * @param mapper the evolution result mapper
         * @return {@code this} builder, for command chaining
         * @throws NullPointerException if the given {@code resultMapper} is
         *                              {@code null}
         * @see EvolutionResult#toUniquePopulation()
         * @since 4.0
         */
        public CustomizedEngine.Builder<G, C> mapping(
                final Function<
                        ? super EvolutionResult<G, C>,
                        EvolutionResult<G, C>
                        > mapper
        ) {
            _mapper = requireNonNull(mapper::apply);
            return this;
        }

        /**
         * Builds an new {@code Engine} instance from the set properties.
         *
         * @return an new {@code Engine} instance from the set properties
         */
        public CustomizedEngine<G, C> build() {
            return new CustomizedEngine<>(
                    _fitnessFunction,
                    _genotypeFactory,
                    _fitnessScaler,
                    _survivorsSelector,
                    _offspringSelector,
                    _alterer,
                    _validator,
                    _optimize,
                    getOffspringCount(),
                    getSurvivorsCount(),
                    _maximalPhenotypeAge,
                    _executor,
                    _clock,
                    _individualCreationRetries,
                    _mapper,
                    process
            );
        }

        private int getSurvivorsCount() {
            return _populationSize - getOffspringCount();
        }

        private int getOffspringCount() {
            return (int) round(_offspringFraction * _populationSize);
        }

        /**
         * Return the used {@link Alterer} of the GA.
         *
         * @return the used {@link Alterer} of the GA.
         */
        public Alterer<G, C> getAlterers() {
            return _alterer;
        }

        /**
         * Return the {@link Clock} the engine is using for measuring the execution
         * time.
         *
         * @return the clock used for measuring the execution time
         * @since 3.1
         */
        public Clock getClock() {
            return _clock;
        }

        /**
         * Return the {@link Executor} the engine is using for executing the
         * evolution steps.
         *
         * @return the executor used for performing the evolution steps
         * @since 3.1
         */
        public Executor getExecutor() {
            return _executor;
        }

        /**
         * Return the fitness function of the GA engine.
         *
         * @return the fitness function
         * @since 3.1
         */
        public Function<? super Genotype<G>, ? extends C> getFitnessFunction() {
            return _fitnessFunction;
        }

        /**
         * Return the fitness scaler of the GA engine.
         *
         * @return the fitness scaler
         * @since 3.1
         */
        public Function<? super C, ? extends C> getFitnessScaler() {
            return _fitnessScaler;
        }

        /**
         * Return the used genotype {@link Factory} of the GA. The genotype factory
         * is used for creating the initial population and new, random individuals
         * when needed (as replacement for invalid and/or died genotypes).
         *
         * @return the used genotype {@link Factory} of the GA.
         * @since 3.1
         */
        public Factory<Genotype<G>> getGenotypeFactory() {
            return _genotypeFactory;
        }

        /**
         * Return the maximal allowed phenotype age.
         *
         * @return the maximal allowed phenotype age
         * @since 3.1
         */
        public long getMaximalPhenotypeAge() {
            return _maximalPhenotypeAge;
        }

        /**
         * Return the offspring fraction.
         *
         * @return the offspring fraction.
         */
        public double getOffspringFraction() {
            return _offspringFraction;
        }

        /**
         * Return the used offspring {@link Selector} of the GA.
         *
         * @return the used offspring {@link Selector} of the GA.
         * @since 3.1
         */
        public Selector<G, C> getOffspringSelector() {
            return _offspringSelector;
        }

        /**
         * Return the used survivor {@link Selector} of the GA.
         *
         * @return the used survivor {@link Selector} of the GA.
         * @since 3.1
         */
        public Selector<G, C> getSurvivorsSelector() {
            return _survivorsSelector;
        }

        /**
         * Return the optimization strategy.
         *
         * @return the optimization strategy
         * @since 3.1
         */
        public Optimize getOptimize() {
            return _optimize;
        }

        /**
         * Return the number of individuals of a population.
         *
         * @return the number of individuals of a population
         * @since 3.1
         */
        public int getPopulationSize() {
            return _populationSize;
        }

        /**
         * Return the maximal number of attempt before the {@code Engine} gives
         * up creating a valid individual ({@code Phenotype}).
         *
         * @return the maximal number of {@code Phenotype} creation attempts
         * @since 3.1
         */
        public int getIndividualCreationRetries() {
            return _individualCreationRetries;
        }

        /**
         * Return the evolution result mapper.
         *
         * @return the evolution result mapper
         * @since 4.0
         */
        public UnaryOperator<EvolutionResult<G, C>> getMapper() {
            return _mapper;
        }

        /**
         * Create a new builder, with the current configuration.
         *
         * @return a new builder, with the current configuration
         * @since 3.1
         */
        @Override
        public CustomizedEngine.Builder<G, C> copy() {
            return new CustomizedEngine.Builder<G, C>(_genotypeFactory, _fitnessFunction)
                    .alterers(_alterer)
                    .clock(_clock)
                    .executor(_executor)
                    .fitnessScaler(_fitnessScaler)
                    .maximalPhenotypeAge(_maximalPhenotypeAge)
                    .offspringFraction(_offspringFraction)
                    .offspringSelector(_offspringSelector)
                    .phenotypeValidator(_validator)
                    .optimize(_optimize)
                    .populationSize(_populationSize)
                    .survivorsSelector(_survivorsSelector)
                    .individualCreationRetries(_individualCreationRetries)
                    .mapping(_mapper);
        }

    }

}
