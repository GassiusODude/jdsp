/**
 * Filter object
 * @author Keith Chow
 * @since December 23, 2019
 */

package jdsp.filters;
import jdsp.math.Convolve;
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
    public double[] getNumerator(){return this.numerator;}
    public double[] getDenominator(){return this.denominator;}

    // ======================================================================
    // -------------------------  set methods  ------------------------------
    // ======================================================================
    public void setNumerator(double [] numerator){
        this.numerator = numerator;
    }
    public void setDenominator(double[] denominator){
        this.denominator = denominator;
    }

    public double[] filterComplexInterleaved(double[] complexInput){
        return Convolve.convolveRealComplex(this.getNumerator(), complexInput);
    }
    public double[] filterReal(double[] realInput){
        return Convolve.convolve(this.getNumerator(), realInput);
    }
}