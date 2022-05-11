public class RunConfig {
    
    // constant to specify population size 
    public static final int PARTICLES = 30;

    // constant to specify the dimension of the search space
    public static final int DIM = 10;

    // constant to sepcify how many iterations should be run
    public static final int ITERATIONS = 5000;
    
    // constant to specify how often quality measurements are evaluated
    public static final int GRANULARITY = 5;

    // F and CR settings for Differential Evolution to use by default
    public static final double[] DE_PARAMS = {0.5, 0.5};
   
    // W C1 and C2 settings for Particle Swarm Optimization to use by default
    public static final double[] PSO_PARAMS = {0.7, 1.4, 1.4};

    // Number of independent trials on which to base our observations
    public static final int TRIALS = 20;
}
