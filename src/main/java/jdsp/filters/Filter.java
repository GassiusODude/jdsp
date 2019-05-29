package jdsp.filters;
import jdsp.filters.FilterDesign;
import java.security.InvalidParameterException;

/**
 * The Filter class will implement the following static methods:
 *      convolve()
 *
 * @author GassiusODude
 * @since May 27, 2019
 * @version 0.0
 */
public class Filter{
    /** Numerator of filter */
    private float[] coefNumerator;
    /** Denominator of filter */
    private float[] coefDenominator;
    private float[] filterState;

    /**
     * Constructor
     *
     * @param numNumerator Number of numerator elements.
     */
    public Filter(int numNumerator) throws InvalidParameterException {
        // -------------------------  error checking  -----------------------
        if (numNumerator < 1)
            throw new InvalidParameterException(
                "Number Numerator Coefficients should be >= 1");

        // initialize to moving average filter
        coefNumerator = FilterDesign.designMovingAverage(numNumerator);
        coefDenominator = new float[0];
    }

    /**
     * Design a filter based on the provided specifications
     * and design technique.
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
                coefNumerator = FilterDesign.designMovingAverage(numNum);
                coefDenominator = new float[0];
                break;

            // handle window design method
            case "BARTLETT":
            case "HAMMING":
            case "HANN":
                coefNumerator = FilterDesign.fir_window_design(
                    numNum, design, bandwidth);
                coefDenominator = new float[0];    
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
        // TODO: track the state of the filter.
        // TODO: add support to include denominator
        return convolve(input, coefNumerator);
    }

    // =====================  static methods  ===============================
    /**
     * Convolve 2 vectors together.
     *
     * @param input1 First vector (length N)
     * @param input2 Second vector (length M)
     * @return The convolved outputed (length M+N-1)
     */
    public static float[] convolve(float[] input1, float[] input2){
        // ----------------  setup local variables  -------------------------
        int len1 = input1.length;
        int len2 = input2.length;
        int len3 = len1 + len2 - 1;
        int start;
        float[] output = new float[len3];


        if (len2 >= len1){
            for (int ind0 = 0; ind0 < len1; ind0++){
                for (int ind1 = 0; ind1 < ind0+1; ind1++){
                    output[ind0] += input1[ind0 - ind1] * input2[ind1];
                }
            }
            for (int ind0 = len1; ind0 < len3; ind0++){
                start = (ind0-len2+1<0) ? 0 : ind0 - len2 + 1;
                for (int ind1=start; ind1 < len1; ind1++){
                    float tmp = input2[ind0-ind1];
                    output[ind0] += input1[ind1] * tmp;
                }
            }
        }
        else{
            for (int ind0 = 0; ind0 < len2; ind0++){
                for (int ind1 = 0; ind1 < ind0 + 1; ind1++){
                    output[ind0] += input2[ind0 - ind1] * input1[ind1];
                }
            }
            for (int ind0 = len2; ind0 < len3; ind0++){
                start = (ind0 - len1 + 1 < 0) ? 0 : ind0 - len1 + 1;
                for (int ind1=start; ind1 < len2; ind1++){
                    float tmp = input1[ind0-ind1];
                    output[ind0] += input2[ind1] * tmp;
                }
            }
        }

        return output;
    }


}
