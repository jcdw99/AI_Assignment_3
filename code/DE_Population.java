public class DE_Population {

    public Vector[] population;
    public Vector best;
    public double bestEval;
    private int dimension;
    private byte FLAG;
    private double F;
    private double CR;


    /**
     * Initialize GE_Population
     *      This is achieved by initializing a collection of vectors randomly disperesed in the solution space.
     *      After the vectors are initialized, the "Most fit" is selected using the provided fitness function.
     *      The fitness function to be used is specified by the flag argument, and the Functions Class.
     *    
     * @param size The size of the population
     * @param dim The dimension of each vector in the population
     * @param flag The flag denoting which objective function to minimize
     * @param F The weighted difference coefficient
     * @param CR the crossover probability coefficient
     * @throws Exception
     */
    public DE_Population(double[] params, int size, int dim, byte flag)  throws Exception {
        // set global instance parameters 
        this.dimension = dim;
        this.F = params[0];
        this.CR = params[1];
        this.FLAG = flag;

        // initialize population structure, and vectors inside structure
        this.population = new Vector[size];
        Vector domain = SingleObjectiveFunctions.getDomain(flag);
        for (int i = 0; i < size; i++) {
            this.population[i] = Vector.randBetween(domain.atIndex(0), domain.atIndex(1), dim);
        }

        // search for most fit individual
        int bestDex = 0;
        double bestEval = SingleObjectiveFunctions.evaluate(this.population[bestDex], flag);
        for (int i = 1; i < size; i++) {
            double thisEval = SingleObjectiveFunctions.evaluate(this.population[i], flag);
            if (thisEval < bestEval) {
                bestEval = thisEval;
                bestDex = i;
            }
        }
        this.bestEval = bestEval;
        this.best = population[bestDex].duplicate();
    }


    /**
     * Do a single iteration of the simulation procedure, iterate all particles and try to beneficially crossover
     */
    public void doIteration() throws Exception {

        // for each population member
        for (int memdex = 0; memdex < population.length; memdex++) {
            
            // first we should choose 3 unique indicies on interval [0,(N-1)] 
            int r1, r2, r3;
            do {
                r1 = (int) (Math.random() * population.length);
                r2 = (int) (Math.random() * population.length);
                r3 = (int) (Math.random() * population.length);
            } while (r1 == r2 || r2 == r3 || r1 == r3);

            // now that we have 3 unique indicies, lets get the crossover version of these population members
            // Step 1, save old member, perform crossover to get new member
            Vector oldVersion = population[memdex].duplicate();
            Vector newVersion = crossOver(oldVersion.duplicate(), population[r1], population[r2], population[r3]);

            // Step 2, get evaluations of old and new members
            double oldEval = SingleObjectiveFunctions.evaluate(oldVersion, FLAG);
            double newEval = SingleObjectiveFunctions.evaluate(newVersion, FLAG);

            // Step 3, Hill climb- lets see if the new member is superiour to the old member
            // if the new member is superiour, overwrite the old member, else: keep the old member
            population[memdex] = (newEval < oldEval) ? newVersion.duplicate() : oldVersion.duplicate();

            // Step 4, we need to check if this member is a new all time best
            best = (newEval < bestEval) ? newVersion.duplicate(): best;
            bestEval = (newEval < bestEval) ? newEval: bestEval;

        }
    }

    /**
     * Perform crossover as described in the paper by Rainer Storn and Kennith Price
     * @param orig
     * @param a
     * @param b
     * @param c
     * @return
     * @throws Exception
     */
    public Vector crossOver(Vector orig, Vector a, Vector b, Vector c) throws Exception {
        
        int n = (int) (Math.random() * dimension);
        Vector bounds = SingleObjectiveFunctions.getDomain(FLAG);
        // change counter
        int l = 0;
        do {
            // get the crossOver value for this index
            double tryCrossover = a.atIndex(n) + F * (b.atIndex(n) - c.atIndex(n));
            // get a backup random value in the domain at this index
            double backup = bounds.atIndex(0) + (Math.random() * (bounds.atIndex(1) - bounds.atIndex(0)));
            // if tryCrossover !in domain, assign backup, else assign tryCrossover to this index
            double assignVal = (tryCrossover > bounds.atIndex(0) && tryCrossover < bounds.atIndex(1)) ? tryCrossover: backup;
            orig.editIndex(n, assignVal);

            // wrap n around if needed
            n = (n + 1) % dimension;
            // ensure we are not double changing indicies, in the worst case we change everything once
            l++;
        } while (Math.random() < CR && l < dimension);

        return orig;
    }



    
}
