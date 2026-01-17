/** Filter object
 * @author Keith Chow
 */

package net.kcundercover.jdsp.filters;
import net.kcundercover.jdsp.math.Convolve;

/** FilterObject */
public class FilterObject{
    /** Numerator of the filter */
    private double[] numerator = {1.0};

    /** Denominator of the filter */
    private double[] denominator = {1.0};

    /** Constructor of the filter object */
    public FilterObject(){
    }

    // ======================================================================
    // -------------------------  get methods  ------------------------------
    // ======================================================================
    /**
     * Get the numerator (feed forward taps)
     * @return Feed forward taps of a filter
     */
    public double[] getNumerator() { return this.numerator; }

    /** 
     * Get the denominatory (feedback taps)
     * @return The feedback taps
     */
    public double[] getDenominator() { return this.denominator; }

    // ======================================================================
    // -------------------------  set methods  ------------------------------
    // ======================================================================
    /** Set the numerator(feedfoward taps) of the FilterObject
     *
     * @param numerator Numerator of the filter
     */
    public void setNumerator(double [] numerator){
        assert numerator.length >= 1:
            "Expecting numerator with length >= 1";
        this.numerator = numerator;
    }

    /** Set the denominator (feedback taps) of the FilterObject
     *
     * @param denominator The new denominator of the filter object
     */
    public void setDenominator(double[] denominator){
        this.denominator = denominator;
    }

    /**Filter applied on a complex interleaved input
     *
     * @param complexInput Complex interleaved input signal
     * @return Complex interleaved filted output
     */
    public double[] filterComplexInterleaved(double[] complexInput){
        return Convolve.convolveRealComplex(this.getNumerator(), complexInput);
    }
    /** Filter a real input signal
     *
     * @param realInput Real input signal
     * @return Filtered signal
     */
    public double[] filterReal(double[] realInput){
        return Convolve.convolve(this.getNumerator(), realInput);
    }
}