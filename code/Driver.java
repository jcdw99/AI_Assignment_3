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
        // control params in order [w, c1, c2]
        double[] control = {0.7, 1.4, 1.4};
        PSO_Swarm swarm = new PSO_Swarm(control, particles, dims, flag);
        for (int iter = 0; iter < iterations; iter++) {
            swarm.doUpdate();
        }
        System.out.println("PSO found the solution: " + SingleObjectiveFunctions.evaluate(swarm.findGBest(), flag) + " for vector\n" + swarm.findGBest());
        System.out.println("In domain? " + SingleObjectiveFunctions.inDomain(swarm.findGBest(), flag));
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
        // control params in order [F, CR]
        double[] control = {0.5, 0.5};
        GE_Population population = new GE_Population(control, particles, dims, flag);
        for (int iter = 0; iter < iterations; iter++) {
            population.doIteration();
        }
        System.out.println("DE found the solution: " + population.bestEval + " for vector\n" + population.best);
        System.out.println("In domain? " + SingleObjectiveFunctions.inDomain(population.best, flag));
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
