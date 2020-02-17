/**
 *
 * @author GassiusODude
 * @version 0.0
 */
package jdsp.filters;
import jdsp.math.Convolve;
import jdsp.filters.FilterDesign;

public class FilterD{
    /** Serial Version UID for this class */
    public final static long serialVersionUID = 0;

    /** Numerator of filter */
    private double[] coefNumerator;

    /** Denominator of filter */
    private double[] coefDenominator;

    /** State of the filter */
    private double[] filterState;

    /** Constructor
     *
     * @param numNumerator Number of numerator elements.
     */
    public FilterD(int numNumerator){
        // -------------------------  error checking  -----------------------
        assert numNumerator >= 1 :
            "Number numerator coefficients should be >= 1";

        // initialize to moving average filter
        coefNumerator = FilterDesign.designMovingAverageD(numNumerator);
        coefDenominator = new double[0];
    }

    /**
     * Design a filter based on the provided specifications
     * and design technique.  This will update interal properties.
     * 
     * @param numNum Number of numerator filter coefs.
     * @param numDen Number of denominator filter coefs.
     * @param design The design technique to use.
     * @param bandwidth Normalized bandwidth. (0.5 = half the sampling rate)
     */
    public void designFilter(int numNum, int numDen, String design, 
            double bandwidth){
        // -------------------------  error checking  -----------------------
        assert numNum >= 1 :
            "Number numerator coefficients should be >= 1";
        assert numDen >= 0 :
            "Number Denominator Coefficients should be > 0";

        // -------------------------  design filter  ------------------------
        switch (design){
            case "MOVING AVERAGE":
                coefNumerator = FilterDesign.designMovingAverageD(numNum);
                coefDenominator = new double[0];
                filterState = new double[numNum - 1];
                break;

            // handle window design method
            case "BARTLETT":
            case "HAMMING":
            case "HANN":
                coefNumerator = FilterDesign.firWindowDesignD(
                    numNum, design, bandwidth);
                coefDenominator = new double[0];
                filterState = new double[numNum - 1];
                break;

            // no matches...design not supported
            default:
                assert false :
                    "Design (" + design + ") not supported.";
        }
        // TODO: update size of internal state
    }

    /** Apply the filter to the input signal
     * @param input Input signal
     * @return Filtered output
     */
    public double[] applyFilter(double[] input){
        // ------------------------  load filter state  ---------------------
        double[] tmp = new double[input.length + filterState.length];
        System.arraycopy(filterState, 0, tmp, 0, filterState.length);
        System.arraycopy(input, 0, tmp, filterState.length, input.length);
        double[] output = Convolve.convolve(tmp, coefNumerator);

        // update filterState
        System.arraycopy(tmp, tmp.length - filterState.length, 
            filterState, 0, filterState.length);

        // prepare output
        tmp = new double[input.length];
        System.arraycopy(output, filterState.length, tmp, 0, tmp.length);

        return tmp;
    }

    // =====================  static methods  ===============================
}
