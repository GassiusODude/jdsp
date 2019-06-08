package jdsp.filters;
import jdsp.filters.FilterDesign;
import jdsp.math.Convolve;
import java.security.InvalidParameterException;
/**
 * The FilterF class will implement the following static methods:
 *
 * @author GassiusODude
 * @since May 27, 2019
 * @version 0.0
 */
public class FilterF{
    /** Numerator of filter */
    private float[] coefNumerator;
    /** Denominator of filter */
    private float[] coefDenominator;

    /** State of the filter */
    private float[] filterState;

    /**
     * Constructor
     *
     * @param numNumerator Number of numerator elements.
     */
    public FilterF(int numNumerator) throws InvalidParameterException {
        // -------------------------  error checking  -----------------------
        if (numNumerator < 1)
            throw new InvalidParameterException(
                "Number Numerator Coefficients should be >= 1");

        // initialize to moving average filter
        coefNumerator = FilterDesign.designMovingAverageF(numNumerator);
        coefDenominator = new float[0];
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
            float bandwidth) throws InvalidParameterException{
        // -------------------------  error checking  -----------------------
        if (numNum < 1)
            throw new InvalidParameterException(
                "Number Numerator Coefficients should be >= 1");
        if (numDen < 0)
            throw new InvalidParameterException(
                "Number Denominator Coefficients should be > 0");

        // -------------------------  design filter  ------------------------
        switch (design){
            case "MOVING AVERAGE":
                coefNumerator = FilterDesign.designMovingAverageF(numNum);
                coefDenominator = new float[0];
                filterState = new float[numNum - 1];
                break;

            // handle window design method
            case "BARTLETT":
            case "HAMMING":
            case "HANN":
                coefNumerator = FilterDesign.firWindowDesignF(
                    numNum, design, bandwidth);
                coefDenominator = new float[0];
                filterState = new float[numNum - 1];    
                break;

            // no matches...design not supported
            default:
                throw new InvalidParameterException(
                    "Design (" + design + ") not supported.");
        }
        // TODO: update size of internal state
    }

    /**
     * Apply the filter to the input signal
     * @param input Signal
     * @return Filtered output
     */
    public float[] applyFilter(float[] input){

        // ------------------------  load filter state  ---------------------
        float[] tmp = new float[input.length + filterState.length];
        System.arraycopy(filterState, 0, tmp, 0, filterState.length);
        System.arraycopy(input, 0, tmp, filterState.length, input.length);
        float[] output = Convolve.convolve(tmp, coefNumerator);


        // update filterState
        System.arraycopy(tmp, tmp.length - filterState.length, 
            filterState, 0, filterState.length);

        // prepare output
        tmp = new float[input.length];
        System.arraycopy(output, filterState.length, tmp, 0, tmp.length);

        return tmp;
    }
}
