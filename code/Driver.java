public class Driver {

    public static byte FLAG;

    /**
     * Run standard Intertia Weight PSO
     *      This function runs the entire PSO evaluation procedure for 1 independent trial, using  
     *      the control parameter configurations specified in the method body. These configurations
     *      have been shown to (1) adhere to derived poli-stability conditions, and (2) yield
     *      favorable performance.
     * 
     * @param iterations Number of iterations particles may explore
     * @param particles Number of particles participating in exploration
     * @param dims Dimension of the search space
     * @param flag The flag used to specify which benchmark function should be optimized
     * @throws Exception If Vector.java is misused
     */
    public static void runPSO(int iterations, int particles, int dims, byte flag) throws Exception {
        PSO_Swarm swarm = new PSO_Swarm(RunConfig.PSO_PARAMS, particles, dims, flag);
        for (int iter = 0; iter <= iterations; iter++) {
            swarm.doUpdate();
        }
        System.out.println(SingleObjectiveFunctions.getName(flag) + " PSO found the solution: " + swarm.gBestEval + " for vector\n" + swarm.gBestVec);
    }

    /**
     * Runs DE/Rand/1/BIN Differential Evolution
     *      This function runs the entire DE evaluation procedure for 1 independent trial, using
     *      the control parameter configurations specified in the method body. Although these 
     *      configurations aren't optimized, they seem to work well across a broad variety of benchmark functions.
     *      Optimization of these configurations may be helpful.
     * 
     * @param iterations Number of iterations particles may explore
     * @param particles Number of particles participating in exploration
     * @param dims Dimension of the search space
     * @param flag The flag used to specify which benchmark function should be optimized
     * @throws Exception If Vector.java is misused
     */
    public static void runDE(int iterations, int particles, int dims, byte flag) throws Exception {

        DE_Population population = new DE_Population(RunConfig.DE_PARAMS, particles, dims, flag);
        for (int iter = 0; iter <= iterations; iter++) {
            population.doIteration();
        }
        System.out.println(SingleObjectiveFunctions.getName(flag) + " DE found the solution: " + population.bestEval + " for vector\n" + population.best);
    }


    /**
     * Simulates Differential Evolution over Independent trials. 
     *      Configurations for the simulation procedure are found in RunConfig.java
     * @param flag
     */
    public static void simulateDE(byte flag) throws Exception {
        
        // init data storage structure if record mode is specified in RunConfig. The Structure is ROWS:Trial x COL:Iteration_of_trial
        double[][] data = (RunConfig.RECORD_MODE) ? new double[RunConfig.TRIALS][RunConfig.ITERATIONS / RunConfig.GRANULARITY + 1] : null;
       
        // simulate DE over independent trials, collect the results into a single structure
        for (int trial = 0; trial < RunConfig.TRIALS; trial++) {

            System.out.printf("%s%s %sRunning Differential Evolution Trial: %s%d%s\n", Utilities.RED, SingleObjectiveFunctions.getName(FLAG), Utilities.BLUE, Utilities.YELLOW, trial + 1, Utilities.RESET);
            //set up the population independently, for each trial
            DE_Population population = new DE_Population(RunConfig.DE_PARAMS, RunConfig.PARTICLES, RunConfig.DIM, flag);
            for (int iter = 0; iter <= RunConfig.ITERATIONS; iter++) {
          
                // do a DE generation update
                population.doIteration();
                
                // write data to structure if specified
                if (iter % RunConfig.GRANULARITY == 0 && RunConfig.RECORD_MODE) {
                    data[trial][iter/RunConfig.GRANULARITY] = population.bestEval;
                }

            }

        }
         // write the recorded data to files if specified by RunConfig.java
         Utilities.writeFile(data, "de_raw", flag);
         Utilities.writeFile(Utilities.averageAtTime(data), "de_avg", flag);
         Utilities.writeFile(Utilities.varAtTime(data), "de_var", flag);

    }

    /**
     * Simulates Particle Swarm Optimization over Independent trials. 
     *      Configurations for the simulation procedure are found in RunConfig.java
     * @param flag
     */
    public static void simulatePSO(byte flag) throws Exception {
        
        // init data storage structure if record mode is specified in RunConfig. The Structure is ROWS:Trial x COL:Iteration_of_trial
        double[][] data = (RunConfig.RECORD_MODE) ? new double[RunConfig.TRIALS][RunConfig.ITERATIONS / RunConfig.GRANULARITY + 1] : null;

        // simulate DE over independent trials, collect the results into a single structure
        for (int trial = 0; trial < RunConfig.TRIALS; trial++) {

            System.out.printf("%s%s %s Running PSO Trial: %s%d%s\n", Utilities.RED, SingleObjectiveFunctions.getName(FLAG), Utilities.BLUE, Utilities.YELLOW, trial + 1, Utilities.RESET);
            // set up the population independently, for each trial
            PSO_Swarm swarm = new PSO_Swarm(RunConfig.PSO_PARAMS, RunConfig.PARTICLES, RunConfig.DIM, flag);
            for (int iter = 0; iter <= RunConfig.ITERATIONS; iter++) {
               
                // do a PSO procedure update
                swarm.doUpdate();

                // write data to structure if specified
                if (iter % RunConfig.GRANULARITY == 0 && RunConfig.RECORD_MODE) {
                    data[trial][iter/RunConfig.GRANULARITY] = swarm.gBestEval;
                }
            }
        }
        // write the recorded data to files if specified by RunConfig.java
        Utilities.writeFile(data, "pso_raw", flag);
        Utilities.writeFile(Utilities.averageAtTime(data), "pso_avg", flag);
        Utilities.writeFile(Utilities.varAtTime(data), "pso_var", flag);
    }


    /**
     * Short Example driver into the Program usage.
     * 
     * @param args [  (byte) FLAG   ]
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {

        // Used to specify a benchmark function to be optimized
        FLAG = Byte.parseByte(args[0]);
        simulateDE(FLAG);
        simulatePSO(FLAG);

    }

}
