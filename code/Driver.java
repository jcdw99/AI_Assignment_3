import java.io.File;
import java.util.Scanner;
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
     * Runs Big Bang Big Crunch as proposed by the literature
     *      This function runs the entire BigBagBigCrunch evaluation procedure for 1 independent trial, using
     *      the control parameter configurations specified in the RunConfig.java.
     * 
     * @param iterations Number of iterations particles may explore
     * @param particles Number of particles participating in exploration
     * @param dims Dimension of the search space
     * @param flag The flag used to specify which benchmark function should be optimized
     * @throws Exception If Vector.java is misused
     */
    public static void runBB(int iterations, int particles, int dims, byte flag, boolean orig) throws Exception {
        BB_BC_Population population = new BB_BC_Population(particles, dims, flag, orig);
        for (int iter = 0; iter <= iterations; iter++) {
            population.doIteration(iter);
            System.out.println(SingleObjectiveFunctions.getName(flag) + " BBBC found the solution: " + SingleObjectiveFunctions.evaluate(population.centroid, FLAG) + " for vector\n" + population.centroid);

        }
        // System.out.println(SingleObjectiveFunctions.getName(flag) + " BBBC found the solution: " + SingleObjectiveFunctions.evaluate(population.centroid, FLAG) + " for vector\n" + population.centroid);
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

            System.out.printf("%s%s %sRunning Differential Evolution Trial: %s%d%s\n", Utilities.RED, SingleObjectiveFunctions.getName(FLAG), Utilities.WHITE, Utilities.YELLOW, trial + 1, Utilities.RESET);
            // set up the population independently, for each trial

            // check RunConfig field, if true we should use tuned values, else use default.
            double[] params = (RunConfig.OPTIMIZED) ? readControlParams((byte) 2, flag): RunConfig.PSO_PARAMS;

            DE_Population population = new DE_Population(params, RunConfig.PARTICLES, RunConfig.DIM, flag);
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

            System.out.printf("%s%s %s Running PSO Trial: %s%d%s\n", Utilities.RED, SingleObjectiveFunctions.getName(FLAG), Utilities.WHITE, Utilities.YELLOW, trial + 1, Utilities.RESET);
                        
            
            // check RunConfig field, if true we should use tuned values, else use default.
            double[] params = (RunConfig.OPTIMIZED) ? readControlParams((byte) 1, flag): RunConfig.PSO_PARAMS;
            
            // set up the population independently, for each trial
            PSO_Swarm swarm = new PSO_Swarm(params, RunConfig.PARTICLES, RunConfig.DIM, flag);
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
     * Reads the tuned control parameter configuration file from the ../params directory
     * 
     * @param algoFlag flag which specifies either PSO or DE to be used
     * @param funcflag flag which specifies the benchmark function.
     * @return
     * @throws Exception
     */
    public static double[] readControlParams(byte algoFlag, byte funcflag) throws Exception {
        String algo = (algoFlag == 1) ? "pso":"de";
        String filePath = String.format("../params/%s/%s_%s.tuned", algo, SingleObjectiveFunctions.getName(funcflag), algo);
        Scanner sc = new Scanner(new File(filePath));
        String[] data = sc.nextLine().split(",");
        double[] params = new double[data.length];
        for (int i = 0; i < params.length; i++)
            params[i] = Double.parseDouble(data[i]);
       return params;
    }

    /**
     * Simulates Big Bang Big Crunch over Independent trials. 
     *      Configurations for the simulation procedure are found in RunConfig.java
     * @param flag
     */
    public static void simulateBB(byte flag, boolean orig) throws Exception {
        
        // init data storage structure if record mode is specified in RunConfig. The Structure is ROWS:Trial x COL:Iteration_of_trial
        double[][] data = (RunConfig.RECORD_MODE) ? new double[RunConfig.TRIALS][RunConfig.ITERATIONS / RunConfig.GRANULARITY + 1] : null;

        // simulate DE over independent trials, collect the results into a single structure
        for (int trial = 0; trial < RunConfig.TRIALS; trial++) {

            String version = (orig) ? "Original": "Modified";
            System.out.printf("%s%s %s Running %s BB Trial: %s%d%s\n", Utilities.RED, SingleObjectiveFunctions.getName(FLAG), Utilities.WHITE, version, Utilities.YELLOW, trial + 1, Utilities.RESET);
            // set up the population independently, for each trial
            BB_BC_Population population = new BB_BC_Population(RunConfig.PARTICLES, RunConfig.DIM, flag, orig);
            for (int iter = 0; iter <= RunConfig.ITERATIONS; iter++) {
               
                // do a PSO procedure update
                population.doIteration(iter);

                // write data to structure if specified
                if (iter % RunConfig.GRANULARITY == 0 && RunConfig.RECORD_MODE) {
                    data[trial][iter/RunConfig.GRANULARITY] = SingleObjectiveFunctions.evaluate(population.centroid, FLAG);
                }
            }
        }
        // write the recorded data to files if specified by RunConfig.java
        Utilities.writeFile(data, String.format("bb_%s_raw", (orig) ? "orig": "mod"), flag);
        Utilities.writeFile(Utilities.averageAtTime(data), String.format("bb_%s_avg", (orig) ? "orig": "mod"), flag);
        Utilities.writeFile(Utilities.varAtTime(data), String.format("bb_%s_var", (orig) ? "orig": "mod"), flag);
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
        simulateBB(FLAG, true);
        simulateBB(FLAG, false);
    }

}
