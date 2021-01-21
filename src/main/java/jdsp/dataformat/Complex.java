package net.kcundercover.jdsp.dataformat;

public class Complex<T extends Number> extends Number{
    private static final long serialVersionUID = 1L;
    public T real;
    public T imag;

    public Complex(T real, T imag){
        this.real = real;
        this.imag = imag;
    }
    @Override
    public double doubleValue(){
        return real.doubleValue();
    }
    @Override
    public float floatValue(){
        return real.floatValue();
    }
    @Override
    public long longValue(){
        return real.longValue();
    }
    @Override
    public int intValue(){
        return real.intValue();
    }

    public static Complex<Double> add(
            Complex<Double> elem1, Complex<Double> elem2){
        Complex out = new Complex(
            elem1.real.doubleValue() + elem2.real.doubleValue(),
            elem1.imag.doubleValue() + elem2.imag.doubleValue());
        return out;
    }
}