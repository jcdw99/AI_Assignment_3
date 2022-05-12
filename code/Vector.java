import java.util.Random;

public class Vector {

    private double[] data;
    /**
     * construct a new vector object of desired length, initialized
     * to contain only 0.0 entries
     * @param len the length of the desired vector
     */
    public Vector(int len) {
        this.data = new double[len];
    }
    /**
     * construct a new vector object from data array 
     * @param data the input array
     */
    public Vector(double[] data) {
        this.data = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            this.data[i] = data[i];
        }
    }

    /**
     * Duplicates the current vector, to avoid aliasing when assiging vectors to one another
     * @return
     */
    public Vector duplicate() {
        double[] data = new double[this.data.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = this.data[i];
        }
        return new Vector(data);
    }

    public void editIndex(int index, double val) {
        this.data[index] = val;
    }


    /**
     * returns true if vector A dominates Vector B. The usage is boolean res = A.dominates(B)
     * @param that represents Vector B
     * @return Returns true if each entry in A is <= each entry in B, with at least 1 entry being STRICTLY <
     * @throws Exception if vector lengths do not match.
     */
    public boolean dominates(Vector that) throws Exception {
        if (this.data.length != that.data.length)
            throw new Exception("Vector Length Mismatch");
        boolean lessFlag = false;
        for (int i = 0; i < this.data.length; i++) {
            // if (less(this.data[i] , that.atIndex(i))) {
            if (this.data[i] < that.atIndex(i)) {
                lessFlag = true;
                continue;
            }
            if (this.data[i] <= that.atIndex(i))
                continue;
            else
                return false;
        }
        return lessFlag;   
    }

    /**
     * computes the average of this vector, and returns it
     * @return
     */
    public double average() throws Exception {
        double sum = this.sum();
        return sum / this.data.length;
    }

    /**
     * Floors each entry of this vector, returns the result as a new vector
     *      Flooring is done by performing an integer casting. 
     *      1.3 Floored is 1, 3.9 Floored is 3
     * @return
     * @throws Exception
     */
    public Vector floor() throws Exception {
        double[] data = new double[this.data.length];
        for (int i = 0; i < this.data.length; i++) {
            data[i] = (int) this.data[i];
        }
        return new Vector(data);
    }


    /**
     * Computes the distance between these two vectors, if they are regarded to as points,
     * IE vectors draw with the tail at the origin, and the head at the point.
     * 
     * @param that
     * @return
     */
    public double distance(Vector that) throws Exception{
        if (this.data.length != that.data.length)
            throw new Exception("Vector Length Mismatch");
        double sum = 0.0;
        for (int i = 0; i < this.data.length; i++) {
            sum += Math.pow((this.data[i] - that.atIndex(i)) , 2);
        }
        return Math.sqrt(sum);
    }

    public Vector sqrRoot() throws Exception {
        for (double i: this.data) 
            if (i < 0)
                throw new Exception("Square root of negative not defined");

        double[] data = new double[this.data.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = Math.sqrt(this.data[i]);
        }
        return new Vector(data);
    }
    
    /**
     * Shifts the vector by the provided factor. Shift is defined to be a_i + b, where a_i is each entry
     * of vector a, and b is the factor to which this vector should be shifted
     * @param factor
     * @return
     */
    public Vector shift(double factor) {
        double data[] = new double[this.data.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = factor + this.data[i];
        }
        return new Vector(data);
    }

    /**
     * Scales the vector by the provided argument
     * @param scale the scalar multiple by which this vector should be scaled
     * @return the scaled vector result
     */
    public Vector scale(double scale) {
        double[] data = new double[this.data.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = scale * this.data[i];
        }
        return new Vector(data);
    }
    /**
     * Calculate and return sum of entire vector
     * @return sum of vector
     */
    public double sum() throws Exception {
        return sum(0, this.data.length - 1);
    }

    /**
     * Calculate and return sum of vector from provided index, until end
     * @param start index to begin summation, INCLUSIVE
     * @return sum of subvector
     * @throws Exception
     */
    public double sum(int start) throws Exception {
        return sum(start, this.data.length - 1);
    }


    /**
     * Calculate and return sum of vector between start and end index INCLUSIVE
     * @param start index to start sum, INCLUSIVE
     * @param end index to end sum, INCLUSIVE
     * @return sum of vector between indicies
     */
    public double sum(int start, int end) throws Exception {
        if (start < 0 || start > this.data.length - 1)
            throw new Exception("Sum Start Index Out of Bounds");
        if (end < 0 || end > this.data.length - 1)
            throw new Exception("Sum End Index Out of Bounds");
        if (end < start)
            throw new Exception("End Index Precedes Start Index");
        double sum = 0.0;
        for (int i = start; i <= end; i++) {
            sum += this.data[i];
        }
        return sum;
    } 


    
    /**
     * Creates a new vector of specified length where values are initialized using Uniform(0, 1)
     * @param len
     * @return
     */
    public static Vector ZeroOne(int len) {
        double[] data = new double[len];
        for (int i = 0; i < len; i++) {
            data[i] = Math.random();
        }
        return new Vector(data);
    }

    /**
     * Creates a new vector of specified length where values are intalized using Normal(0,1)
     * @param len
     * @return
     */
    public static Vector normZeroOne(int len) {
        Random r = new Random();
        double[] data = new double[len];
        for (int i = 0; i < len; i++) {
            data[i] = r.nextGaussian();
        }
        return new Vector(data);
    }
    /**
     * Creates a new vector of specified length where values are ascending from [1, length]
     * @param len
     * @return
     */
    public static Vector Ascending(int len) {
        double[] data = new double[len];
        for (int i = 0; i < len; i++) {
            data[i] = i + 1;
        }
        return new Vector(data);
    }

    /**
     * returns a random vector where each element is randomly initialized on the interval [low, high]
     * @param low The lower bound of the interval
     * @param high The upper bound of the interval
     * @param len The length of the desired vector
     * @return
     * @throws Exception
     */
    public static Vector randBetween(double low, double high, int len) throws Exception {
        if (high < low)
            throw new Exception("High Bound Precedes Low Bound");
        double[] data = new double[len];
        for (int i = 0; i < len; i++) {
            data[i] = (high - low) * Math.random() + low;
        }
        return new Vector(data);
    }

    public int size() {
        return this.data.length;
    }

    /**
     * returns the entry in the vector at specified index
     * @param index
     * @return
     * @throws Exception if index exceeds length of vector
     */
    public double atIndex(int index) throws Exception {
        if (index >= this.data.length)
            throw new Exception("Index requested out of bounds for vector");
        return this.data[index];
    }
    /**
     * Computes the vector dot product between vectors A and B.
     * If the vector lengths do not match, and error is thrown
     * @param that
     * @return
     * @throws Exception
     */
    public double dot(Vector that) throws Exception {
        if (that == null)
            throw new NullPointerException();
        if (this.data.length != that.data.length)
            throw new Exception("Vector Length Mismatch");
        double runSum = 0.0;
        for (int i = 0; i < this.data.length; i++) {
            runSum += (this.data[i] * that.data[i]);
        }
        return runSum;
    }

    /**
     * Applies the log operation to the calling vector instance
     *      flags:
     *          (0) applies log_2
     *          (1) applies log_10
     *          (2) applies log_e
     *          (n) applies log_n
     * 
     * @param flag
     * @return
     * @throws Exception thrown if log of negative value is attempted 
     */
    public Vector log(int flag) throws Exception {
        for (double i: this.data) {
            if (i <= 0)
                throw new Exception("Log Operation Not Defined");
        }
        double[] data =  new double[this.data.length];
        for (int i = 0; i < data.length; i++) {
            switch(flag) {
                // log base 2
                case 0:
                    data[i] = Math.log(this.data[i]) / Math.log(2);
                    break;
                // log base 10
                case 1:
                    data[i] = Math.log10(this.data[i]);
                    break;
                // natural log
                case 2:
                    data[i] = Math.log(this.data[i]);
                    break;
                // custom base
                default:
                    data[i] = Math.log(this.data[i]) / Math.log(flag);
                    break;
            }
        }
        return new Vector(data);
    }


    public Vector pow(int exponent) {
        double[] data = new double[this.data.length];
        for (int i = 0; i < this.data.length; i++) {
            data[i] = Math.pow(this.data[i], exponent);
        }
        return new Vector(data);
    }



    /**
     * Adds vector a and b, returns a new vector which represents the sum
     * Function throws an error if the lengths of a and b mismatch
     * Intuition is A + B
     * @param that
     * @return sum of vector a and b
     * @throws Exception 
     */
    public Vector add(Vector that) throws Exception {
        if (that == null)
            throw new NullPointerException();
        if (this.data.length != that.data.length)
            throw new Exception("Vector Length Mismatch");
        int len = this.data.length;
        double[] data = new double[len];
        for (int i = 0; i < len; i++) {
            data[i] = this.data[i] + that.data[i];
        }
        return new Vector(data);
    }


    /**
     * computes the index-wise product of two vectors of the same length.
     * 
     * value at index i in both vectors are multiplied and packed into returned vector at index i, for each index i
     * @param that
     * @return
     * @throws Exception if vector lengths are not equivalent
     */
    public Vector prod(Vector that) throws Exception {
        if (this.data.length != that.data.length)
            throw new Exception("Vector Length Mismatch");

        double[] data = new double[this.data.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = this.data[i] * that.atIndex(i);
        }
        return new Vector(data);
    }

    /**
     * Subtracts vector a and b, returns a new vector which represents the 
     * difference between vectors A and B. Intuition is A - B
     * Function throws an error if A and B are different lengths
     * @param that
     * @return subtraction of A and B
     * @throws Exception
     */
    public Vector sub(Vector that) throws Exception {
        return add(neg(that));
    }


    /**
     * Compute the sin of this vector, returns the result in a new vector
     * @return
     */
    public Vector sin() throws Exception {
        double[] data = new double[this.data.length];
        for (int i = 0; i < this.data.length; i++ ) {
            data[i] = Math.sin(this.data[i]);
        }
        return new Vector(data);
    }

    /**
     * Compute the cos of this vector, returns the result in a new vector
     * @return
     * @throws Exception
     */
    public Vector cos() throws Exception {
        double[] data = new double[this.data.length];
        for (int i = 0; i < this.data.length; i++ ) {
            data[i] = Math.cos(this.data[i]);
        }
        return new Vector(data);
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("[ ");
        for (int i = 0; i < this.data.length; i++) {
            b.append(String.format("%.4f", this.data[i]));
            if (i < this.data.length - 1)
                b.append(", ");
        }
        b.append(" ]\n");
        return b.toString();
    }

    private Vector neg(Vector a) {
        double[] data = new double[a.data.length];
        for (int i = 0; i < data.length; i++) 
            data[i] = -1 * a.data[i];
        return new Vector(data);
    }

}