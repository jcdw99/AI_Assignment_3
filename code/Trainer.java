public class Trainer {

    public static void main(String[] args) throws Exception {
        OptimizationSuite s = new OptimizationSuite(Byte.parseByte(args[0]), (byte) 2);
        s.train();
    }
}
