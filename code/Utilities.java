import java.io.PrintWriter;

public class Utilities {

    /**
     * This class should not be instantiable 
     */
    private Utilities() {

    }


    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[1;31m";
    public static final String GREEN = "\u001B[1;32m";
    public static final String YELLOW = "\u001B[1;33m";
    public static final String BLUE = "\u001B[1;34m";
    public static final String MAGENTA = "\u001B[1;35m";
    public static final String CYAN = "\u001B[1;36m";
    public static final String WHITE = "\u001B[1;37m";


    /**
     * Takes a raw data output array[][], where rows specify independent trials, and each column represents
     * the recorded observation for the (colNum * granularity)'th trial, and compute the mean of this column
     * across all rows.
     * @param data
     * @return
     */
    public static double[] averageAtTime(double[][] data) {
        double[] averages = new double[data[0].length];
        for (int col = 0; col < data[0].length; col++){
            double sum = 0.0;
            for (int row = 0; row < data.length; row++) {
                sum += data[row][col];
            }
            averages[col] = sum / (double) data.length;
        }
        return averages;
    }

    /**
     * Takes a raw data output array[][], where rows specify independent trials, and each column represents
     * the recorded observation for the (colNum * granularity)'th trial, and compute the variance of this column
     * across all rows.
     * @param data
     * @return
     */
    public static double[] varAtTime(double[][] data) {

        double[] vars = new double[data[0].length];
        for (int col = 0; col < data[0].length; col++){
            double sum = 0.0;
            for (int row = 0; row < data.length; row++) {
                sum += data[row][col];
            }
            //we begin by putting the average into this index, to be used in second pass
            vars[col] = sum / (double) data.length;
        }

        // second pass, we compute average distance from the average of this column (currently stored in vars[col])
        for (int col = 0; col < data[0].length; col++) {
            double diffSum = 0.0;
            for (int row = 0; row < data.length; row++) {
                // iteratively sum difference
                diffSum += Math.pow(Math.abs(data[row][col] - vars[col]), 2);
            }
            // var formula
            vars[col] = diffSum / (double) (data.length - 1);
        }

        return vars;
    }



    /**
     * Takes the provided file data, computes a string representation of this data, and defers to an overloaded
     * function writeFile(String) which writes this data to a CSV file
     * @param data
     */
    public static void writeFile(double[][] data, String type, byte flag) {

        // if record mode is not true, stop
        if (!RunConfig.RECORD_MODE) return;

        // build up CSV string
        StringBuilder sb = new StringBuilder(colHeader(data[0]));
        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < data[row].length; col++) {
                sb.append("" + data[row][col]);
                if (col < data[row].length - 1)
                    sb.append(",");
            }
            sb.append("\n");
        }

        // now that the file string is built up, write it to the file
        writeFile(sb.toString(), type, flag);
    }

    /**
     * Takes the provided file data, computes a string representation of this data, and defers to an overloaded
     * function writeFile(String) which writes this data to a CSV file
     * @param data
     */
    public static void writeFile(double[] data, String type, byte flag) {
        
        // if record mode is not true, stop
        if (!RunConfig.RECORD_MODE) return;

        // build up CSV string
        StringBuilder sb = new StringBuilder(colHeader(data));
        for (int entry = 0; entry < data.length; entry++) {
            sb.append("" + data[entry]);
            if (entry < data.length - 1)
                sb.append(",");
        }
        sb.append("\n");

        // now that the file string is built up, write it to the file
        writeFile(sb.toString(), type, flag);
    }



    private static String colHeader(double[] data) {
        StringBuilder sb = new StringBuilder();
        for (int entry = 0; entry < data.length; entry++) {
            sb.append(entry * RunConfig.GRANULARITY);
            if (entry < data.length - 1)
                sb.append(",");
        }
        sb.append("\n");
        return sb.toString();
    }


    /**
     * Writes the provided string content to a file
     * @param fileData
     */
    private static void writeFile(String fileData, String type, byte flag) {
        try (PrintWriter writer = new PrintWriter(genFileName(flag, type))) {
            writer.write(fileData);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        } 
    }

    /**
     * Generate the fileName and directory pathway 
     * @param flag used to determine the function used, IE directory pathway
     * @param type
     * @return
     * @throws Exception
     */
    public static String genFileName(byte flag, String type) throws Exception {
        String benchmarkName = SingleObjectiveFunctions.getName(flag);
        String pathway = "../results/";
        return String.format("%s/%s/%s.csv", pathway, benchmarkName, type);

    }   



    
}
