/**
 *
 * @author GassiusODude
 * @since June 2019
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

 }
