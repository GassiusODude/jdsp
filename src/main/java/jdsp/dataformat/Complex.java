package net.kcundercover.jdsp.dataformat;

/**
 * Comple number object
 * @param <T> The type
 */
public class Complex<T extends Number> extends Number{
    private static final long serialVersionUID = 1L;
    /** Real part of the complex number */
    public T real;
    /** Imaginary part of the comple number */
    public T imag;

    /** Constructor
     * @param real Real part of the complex number
     * @param imag Imaginary part of the comple number
     */
    public Complex(T real, T imag){
        this.real = real;
        this.imag = imag;
    }

    /** Get the double value of the real value */
    @Override
    public double doubleValue(){
        return real.doubleValue();
    }
    /** Get the float value of the real value */
    @Override
    public float floatValue(){
        return real.floatValue();
    }
    /** Get the long value of the real value */
    @Override
    public long longValue(){
        return real.longValue();
    }
    /** Get the integer value of the real value */
    @Override
    public int intValue(){
        return real.intValue();
    }

    /**
     * Add two complex numbers together
     * @param elem1 First complex number
     * @param elem2 Second comple number
     * @return The sum of the two complex numbers
     */
    public static Complex<Double> add(
            Complex<Double> elem1, Complex<Double> elem2){
        Complex out = new Complex(
            elem1.real.doubleValue() + elem2.real.doubleValue(),
            elem1.imag.doubleValue() + elem2.imag.doubleValue());
        return out;
    }
}