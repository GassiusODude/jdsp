package jdsp.math;
/**
 *
 * @author GassiusODude
 */
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
        output[0] = sum(signal);
        
        for (int freq=1; freq < nfft/2 + 1; freq++){
            // initialize sum of real and imaginary
            sumReal = 0;
            sumImag = 0;
            
            // --------- exp(j*w*t) = 0.5*cos(wt) + 0.5j * sin(wt)  ---------
            // update mult for the duration for-loop
            mult = (float)(2 * Math.PI * freq / nfft);
            for (int t=0; t < signal.length; t++){
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
     * Calculate the magnitude of each element in the array
     * Assumes complex, interleaved samples.
     *      Sample[0] = complexSig[0] + j * complexSig[1]
     *      Sample[1] = complexSig[2] + j * complexSig[3]
     * 
     * @param complexSig Complex interleaved signal
     * @return Real magnitude vector
     */
    public static float[] magnitude(float[] complexSig){
        // magnitude is real, half the size of interleaved input
        float[] output = new float[complexSig.length/2];
        
        for (int ind0 = 0; ind0 < output.length; ind0++){
            output[ind0] = complexSig[ind0*2]*complexSig[ind0*2] + 
                           complexSig[ind0*2+1]*complexSig[ind0*2+1];
        }
        return output;
    }

    /**
     * Calculate the sum of the vector
     * 
     * @param signal The input signal.
     * @return The sum of the signal.
     */
    public static float sum(float[] signal){
        float my_sum = 0;
        for (int ind0=0; ind0<signal.length; ind0++)
            my_sum += signal[ind0];
        return my_sum;
    }
}
