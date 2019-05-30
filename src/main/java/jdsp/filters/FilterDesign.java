package jdsp.filters;

import java.security.InvalidParameterException;

public class FilterDesign {
    /**
     * Supported windows =
     *      
     * 
     * @param window Window Type from {"BARTLETT", "HAMMING", "HANN"}
     * @param x The x range to design the filter
     * @return
     * @throws InvalidParameterException
     */
    public static float[] designWindow(String window, float[] x)
        throws InvalidParameterException{
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
                throw new InvalidParameterException("Unsupported window type");
        }
        return output;
    }
    public static float[] firWindowDesign(int numTap, String window,
            float normalizeBandwidth) throws InvalidParameterException{
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
        float[] win = designWindow(window, x);

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
            mySum += output[ind0];
        }

        // normalize filter
        for (int ind0 = 0; ind0 < numTap; ind0++){
            output[ind0] /= mySum;
        }
        return output;
    }
    /**
     * Design a moving average filter.
     * @param numNum
     * @return The designed filter
     * @throws InvalidParameterException
     */
    public static float[] designMovingAverage(int numNum)
        throws InvalidParameterException {
        // -------------------------  error checking  -----------------------
        if (numNum < 1)
           throw new InvalidParameterException(
                "Number Numerator Coefficients should be >= 1");

        // initialize to moving average filter
        float[] numerator = new float[numNum];
        for (int ind0 = 0; ind0 < numNum; ind0++)
            numerator[ind0] = 1.0f / numNum;
        return numerator;
    }
}