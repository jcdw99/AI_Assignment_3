
public class PSO_Swarm {

    private byte FLAG;

    public PSO_Particle[] particles;
    public Vector gBestVec;
    public double gBestEval;

    public PSO_Swarm(double[] params, int size, int dim, byte flag) throws Exception {
        // set control parameters
        this.FLAG = flag;

        // initialize swarm structure
        particles = new PSO_Particle[size];
        for (int i = 0; i < size; i++) {
            particles[i] = new PSO_Particle(params, flag, dim);
        }

        // updateGbest
        setGBest(findGBest());
    }

    /**
     * Run the update Procedure for each particle in the swarm
     * After the procedure has updated both velocity and position, compute and update the new gBest
     */
    public void doUpdate() throws Exception {
        // for each particle, do velocity and position update
        for (int i = 0; i < particles.length; i++) {
            particles[i].updateVel();
            particles[i].updatePos();
        }
        // find the new gBest, and make each particle reflect this value
        setGBest(findGBest());
    }

    /**
     * This function iterates the pbest's of the particles in the swarm, and returns the "best PBest" of the swarm
     * 
     * @return
     */
    public Vector findGBest() {
        int bestdex = 0;
        double bestEval = particles[bestdex].pBest;
        for (int i = 1; i < particles.length; i++) {
            if (particles[i].pBest < bestEval)
                bestdex = i;
        }
        return particles[bestdex].pBestVec;
    }

    /**
     * This function assigns the supplied vector as the Gbest for each particle in the swarm. It also assigns
     * gBest evaluation to each particle, this is the evaluation of the supplied vector.
     * @param best
     * @throws Exception
     */
    public void setGBest(Vector best) throws Exception {
        double bestEval = SingleObjectiveFunctions.evaluate(best, FLAG);
        for (int i = 0 ; i < particles.length; i++) {
            particles[i].gBestVec = best.duplicate();
            particles[i].gBest = bestEval;
        }
        this.gBestVec = best.duplicate();
        this.gBestEval = bestEval;
    }

}
