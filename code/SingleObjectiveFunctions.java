public class SingleObjectiveFunctions {
    /**
     * This class is designed to be statically referenced, it should not be instantiable
     */
    private SingleObjectiveFunctions() {

    }

    /**
     * Defers to correct evaluation procedure as referenced by flag argument
     */
    public static double evaluate(Vector pos, byte FLAG) throws Exception {
        switch (FLAG) {
            // Ellip benchmark function
            case 1:
                return Ellip.evaluate(pos);
            case 2:
                return Ackley.evaluate(pos);
            case 3:
                return Rastrigin.evaluate(pos);
            case 4:
                return Step.evaluate(pos);
            case 5:
                return CosMix.evaluate(pos);
            case 6:
                return Quartic.evaluate(pos);
            case 7:
                return Zakharov.evaluate(pos);
            case 8:
                return Salomon.evaluate(pos);
            case 9:
                return BentCigar.evaluate(pos);
            case 10:
                return Mishra1.evaluate(pos);
            default:
                throw new Exception("Function FLAG not recognized");
        }
    }

    /**
     * Defers to correct getDomain procedure as referenced by flag argument
     */
    public static Vector getDomain(byte FLAG) throws Exception {
        switch (FLAG) {
            // Ellip benchmark function
            case 1:
                return Ellip.getDomain();
            case 2:
                return Ackley.getDomain();
            case 3:
                return Rastrigin.getDomain();
            case 4:
                return Step.getDomain();
            case 5:
                return CosMix.getDomain();
            case 6:
                return Quartic.getDomain();
            case 7:
                return Zakharov.getDomain();
            case 8:
                return Salomon.getDomain();
            case 9:
                return BentCigar.getDomain();
            case 10:
                return Mishra1.getDomain();
            default:
                throw new Exception("Function FLAG not recognized");
        }
    }

    /**
     * Defers to correct inDomain procedure as referenced by flag argument
     */
    public static boolean inDomain(Vector pos, byte FLAG) throws Exception {
        switch (FLAG) {
            // Ellip benchmark function
            case 1:
                return Ellip.inDomain(pos);
            case 2:
                return Ackley.inDomain(pos);
            case 3:
                return Rastrigin.inDomain(pos);
            case 4:
                return Step.inDomain(pos);
            case 5:
                return CosMix.inDomain(pos);
            case 6:
                return Quartic.inDomain(pos);
            case 7:
                return Zakharov.inDomain(pos);
            case 8:
                return Salomon.inDomain(pos);
            case 9:
                return BentCigar.inDomain(pos);
            case 10:
                return Mishra1.inDomain(pos);
            default:
                throw new Exception("Function FLAG not recognized");
        }
    }


    /**
     * Ackley benchmark function, it is denoted by the flag [2]
     *      The minimum of this function is 0 where each x_i = 0
     */
    static class Ackley {
        public static double evaluate(Vector pos) throws Exception {
            double a = 20;
            double b = 0.2;
            double c = 2 * Math.PI;

            double firstTerm = -a * Math.exp(-b * Math.sqrt(pos.pow(2).average()));
            double secondTerm = Math.exp(pos.scale(c).cos().average());
 
            return firstTerm - secondTerm + a + Math.E;
        }
        public static Vector getDomain() {
            double[] dat = {-5, 5};
            return new Vector(dat);
        }
        public static boolean inDomain(Vector pos) throws Exception {
            Vector dom = getDomain();
            for (int i = 0; i < pos.size(); i++) {
                boolean outDom = pos.atIndex(i) < dom.atIndex(0) || pos.atIndex(i) > dom.atIndex(1);
                if (outDom)
                    return false;
            }
            return true;
        }
    }

    /**
     * BentCigar benchmark function, it is denoted by the flag [9]
     *      The minimum of this function is 0 where each x_i = 0
     */
    static class BentCigar {
        public static double evaluate(Vector pos) throws Exception {
            return Math.pow(pos.atIndex(0),2) + 1_000_000.0 * pos.pow(2).sum(1);
        }
        public static Vector getDomain() {
            double[] dat = {-100, 100};
            return new Vector(dat);
        }
        public static boolean inDomain(Vector pos) throws Exception {
            Vector dom = getDomain();
            for (int i = 0; i < pos.size(); i++) {
                boolean outDom = pos.atIndex(i) < dom.atIndex(0) || pos.atIndex(i) > dom.atIndex(1);
                if (outDom)
                    return false;
            }
            return true;
        }
    }


    /**
     * Zakharov's benchmark function, it is denoted by the flag [7]
     *      The minimum of this function is 0 where each x_i = 0
     */
    static class Zakharov {
        public static double evaluate(Vector pos) throws Exception {
            Vector ascending = Vector.Ascending(pos.size());
            double firstComp = pos.pow(2).sum();
            double secondComp = Math.pow(ascending.scale(0.5).prod(pos).sum(), 2);
            double thirdComp = Math.pow(ascending.scale(0.5).prod(pos).sum(), 4);
            return firstComp + secondComp + thirdComp;
        }
        public static Vector getDomain() {
            double[] dat = {-5, 10};
            return new Vector(dat);
        }
        public static boolean inDomain(Vector pos) throws Exception {
            Vector dom = getDomain();
            for (int i = 0; i < pos.size(); i++) {
                boolean outDom = pos.atIndex(i) < dom.atIndex(0) || pos.atIndex(i) > dom.atIndex(1);
                if (outDom)
                    return false;
            }
            return true;
        }
    }

    /**
     * Mishra1 benchmark function, it is denoted by the flag [10]
     *      The minimum of this function is 0 where each x_i = 0
     */
    static class Mishra1 {
        public static double evaluate(Vector pos) throws Exception {
            double g = pos.size() - pos.sum(0, pos.size() - 2);
            return Math.pow(1 + g, g) - 2;
        }
        public static Vector getDomain() {
            double[] dat = {0, 1};
            return new Vector(dat);
        }
        public static boolean inDomain(Vector pos) throws Exception {
            Vector dom = getDomain();
            for (int i = 0; i < pos.size(); i++) {
                boolean outDom = pos.atIndex(i) < dom.atIndex(0) || pos.atIndex(i) > dom.atIndex(1);
                if (outDom)
                    return false;
            }
            return true;
        }
    }

    /**
     * Salomon's benchmark function, it is denoted by the flag [8]
     *      The minimum of this function is 0 where each x_i = 0
     */
    static class Salomon {
        public static double evaluate(Vector pos) throws Exception {
            double term = Math.sqrt(pos.pow(2).sum());
            return 1 - Math.cos(2 * Math.PI * term) + 0.1 * term;
        }
        public static Vector getDomain() {
            double[] dat = {-100, 100};
            return new Vector(dat);
        }
        public static boolean inDomain(Vector pos) throws Exception {
            Vector dom = getDomain();
            for (int i = 0; i < pos.size(); i++) {
                boolean outDom = pos.atIndex(i) < dom.atIndex(0) || pos.atIndex(i) > dom.atIndex(1);
                if (outDom)
                    return false;
            }
            return true;
        }
    }


    /**
     * Cosine Mixture benchmark function, it is denoted by the flag [5]
     *      The minimum of this function is 0 where each x_i = 0
     */
    static class CosMix {
        public static double evaluate(Vector pos) throws Exception {
            return -0.1 * pos.scale(5 * Math.PI).cos().sum() + pos.pow(2).sum() + 1;
        }
        public static Vector getDomain() {
            double[] dat = {-1, 1};
            return new Vector(dat);
        }
        public static boolean inDomain(Vector pos) throws Exception {
            Vector dom = getDomain();
            for (int i = 0; i < pos.size(); i++) {
                boolean outDom = pos.atIndex(i) < dom.atIndex(0) || pos.atIndex(i) > dom.atIndex(1);
                if (outDom)
                    return false;
            }
            return true;
        }
    }

    /**
     * Rastrigin benchmark function, it is denoted by the flag [3]
     *      The minimum of this function is 0 where each x_i = 0
     */
    static class Rastrigin {
        public static double evaluate(Vector pos) throws Exception {
            double a = 10;
            return a * pos.size() + (pos.pow(2).sub(pos.scale(2 * Math.PI).cos().scale(a)).sum());
        }
        public static Vector getDomain() {
            double[] dat = {-5.12, 5.12};
            return new Vector(dat);
        }
        public static boolean inDomain(Vector pos) throws Exception {
            Vector dom = getDomain();
            for (int i = 0; i < pos.size(); i++) {
                boolean outDom = pos.atIndex(i) < dom.atIndex(0) || pos.atIndex(i) > dom.atIndex(1);
                if (outDom)
                    return false;
            }
            return true;
        }
    }

    /**
     * Quartic benchmark function, it is denoted by the flag [6]
     *      The minimum of this function is 0 where each x_i = 0
     */
    static class Quartic {
        public static double evaluate(Vector pos) throws Exception {
           return Vector.Ascending(pos.size()).prod(pos.pow(4)).sum();
        }
        public static Vector getDomain() {
            double[] dat = {-1.28, 1.28};
            return new Vector(dat);
        }
        public static boolean inDomain(Vector pos) throws Exception {
            Vector dom = getDomain();
            for (int i = 0; i < pos.size(); i++) {
                boolean outDom = pos.atIndex(i) < dom.atIndex(0) || pos.atIndex(i) > dom.atIndex(1);
                if (outDom)
                    return false;
            }
            return true;
        }
    }


    /**
     * Step benchmark function, it is denoted by the flag [4]
     *      The minimum of this function is 0 where each x_i = (-1, 1)
     *      There are an infinite amount of global minimums with this function
     */
    static class Step {
        public static double evaluate(Vector pos) throws Exception {
            return pos.pow(2).floor().sum();
        }
        public static Vector getDomain() {
            double[] dat = {-100, 100};
            return new Vector(dat);
        }
        public static boolean inDomain(Vector pos) throws Exception {
            Vector dom = getDomain();
            for (int i = 0; i < pos.size(); i++) {
                boolean outDom = pos.atIndex(i) < dom.atIndex(0) || pos.atIndex(i) > dom.atIndex(1);
                if (outDom)
                    return false;
            }
            return true;
        }
    }


    /**
     * Elliptic benchmark function, it is denonted by the flag [1] 
     *      The minimum of this function is 0, where each x_i = 0 
     */
    static class Ellip {

        public static double evaluate(Vector pos) throws Exception {
            Vector square = pos.pow(2);
            return Vector.Ascending(pos.size()).prod(square).sum();
        }
        public static Vector getDomain() {
            double[] dat = {-5, 5};
            return new Vector(dat);
        }
        public static boolean inDomain(Vector pos) throws Exception {
            Vector dom = getDomain();
            for (int i = 0; i < pos.size(); i++) {
                boolean outDom = pos.atIndex(i) < dom.atIndex(0) || pos.atIndex(i) > dom.atIndex(1);
                if (outDom)
                    return false;
            }
            return true;
        }
    }
    
}
