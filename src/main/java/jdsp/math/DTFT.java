/**
 * The discrete time fourier transform.
 * 
 * @author GassiusODude
 */
package jdsp.math;
import jdsp.math.Vector;
public class DTFT {
    public final static long serialVersionUID = 0;
    /**
     * Calculates the discrete time fourier transform of a real input signal
     * @param signal Real signal
     * @param nfft Number of FFT points
     * @return Complex FFT (with interleaved floats)
     */
    public static float[] discreteFourierTransform(float[] signal, int nfft){
        // --------------------  prepare variables  -------------------------
        float sumReal, sumImag, mult;

        // prepare output
        float[] output = new float[nfft * 2];
        int len_out = output.length;
        output[0] = Vector.sum(signal) / 2;
        
        for (int freq=1; freq < nfft/2 + 1; freq++){
            // initialize sum of real and imaginary
            sumReal = 0;
            sumImag = 0;
            
            // --------- exp(j*w*t) = 0.5*cos(wt) + 0.5j * sin(wt)  ---------
            // update mult for the duration for-loop
            mult = (float)(2 * Math.PI * freq / nfft);
            for (int t = 0; t < signal.length; t++){
                sumReal += signal[t] * Math.cos(mult * t);
                sumImag += signal[t] * Math.sin(mult * t);
            }
            sumReal /= 2;
            sumImag /= 2;

            // ----------------------  update output  -----------------------
            output[freq * 2] = sumReal;
            output[freq * 2 + 1] = sumImag;

            // input is real, fft will be symmetric
            output[len_out - freq * 2] = sumReal;
            output[len_out - freq * 2 + 1] = sumImag;
        }
        return output;
    }
    /**
     * Calculates the discrete time fourier transform of a real input signal
     * @param signal Complex signal
     * @param nfft Number of FFT points
     * @return Complex FFT (with interleaved floats)
     */
    public static float[] discreteFourierTransformComplex(float[] signal, int nfft){
        // --------------------  prepare variables  -------------------------
        float sumReal, sumImag, mult;

        // prepare output
        float[] output = new float[nfft * 2];
        int len_out = output.length;
        output[0] = Vector.sum(signal) / 2;
        
        for (int freq=1; freq < nfft -1 ; freq++){
            // initialize sum of real and imaginary
            sumReal = 0;
            sumImag = 0;
            
            // --------- exp(j*w*t) = 0.5*cos(wt) + 0.5j * sin(wt)  ---------
            // update mult for the duration for-loop
            mult = (float)(2 * Math.PI * freq / nfft);
            for (int t = 0; t < signal.length; t+=2){
                sumReal += signal[t] * Math.cos(mult * t/2.0)
                    - signal[t+1] * Math.sin(mult * t/2.0);
                sumImag += signal[t] * Math.sin(mult * t/2.0) 
                    + signal[t+1] * Math.cos(mult * t/2.0);
            }
            sumReal /= 2;
            sumImag /= 2;

            // ----------------------  update output  -----------------------
            output[freq * 2] = sumReal;
            output[freq * 2 + 1] = sumImag;
        }
        return output;
    }

    /**
     * Shift the FFT by half.
     * @param input Input signal
     * @return Shifted array
     */
    public static float[] fftShift(float[] input){
        int mid = input.length / 2;
        float[] output = new float[input.length];
        System.arraycopy(input, 0, output, output.length - mid, mid);
        System.arraycopy(input, input.length - mid,
                         output, 0, input.length - mid);
        return output;
    }
 }
