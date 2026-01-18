/**
 * Implements histogram functionality.
 */
package net.kcundercover.jdsp.math;
import net.kcundercover.jdsp.math.Vector;

/** Histogram object */
public class Histogram {
    /** Default histogram constructor */
    public Histogram() {}
    /** Histogram counts */
    private int[] histCounts = new int[1];

    /** Histogram edges */
    double[] histEdges = new double[1];

    /** Min max */
    double[] minMax = new double[2];

    /**
     * Get the histogram counts for the last call to histogram
     * @return The integer counts
     */
    public int[] getHistCounts(){
        return histCounts;
    }

    /**
     * Get the Histogram edges used in the last call to histogram
     * @return The edges used
     */
    public double[] getHistEdges(){
        return histEdges;
    }

    /**
     * Calculate the histogram of the given vector
     * This function updates the histCounts, histEdges, minMax
     * @param vec1 Input vector
     * @param numBins Number of bins (should be positive and greater than 1)
     * @return The discovered histCounts.
     */
    public int[] histogram(double[] vec1, int numBins){
        // -----------------------  error checking  -------------------------
        if (numBins <= 1){
            throw new IllegalArgumentException("numBins should be greater than 1");
        }
        minMax = Vector.getMinMax(vec1);

        if (minMax[1] == minMax[0]){
            // only one value
            histEdges = new double[1];
            histEdges[0] = minMax[0] + 1;
            histCounts = new int[1];
            histCounts[0] = vec1.length;
            return histCounts;
        }

        // identify range
        double range = minMax[1] - minMax[0];

        // -----------------------  setup edges  ----------------------------
        histEdges = new double[numBins];
        for (int ind0 = 0; ind0 < numBins; ind0++){
            histEdges[ind0] = range / (numBins - 1) * (ind0 + 0.5);
        }
        // ----------------------  update counts  ---------------------------
        // reset binCount
        histCounts = new int[numBins];

        // update
        for (double elem : vec1){
            for (int ind0 = 0; ind0 < histEdges.length; ind0++)
                if (elem < histEdges[ind0]){
                    histCounts[ind0]++;
                    break;
                }
        }
        return histCounts;
    }
}