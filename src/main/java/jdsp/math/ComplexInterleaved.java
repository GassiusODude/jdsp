/**
 * This class contains a number of convenience functions for working with
 * complex interleaved data.
 * 
 * @author GassiusODude
 * @since June 2019
 */
package jdsp.math;
public class ComplexInterleaved {
    // --------------------------  double support  --------------------------
    /**
     * Calculate the phase of the complex signal
     * @param complexSig The input complex signal
     * @return The real valued angle (with values in range -pi to pi)
     */
    public static double[] angle(double[] complexSig){
        double[] output = new double[complexSig.length / 2];
        for (int ind0 = 0; ind0 < output.length; ind0++){
            output[ind0] = Math.atan2(complexSig[ind0 * 2 + 1],
                complexSig[ind0 * 2]);
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
    public static double[] magnitude(double[] complexSig){
        // magnitude is real, half the size of interleaved input
        double[] output = new double[complexSig.length / 2];
        
        for (int ind0 = 0; ind0 < output.length; ind0++){
            output[ind0] = complexSig[ind0 * 2] * complexSig[ind0 * 2] + 
                           complexSig[ind0 * 2 + 1] * complexSig[ind0 * 2 + 1];
            output[ind0] = Math.sqrt(output[ind0]);
        }
        return output;
    }

    /**
     * Frequency translate a real signal.  Output will be complex
     * @param inSig Input signal (real)
     * @param normFreq Normalized frequency shift
     * @return Complex interleaved output
     */
    public static double[] freqTranslateReal(double[] inSig, double normFreq){
        if (normFreq > 1 || normFreq < -1)
            throw new IllegalArgumentException(
                "norm frequency should be in range (-1,1)");
        // --------------------  perform translate  -------------------------
        double pi = Math.PI;
        double[] output = new double[inSig.length * 2];
        for (int ind0 = 0; ind0 < inSig.length; ind0++){
            output[ind0 * 2] =
                inSig[ind0] * Math.cos(pi * normFreq * ind0);
            output[ind0 * 2 + 1] = 
                inSig[ind0] * Math.sin(pi * normFreq * ind0);
        }
        return output;
    }
    /**
     * Frequency translate a complex signal.  Output will be complex
     * @param inSig Input signal (complex interleaved)
     * @param normFreq Normalized frequency shift
     * @return Complex interleaved output
     */
    public static double[] freqTranslateComplex(double[] inSig, double normFreq){
        if (normFreq > 1 || normFreq < -1)
            throw new IllegalArgumentException(
                "norm frequency should be in range (-1,1)");
        // --------------------  perform translate  -------------------------
        double pi = Math.PI*0.5;
        double[] output = new double[inSig.length];
        for (int ind0 = 0; ind0 < inSig.length; ind0+=2){
            output[ind0] =
                inSig[ind0] * Math.cos(pi * normFreq * ind0) -
                inSig[ind0 + 1] * Math.sin(pi * normFreq * ind0);

            output[ind0 + 1] = 
                inSig[ind0] * Math.sin(pi * normFreq * ind0) +
                inSig[ind0 + 1] * Math.cos(pi * normFreq * ind0);
        }
        return output;
    }


    // --------------------------  float support  --------------------------
    /**
     * Calculate the phase of the complex signal
     * @param complexSig The input complex signal
     * @return The real valued angle (with values in range -pi to pi)
     */
    public static float[] angle(float[] complexSig){
        float[] output = new float[complexSig.length / 2];
        for (int ind0 = 0; ind0 < output.length; ind0++){
            output[ind0] = (float) Math.atan2(complexSig[ind0 * 2 + 1],
                complexSig[ind0 * 2]);
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
        float[] output = new float[complexSig.length / 2];
        
        for (int ind0 = 0; ind0 < output.length; ind0++){
            output[ind0] = complexSig[ind0 * 2] * complexSig[ind0 * 2] + 
                           complexSig[ind0 * 2 + 1] * complexSig[ind0 * 2 + 1];
            output[ind0] = (float) Math.sqrt(output[ind0]);
        }
        return output;
    }
}