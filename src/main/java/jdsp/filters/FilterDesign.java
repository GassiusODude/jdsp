/**
 * @author Keith Chow
 */
package net.kcundercover.jdsp.filters;
public class FilterDesign {
    /** Design filter based on windowing technique
     *
     * @param window Window Type from {"BARTLETT", "HAMMING", "HANN"}
     * @param x The x range to design the filter
     * @return The designed window function
     */
    public static float[] designWindowF(String window, float[] x){
        // ---------------------------  prepare output  ---------------------
        float[] output = new float[x.length];
        float pi = (float)Math.PI;

        switch (window){
            case "BARTLETT":
                for (int ind0 = 0; ind0 < x.length; ind0++)
                    output[ind0] = 1.0f - (float)Math.abs(2 * x[ind0]);
                break;
            case "HAMMING":
                for (int ind0 = 0; ind0 < x.length; ind0++)
                    output[ind0] = 0.54f + 0.46f *
                        (float) Math.cos(2.0f * pi * x[ind0]);
                break;
            case "HANN":
                for (int ind0 = 0; ind0 < x.length; ind0++)
                    output[ind0] = 0.5f + 0.5f *
                        (float) Math.cos(2.0f * pi * x[ind0]);
                break;

            default:
                assert false: "Unsupported window type: " + window;
        }
        return output;
    }
    /** Design an FIR filter with the WindowDesign Method.
     *
     * This first designs an ideal IIR filter of the specified bandwidth.
     * This is multipled in time with the desired window.
     * @param numTap Number of elements of the filter
     * @param window The String name of the type of window
     * @param normalizeBandwidth The desired bandwidth of the filter.
     * @return The designed filter
     */
    public static float[] firWindowDesignF(int numTap, String window,
            float normalizeBandwidth){
        // ---------------------  prepare variables  ------------------------
        // prepare output
        float [] output = new float[numTap];

        // local variables
        float[] x = new float[numTap];
        float pi = (float)Math.PI;
        float tmp;
        // -------------------------  prepare x  ----------------------------
        float xInc = 1.0f / numTap;

        if ((numTap&1) == 1){
            // odd
            x[0] = -(numTap - 1) / 2.0f / numTap;
        }else{
            // even
            x[0] = (0.5f - numTap / 2) / numTap;
        }
        for (int ind0 = 1; ind0 < numTap; ind0 ++){
            x[ind0] = x[ind0 - 1] + xInc;
        }
        // -----------------------  design window  -------------------------
        float[] win = designWindowF(window, x);

        // -----------------  design ideal IIR filter  ----------------------
        // load sinc into output
        for (int ind0 = 0; ind0 < numTap; ind0++){
            if (x[ind0] == 0){
                output[ind0] = 1.0f;
            }
            else{
                tmp = normalizeBandwidth * pi * x[ind0] * numTap;
                output[ind0] = (float) Math.sin(tmp) / tmp;
            }
        }

        // ----------------------  design window  ---------------------------
        float mySum = 0.0f;

        // multiply IIR and window in time == convolve idea
        for (int ind0 = 0; ind0 < numTap; ind0++){
            output[ind0] *= win[ind0];
            mySum += Math.abs(output[ind0]);
        }

        // normalize filter
        for (int ind0 = 0; ind0 < numTap; ind0++){
            output[ind0] /= mySum;
        }
        return output;
    }
    /** Design a moving average filter.
     * @param numNum Number of elements of the filter.
     * @return The designed filter
     */
    public static float[] designMovingAverageF(int numNum){
        // -------------------------  error checking  -----------------------
        assert numNum >= 1 :
            "Number Numerator Coefficients should be >= 1";

        // initialize to moving average filter
        float[] numerator = new float[numNum];
        java.util.Arrays.fill(numerator, 1.0f / numNum);

        return numerator;
    }
    // ======================================================================
    // -------------------  double version  ---------------------------------
    // ======================================================================

    /** Design filter
     *
     * @param window Window Type from {"BARTLETT", "HAMMING", "HANN"}
     * @param x The x range to design the filter
     * @return The designed window function
     */
    public static double[] designWindowD(String window, double[] x)
            throws IllegalArgumentException{
        // ---------------------------  prepare output  ---------------------
        double[] output = new double[x.length];
        double pi = Math.PI;

        switch (window){
            case "BARTLETT":
                for (int ind0 = 0; ind0 < x.length; ind0++)
                    output[ind0] = 1.0f - Math.abs(2 * x[ind0]);
                break;
            case "HAMMING":
                for (int ind0 = 0; ind0 < x.length; ind0++)
                    output[ind0] = 0.54 + 0.46 *
                        Math.cos(2.0 * pi * x[ind0]);
                break;
            case "HANN":
                for (int ind0 = 0; ind0 < x.length; ind0++)
                    output[ind0] = 0.5f + 0.5 *
                        Math.cos(2.0 * pi * x[ind0]);
                break;

            default:
                assert false: "Unsupported window type: "+ window;
        }
        return output;
    }
    /**Design an FIR filter with the WindowDesigen Method.
     * This first designs an ideal IIR filter of the specified bandwidth.
     * This is multipled in time with the desired window.
     * @param numTap Number of elements of the filter
     * @param window The String name of the type of window
     * @param normalizeBandwidth The desired bandwidth of the filter.
     * @return The designed filter
     */
    public static double[] firWindowDesignD(int numTap, String window,
            double normalizeBandwidth){
        // ---------------------  prepare variables  ------------------------
        // prepare output
        double [] output = new double[numTap];

        // local variables
        double[] x = new double[numTap];
        double pi = Math.PI;
        double tmp;
        // -------------------------  prepare x  ----------------------------
        double xInc = 1.0f / numTap;

        if ((numTap&1) == 1){
            // odd
            x[0] = -(numTap - 1) / 2.0 / numTap;
        }else{
            // even
            x[0] = (0.5 - numTap / 2) / numTap;
        }
        for (int ind0 = 1; ind0 < numTap; ind0 ++){
            x[ind0] = x[ind0 - 1] + xInc;
        }
        // -----------------------  design window  -------------------------
        double[] win = designWindowD(window, x);

        // -----------------  design ideal IIR filter  ----------------------
        // load sinc into output
        for (int ind0 = 0; ind0 < numTap; ind0++){
            if (x[ind0] == 0){
                output[ind0] = 1.0;
            }
            else{
                tmp = normalizeBandwidth * pi * x[ind0] * numTap;
                output[ind0] = Math.sin(tmp) / tmp;
            }
        }

        // ----------------------  design window  ---------------------------
        double mySum = 0.0;

        // multiply IIR and window in time == convolve idea
        for (int ind0 = 0; ind0 < numTap; ind0++){
            output[ind0] *= win[ind0];
            mySum += Math.abs(output[ind0]);
        }

        // normalize filter
        for (int ind0 = 0; ind0 < numTap; ind0++){
            output[ind0] /= mySum;
        }
        return output;
    }
    /** Design a moving average filter.
     * @param numNum Number of elements of the filter.
     * @return The designed filter
     */
    public static double[] designMovingAverageD(int numNum){
        // -------------------------  error checking  -----------------------
        assert numNum >= 1:
            "Number Numerator Coefficients should be >= 1";

        // initialize to moving average filter
        double[] numerator = new double[numNum];
        for (int ind0 = 0; ind0 < numNum; ind0++)
            numerator[ind0] = 1.0 / numNum;
        return numerator;
    }

    /** Design a square-root raised cosine filter
     *
     * @param numNum The number of taps for the FIR filter
     * @param sampleRate The sampling rate to design filter against
     * @param baud The baud of the signal or the inverse of the desired period.
     * @param rolloff The rolloff of the RRC
     * @return Return the designed filter
     */
    public static double[] designSRRC(int numNum, double sampleRate,
            double baud, double rolloff){
        // ---------------  setup internal variables  -----------------------
        double time;
        double[] filter = new double[numNum];
        double pi = Math.PI;
        double _4b = 4 * rolloff;
        // -----------------------  setup time  -----------------------------
        for (int ind0 = 0; ind0 < filter.length; ind0++){
            time = (ind0 - (numNum - 1) / 2.0) / sampleRate;
            if (time == 0)
                filter[ind0] = baud * (1 + rolloff * (4 / pi - 1));
            else if (Math.abs(time) == 1.0 / (baud * 4 * rolloff)){
                filter[ind0] = rolloff * baud / Math.sqrt(2) *
                    ((1 + 2 / pi) * Math.sin(pi / _4b) +
                    (1 - 2 / pi) * Math.cos(pi / _4b));
            }
            else{
                filter[ind0] = baud * (Math.sin(pi * time * baud * (1 - rolloff)) +
                    _4b * time * baud * Math.cos(pi * time * baud * (1 + rolloff)))
                    / (pi * time * baud * (1 - Math.pow(_4b * time * baud, 2)));
            }
       }

        return filter;
    }
}