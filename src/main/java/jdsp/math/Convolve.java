/**
 * Convolution package
 * @author Keith Chow
 */
package net.kcundercover.jdsp.math;

/** Convolve class */
public class Convolve {
    /** Serial Version UID */
    public final static long serialVersionUID = 0;

    /** Default Constructor */
    public Convolve() {}

    /**
     * Convolve 2 vectors together.
     *
     * @param input1 First vector (length N) real values
     * @param input2 Second vector (length M) real values
     * @return The convolved outputed (length M + N - 1)
     */
    public static int[] convolve(int[] input1, int[] input2){
        // ----------------  setup local variables  -------------------------
        int len1 = input1.length;
        int len2 = input2.length;
        int len3 = len1 + len2 - 1;
        int start;

        // allocate output
        int[] output = new int[len3];

        if (len2 >= len1){
            for (int ind0 = 0; ind0 < len1; ind0++){
                for (int ind1 = 0; ind1 < ind0 + 1; ind1++){
                    output[ind0] += input1[ind0 - ind1] * input2[ind1];
                }
            }
            for (int ind0 = len1; ind0 < len3; ind0++){
                start = (ind0 - len2 + 1 < 0) ? 0 : ind0 - len2 + 1;
                for (int ind1=start; ind1 < len1; ind1++){
                    int tmp = input2[ind0 - ind1];
                    output[ind0] += input1[ind1] * tmp;
                }
            }
        }
        else{
            for (int ind0 = 0; ind0 < len2; ind0++){
                for (int ind1 = 0; ind1 < ind0 + 1; ind1++){
                    output[ind0] += input2[ind0 - ind1] * input1[ind1];
                }
            }
            for (int ind0 = len2; ind0 < len3; ind0++){
                start = (ind0 - len1 + 1 < 0) ? 0 : ind0 - len1 + 1;
                for (int ind1=start; ind1 < len2; ind1++){
                    int tmp = input1[ind0 - ind1];
                    output[ind0] += input2[ind1] * tmp;
                }
            }
        }

        return output;
    }

    /**
     * Convolve 2 vectors together.
     *
     * @param input1 First vector (length N) real values
     * @param input2 Second vector (length M) real values
     * @return The convolved outputed (length M+N-1)
     */
    public static float[] convolve(float[] input1, float[] input2){
        // ----------------  setup local variables  -------------------------
        int len1 = input1.length;
        int len2 = input2.length;
        int len3 = len1 + len2 - 1;
        int start;
        float[] output = new float[len3];

        if (len2 >= len1){
            for (int ind0 = 0; ind0 < len1; ind0++){
                for (int ind1 = 0; ind1 < ind0 + 1; ind1++){
                    output[ind0] += input1[ind0 - ind1] * input2[ind1];
                }
            }
            for (int ind0 = len1; ind0 < len3; ind0++){
                start = (ind0 - len2 + 1 < 0) ? 0 : ind0 - len2 + 1;
                for (int ind1=start; ind1 < len1; ind1++){
                    float tmp = input2[ind0 - ind1];
                    output[ind0] += input1[ind1] * tmp;
                }
            }
        }
        else{
            for (int ind0 = 0; ind0 < len2; ind0++){
                for (int ind1 = 0; ind1 < ind0 + 1; ind1++){
                    output[ind0] += input2[ind0 - ind1] * input1[ind1];
                }
            }
            for (int ind0 = len2; ind0 < len3; ind0++){
                start = (ind0 - len1 + 1 < 0) ? 0 : ind0 - len1 + 1;
                for (int ind1=start; ind1 < len2; ind1++){
                    float tmp = input1[ind0 - ind1];
                    output[ind0] += input2[ind1] * tmp;
                }
            }
        }

        return output;
    }

    /**
     * Convolve 2 vectors together.
     *
     * @param input1 First vector (length N)
     * @param input2 Second vector (length M)
     * @return The convolved outputed (length M+N-1)
     */
    public static final double[] convolve(double[] input1, double[] input2){
        // ----------------  setup local variables  -------------------------
        int len1 = input1.length;
        int len2 = input2.length;
        int len3 = len1 + len2 - 1;
        int start;
        double[] output = new double[len3];

        if (len2 >= len1){
            for (int ind0 = 0; ind0 < len1; ind0++){
                for (int ind1 = 0; ind1 < ind0+1; ind1++){
                    output[ind0] += input1[ind0 - ind1] * input2[ind1];
                }
            }
            for (int ind0 = len1; ind0 < len3; ind0++){
                start = (ind0-len2+1<0) ? 0 : ind0 - len2 + 1;
                for (int ind1=start; ind1 < len1; ind1++){
                    double tmp = input2[ind0 - ind1];
                    output[ind0] += input1[ind1] * tmp;
                }
            }
        }
        else{
            for (int ind0 = 0; ind0 < len2; ind0++){
                for (int ind1 = 0; ind1 < ind0 + 1; ind1++){
                    output[ind0] += input2[ind0 - ind1] * input1[ind1];
                }
            }
            for (int ind0 = len2; ind0 < len3; ind0++){
                start = (ind0 - len1 + 1 < 0) ? 0 : ind0 - len1 + 1;
                for (int ind1=start; ind1 < len2; ind1++){
                    double tmp = input1[ind0 - ind1];
                    output[ind0] += input2[ind1] * tmp;
                }
            }
        }

        return output;
    }

    /**
     * Convolve 2 vectors together.
     *
     * @param input1 First vector (length N)
     * @param input2 Second vector (length M * 2) complex interleaved
     * @return The convolved outputed (length M+N-1)
     */
    public static final double[] convolveRealComplex(double[] input1, double[] input2){
        // ---------------------  error checking  ---------------------------
        // expecting complex interleaved should be even.
        if (input2.length % 2 == 1)
            throw new IllegalArgumentException(
                "Expecting interleaved complex, should be even number");

        // ----------------  setup local variables  -------------------------
        int len1 = input1.length;
        int len2 = input2.length / 2;
        int len3 = len1 + len2 - 1;
        int start;

        // output is complex interlevaved.  double number of elements
        double[] output = new double[len3 * 2];

        if (len2 >= len1){
            for (int ind0 = 0; ind0 < len1; ind0++){
                for (int ind1 = 0; ind1 < ind0+1; ind1++){
                    output[ind0 * 2] += input1[ind0 - ind1] * input2[ind1 * 2];
                    output[ind0 * 2 + 1] += input1[ind0 - ind1] * input2[ind1 * 2 + 1];
                }
            }
            for (int ind0 = len1; ind0 < len3; ind0++){
                start = (ind0-len2+1<0) ? 0 : ind0 - len2 + 1;
                for (int ind1=start; ind1 < len1; ind1++){
                    output[ind0 * 2] += input1[ind1] * input2[2 * (ind0 - ind1)];
                    output[ind0 * 2 + 1] += input1[ind1] * input2[2 * (ind0 - ind1) + 1];
                }
            }
        }
        else{
            for (int ind0 = 0; ind0 < len2; ind0++){
                for (int ind1 = 0; ind1 < ind0 + 1; ind1++){
                    output[ind0 * 2] += input2[2*(ind0 - ind1)] * input1[ind1];
                    output[ind0 * 2 + 1] += input2[2*(ind0 - ind1) + 1] * input1[ind1];
                }
            }
            for (int ind0 = len2; ind0 < len3; ind0++){
                start = (ind0 - len1 + 1 < 0) ? 0 : ind0 - len1 + 1;
                for (int ind1=start; ind1 < len2; ind1++){
                    double tmp = input1[ind0 - ind1];
                    output[2 * ind0] += input2[2 * ind1] * input1[ind0-ind1];
                    output[2 * ind0 + 1] += input2[2 * ind1 + 1] * input1[ind0 - ind1];
                }
            }
        }

        return output;
    }
}