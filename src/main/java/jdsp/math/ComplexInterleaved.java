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