class PSO_Particle {


    public Vector position;
    public Vector pBestVec;
    public double pBest;
    public Vector gBestVec;
    public double gBest;
    public Vector velocity;
    public double w;
    public double c1;
    public double c2;
    private byte FLAG;
    public int DIM;

    /**
     * Construct a PSO_Particle
     * @param controlParams list of model control parameters of form {w, c1, c2}
     * @param flag flag which denotes the objective function this particle attempts to minimnize
     * @param dim The dimension of this particles search space
     */
    public PSO_Particle(double[] controlParams, byte flag, int dim) throws Exception {
        //set useful global fields
        this.DIM = dim;
        this.FLAG = flag;

        // control params of the form [w, c1, c2]
        this.w = controlParams[0];
        this.c1 = controlParams[1];
        this.c2 = controlParams[2];

        Vector domain = SingleObjectiveFunctions.getDomain(flag);

        this.position = Vector.randBetween(domain.atIndex(0), domain.atIndex(1), dim);
        this.pBestVec = this.position.duplicate();
        this.pBest = evaluatePos();
        this.velocity = new Vector(dim);
    }

    /**
     * Runs the standard PSO velocity update equation
     * 
     * Assigns the updated velocity to this particle's velocity
     * @throws Exception
     */
    public void updateVel() throws Exception {
        Vector inWeight = velocity.scale(w);
        Vector cog = Vector.ZeroOne(DIM).scale(c1).prod(pBestVec.sub(position));
        Vector soc = Vector.ZeroOne(DIM).scale(c2).prod(gBestVec.sub(position));
        this.velocity = cog.add(inWeight).add(soc);
    }

    /**
     * Updates the position, assumes that the velocity was previously updated
     * 
     * This will update pbestVal and pbestVec if needed
     */
    public void updatePos() throws Exception {
        this.position = this.velocity.add(this.position);

        if (evaluatePos() < this.pBest && SingleObjectiveFunctions.inDomain(this.position, FLAG)) {
            this.pBest = evaluatePos();
            this.pBestVec = this.position.duplicate();
        }
    }

    /**
     * update the GBest value of this particle to reflect the provided vector
     */
    public void updateGBest(Vector newBest) throws Exception {
        this.gBestVec = newBest.duplicate();
        this.gBest = evaluateVec(newBest);
    }

    /**
     * Evaluate the quality of this particles position with respect to the objective function being minimized
     * @return
     */
    public double evaluatePos() throws Exception {
        return SingleObjectiveFunctions.evaluate(this.position, this.FLAG);
    }

    /**
     * Evaluate the quality of the provided vector with respect to the objective function being minimized
     * @param vec
     * @return
     * @throws Exception
     */
    public double evaluateVec(Vector vec) throws Exception {
        return SingleObjectiveFunctions.evaluate(vec, this.FLAG);
    }

}