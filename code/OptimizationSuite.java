import java.io.File;
import java.io.FileWriter;

class OptimizationSuite {

    // flags specifying which algorithm we are optimizing parameters for
    private final byte PSO = 1;
    private final byte DE = 2;
    private final byte BB = 3;
    private final int GRANULARITY = 15;
    public int SIMULATIONS = 3;

    public double bestResult;

    // the list of parameters that we would like to optimize
    public Vector bestParamConfig;

    // the benchmark function we are optimizing with respect to
    public byte benchFlag;
    
    // a flag that will specify which algorithm to optimize (DE, BB, PSO)
    public byte algoFlag;

    public OptimizationSuite(byte benchFlag, byte algoFlag) {
        this.algoFlag = algoFlag;
        this.benchFlag = benchFlag;
        this.bestResult = Double.MAX_VALUE;
    }


    public void train() throws Exception {
        switch (algoFlag) {
            case PSO:
                trainPSO();
                return;
            case DE:
                trainDE();
                return;
            case BB:
                // trainBB();
                break;
            default:
                System.out.printf("%sAlgo-Flag (%d) not recognized%s\n", Utilities.RED, algoFlag, Utilities.RESET);
                System.exit(1);
        }
    }

    /**
     * Run the training procedure for Inertia-weight particle swarm optimization.
     * 
     * The parameters we need to optimize for this algorithm are the inertia weight component (w), 
     * and the two update component scale coefficients (c1 and c2). 
     * 
     * (w) is strictly in the interval [0,1] and we will sample c1,c2 on the interval [0,2] provided
     * that the poli-stability conditions derived for this algorithm are satisfied.
     * 
     */
    private void trainPSO() throws Exception {
        
        double wBound = 1;
        double cBound = 2;
        // w possibilities
        int count = 0;
        for (double w = 0; w < wBound; w += wBound/GRANULARITY) {
            for (double c1 = 0; c1 < cBound; c1 += cBound/GRANULARITY) {
                for (double c2 = 0; c2 < cBound; c2 += cBound/GRANULARITY) {
                    if (!stablePSO(w, c1, c2)) continue;
                    // this configuration is stable, lets test it.
                    double[] config = {w, c1, c2};
                    double avgResult = 0.0;
                    for (int i = 0; i < SIMULATIONS; i++) {
                        PSO_Swarm swarm = new PSO_Swarm(config, RunConfig.PARTICLES, RunConfig.DIM, algoFlag);
                        for (int iter = 0; iter < RunConfig.ITERATIONS; iter++)
                            swarm.doUpdate();
                        double result = swarm.gBestEval;
                        avgResult += result;                        
                    }
                    avgResult /= 3;
                    // check if this setting is worth saving
                    if (avgResult < this.bestResult) {
                        Vector best = new Vector(config);
                        System.out.printf("%s%s%s Found %s%s%s which yields %s%7f%s < %s%7f%s\n", 
                            Utilities.BLUE, SingleObjectiveFunctions.getName(benchFlag), Utilities.RESET,
                            Utilities.YELLOW, best.toString().replaceAll("\n", ""), Utilities.RESET, Utilities.GREEN,
                            avgResult, Utilities.RESET, Utilities.RED, this.bestResult, Utilities.RESET
                        );
                        this.bestResult = avgResult;
                        this.bestParamConfig = best;
                    }
                    count++;
                    System.out.printf("%s%s:%d/%d%s\n", Utilities.YELLOW, SingleObjectiveFunctions.getName(benchFlag), count, 15*15*15, Utilities.RESET);
                }
            }
            System.out.printf("%s%s%s Writing to File\n", Utilities.YELLOW, SingleObjectiveFunctions.getName(benchFlag), Utilities.RESET);  
            toFile();
        }   
        System.out.printf("\n\n%sDONE:%s\tThe Best Setting for %s%s%s is %s%s\n", Utilities.GREEN, Utilities.RESET, 
        Utilities.GREEN, SingleObjectiveFunctions.getName(this.benchFlag), Utilities.RESET, Utilities.YELLOW, this.bestParamConfig.toString().replaceAll("\n", ""),Utilities.RESET);

        // write this result to a file?
        toFile();
    }

    private void trainDE() throws Exception {
             
        double fBound = 1;
        double crBound = 2;
        // w possibilities
        int count = 0;
        for (double f = 0; f < fBound; f += fBound/(2*GRANULARITY)) {
            for (double cr = 0; cr < crBound; cr += crBound/(2*GRANULARITY)) {
                    // this configuration is stable, lets test it.
                    double[] config = {f, cr};
                    double avgResult = 0.0;
                    for (int i = 0; i < SIMULATIONS; i++) {
                        DE_Population pop = new DE_Population(config, RunConfig.PARTICLES, RunConfig.DIM, algoFlag);
                        for (int iter = 0; iter < RunConfig.ITERATIONS; iter++)
                            pop.doIteration();
                        double result = pop.bestEval;
                        avgResult += result;                        
                    }
                    avgResult /= 3;
                    // check if this setting is worth saving
                    if (avgResult < this.bestResult) {
                        Vector best = new Vector(config);
                        System.out.printf("%s%s%s Found %s%s%s which yields %s%7f%s < %s%7f%s\n", 
                            Utilities.BLUE, SingleObjectiveFunctions.getName(benchFlag), Utilities.RESET,
                            Utilities.YELLOW, best.toString().replaceAll("\n", ""), Utilities.RESET, Utilities.GREEN,
                            avgResult, Utilities.RESET, Utilities.RED, this.bestResult, Utilities.RESET
                        );
                        this.bestResult = avgResult;
                        this.bestParamConfig = best;
                    }
                    count++;
                    System.out.printf("%s%s:%d/%d%s\n", Utilities.YELLOW, SingleObjectiveFunctions.getName(benchFlag), count, 30*30, Utilities.RESET);
            }
            System.out.printf("%s%s%s Writing to File\n", Utilities.YELLOW, SingleObjectiveFunctions.getName(benchFlag), Utilities.RESET);  
            toFile();
        }   
        System.out.printf("\n\n%sDONE:%s\tThe Best Setting for %s%s%s is %s%s\n", Utilities.GREEN, Utilities.RESET, 
        Utilities.GREEN, SingleObjectiveFunctions.getName(this.benchFlag), Utilities.RESET, Utilities.YELLOW, this.bestParamConfig.toString().replaceAll("\n", ""),Utilities.RESET);

        // write this result to a file?
        toFile();
    

    }


    // private void trainBB() throws Exception {
             
    //     double kBound = 0.1;
    //     // w possibilities
    //     int count = 0;
    //     for (double k = kBound/(6*GRANULARITY); k < kBound; k += kBound/(6*GRANULARITY)) {
    //                 // this configuration is stable, lets test it.
    //                 double avgResult = 0.0;
    //                 for (int i = 0; i < SIMULATIONS; i++) {
    //                     BB_BC_Population pop = new BB_BC_Population(RunConfig.PARTICLES, RunConfig.DIM, algoFlag, k);
    //                     for (int iter = 0; iter < RunConfig.ITERATIONS; iter++)
    //                         pop.doIteration(iter);
    //                     double result = SingleObjectiveFunctions.evaluate(pop.centroid, algoFlag);
    //                     avgResult += result;                        
    //                 }
    //                 avgResult /= 3;
    //                 // check if this setting is worth saving
    //                 if (avgResult < this.bestResult) {
    //                     double[] dat = {k}; 
    //                     Vector best = new Vector(dat);
    //                     System.out.printf("%s%s%s Found %s%s%s which yields %s%7f%s < %s%7f%s\n", 
    //                         Utilities.BLUE, SingleObjectiveFunctions.getName(benchFlag), Utilities.RESET,
    //                         Utilities.YELLOW, best.toString().replaceAll("\n", ""), Utilities.RESET, Utilities.GREEN,
    //                         avgResult, Utilities.RESET, Utilities.RED, this.bestResult, Utilities.RESET
    //                     );
    //                     this.bestResult = avgResult;
    //                     this.bestParamConfig = best;
    //                 }
    //                 count++;
    //                 System.out.printf("%s%s:%d/%d%s\n", Utilities.YELLOW, SingleObjectiveFunctions.getName(benchFlag), count, 6 * GRANULARITY, Utilities.RESET);
    //     }   
    //     System.out.printf("\n\n%sDONE:%s\tThe Best Setting for %s%s%s is %s%s\n", Utilities.GREEN, Utilities.RESET, 
    //     Utilities.GREEN, SingleObjectiveFunctions.getName(this.benchFlag), Utilities.RESET, Utilities.YELLOW, this.bestParamConfig.toString().replaceAll("\n", ""),Utilities.RESET);

    //     // write this result to a file?
    //     toFile();
    

    // }


    private void toFile() {
        String algo;
        if (algoFlag == 1)
            algo = "pso";
        else if (algoFlag == 2)
            algo = "de";
        else 
            algo = "bbmod";
        try {
            FileWriter f = new FileWriter(new File(String.format("../params/%s/%s_%s.tuned", algo, SingleObjectiveFunctions.getName(this.benchFlag), algo)));
            String output =  "";
            for (int i = 0; i < this.bestParamConfig.size(); i++) {
                output += this.bestParamConfig.atIndex(i);
                if (i < this.bestParamConfig.size() - 1)
                    output += ",";
            }
            f.write(output);
            f.close();
        } catch (Exception e) {
            System.out.printf("%sFILE NOT FOUND%s\n", Utilities.RED, Utilities.RESET);
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * The poli-stability equation for inertia-weight particle swarm optimization. If a control parameter vector
     * satisfy this stability condition, it has been shown that the particle velocity will converge to 0.
     *          (c1 + c2) <  [ 24(1 - w^2) ]  /  [7 - 5w]
     * @param setting : a control configuration vector in the order (w, c1, c2)
     * @return true if the condition is satisfied, false otherwise
     */
    private boolean stablePSO(double w, double c1, double c2) {

        return (
            (c1 + c2)  < ((24 * (1 - Math.pow(w, 2))) / (7 - 5 * w))
        );

    }






}