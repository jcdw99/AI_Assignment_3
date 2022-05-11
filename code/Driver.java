public class Driver {

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
        for (int iter = 0; iter < iterations; iter++) {
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

        GE_Population population = new GE_Population(RunConfig.DE_PARAMS, particles, dims, flag);
        for (int iter = 0; iter < iterations; iter++) {
            population.doIteration();
        }
        System.out.println(SingleObjectiveFunctions.getName(flag) + " DE found the solution: " + population.bestEval + " for vector\n" + population.best);
    }


    /**
     * Simulates Differential Evolution over Independent trials. 
     *      Configurations for the simulation procedure are found in RunConfig.java
     * @param flag
     */
    public static void simulateDE(byte flag) {
        // simulate DE over independent trials, collect the results into a single structure
        for (int trial = 0; trial < RunConfig.TRIALS; trial++) {

        }
    }

    /**
     * Simulates Particle Swarm Optimization over Independent trials. 
     *      Configurations for the simulation procedure are found in RunConfig.java
     * @param flag
     */
    public static void simulatePSO(byte flag) {
        // simulate DE over independent trials, collect the results into a single structure
        for (int trial = 0; trial < RunConfig.TRIALS; trial++) {

        }
    }

    /**
     * Short Example driver into the Program usage.
     * 
     * @param args [  (byte) FLAG   ]
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {

        // Used to specify a benchmark function to be optimized
        Byte FLAG = Byte.parseByte(args[0]);

        runPSO(RunConfig.ITERATIONS, RunConfig.PARTICLES, RunConfig.DIM, FLAG);
        runDE(RunConfig.ITERATIONS, RunConfig.PARTICLES, RunConfig.DIM, FLAG);
    }

}
