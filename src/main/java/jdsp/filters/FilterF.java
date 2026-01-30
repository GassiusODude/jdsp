package net.kcundercover.jdsp.filters;
import java.security.InvalidParameterException;
import java.util.Arrays;
import net.kcundercover.jdsp.filters.FilterDesign;
import net.kcundercover.jdsp.math.Convolve;
/**
 * The FilterF class will implement the following static methods:
 *
 * @author GassiusODude
 * @version 0.0
 */
public class FilterF{
    /** Numerator of filter */
    private float[] coefNumerator;

    /** Denominator of filter */
    private float[] coefDenominator;

    /** State of the filter (real) */
    private float[] filterStateReal;

    /** State of the filter (imaginary) */
    private float[] filterStateImag;

    /**Constructor
     *
     * @param numNumerator Number of numerator elements.
     */
    public FilterF(int numNumerator){
        // -------------------------  error checking  -----------------------
        assert numNumerator >= 1 :
            "Number Numerator Coefficients should be >= 1";

        // initialize to moving average filter
        designFilter(numNumerator, 0, "MOVING AVERAGE", 0.5f);
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
            float bandwidth) throws InvalidParameterException {
        // -------------------------  error checking  -----------------------
        assert numNum >= 1:
            "Number Numerator Coefficients should be >= 1";
        assert numDen >= 0:
            "Number Denominator Coefficients should be > 0";

        // -------------------------  design filter  ------------------------
        switch (design){
            case "MOVING AVERAGE":
                coefNumerator = FilterDesign.designMovingAverageF(numNum);
                coefDenominator = new float[0];
                break;

            // handle window design method
            case "BARTLETT":
            case "HAMMING":
            case "HANN":
                coefNumerator = FilterDesign.firWindowDesignF(
                    numNum, design, bandwidth);
                coefDenominator = new float[0];

                break;

            // no matches...design not supported
            default:
                throw new InvalidParameterException(
                    "Design (" + design + ") not supported.");
        }
        filterStateReal = new float[numNum - 1];
        filterStateImag = new float[numNum - 1];
    }

    /**
     * Apply the filter to the input signal
     * @param input Signal
     * @return Filtered output
     */
    public float[] applyFilter(float[] input){

        // ------------------------  load filter state  ---------------------
        float[] tmp = new float[input.length + filterStateReal.length];
        System.arraycopy(filterStateReal, 0, tmp, 0, filterStateReal.length);
        System.arraycopy(input, 0, tmp, filterStateReal.length, input.length);
        float[] output = Convolve.convolve(tmp, coefNumerator);


        // update filterState
        System.arraycopy(tmp, tmp.length - filterStateReal.length,
            filterStateReal, 0, filterStateReal.length);
        if (input.length >= filterStateImag.length) {
            // imaginary side was 0.
            Arrays.fill(filterStateImag, 0.0f);

        } else {
            System.arraycopy(
                filterStateImag, input.length,
                filterStateImag, 0, filterStateImag.length - input.length);
            // zeropad the rest
            Arrays.fill(
                filterStateImag,
                filterStateImag.length - input.length, filterStateImag.length,
                0.0f);
        }

        // prepare output
        tmp = new float[input.length];
        System.arraycopy(output, filterStateReal.length, tmp, 0, tmp.length);

        return tmp;
    }

    /** Apply the filter to the input signal (complex signal)
     * @param inputReal Input signal (real part)
     * @param inputImag Input signal (imaginary part)
     * @return Filtered output Complex
     */
    public float[][] applyFilter(float[] inputReal, float[] inputImag){
        float[][] out2= new float[2][inputReal.length];
        float[] filter_out, tmp;
        tmp = new float[inputReal.length + filterStateReal.length];

        // -------------------- filter real  --------------------------------
        // load in the filter state
        System.arraycopy(filterStateReal, 0, tmp, 0, filterStateReal.length);
        System.arraycopy(inputReal, 0, tmp, filterStateReal.length, inputReal.length);

        filter_out = Convolve.convolve(tmp, coefNumerator);

        // track state of filterStateReal for next call
        System.arraycopy(
            tmp, tmp.length - filterStateReal.length,
            filterStateReal, 0, filterStateReal.length);

        // NOTE: remote filter delay
        System.arraycopy(filter_out, filterStateReal.length, out2[0], 0, inputReal.length);

        // -------------------- filter imag  --------------------------------
        // load in the filter state
        System.arraycopy(filterStateImag, 0, tmp, 0, filterStateImag.length);
        System.arraycopy(inputImag, 0, tmp, filterStateImag.length, inputImag.length);

        filter_out = Convolve.convolve(tmp, coefNumerator);

        // track state of filterStateReal for next call
        System.arraycopy(
            tmp, tmp.length - filterStateReal.length,
            filterStateReal, 0, filterStateReal.length);

        // NOTE: remote filter delay
        System.arraycopy(filter_out, filterStateReal.length, out2[1], 0, tmp.length);

        return out2;
    }
}
