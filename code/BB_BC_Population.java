public class BB_BC_Population {

    public Vector[] population;
    public Vector centroid;
    private byte FLAG;


    public BB_BC_Population(int size, int dim, byte flag) throws Exception {
        
        // set global instance parameters 
        this.FLAG = flag;

        // initialize population structure, and vectors inside structure
        this.population = new Vector[size];
        Vector domain = SingleObjectiveFunctions.getDomain(flag);
        for (int i = 0; i < size; i++) {
            this.population[i] = Vector.randBetween(domain.atIndex(0), domain.atIndex(1), dim);
        }

        // work out the initial center of gravity
        doBigCrunch();
    }


    /**
     * compute the scaled evaluation of the supplied vector.
     * 
     * The scaled evaluation referes to mass in this context. We need to be careful because the minimums in most of these objective
     * functions is 0.0, and naturally dividing by zero results in an exception. 
     * 
     * As a result, we check if the current evaluation is 0, if so, return Double.MaxVal
     * @param vec
     * @return
     * @throws Exception
     */
    public double scaleEvaluate(Vector vec) throws Exception {
        double eval = SingleObjectiveFunctions.evaluate(vec, this.FLAG);
        if (eval == 0.0) {
            return Double.MAX_VALUE;
        }
        else return 1.0 / eval;
    }

    /**
     * Do the big bang procedure. This is accomplished by re-initializing particles in the population 
     * around the centroid following a scaled gaussian distribution. The scale is computed as a proportion of 
     * the search domain width, where this proportion decreases over time.
     * @param proportion The argument is the current iteration number / total number of iterations to execute.
     * @throws Exception
     */
    public void doBigBang(double proportion) throws Exception {

        double k = 0.005;
        double explosion_factor = Math.pow(k, proportion) - k;
        Vector domain = SingleObjectiveFunctions.getDomain(this.FLAG);
        double domain_width_fact = (domain.atIndex(1) - domain.atIndex(0)) * RunConfig.DOM_WIDTH_FACTOR;
        // Vector domain = SingleObjectiveFunctions.getDomain(this.FLAG);
        // double widthProp = Math.pow(1 - proportion, 2);
        // double domWidth = RunConfig.DOM_WIDTH_FACTOR * (domain.atIndex(1) - domain.atIndex(0)) * widthProp;
        for (int i = 0; i <  this.population.length; i++) {
            // Vector scaleNormVec = Vector.normZeroOne(this.centroid.size()).scale(domWidth).add(this.centroid);
            Vector scaleNormVec = Vector.normZeroOne(this.centroid.size()).scale(domain_width_fact * explosion_factor).add(this.centroid);
            this.population[i] = scaleNormVec;
        }
    }

    /**
     * This does 1 iteration of the BB_BC optimization procedure. A current iteration number is required 
     * because the big bang procedure requires a power constant, which represents how far particles should be 
     * pushed away from the centroid. This naturally needs to decrease as iterations progress to facilitate 
     * convergence. The MAXITERATIONS is read from the RunConfig.java file.
     * 
     * @param iteration
     * @throws Exception
     */
    public void doIteration(int iteration) throws Exception {
        doBigBang(((double) iteration) / ((double)RunConfig.ITERATIONS));
        doBigCrunch();
    }


    /**
     * Takes the supplied vector and forces the vector to be within the search domain specified by the objective function.
     * This is accomplished by treating walls as "Sticky", where index values are reset to be on the offending bound
     * @param centroid
     * @return
     * @throws Exception
     */
    private Vector makeInBound(Vector centroid) throws Exception {
        Vector bounds = SingleObjectiveFunctions.getDomain(this.FLAG);
        for (int i = 0; i < centroid.size(); i++) {
            if (centroid.atIndex(i) < bounds.atIndex(0))
                centroid.editIndex(i, bounds.atIndex(0));
            if (centroid.atIndex(i) > bounds.atIndex(1))
                centroid.editIndex(i, bounds.atIndex(1));
        }
        return centroid;
    }

    /**
     * Determines the centroid (center of mass) of the collection of particles
     * 
     * In this context, mass is defined as the inverse of objective function evaluation. Because we are minimizing, and the absolute minimum
     * for the objective function's is 0, mass approaches infinity as evaluation approaches 0.
     * @return
     * @throws Exception
     */
    public void doBigCrunch() throws Exception {
        // Start by choosing the first particle in the population, and scaling it by (1 / func_eval)

        double scaleEvalSum = scaleEvaluate(this.population[0]);
        Vector sum = this.population[0].scale(scaleEvalSum);

        // for each remaining vector in the population, iteratively build up this sum, scaling each vector the same way
        for (int i = 1; i < this.population.length; i++) {
            double thisScaleEval = scaleEvaluate(this.population[i]);
            if (thisScaleEval == Double.MAX_VALUE) continue;
            sum = sum.add(this.population[i].scale(thisScaleEval));
            // check for double overflow before overflow occurs by reordering if (X + Y > Double.MaxValue)
            if (scaleEvalSum > Double.MAX_VALUE - thisScaleEval) {
                scaleEvalSum = Double.MAX_VALUE;
                continue;
            }
            scaleEvalSum += thisScaleEval;
        }
        // return the summation vector scaled by the sum of the scaled evaluations
        if (this.centroid == null) {
            this.centroid = makeInBound(sum.scale(1.0 / scaleEvalSum));
        }
        else {
            Vector newCentroid = makeInBound(sum.scale(1.0 / scaleEvalSum));
            double newEval = SingleObjectiveFunctions.evaluate(newCentroid, FLAG);
            double oldEval = SingleObjectiveFunctions.evaluate(this.centroid, FLAG);
            this.centroid = (newEval < oldEval) ? newCentroid: this.centroid;
        }
    }

}
